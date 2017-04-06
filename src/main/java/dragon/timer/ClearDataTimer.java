package main.java.dragon.timer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;
import main.java.dragon.utils.CommonConstants;

@Component
public class ClearDataTimer {
	@Autowired
	private VMDao vmDao;
	@Autowired
	private ImageDao imageDao;
	
	@Scheduled(cron="0 0 10 * * ?")
	public void clearImageAndVmInfo(){
		List<Image> images = imageDao.selectAllImage();
		for(Image image : images){
			if(image.getStatus() == CommonConstants.IMAGE_DELETED_STATUS){
				imageDao.deleteImage(image.getId());
			}
		}
		List<VmInstance> vmInstances = vmDao.selectAllVm();
		for(VmInstance vmInstance : vmInstances){
			if(vmInstance.getStatus() == CommonConstants.VM_DELETED_STATUS){
				List<VmStorage> vmStorages = vmDao.selectVmStorageByVmId(vmInstance.getId());
				List<VmNetwork> vmNetworks = vmDao.selectVmNetwrokByVmId(vmInstance.getId());
				vmInstance.setVmNetWorks(listVmVmNetwork2SetVmNetwork(vmNetworks));
				vmInstance.setVmStorages(listVmStorage2SetVmStorage(vmStorages));
				vmDao.deleteVm(vmInstance.getId());
			}
		}
	}
	
	private Set<VmStorage> listVmStorage2SetVmStorage(List<VmStorage> objects){
		Set<VmStorage> oSet = new HashSet<VmStorage>();
		for(VmStorage object : objects){
			oSet.add(object);
		}
		return oSet;
	}
	
	private Set<VmNetwork> listVmVmNetwork2SetVmNetwork(List<VmNetwork> objects){
		Set<VmNetwork> oSet = new HashSet<VmNetwork>();
		for(VmNetwork object : objects){
			oSet.add(object);
		}
		return oSet;
	}

}
