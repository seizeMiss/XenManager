package main.java.dragon.thread;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.xenapi.XenApiUtil;

public class StartVmThread implements Runnable{
	
	private VMDao vmDao;
	private String id;
	private Connection connection;
	
	public StartVmThread(String id, Connection connection,VMDao vmDao) {
		this.id = id;
		this.connection = connection;
		this.vmDao = vmDao;
	}

	@Override
	public void run() {
		try {
			openVm(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void openVm(String id) throws Exception {
		VM vm = getVmByVmInstanceId(id);
		Task task = vm.startAsync(connection, false, false);
		XenApiUtil.waitForTask(connection, task, 2000);
		VmInstance vmInstance = vmDao.selectVmById(id);
		vmInstance.setStatus(CommonConstants.VM_OPEN_STATUS);
		vmInstance.setPowerStatus(CommonConstants.VM_POWER_START);
		vmInstance.setUpdateTime(new Date());
		vmDao.updateVm(vmInstance);
	}
	
	private VM getVmByVmInstanceId(String id) throws Exception {
		VmInstance vmInstance = vmDao.selectVmById(id);
		return VM.getByUuid(connection, vmInstance.getUuid());
	}

}
