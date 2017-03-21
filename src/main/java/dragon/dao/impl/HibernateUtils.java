package main.java.dragon.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateUtils {
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 获得session
	 * @return
	 */
	protected Session getSession(){
		Session session = sessionFactory.getCurrentSession();
		if(session.isOpen()){
			return session;
		}
		return sessionFactory.openSession();
	}
	/**
	 * 关闭session
	 * @param session
	 */
	protected void closeSession(Session session){
		if(session != null){
			if(session.isOpen()){
				session.close();
			}
		}
	}
	/**
	 * 执行需要输入数据的sql语句
	 * @param session
	 * @param hql
	 * @param objects
	 * @return
	 */
	protected Query queryByParams(Session session,String hql,Object ...objects){
		Query query = session.createQuery(hql);
		for (int i = 0; i < objects.length; i++){
			query.setParameter(i, objects[i]);
		}
		return query;
	}

}
