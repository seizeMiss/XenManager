package main.java.dragon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.dao.impl.AsynSessionThread;
import main.java.dragon.pojo.Image;
import main.java.dragon.service.ImageService;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.ImageAPI;
import main.java.dragon.xenapi.VmAPI;
import main.java.dragon.xenapi.XenApiUtil;

@Service
@Transactional
public class ImageServiceImpl extends ConnectionUtil implements ImageService{
	@Autowired
	private ImageDao imageDao;
	
	public ImageServiceImpl() throws Exception {
		super();
	}

	@Override
	public boolean addImage(Image image) {
		ImageAPI imageAPI = null;
		VmAPI vmAPI = null;
		try {
			imageAPI = new ImageAPI();
			vmAPI = new VmAPI();
			String uuid = image.getUuid();
			VM vm = imageAPI.getVmByUuid(uuid);
			String id = StringUtils.generateUUID();
			image.setId(id);
			image.setCreateTime(new Date());
			image.setImageSize(0);
			image.setStatus(CommonConstants.IMAGE_CREATING_STATUS);
			Map<String, String> map = vmAPI.getOsVersion(vm);
			if(map != null){
				image.setOsType(StringUtils.isEmpty(map.get("distro")) ? "" : map.get("distro"));
				image.setOsName(StringUtils.isEmpty(map.get("name")) ? "" : map.get("name").split("\\|")[0]);
			}
			imageDao.insertImage(image);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						VDI copyVdi = createImageByVm(vm, image.getName());
						if(!copyVdi.isNull()){
							image.setImageSize((int)(copyVdi.getVirtualSize(connection)/1024/1024/1024));
							image.setUuid(copyVdi.getUuid(connection));
							image.setStatus(CommonConstants.IMAGE_AVAILABLE_STATUS);
							imageDao.saveImage(image);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}).start();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public VDI createImageByVm(VM vm, String name) throws Exception {
		
		VDI srcVdi = new ImageAPI().getSystemVdiByVm(vm);
		// 对vdi仅限拷贝
		Task task = srcVdi.copyAsync(connection, srcVdi.getSR(connection));
		XenApiUtil.waitForTask(connection, task, 2000);
		
		VDI newVdi = Types.toVDI(task, connection);
		newVdi.setNameLabel(connection, name);
		
		return newVdi;
	}
	@Override
	public void updateImage(Image image) {
		imageDao.saveImage(image);
	}

	@Override
	public List<Image> getAllImages() {
		List<Image> images = new ArrayList<Image>();
		for(Image image : imageDao.selectAllImage()){
			if(image.getStatus() != CommonConstants.IMAGE_DELETED_STATUS){
				images.add(image);
			}
		}
		return images;
	}

	@Override
	public Image getImageById(String id) {
		return imageDao.selectImageById(id);
	}

	@Override
	public List<Image> getImagesByCondition(String imageName, String imageStatus, String imageOsType) {
		int status = 0;
		if(!StringUtils.isEmpty(imageStatus)){
			if(imageStatus.equals(CommonConstants.IMAGE_AVAILABLE)){
				status = 1;
			}
			if(imageStatus.equals(CommonConstants.IMAGE_NO_AVAILABEL)){
				status = -1;
			}
		}
		imageName = StringUtils.isEmpty(imageName) ? "" : imageName;
		imageOsType = StringUtils.isEmpty(imageOsType) ?  "" : imageOsType;
		List<Image> images = new ArrayList<Image>();
		for(Image image : imageDao.selectImageByCondition(imageName, status, imageOsType)){
			if(image.getStatus() != CommonConstants.IMAGE_DELETED_STATUS){
				images.add(image);
			}
		}
		return images;
	}

	@Override
	public boolean deleteImages(String ids) {
		Image image = imageDao.selectImageById(ids);
		if(StringUtils.isEmpty(image)){
			return false;
		}
		String uuid = image.getUuid();
		image.setStatus(CommonConstants.IMAGE_DELETING_STATUS);
		imageDao.saveImage(image);
		new Thread(new Runnable() {
			public void run() {
				try {
					deleteImage(uuid,image);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return true;
	}
	
	private boolean deleteImage(String uuid,Image image) throws Exception{
		VDI vdi = VDI.getByUuid(connection, uuid);
		Task task = vdi.destroyAsync(connection);
		XenApiUtil.waitForTask(connection, task, 2000);
		image.setStatus(CommonConstants.IMAGE_DELETED_STATUS);
		imageDao.saveImage(image);
		return true;
	}

	@Override
	public List<Image> getImagesByName(String name) {
		// TODO Auto-generated method stub
		List<Image> images = new ArrayList<Image>();
		for(Image image : imageDao.selectImageByName(name)){
			if(image.getStatus() != CommonConstants.IMAGE_DELETED_STATUS){
				images.add(image);
			}
		}
		return images;
	}

	@Override
	public List<Image> getImagesByIds(String ids) {
		// TODO Auto-generated method stub
		List<Image> images = new ArrayList<Image>();
		String[] selectedIds = ids.split(";");
		for(String id : selectedIds){
			Image image = imageDao.selectImageById(id);
			images.add(image);
		}
		return images;
	}
	

}
