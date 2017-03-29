package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.pojo.Image;
@Repository
@Transactional
public class ImageDaoImpl extends HibernateUtils implements ImageDao {

	@Override
	public void insertImage(Image image) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.save(image);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
	}

	@Override
	public void saveImage(Image image) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.update(image);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
//			session.getTransaction().rollback();
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
	}

	@Override
	public List<Image> selectAllImage() {
		// TODO Auto-generated method stub
		Session session = null;
		List<Image> images = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from HostInstance";
			Query query = session.createQuery(hql);
			images = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		
		return images;
	}

	@Override
	public Image selectImageById(String id) {
		// TODO Auto-generated method stub
		Session session = null;
		Image image = null;
		try {
			session = getSession();
			session.beginTransaction();
			image = (Image) session.get(Image.class, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return image;
	}

}
