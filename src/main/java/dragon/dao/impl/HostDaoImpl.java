package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.HostDao;
import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;

@Repository
@Transactional
public class HostDaoImpl extends HibernateUtils implements HostDao {

	@Override
	public void insertHost(HostInstance host) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.save(host);
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
	public void updateHost(HostInstance host) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.update(host);
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
	public List<HostInstance> selectAllHost() {
		// TODO Auto-generated method stub
		Session session = null;
		List<HostInstance> hosts = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from HostInstance";
			Query query = session.createQuery(hql);
			hosts = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		
		return hosts;
	}

	@Override
	public HostInstance selectHostById(String id) {
		// TODO Auto-generated method stub
		Session session = null;
		HostInstance host = null;
		try {
			session = getSession();
			session.beginTransaction();
			host = (HostInstance) session.get(Cluster.class, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return host;
	}

}
