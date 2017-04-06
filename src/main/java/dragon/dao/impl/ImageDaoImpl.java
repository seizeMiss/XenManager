package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.VmInstance;
@Repository
public class ImageDaoImpl extends HibernateUtils implements ImageDao {

	@Override
	public void insertImage(Image image) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = sessionFactory.openSession();
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
			session = sessionFactory.openSession();
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
			session = sessionFactory.openSession();
			session.beginTransaction();
			String hql = "from Image";
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
			session = sessionFactory.openSession();
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

	@Override
	public List<Image> selectImageByCondition(String imageName, int status, String imageOsType) {
		// TODO Auto-generated method stub
		Session session = null;
		List<Image> images = null;
		try {
			session = getSession();
			session.beginTransaction();
			StringBuffer hql = new StringBuffer();
			if(status == 0){
				hql.append("from Image where name like '" + imageName 
					+ "%' and osType like '" + imageOsType + "%'");
			}else{
				hql.append("from Image where name like '" + imageName 
					+ "%' and status=" + status + " and osType like '" + imageOsType + "%'");
			}
			Query query = session.createQuery(hql.toString());
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
	public void deleteImage(String id) {
		// TODO Auto-generated method stub
		Session session = null;
		Image image = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			image = (Image) session.load(Image.class, id);
			session.delete(image);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
	}

	@Override
	public List<Image> selectImageByName(String name) {
		Session session = null;
		List<Image> images = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from Image where name like '" + name + "%'";
			System.out.println(hql);
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

}
