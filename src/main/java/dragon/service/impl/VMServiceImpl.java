package main.java.dragon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.taglibs.standard.tag.common.fmt.ParseDateSupport;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Host;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.Types.XenAPIException;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VMGuestMetrics;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.HostDao;
import main.java.dragon.dao.ImageDao;
import main.java.dragon.dao.StorageDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.Storage;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;
import main.java.dragon.service.VMService;
import main.java.dragon.thread.AddVmThread;
import main.java.dragon.thread.DeleteVmsThread;
import main.java.dragon.thread.ModifyVMThread;
import main.java.dragon.thread.ReStartVMThread;
import main.java.dragon.thread.ShutdownVmThread;
import main.java.dragon.thread.StartVmThread;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.ConnectionInfoParseXml;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.VmAPI;
import main.java.dragon.xenapi.VolumeAPI;
import main.java.dragon.xenapi.XenApiUtil;

@Service
@Transactional
public class VMServiceImpl extends ConnectionUtil implements VMService {
	@Autowired
	private VMDao vmDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private ImageDao imageDao;
	@Autowired
	private ClusterDao clusterDao;

	private VmInstance vmInstance;

	public VMServiceImpl() throws Exception {
		super();
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
	
	private int getStatusByVmPowerStatus(String powerStatus){
		int status = 0;
		switch (powerStatus) {
		case CommonConstants.VM_POWER_START:
			status = CommonConstants.VM_OPEN_STATUS;
			break;
		case CommonConstants.VM_POWER_CLOSED:
			status = CommonConstants.VM_CLOSE_STATUS;
			break;
		case CommonConstants.VM_POWER_CLOSING:
			status = CommonConstants.VM_CLOSING_STATUS;
			break;
		case CommonConstants.VM_POWER_CREATING:
			status = CommonConstants.VM_CREATING_STATUS;
			break;
		case CommonConstants.VM_POWER_DELETED:
			status = CommonConstants.VM_DELETED_STATUS;
			break;
		case CommonConstants.VM_POWER_EDITING:
			status = CommonConstants.VM_EDITING_STATUS;
			break;
		case CommonConstants.VM_POWER_RESTARTING:
			status = CommonConstants.VM_RESTARTING_STATUS;
			break;
		case CommonConstants.VM_POWER_STARTING:
			status = CommonConstants.VM_OPENING_STATUS;
			break;
		default:
			break;
		}
		
		return status;
	}
	public void addVm(){
		List<VmInstance> vmInstances = null;
		try {
			vmInstances = getVmInstance();
			if(!StringUtils.isEmpty(vmInstances)){
				for(VmInstance vmInstance : vmInstances){
					vmDao.insertVm(vmInstance);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<VmInstance> getVmInstance() throws Exception {

		Map<String, String> connectionInfo = ConnectionInfoParseXml.getXenConenctionInfo();
		Cluster cluster = clusterDao.selectClusterByName(connectionInfo.get("cluster-name"));
		String clusterId = cluster.getId();
		String hostId = "";
		String imageId = "120dceda-a2aa-45a6-88d0-21e06c88ac31";
		String storageId = "";
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
		boolean isVmExist = false;
		List<VmInstance> vmInstances = new ArrayList<VmInstance>();
		List<VmInstance> selectVmInstances = vmDao.selectAllVm();
		Set<VM> vms = VM.getAll(connection);
		for (VM vm : vms) {
			if (XenApiUtil.isAvailableVm(vm)) {
				uuid = vm.getUuid(connection);
				if(!StringUtils.isEmpty(selectVmInstances)){
					for(VmInstance vmInstance : selectVmInstances){
						if(vmInstance.getUuid().equals(uuid)){
							isVmExist = true;
						}
					}
				}
				if(isVmExist){
					isVmExist = false;
					continue;
				}
				VmAPI vmAPI = new VmAPI();
				String id = StringUtils.generateUUID();
				Storage storage = storageDao.selectStorageByUuid(vmAPI.getSrUuidByVm(vm));
				storageId = storage.getId();
				Host host = vmAPI.getHostByVm(vm);
				HostInstance hostInstance = hostDao.selectHostByUuid(host.getUuid(connection));
				hostId = hostInstance.getId();
				name = vm.getNameLabel(connection);
				status = getStatusByVmPowerStatus(vm.getPowerState(connection).toString());
				powerStatus = vm.getPowerState(connection).toString();
				cpu = vm.getMetrics(connection).getVCPUsNumber(connection).intValue();
				memory = (int) (vm.getMemoryTarget(connection) / 1024 / 1024);
				VmInstance vmInstance = new VmInstance(id, clusterId, hostId, imageId, storageId, uuid, name, vmIp,
						status, powerStatus, createTime, updateTime, osType, osName, cpu, memory, systemDisk);
				vmInstance.setVmNetWorks(getVmNetwork(vm, id));
				vmInstance.setVmStorages(getVmStorage(vm, id, storageId));
				vmInstances.add(vmInstance);
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

	@Override
	public void saveVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork) {

	}

	@Override
	public List<VmInstance> getAllVm() {
		List<VmInstance> vmInstances = vmDao.selectAllVm();
		return vmInstances;
	}

	@Override
	public void deleteVm(String ids) {

	}

	@Override
	public List<VmInstance> getVmInstanceByName(String name) {
		List<VmInstance> vmInstances = new ArrayList<VmInstance>();
		for (VmInstance vmInstance : vmDao.selectVmInstanceByName(name)) {
			if (vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS) {
				vmInstances.add(vmInstance);
			}
		}
		return vmInstances;
	}

	@Override
	public List<VmInstance> getVmInstanceByCondition(String vmName, String vmStatus, String vmOsType) {
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
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_OPENING_STATUS);
		vmDao.updateVm(vmInstance);
		StartVmThread startVmThread = new StartVmThread(id, connection, vmDao);
		new Thread(startVmThread).start();// 开启线程
		return vmInstance;
	}

	@Override
	public VmInstance closeVm(String id) {
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_CLOSING_STATUS);
		vmDao.updateVm(vmInstance);
		ShutdownVmThread shutdownVmThread = new ShutdownVmThread(id, connection, vmDao);
		new Thread(shutdownVmThread).start();
		return vmInstance;
	}

	@Override
	public VmInstance restartVm(String id) {
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_RESTARTING_STATUS);
		vmDao.updateVm(vmInstance);
		ReStartVMThread startVMThread = new ReStartVMThread(id, connection, vmDao);
		new Thread(startVMThread).start();
		return vmInstance;
	}

	@Override
	public List<VmInstance> deleteVms(String ids) {
		List<VmInstance> vmInstances = new ArrayList<VmInstance>();
		String[] selectedIds = ids.split(";");
		for (String id : selectedIds) {
			VmInstance vmInstance = vmDao.selectVmById(id);
			vmInstance.setStatus(CommonConstants.VM_DELETING_STATUS);
			vmInstance.setUpdateTime(new Date());
			vmInstances.add(vmInstance);
			vmDao.updateVm(vmInstance);
			DeleteVmsThread deleteVmsThread = new DeleteVmsThread(vmInstance, connection, vmDao);
			new Thread(deleteVmsThread).start();

		}
		return vmInstances;
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
		initVmInstance(vmName, clusterId, imageId, cpuCount, memoryAlloSize, storageId);
		vmDao.insertVm(vmInstance);
		AddVmThread addVmThread = new AddVmThread(connection, vmInstance, userDisk, srStorage, vmDao, imageDao);
		new Thread(addVmThread).start();
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
		initVmInstance(vmName, clusterId, imageId, cpuCount, memoryAlloSize, storageId);
		for (int i = 0; i < vmCount; i++) {
			vmDao.insertVm(vmInstance);
			new Thread(new Runnable() {
				@Override
				public void run() {
					SR sr = null;
					try {
						sr = SR.getByUuid(connection, srStorage.getUuid());
						synchronized (vmInstance) {
							// createVmByImage(vmInstance, userDisk, sr);
							vmInstance.notifyAll();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		return backType;
	}

	private void initVmInstance(String name, String clusterId, String imageId, int cpuCount, int memorySize,
			String storageId) {
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
		vmInstance.setMemory(memorySize * 1024);
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
		if (!StringUtils.isEmpty(userDisk)) {
			String[] userDisks = userDisk.split(";");
			for (String diskSize : userDisks) {
				userDiskTotal += Integer.parseInt(diskSize);
			}
		}
		return userDiskTotal;
	}

	@Override
	public VmInstance getVmInstanceById(String id) {
		return vmDao.selectVmById(id);
	}

	@Override
	public int modifyVm(String cpu, String memory, VmInstance modifiedVm) {
		int result = 0;
		long cpuNumber = Long.parseLong(cpu);
		long memorySize = Long.parseLong(memory);
		if (cpuNumber > getClusterCpuCount(modifiedVm.getClusterId())) {
			result = -1;
			return result;
		}
		modifiedVm.setStatus(CommonConstants.VM_EDITING_STATUS);
		vmDao.updateVm(modifiedVm);
		ModifyVMThread modifyVMThread = new ModifyVMThread(modifiedVm, connection, memorySize, cpuNumber, vmDao);
		new Thread(modifyVMThread).start();

		return result;
	}

	private double getClusterMemoryFreeSize() throws Exception {
		FetchDynamicData data = new FetchDynamicData();
		double memoryFreeSize = 0;
		memoryFreeSize = (double) data.getIndexNeedInfo().get("memory_free");
		return memoryFreeSize;
	}

	@Override
	public List<VmInstance> getVmInstancesByIds(String ids) {
		List<VmInstance> vmInstances = new ArrayList<VmInstance>();
		String[] selectedIds = ids.split(";");
		for (String id : selectedIds) {
			VmInstance vmInstance = vmDao.selectVmById(id);
			if (!StringUtils.isEmpty(vmInstance)) {
				vmInstances.add(vmInstance);
			}
		}
		return vmInstances;
	}

	@Override
	public List<VmInstance> getVmCountByClusterId(String clusterId) {
		List<VmInstance> vmInstances = new ArrayList<>();
		for (VmInstance vmInstance : vmDao.selectVmInstanceByClusterId(clusterId)) {
			if (vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS) {
				vmInstances.add(vmInstance);
			}
		}
		return vmInstances;
	}

	@Override
	public List<VmInstance> getVmCountByHostId(String hostId) {
		List<VmInstance> vmInstances = new ArrayList<>();
		for (VmInstance vmInstance : vmDao.selectVmInstanceByHostId(hostId)) {
			if (vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS) {
				vmInstances.add(vmInstance);
			}
		}
		return vmInstances;
	}

	@Override
	public int getUserDiskSize(String vmId) {
		List<VmStorage> storages = vmDao.selectVmStorageByVmId(vmId);
		int userDiskSize = 0;
		for (VmStorage vmStorage : storages) {
			if (vmStorage.getStorageType().equals("user") && !vmStorage.getName().endsWith(".iso")) {
				userDiskSize += vmStorage.getSize();
			}
		}
		return userDiskSize;
	}

}
