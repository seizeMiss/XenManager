package main.java.dragon.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VMGuestMetrics;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.Storage;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.VolumeAPI;
import main.java.dragon.xenapi.XenApiUtil;

public class AddVmThread implements Runnable{
	@Autowired
	private VMDao vmDao;
	@Autowired
	private ImageDao imageDao;
	
	private Connection connection;
	private VmInstance vmInstance;
	private String userDisk;
	private Storage srStorage;
	
	public AddVmThread(Connection connection, VmInstance vmInstance, String userDisk, Storage storage) {
		this.connection = connection;
		this.vmInstance = vmInstance;
		this.userDisk = userDisk;
		this.srStorage = storage;
	}
	
	@Override
	public void run() {
		SR sr = null;
		try {
			sr = SR.getByUuid(connection, srStorage.getUuid());
			createVmByImage(vmInstance, userDisk, sr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void createVmByImage(VmInstance vmInstance, String userDisk, SR sr) throws Exception {
		VolumeAPI volAPI = new VolumeAPI();
		String name = vmInstance.getName();
		// VM vmTemplate = getSystemTemplate(os);
		VM.Record vmRecord = createVmRecord(name, vmInstance.getMemory(), vmInstance.getCpu());
		// 根据虚机属性创建虚拟机
		Task task = VM.createAsync(connection, vmRecord);
		XenApiUtil.waitForTask(connection, task, 2000);
		VM newVM = Types.toVM(task, connection);
		Image image = imageDao.selectImageById(vmInstance.getImageId());
		VDI srcVDI = VDI.getByUuid(connection, image.getUuid());

		VDI newVdi = volAPI.cloneNewVDI(srcVDI, name + "_COPY");

		attachVDI(newVM, newVdi, true);
		// 添加用户磁盘
		if (!StringUtils.isEmpty(userDisk)) {
			String[] userDisks = userDisk.split(";");
			for (int i = 0; i < userDisks.length; i++) {
				int diskSize = Integer.parseInt(userDisks[i]);
				createDisk(newVM, sr, name + "-userDate-" + i, diskSize);
			}
		}
		// 创建虚拟光驱
		// makeCDDrive(newVM);
		Network network = getDefaultNetwork();
		// 创建网络并挂载到虚机上
		VIF vif = makeVIF(newVM, network, "0");
		String ipAddress = "-";
		VMGuestMetrics guestMetrics = newVM.getGuestMetrics(connection);
		if (guestMetrics != null && !guestMetrics.isNull()) {
			Map<String, String> networkInfo = guestMetrics.getNetworks(connection);
			String ipKey = vif.getDevice(connection) + "/ip";
			if (networkInfo != null && networkInfo.size() > 0) {
				ipAddress = networkInfo.get(ipKey);
			}
		}
		Set<VmNetwork> vmNetworks = getVmNetwork(newVM, vmInstance.getId());
		Set<VmStorage> vmStorages = getVmStorage(newVM, vmInstance.getId(), vmInstance.getStorageId());
		vmInstance.setStatus(CommonConstants.VM_CLOSE_STATUS);
		vmInstance.setPowerStatus(newVM.getPowerState(connection).toString());
		vmInstance.setVmIp(ipAddress);
		vmInstance.setVmStorages(vmStorages);
		vmInstance.setVmNetWorks(vmNetworks);
		vmInstance.setUuid(newVM.getUuid(connection));
		vmInstance.setUpdateTime(new Date());
		vmDao.updateVmAndInsertOther(vmInstance);
	}

	// 根据传入的参数生成虚机的属性。
	private VM.Record createVmRecord(String name, long ram, long cpu) {

		// 该值为Windows平台的配置。
		Map<String, String> platform = new HashMap<String, String>();
		platform.put("viridian", "true");
		platform.put("viridian_time_ref_count", "true");
		platform.put("viridian_reference_tsc", "true");
		platform.put("acpi", "1");
		platform.put("apic", "true");
		platform.put("pae", "true");
		platform.put("nx", "true");
		platform.put("cores-per-socket", "1");
		platform.put("timeoffset", "28806");
		platform.put("device_id", "0002");
		VM.Record vmRecord = new VM.Record();
		vmRecord.memoryStaticMax = ram * 1024 * 1024;
		vmRecord.memoryStaticMin = ram * 1024 * 1024;
		vmRecord.memoryDynamicMax = ram * 1024 * 1024;
		vmRecord.memoryDynamicMin = ram * 1024 * 1024;
		vmRecord.memoryTarget = 0L;
		vmRecord.recommendations = "<restrictions><restriction field=\"memory-static-max\" max=\"137438953472\" /><restriction field=\"vcpus-max\" max=\"16\" /><restriction property=\"number-of-vbds\" max=\"16\" /><restriction property=\"number-of-vifs\" max=\"7\" /></restrictions>";
		vmRecord.VCPUsMax = cpu;
		vmRecord.VCPUsAtStartup = cpu;
		vmRecord.nameLabel = name;
		vmRecord.otherConfig = new HashMap<String, String>();
		vmRecord.platform = platform;
		vmRecord.HVMShadowMultiplier = 1.0d;
		vmRecord.HVMBootPolicy = "BIOS order";
		vmRecord.actionsAfterCrash = Types.OnCrashBehaviour.RESTART;
		vmRecord.actionsAfterReboot = Types.OnNormalExit.RESTART;
		vmRecord.actionsAfterShutdown = Types.OnNormalExit.DESTROY;

		return vmRecord;
	}

	// 挂载VDI到VM上
	private void attachVDI(VM vm, VDI vdi, boolean systemDisk) throws Exception {

		VBD.Record vbdRecord = new VBD.Record();
		vbdRecord.VM = vm;
		vbdRecord.VDI = vdi;
		vbdRecord.mode = Types.VbdMode.RW;
		vbdRecord.type = Types.VbdType.DISK;
		// 系统盘的，userdevice必须为"0"
		vbdRecord.userdevice = systemDisk ? "0" : getVbdDevicePosition(vm);
		VBD.create(connection, vbdRecord);
	}

	// 获取第一个网络
	private Network getDefaultNetwork() throws Exception {
		Set<Network> networks = Network.getAll(connection);

		for (Network i : networks) {
			return i;
		}
		throw new Exception("No networks found!");
	}

	// 获取可挂载的设备位置
	public String getVbdDevicePosition(VM vm) throws Exception {
		Set<VBD> vbdSet = vm.getVBDs(connection);
		Set<String> devicePositionSet = new HashSet<String>();
		for (VBD vbd : vbdSet) {
			VBD.Record record = vbd.getRecord(connection);
			devicePositionSet.add(record.userdevice);
		}

		for (int i = 1; i < 16; i++) {
			if (!devicePositionSet.contains(String.valueOf(i))) {
				return String.valueOf(i);
			}
		}
		throw new Exception("Can't find vaild device position");

	}

	// 创建一个网卡并挂载到VM上。
	private VIF makeVIF(VM newVm, Network network, String device) throws Exception {
		VIF.Record newvifrecord = new VIF.Record();
		// These three parameters are used in the command line VIF creation
		newvifrecord.VM = newVm;
		newvifrecord.network = network;
		newvifrecord.device = device;
		newvifrecord.MTU = 1500L;
		newvifrecord.lockingMode = Types.VifLockingMode.NETWORK_DEFAULT;

		return VIF.create(connection, newvifrecord);
	}

	// 创建空的磁盘,并挂载到VM上
	private void createDisk(VM vm, SR sr, String name, long size) throws Exception {
		VDI.Record vdir = new VDI.Record();
		vdir.type = Types.VdiType.USER;
		// 代表将放置 VDI 的位的物理存储
		vdir.SR = sr;
		vdir.nameLabel = name;
		vdir.virtualSize = size * 1024 * 1024 * 1024;
		vdir.readOnly = false;
		// 创建空白磁盘映像
		VDI vdi = VDI.create(connection, vdir);
		attachDisk(vm, vdi, false);
	}

	private void attachDisk(VM vm, VDI vdi, boolean isSystemDisk) throws Exception {
		VBD.Record vbdRecord = new VBD.Record();
		vbdRecord.VM = vm;
		vbdRecord.VDI = vdi;
		// 指定 VDI 是以只读方式还是读写方式连接。
		vbdRecord.mode = Types.VbdMode.RW;
		// 指定 VDI 是应当作为常规磁盘还是作为 CD 显示在 VM 中
		vbdRecord.type = Types.VbdType.DISK;
		if (isSystemDisk) {
			vbdRecord.bootable = true;
			vbdRecord.userdevice = "0";
		} else {
			// 指定来宾系统中的块设备，通过该设备正在 VM 中运行的应用程序将能读/写 VDI 的位。
			vbdRecord.userdevice = getVbdDeviceAvailablePosition(connection, vm);
		}
		vbdRecord.device = "xvda";
		VBD.create(connection, vbdRecord);
	}

	/**
	 * 获取磁盘挂载的position
	 */
	public static String getVbdDeviceAvailablePosition(Connection connection, VM vm) throws Exception {
		Set<VBD> vbds = vm.getVBDs(connection);
		Set<String> devicePositionSet = new HashSet<String>();
		for (VBD vbd : vbds) {
			VBD.Record vbdRecord = vbd.getRecord(connection);
			devicePositionSet.add(vbdRecord.userdevice);
		}

		for (int i = 1; i < 16; i++) {
			if (!devicePositionSet.contains(String.valueOf(i))) {
				return String.valueOf(i);
			}
		}
		throw new Exception("vm can't find valid user_device position");
	}
	
	private Set<VmStorage> getVmStorage(VM vm, String vmId, String storageId) throws Exception {
		Set<VmStorage> vmStorages = new HashSet<>();
		Set<VBD> vbds = vm.getVBDs(connection);
		for (VBD vbd : vbds) {
			VmStorage vmStorage = new VmStorage();
			VDI vdi = vbd.getVDI(connection);
			if (!vdi.isNull() && vdi.getVirtualSize(connection) > 0) {
				vmStorage.setId(StringUtils.generateUUID());
				vmStorage.setName(vdi.getNameLabel(connection));
				vmStorage.setStorageType(vdi.getType(connection).toString());
				vmStorage.setVmId(vmId);
				vmStorage.setSize((int) (vdi.getVirtualSize(connection) / 1024 / 1024 / 1024));
				vmStorage.setDescription(vdi.getNameDescription(connection));
				vmStorage.setStorageId(storageId);
				vmStorages.add(vmStorage);
			}
		}
		return vmStorages;
	}

	private Set<VmNetwork> getVmNetwork(VM vm, String vmId) throws Exception {
		Set<VmNetwork> vmNetworks = new HashSet<>();
		Set<VIF> vifs = vm.getVIFs(connection);
		for (VIF vif : vifs) {
			VmNetwork vmNetwork = new VmNetwork();
			Network network = vif.getNetwork(connection);
			String id = StringUtils.generateUUID();
			vmNetwork.setId(id);
			vmNetwork.setMacAddress(vif.getMAC(connection));
			vmNetwork.setNetworkId(network.getUuid(connection));
			vmNetwork.setNetworkName(network.getNameLabel(connection));
			vmNetwork.setVmId(vmId);
			vmNetworks.add(vmNetwork);
		}
		return vmNetworks;
	}

	

}
