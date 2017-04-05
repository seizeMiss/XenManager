package main.java.dragon.dao;

import java.util.List;

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
	public void deleteVm(String id);
	public List<VmInstance> selectVmInstanceByName(String name);
	public List<VmInstance> selectVmInstanceByCondition(String vmName, int status, String vmOsType);
}
