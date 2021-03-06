package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.Account;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;
import main.java.dragon.utils.StringUtils;

@Repository
@Transactional
public class VmDaoImpl extends HibernateUtils implements VMDao{

	@Override
	public void insertVm(VmInstance vmInstance) {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.save(vmInstance);
			if(!StringUtils.isEmpty(vmInstance.getVmStorages())){
				for(VmStorage vmStorage : vmInstance.getVmStorages()){
					session.save(vmStorage);
				}
			}
			if(!StringUtils.isEmpty(vmInstance.getVmNetWorks())){
				for(VmNetwork vmNetwork : vmInstance.getVmNetWorks()){
					session.save(vmNetwork);
				}
			}
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
	}
	@Override
	public void updateVmAndInsertOther(VmInstance vmInstance) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			if(!StringUtils.isEmpty(vmInstance.getVmStorages())){
				for(VmStorage vmStorage : vmInstance.getVmStorages()){
					session.save(vmStorage);
				}
			}
			if(!StringUtils.isEmpty(vmInstance.getVmNetWorks())){
				for(VmNetwork vmNetwork : vmInstance.getVmNetWorks()){
					session.save(vmNetwork);
				}
			}
			session.update(vmInstance);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
	}

	@Override
	public void updateVm(VmInstance vmInstance) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.update(vmInstance);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
	}

	@Override
	public List<VmInstance> selectAllVm() {
		Session session = null;
		List<VmInstance> vmInstances = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			String hql = "from VmInstance";
			Query query = session.createQuery(hql);
			vmInstances = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		
		return vmInstances;
	}

	@Override
	public VmInstance selectVmById(String id) {
		Session session = null;
		VmInstance vmInstance = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			vmInstance = (VmInstance) session.get(VmInstance.class, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return vmInstance;
	}

	@Override
	public List<VmStorage> selectVmStorageByVmId(String id) {
		Session session = null;
		List<VmStorage> selectVmStorage = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			String hql = "from VmStorage where vmId = ?";
			Query query = queryByParams(session, hql, id);
			selectVmStorage = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return selectVmStorage;
	}

	@Override
	public List<VmNetwork> selectVmNetwrokByVmId(String id) {
		Session session = null;
		List<VmNetwork> selectVmNetworks = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			String hql = "from VmStorage where vmId = ?";
			Query query = queryByParams(session, hql, id);
			selectVmNetworks = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return selectVmNetworks;
	}

	@Override
	public VmInstance deleteVm(String id) {
		Session session = null;
		VmInstance vmInstance = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			vmInstance = (VmInstance) session.load(Account.class, id);
			session.delete(vmInstance);
			List<VmStorage> vmStorages = selectVmStorageByVmId(id);
			List<VmNetwork> vmNetworks = selectVmNetwrokByVmId(id);
			for(VmStorage vmStorage : vmStorages){
				session.delete(vmStorage);
			}
			for(VmNetwork vmNetwork : vmNetworks){
				session.delete(vmNetwork);
			}
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
		return vmInstance;
	}

	@Override
	public List<VmInstance> selectVmInstanceByName(String name) {
		Session session = null;
		List<VmInstance> vmInstances = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from VmInstance where name like '" + name + "%'";
			System.out.println(hql);
			Query query = session.createQuery(hql);
			vmInstances = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return vmInstances;
	}

	@Override
	public List<VmInstance> selectVmInstanceByCondition(String vmName, int status, String vmOsType) {
		Session session = null;
		List<VmInstance> vmInstances = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "";
			if(status == 0){
				hql = "from VmInstance where name like '" + vmName + "%' and "
						+ "osType like '" + vmOsType + "%'";
			}else{
				hql = "from VmInstance where name like '" + vmName + "%' and "
						+ "status =" + status + " and osType like '" + vmOsType + "%'";
			}
			System.out.println(hql);
			Query query = session.createQuery(hql);
			vmInstances = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return vmInstances;
	}
	@Override
	public List<VmInstance> selectVmInstanceByClusterId(String clusterId) {
		Session session = null;
		List<VmInstance> vmInstances = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from VmInstance where clusterId = ?";
			System.out.println(hql);
			Query query = queryByParams(session, hql, clusterId);
			vmInstances = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return vmInstances;
	}
	@Override
	public List<VmInstance> selectVmInstanceByHostId(String hostId) {
		Session session = null;
		List<VmInstance> vmInstances = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from VmInstance where hostId = ?";
			Query query = queryByParams(session, hql, hostId);
			vmInstances = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return vmInstances;
	}


}
