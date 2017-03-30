package main.java.dragon.service.impl;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.common.util.StringHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Network;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;
import main.java.dragon.service.VMService;
import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.VolumeAPI;
import main.java.dragon.xenapi.XenApiUtil;

@Service
@Transactional
public class VMServiceImpl extends ConnectionUtil implements VMService {
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private VMDao vmDao;

	public VMServiceImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
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
					vmInstance.setVmStorages(getVmStorage(vm, id));
					vmInstances.add(vmInstance);
				}
			}
		}
		return vmInstances;
	}

	private List<VmStorage> getVmStorage(VM vm, String vmId) throws Exception {
		List<VmStorage> vmStorages = new ArrayList<>();
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
				vmStorage.setStorageId("44c9f5e8-9d29-43ac-b866-6079b440ce67");
				vmStorages.add(vmStorage);
			}
		}
		return vmStorages;
	}

	private List<VmNetwork> getVmNetwork(VM vm, String vmId) throws Exception {
		List<VmNetwork> vmNetworks = new ArrayList<>();
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
		// TODO Auto-generated method stub
		return vmDao.selectVmInstanceByName(name);
	}

	private void openVm(String id) throws Exception {
		VM vm = getVmByVmInstanceId(id);
		Task task = vm.startAsync(connection, false, false);
		XenApiUtil.waitForTask(connection, task, 2000);
	}

	private void closeVm(String id) throws Exception {
		VM vm = getVmByVmInstanceId(id);
		Task task = vm.shutdownAsync(connection);
		XenApiUtil.waitForTask(connection, task, 2000);
	}

	private void rebootVm(String id) throws Exception {
		VM vm = getVmByVmInstanceId(id);
		Task task = vm.hardRebootAsync(connection);
		XenApiUtil.waitForTask(connection, task, 2000);
	}

	private void createVmByImage(String name, String imageName, 
			long ram, long cpu) throws Exception {
		VolumeAPI volAPI = new VolumeAPI();
		// VM vmTemplate = getSystemTemplate(os);
		VM.Record vmRecord = createVmRecord(name, ram, cpu);
		// 根据虚机属性创建虚拟机
		VM newVM = VM.create(connection, vmRecord);
		VDI srcVDI = volAPI.getVDIByName(imageName);

		VDI newVdi = volAPI.cloneNewVDI(srcVDI, name + "_COPY_" + imageName);

		attachVDI(newVM, newVdi, true);

		// 创建虚拟光驱
		// makeCDDrive(newVM);

		// 创建网络并挂载到虚机上
		makeVIF(newVM, getDefaultNetwork(), "0");
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

}
