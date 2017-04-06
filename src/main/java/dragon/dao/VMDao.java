package main.java.dragon.dao;

import java.util.List;
import java.util.Set;

import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;

public interface VMDao {
	public void insertVm(VmInstance vmInstance);
	public void updateVm(VmInstance vmInstance);
	public void updateVmAndInsertOther(VmInstance vmInstance);
	public List<VmInstance> selectAllVm();
	public VmInstance selectVmById(String id);
	public List<VmStorage> selectVmStorageByVmId(String id);
	public List<VmNetwork> selectVmNetwrokByVmId(String id);
	public VmInstance deleteVm(String id);
	public List<VmInstance> selectVmInstanceByName(String name);
	public List<VmInstance> selectVmInstanceByCondition(String vmName, int status, String vmOsType);
	public List<VmInstance> selectVmInstanceByClusterId(String clusterId);
	public List<VmInstance> selectVmInstanceByHostId(String hostId);
}
