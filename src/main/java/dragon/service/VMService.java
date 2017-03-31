package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;

public interface VMService {
	public void addVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork) throws Exception;
	public void saveVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork);
	public List<VmInstance> getAllVm();
	public void deleteVm(String ids);
	public List<VmInstance> getVmInstanceByName(String name);
	public List<VmInstance> getVmInstanceByCondition(String vmName,String vmStatus,String vmOsType);
	public VmInstance restartVm(String id);
	public VmInstance startVm(String id);
	public VmInstance closeVm(String id);
	public List<VmInstance> deleteVms(String ids);
	
}
