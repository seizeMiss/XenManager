package main.java.dragon.thread;

import org.springframework.beans.factory.annotation.Autowired;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.VDI;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.pojo.Image;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.xenapi.XenApiUtil;

public class DeleteImageThread implements Runnable{
	@Autowired
	private ImageDao imageDao;
	
	private String uuid;
	private Image image;
	private Connection connection;
	
	public DeleteImageThread(String uuid, Image image, Connection connection) {
		this.image = image;
		this.uuid = uuid;
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			deleteImage(uuid, image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean deleteImage(String uuid,Image image) throws Exception{
		VDI vdi = VDI.getByUuid(connection, uuid);
		Task task = vdi.destroyAsync(connection);
		XenApiUtil.waitForTask(connection, task, 2000);
		image.setStatus(CommonConstants.IMAGE_DELETED_STATUS);
		imageDao.saveImage(image);
		return true;
	}
	
}
