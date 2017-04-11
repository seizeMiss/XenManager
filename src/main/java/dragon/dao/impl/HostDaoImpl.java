package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.HostDao;
import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.utils.StringUtils;

@Repository
@Transactional
public class HostDaoImpl extends HibernateUtils implements HostDao {

	@Override
	public void insertHost(List<HostInstance> hosts) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			for(HostInstance hostInstance : hosts){
				session.save(hostInstance);
			}
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
	public void updateHost(List<HostInstance> hosts) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			for(HostInstance hostInstance : hosts){
				session.update(hostInstance);
			}
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
	public List<HostInstance> selectAllHost() {
		// TODO Auto-generated method stub
		Session session = null;
		List<HostInstance> hosts = null;
		try {
			session = sessionFactory.openSession();
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
			host = (HostInstance) session.get(HostInstance.class, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return host;
	}

	@Override
	public List<HostInstance> selectHostByClusterId(String id) {
		Session session = null;
		List<HostInstance> hosts = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from HostInstance where clusterId = ?";
			Query query = queryByParams(session, hql, id);
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
	public HostInstance selectHostByUuid(String uuid) {
		Session session = null;
		List<HostInstance> hosts = null;
		HostInstance hostInstance = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from HostInstance where uuid = ?";
			Query query = queryByParams(session, hql, uuid);
			hosts = query.list();
			if(!StringUtils.isEmpty(hosts)){
				hostInstance = hosts.get(0);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		
		return hostInstance;
	}

}
