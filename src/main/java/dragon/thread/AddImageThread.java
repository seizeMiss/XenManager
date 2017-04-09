package main.java.dragon.thread;

import org.springframework.beans.factory.annotation.Autowired;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.pojo.Image;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.xenapi.ImageAPI;
import main.java.dragon.xenapi.XenApiUtil;

public class AddImageThread implements Runnable{
	@Autowired
	private ImageDao imageDao;
	
	private Connection connection;
	private VM vm;
	private Image image;
	
	public AddImageThread(Connection connection, VM vm, Image image) {
		this.image = image;
		this.vm = vm;
		this.connection = connection;
	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		VDI copyVdi;
		try {
			copyVdi = createImageByVm(vm, image.getName());
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
	
	private VDI createImageByVm(VM vm, String name) throws Exception {
		
		VDI srcVdi = new ImageAPI().getSystemVdiByVm(vm);
		// 对vdi仅限拷贝
		Task task = srcVdi.copyAsync(connection, srcVdi.getSR(connection));
		XenApiUtil.waitForTask(connection, task, 2000);
		
		VDI newVdi = Types.toVDI(task, connection);
		newVdi.setNameLabel(connection, name);
		
		return newVdi;
	}

}
