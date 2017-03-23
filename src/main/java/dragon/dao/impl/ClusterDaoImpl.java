package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import main.java.dragon.dao.IClusterDao;
import main.java.dragon.pojo.Cluster;

public class ClusterDaoImpl extends HibernateUtils implements IClusterDao{

	@Override
	public void insertCluster(Cluster cluster) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.save(cluster);
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
	public void updateCluster(Cluster cluster) {
		// TODO Auto-generated method stub
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.update(cluster);
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
	public List<Cluster> selectAllClusters() {
		Session session = null;
		List<Cluster> clusters = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from Cluster";
			Query query = session.createQuery(hql);
			clusters = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		
		return clusters;
	}

	@Override
	public Cluster selectClusterById(String id) {
		Session session = null;
		Cluster cluster = null;
		try {
			session = getSession();
			session.beginTransaction();
			cluster = (Cluster) session.get(Cluster.class, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cluster;
	}

}
