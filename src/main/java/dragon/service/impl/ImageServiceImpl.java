package main.java.dragon.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.mysql.fabric.xmlrpc.base.Data;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.pojo.Image;
import main.java.dragon.service.ImageService;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.ComputerAPI;
import main.java.dragon.xenapi.ImageAPI;
import main.java.dragon.xenapi.VmAPI;

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
		VM vm = null;
		VDI copyVdi = null;
		try {
			imageAPI = new ImageAPI();
			vmAPI = new VmAPI();
			String uuid = image.getUuid();
			vm = imageAPI.getVmByUuid(uuid);
			copyVdi = imageAPI.createImageByVm(vm, image.getName());
			if(!copyVdi.isNull()){
				image.setCreateTime(new Date());
				image.setId(StringUtils.generateUUID());
				image.setImageSize((int)(copyVdi.getVirtualSize(connection)/1024/1024/1024));
				Map<String, String> map = vmAPI.getOsVersion(vm);
				if(map != null){
					image.setOsType(StringUtils.isEmpty(map.get("distro")) ? "" : map.get("distro"));
					image.setOsType(StringUtils.isEmpty(map.get("distro")) ? "" : map.get("name").split("|")[0]);
				}
				image.setUuid(copyVdi.getUuid(connection));
				imageDao.insertImage(image);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public void updateImage(Image image) {
		imageDao.saveImage(image);
	}

	@Override
	public List<Image> getAllImages() {
		return imageDao.selectAllImage();
	}

	@Override
	public Image getImageById(String id) {
		return imageDao.selectImageById(id);
	}

}
