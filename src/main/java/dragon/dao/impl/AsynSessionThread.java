package main.java.dragon.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.pojo.Image;
import main.java.dragon.xenapi.ImageAPI;
import main.java.dragon.xenapi.XenApiUtil;

@Repository
@Transactional
public class AsynSessionThread implements Runnable{
	private VM vm;
	private Image image;
	private Connection connection;
	@Resource
	private SessionFactory sessionFactory;
	
	public AsynSessionThread() {
		// TODO Auto-generated constructor stub
	}
	
	public AsynSessionThread(VM vm, Image image, Connection connection, SessionFactory sessionFactory) {
		// TODO Auto-generated constructor stub
		this.vm = vm;
		this.image = image;
		this.connection = connection;
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, session);
		VDI copyVdi = null;
		try {
			copyVdi = createImageByVm(vm, image.getName());
			if(!copyVdi.isNull()){
				image.setImageSize((int)(copyVdi.getVirtualSize(connection)/1024/1024/1024));
				image.setUuid(copyVdi.getUuid(connection));
				image.setStatus(1);
				session.beginTransaction();
				session.update(image);
				session.flush();
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			if(session != null){
				session.close();
			}
		}
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

}
