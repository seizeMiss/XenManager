package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;

public interface VMService {
	public void addVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork) throws Exception;
	public int addVmBatch(String vmName, String vmNumber, String clusterId, String iamgeUuid, String cpuNumber, String memorySize,String storageLocation, String userDisk);
	public int addVm(String vmName, String clusterId, String iamgeUuid, String cpuNumber, String memorySize,String storageLocation, String userDisk);
	public void saveVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork);
	public List<VmInstance> getAllVm();
	public void deleteVm(String ids);
	public List<VmInstance> getVmInstanceByName(String name);
	public List<VmInstance> getVmInstanceByCondition(String vmName,String vmStatus,String vmOsType);
	public VmInstance restartVm(String id);
	public VmInstance startVm(String id);
	public VmInstance closeVm(String id);
	public List<VmInstance> deleteVms(String ids);
	public VmInstance getVmInstanceById(String id);
	public int modifyVm(String cpu, String memory, VmInstance modifyVm);
	public List<VmInstance> getVmInstancesByIds(String ids);
	public List<VmInstance> getVmCountByClusterId(String clusterId);
	public List<VmInstance> getVmCountByHostId(String hostId);
	public int getUserDiskSize(String vmId);
}
