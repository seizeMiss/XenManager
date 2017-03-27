package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.StorageDao;
import main.java.dragon.pojo.Storage;

@Repository
@Transactional
public class StorageDaoImpl extends HibernateUtils implements StorageDao {

	@Override
	public void insertStorage(Storage storage) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.save(storage);
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
	public void updateStorage(Storage storage) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.update(storage);
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
	public List<Storage> selectAllStorage() {
		// TODO Auto-generated method stub
		Session session = null;
		List<Storage> storages = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from Storage";
			Query query = session.createQuery(hql);
			storages = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		
		return storages;
	}

	@Override
	public Storage selectStorageById(String id) {
		// TODO Auto-generated method stub
		Session session = null;
		Storage storage = null;
		try {
			session = getSession();
			session.beginTransaction();
			storage = (Storage) session.get(Storage.class, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return storage;
	}

}
