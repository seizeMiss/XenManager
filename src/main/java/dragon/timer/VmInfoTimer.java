package main.java.dragon.timer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VMGuestMetrics;

import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.service.impl.ConnectionUtil;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.StringUtils;

@Component
public class VmInfoTimer extends ConnectionUtil {
	
	@Autowired
	private VMDao vmDao;
	
	public VmInfoTimer() throws Exception {
		super();
	}

	@Scheduled(cron = "0/60 * * * * ?") // 每一分钟更新一次
	public void refreshVmInfo() {
		List<VmInstance> vmInstances = vmDao.selectAllVm();
		try {
			if(!StringUtils.isEmpty(vmInstances)){
				for (VmInstance vmInstance : vmInstances) {
					//检测虚拟机的状态是否是删除、创建中、不可用状态
					if(vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS
							&& vmInstance.getStatus() != CommonConstants.VM_CREATING_STATUS
							&& vmInstance.getStatus() != CommonConstants.VM_NO_AVAILABEL_STATUS){
						VM vm = VM.getByUuid(connection, vmInstance.getUuid());
						String realPowerStatus = vm.getPowerState(connection).toString();
						//判断是否需要更新数据，当虚拟机真实状态和数据库的状态不一致的时候
						if (!vmInstance.getPowerStatus().equals(realPowerStatus)) {
							VmInstance modifiedVmInstance = getVmInstanceByVM(vm, vmInstance);
							vmDao.updateVm(modifiedVmInstance);
						}
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
			String ipAddress = "-";
			
			VMGuestMetrics guestMetrics = vm.getGuestMetrics(connection);
			if(guestMetrics != null && !guestMetrics.isNull()){
				Map<String, String> networkInfo = guestMetrics.getNetworks(connection);
				VIF vif = getDefaultVIF(vm);
				String ipKey = vif.getDevice(connection) + "/ip";
				if(networkInfo != null && networkInfo.size() > 0){
					ipAddress = networkInfo.get(ipKey);
				}
			}
			vmInstance.setVmIp(ipAddress);
			vmInstance.setSystemDisk(systemDiskSize);
			vmInstance.setPowerStatus(realPowerStatus);
			if(!realPowerStatus.equals(CommonConstants.VM_POWER_CLOSED)){
				vmInstance.setCpu(cpu);
				vmInstance.setMemory(memory);
			}
			vmInstance.setStatus(status);
			vmInstance.setUpdateTime(new Date());
			vmInstance.setName(name);
		}
		return vmInstance;
	}
	private VIF getDefaultVIF(VM vm) throws Exception{
		Set<VIF> vifs = vm.getVIFs(connection);
		VIF defaultVif = null;
		for(VIF vif : vifs){
			if(vif != null && !vif.isNull()){
				defaultVif = vif;
			}
		}
		return defaultVif;
	}
	
	private int getSystemDiskSize(VM vm) throws Exception{
		int systemDiskSize = 0;
		Set<VBD> vbds = vm.getVBDs(connection);
		for(VBD vbd : vbds){
			VDI vdi = vbd.getVDI(connection);
//			System.out.println(vdi.getType(connection));
			if(!vdi.isNull() && vdi.getType(connection).toString().equals("system")){
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
