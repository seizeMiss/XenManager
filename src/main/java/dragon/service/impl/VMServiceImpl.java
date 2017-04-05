package main.java.dragon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.HostDao;
import main.java.dragon.dao.ImageDao;
import main.java.dragon.dao.StorageDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.Storage;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;
import main.java.dragon.service.VMService;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.VolumeAPI;
import main.java.dragon.xenapi.XenApiUtil;

@Service
@Transactional
public class VMServiceImpl extends ConnectionUtil implements VMService {
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private VMDao vmDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private ImageDao imageDao;

	private VmInstance vmInstance;

	public VMServiceImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	private int getStatusByVmStatus(String vmStatus) {
		int status = 0;
		switch (vmStatus) {
		case CommonConstants.VM_IS_CREATING:
			status = CommonConstants.VM_CREATING_STATUS;
			break;
		case CommonConstants.VM_IS_RESTARTING:
			status = CommonConstants.VM_RESTARTING_STATUS;
			break;
		case CommonConstants.VM_IS_CLOSING:
			status = CommonConstants.VM_CLOSING_STATUS;
			break;
		case CommonConstants.VM_IS_DELETING:
			status = CommonConstants.VM_DELETING_STATUS;
			break;
		case CommonConstants.VM_IS_NO_AVAILABEL:
			status = CommonConstants.VM_NO_AVAILABEL_STATUS;
			break;
		case CommonConstants.VM_IS_AVAILABEL:
			status = CommonConstants.VM_AVAILABEL_STATUS;
			break;
		case CommonConstants.VM_IS_OPEN:
			status = CommonConstants.VM_OPEN_STATUS;
			break;
		case CommonConstants.VM_IS_CLOSE:
			status = CommonConstants.VM_CLOSE_STATUS;
			break;
		default:
			break;
		}
		return status;
	}

	private List<VmInstance> getVmInstance() throws Exception {

		String clusterId = "22f7d051-d8ea-4646-8692-7de4d189b8c4";
		String hostId = "e120960b-d35b-405b-81c1-49ca8599edc0";
		String imageId = "6e7beda7-9fb9-45c7-ab5a-6e4bb17fbcea";
		String storageId = "44c9f5e8-9d29-43ac-b866-6079b440ce67";
		String uuid = "";
		String name = "";
		String vmIp = "";
		int status = 0;
		String powerStatus = "";
		Date createTime = new Date();
		Date updateTime = new Date();
		String osType = "";
		String osName = "";
		int cpu = 0;
		int memory = 0;
		int systemDisk = 20;
		List<VmInstance> vmInstances = new ArrayList<VmInstance>();
		Set<VM> vms = VM.getAll(connection);
		for (VM vm : vms) {
			if (XenApiUtil.isAvailableVm(vm)) {
				if (!vm.getNameLabel(connection).equals("cbl-vm-1")) {
					String id = StringUtils.generateUUID();
					uuid = vm.getUuid(connection);
					name = vm.getNameLabel(connection);
					if (vm.getDomid(connection) > 0) {
						status = 1;
					}
					powerStatus = vm.getPowerState(connection).toString();
					cpu = vm.getMetrics(connection).getVCPUsNumber(connection).intValue();
					memory = (int) (vm.getMemoryTarget(connection) / 1024 / 1024);
					VmInstance vmInstance = new VmInstance(id, clusterId, hostId, imageId, storageId, uuid, name, vmIp,
							status, powerStatus, createTime, updateTime, osType, osName, cpu, memory, systemDisk);
					vmInstance.setVmNetWorks(getVmNetwork(vm, id));
//					vmInstance.setVmStorages(getVmStorage(vm, id));
					vmInstances.add(vmInstance);
				}
			}
		}
		return vmInstances;
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

	@Override
	public void addVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork) throws Exception {
		// TODO Auto-generated method stub
		List<VmInstance> vmInstances = getVmInstance();
		for (int i = 0; i < vmInstances.size(); i++) {
			vmDao.insertVm(vmInstances.get(i));
		}
	}

	@Override
	public void saveVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<VmInstance> getAllVm() {
		// TODO Auto-generated method stub
		List<VmInstance> vmInstances = vmDao.selectAllVm();
		return vmInstances;
	}

	@Override
	public void deleteVm(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<VmInstance> getVmInstanceByName(String name) {
		return vmDao.selectVmInstanceByName(name);
	}

	@Override
	public List<VmInstance> getVmInstanceByCondition(String vmName, String vmStatus, String vmOsType) {
		// TODO Auto-generated method stub
		vmName = StringUtils.isEmpty(vmName) ? "" : vmName;
		vmOsType = StringUtils.isEmpty(vmOsType) ? "" : vmOsType;
		int status = 0;
		if (!StringUtils.isEmpty(vmStatus)) {
			status = getStatusByVmStatus(vmStatus);
		}
		return vmDao.selectVmInstanceByCondition(vmName, status, vmOsType);
	}

	@Override
	public VmInstance startVm(String id) {
		// TODO Auto-generated method stub
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_OPENING_STATUS);
		vmInstance.setPowerStatus(CommonConstants.VM_POWER_START);
		vmDao.updateVm(vmInstance);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					openVm(id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return vmInstance;
	}

	private void openVm(String id) throws Exception {
		VM vm = getVmByVmInstanceId(id);
		Task task = vm.startAsync(connection, false, false);
		XenApiUtil.waitForTask(connection, task, 2000);
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_OPEN_STATUS);
		vmDao.updateVm(vmInstance);
	}

	@Override
	public VmInstance closeVm(String id) {
		// TODO Auto-generated method stub
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_CLOSING_STATUS);
		vmInstance.setPowerStatus(CommonConstants.VM_POWER_CLOSED);
		vmDao.updateVm(vmInstance);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					shutdownVm(id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return vmInstance;
	}

	private void shutdownVm(String id) throws Exception {
		VM vm = getVmByVmInstanceId(id);
		Task task = vm.shutdownAsync(connection);
		XenApiUtil.waitForTask(connection, task, 2000);
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_CLOSE_STATUS);
		vmDao.updateVm(vmInstance);
	}

	@Override
	public VmInstance restartVm(String id) {
		// TODO Auto-generated method stub
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_RESTARTING_STATUS);
		vmInstance.setPowerStatus(CommonConstants.VM_POWER_RESTARTING);
		vmDao.updateVm(vmInstance);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					rebootVm(id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return vmInstance;
	}

	private void rebootVm(String id) throws Exception {
		VM vm = getVmByVmInstanceId(id);
		Task task = vm.hardRebootAsync(connection);
		XenApiUtil.waitForTask(connection, task, 2000);
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_OPEN_STATUS);
		vmDao.updateVm(vmInstance);
	}

	@Override
	public List<VmInstance> deleteVms(String ids) {
		// TODO Auto-generated method stub
		return null;
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
		if(!StringUtils.isEmpty(userDisk)){
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
		makeVIF(newVM, network, "0");
		Set<VmNetwork> vmNetworks = getVmNetwork(newVM, vmInstance.getId());
		Set<VmStorage> vmStorages = getVmStorage(newVM, vmInstance.getId(), vmInstance.getStorageId());
		vmInstance.setStatus(CommonConstants.VM_CLOSE_STATUS);
		vmInstance.setPowerStatus(newVM.getPowerState(connection).toString());
		vmInstance.setVmStorages(vmStorages);
		vmInstance.setVmNetWorks(vmNetworks);
		vmInstance.setUuid(newVM.getUuid(connection));
		vmDao.updateVmAndInsertOther(vmInstance);
	}

	private VM getVmByVmInstanceId(String id) throws Exception {
		VmInstance vmInstance = vmDao.selectVmById(id);
		return VM.getByUuid(connection, vmInstance.getUuid());
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
	// 修改虚拟机配置
		public void modfiyVmConfig(VM vm, long memory, long cpu) throws Exception {

			long memorySize = memory * 1024 * 1024;
			vm.setMemoryLimits(connection, memorySize, memorySize, memorySize,
					memorySize);
			VM.Record record = vm.getRecord(connection);

			// 修改CPU配置时，需要对CPU的个数进行判断，并确定配置的先后顺序。
			if (record.VCPUsAtStartup > cpu) {
				vm.setVCPUsAtStartup(connection, cpu);
				vm.setVCPUsMax(connection, cpu);
			} else {
				vm.setVCPUsMax(connection, cpu);
				vm.setVCPUsAtStartup(connection, cpu);
			}
		}

	@Override
	public int addVm(String vmName, String clusterId, String imageId, String cpuNumber, String memorySize,
			String storageId, String userDisk) {
		int backType = 1;
		int cpuCount = Integer.parseInt(cpuNumber);
		int memoryAlloSize = Integer.parseInt(memorySize);
		Storage srStorage = storageDao.selectStorageById(storageId);
		int cpuTotal = getClusterCpuCount(clusterId);
		if (cpuCount > cpuTotal) {
			return backType = 2;// cpu个数不够
		}
		int storage = getClusterStorageFree(srStorage);
		int userDiskTotal = getUserDiskTotal(userDisk);
		if (storage < userDiskTotal) {
			return backType = 3;// 存储大小不够
		}
		initVmInstance(vmName, clusterId, imageId, cpuCount, memoryAlloSize,storageId);
		vmDao.insertVm(vmInstance);
		new Thread(new Runnable() {
			@Override
			public void run() {
				SR sr = null;
				try {
					sr = SR.getByUuid(connection, srStorage.getUuid());
					createVmByImage(vmInstance, userDisk, sr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return backType;
	}

	@Override
	public int addVmBatch(String vmName, String vmNumber, String clusterId, String imageId, String cpuNumber,
			String memorySize, String storageId, String userDisk) {
		int backType = 1;
		int vmCount = Integer.parseInt(vmNumber);
		int cpuCount = Integer.parseInt(cpuNumber);
		int memoryAlloSize = Integer.parseInt(memorySize);
		int cpuTotal = getClusterCpuCount(clusterId);
		Storage srStorage = storageDao.selectStorageById(storageId);
		if (cpuCount > cpuTotal) {
			return backType = 2;// cpu个数不够
		}
		int storage = getClusterStorageFree(srStorage);
		int userDiskTotal = getUserDiskTotal(userDisk);
		if (storage < userDiskTotal) {
			return backType = 3;// 存储大小不够
		}
		initVmInstance(vmName, clusterId, imageId, cpuCount, memoryAlloSize,storageId);
		for (int i = 0; i < vmCount; i++) {
			vmDao.insertVm(vmInstance);
			new Thread(new Runnable() {
				@Override
				public void run() {
					SR sr = null;
					try {
						sr = SR.getByUuid(connection, srStorage.getUuid());
						synchronized (vmInstance) {
							createVmByImage(vmInstance, userDisk, sr);
							vmInstance.notifyAll();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		return backType;
	}

	private void initVmInstance(String name, String clusterId, String imageId, int cpuCount, int memorySize,String storageId) {
		vmInstance = new VmInstance();
		vmInstance.setId(StringUtils.generateUUID());
		vmInstance.setClusterId(clusterId);
		vmInstance.setImageId(imageId);
		vmInstance.setStorageId(storageId);
		Image image = imageDao.selectImageById(imageId);
		List<HostInstance> hostInstances = hostDao.selectHostByClusterId(clusterId);
		if (!StringUtils.isEmpty(hostInstances)) {
			vmInstance.setHostId(hostInstances.get(0).getId());
		}
		vmInstance.setName(name);
		vmInstance.setStatus(CommonConstants.VM_CREATING_STATUS);
		vmInstance.setPowerStatus(CommonConstants.VM_POWER_CREATING);
		vmInstance.setCreateTime(new Date());
		vmInstance.setUpdateTime(new Date());
		vmInstance.setOsName(image.getOsName());
		vmInstance.setOsType(image.getOsType());
		vmInstance.setCpu(cpuCount);
		vmInstance.setMemory(memorySize*1024);
		vmInstance.setSystemDisk(image.getImageSize());
	}

	private int getClusterCpuCount(String clusterId) {
		int cpuCount = 0;
		List<HostInstance> hostInstances = hostDao.selectHostByClusterId(clusterId);
		for (HostInstance hostInstance : hostInstances) {
			cpuCount += hostInstance.getCpuTotal();
		}
		return cpuCount;
	}

	private int getClusterStorageFree(Storage storage) {
		int storageFree = 0;
		storageFree = storage.getStorageTotal() - storage.getStorageUsed();
		return storageFree;
	}

	private int getUserDiskTotal(String userDisk) {
		int userDiskTotal = 0;
		if(!StringUtils.isEmpty(userDisk)){
			String[] userDisks = userDisk.split(";");
			for (String diskSize : userDisks) {
				userDiskTotal += Integer.parseInt(diskSize);
			}
		}
		return userDiskTotal;
	}

	@Override
	public VmInstance getVmInstanceById(String id) {
		// TODO Auto-generated method stub
		return vmDao.selectVmById(id);
	}

}
