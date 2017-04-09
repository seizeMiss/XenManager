package main.java.dragon.thread;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.utils.CommonConstants;

public class DeleteVmsThread implements Runnable {

	@Autowired
	private VMDao vmDao;

	private VmInstance vmInstance;
	private Connection connection;
	
	public DeleteVmsThread(VmInstance vmInstance, Connection connection) {
		this.vmInstance = vmInstance;
		this.connection = connection;
	}
	
	@Override
	public void run() {
		try {
			VM vm = VM.getByUuid(connection, vmInstance.getUuid());
			deleteVm(vm, vmInstance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除虚拟机
	private synchronized void deleteVm(VM vm, VmInstance vmInstance) throws Exception {
		Set<VBD> vbds = vm.getVBDs(connection);
		for (VBD vbd : vbds) {
			VDI vdi = vbd.getVDI(connection);
			if (!vdi.getType(connection).toString().endsWith(".iso")) {
				vdi.destroy(connection);
			}
			if (null != vbd && !vbd.isNull()) {
				// vbd.destroy(connection);
			}
		}
		vm.destroy(connection);

		vmInstance.setPowerStatus(CommonConstants.VM_POWER_DELETED);
		vmInstance.setStatus(CommonConstants.VM_DELETED_STATUS);
		vmInstance.setUpdateTime(new Date());
		vmDao.updateVm(vmInstance);
	}

}
