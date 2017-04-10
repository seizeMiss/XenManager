package main.java.dragon.thread;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.utils.CommonConstants;

public class ModifyVMThread implements Runnable {
	
	private VMDao vmDao;
	private VmInstance vmInstance;
	private Connection connection;
	private long memorySize;
	private long cpuNumber;
	
	public ModifyVMThread(VmInstance vmInstance, Connection connection, long memorySize, long cpuNumber, VMDao vmDao) {
		this.vmInstance = vmInstance;
		this.connection = connection;
		this.memorySize = memorySize;
		this.cpuNumber = cpuNumber;
		this.vmDao = vmDao;
	}

	@Override
	public void run() {
		try {
			VM vm = VM.getByUuid(connection, vmInstance.getUuid());
			modfiyVmConfig(vm, memorySize, cpuNumber, vmInstance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 修改虚拟机配置
	private void modfiyVmConfig(VM vm, long memory, long cpu, VmInstance vmInstance) throws Exception {

		long memorySize = memory * 1024 * 1024 * 1024;
		vm.setMemoryLimits(connection, memorySize, memorySize, memorySize, memorySize);
		VM.Record record = vm.getRecord(connection);

		// 修改CPU配置时，需要对CPU的个数进行判断，并确定配置的先后顺序。
		if (record.VCPUsAtStartup > cpu) {
			vm.setVCPUsAtStartup(connection, cpu);
			vm.setVCPUsMax(connection, cpu);
		} else {
			vm.setVCPUsMax(connection, cpu);
			vm.setVCPUsAtStartup(connection, cpu);
		}
		vmInstance.setStatus(CommonConstants.VM_CLOSE_STATUS);
		vmInstance.setPowerStatus(CommonConstants.VM_POWER_CLOSED);
		vmInstance.setUpdateTime(new Date());
		vmInstance.setCpu((int) cpu);
		vmInstance.setMemory((int) memory * 1024);
		vmDao.updateVm(vmInstance);
	}

}
