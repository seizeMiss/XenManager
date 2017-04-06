package main.java.dragon.timer;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.service.impl.ConnectionUtil;
import main.java.dragon.utils.CommonConstants;

@Component
public class VmInfoTimer extends ConnectionUtil {
	
	@Autowired
	private VMDao vmDao;
	
	public VmInfoTimer() throws Exception {
		super();
	}

	@Scheduled(cron = "0/30 * * * * ?") // 每十秒更新一次
	public void refreshVmInfo() {
		List<VmInstance> vmInstances = vmDao.selectAllVm();
		try {
			for (VmInstance vmInstance : vmInstances) {
				if(vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS){
					VM vm = VM.getByUuid(connection, vmInstance.getUuid());
					String realPowerStatus = vm.getPowerState(connection).toString();
					if (!vmInstance.getPowerStatus().equals(realPowerStatus)) {
						VmInstance modifiedVmInstance = getVmInstanceByVM(vm, vmInstance);
						vmDao.updateVm(modifiedVmInstance);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private VmInstance getVmInstanceByVM(VM vm, VmInstance vmInstance) throws Exception{
		String name = "";
		int cpu = 0;
		int memory = 0;
		int systemDiskSize = 0;
		int status = 0;
		String realPowerStatus = "";
		if(vm != null && !vm.isNull()){
			name = vm.getNameLabel(connection);
			systemDiskSize = getSystemDiskSize(vm);
			realPowerStatus = vm.getPowerState(connection).toString();
			cpu = vm.getMetrics(connection).getVCPUsNumber(connection).intValue();
			memory = (int) (vm.getMemoryTarget(connection) / 1024 / 1024);
			status = getStatusByPowerStatus(realPowerStatus);
			vmInstance.setSystemDisk(systemDiskSize);
			vmInstance.setPowerStatus(realPowerStatus);
			if(realPowerStatus.equals(CommonConstants.VM_POWER_CLOSED)){
				vmInstance.setCpu(cpu);
				vmInstance.setMemory(memory);
			}
			vmInstance.setStatus(status);
			vmInstance.setUpdateTime(new Date());
			vmInstance.setName(name);
		}
		return vmInstance;
	}
	
	private int getSystemDiskSize(VM vm) throws Exception{
		int systemDiskSize = 0;
		Set<VBD> vbds = vm.getVBDs(connection);
		for(VBD vbd : vbds){
			VDI vdi = vbd.getVDI(connection);
			System.out.println(vdi.getType(connection));
			if(vdi.getType(connection).toString().equals("system")){
				systemDiskSize = (int) (vdi.getVirtualSize(connection)/1024/1024/1024);
			}
		}
		return systemDiskSize;
	}

	private int getStatusByPowerStatus(String powerStatus) {
		int status = 0;
		switch (powerStatus) {
		case CommonConstants.VM_POWER_CLOSED:
			status = CommonConstants.VM_CLOSE_STATUS;
			break;
		case CommonConstants.VM_POWER_CLOSING:
			status = CommonConstants.VM_CLOSING_STATUS;
			break;
		case CommonConstants.VM_POWER_CREATING:
			status = CommonConstants.IMAGE_CREATING_STATUS;
			break;
		case CommonConstants.VM_POWER_DELETING:
			status = CommonConstants.IMAGE_DELETING_STATUS;
			break;
		case CommonConstants.VM_POWER_RESTARTING:
			status = CommonConstants.VM_RESTARTING_STATUS;
			break;
		case CommonConstants.VM_POWER_START:
			status = CommonConstants.VM_OPEN_STATUS;	
			break;
		default:
			break;
		}

		return status;
	}

}
