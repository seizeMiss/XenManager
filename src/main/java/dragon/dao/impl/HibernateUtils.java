package main.java.dragon.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateUtils {
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession(){
		Session session = sessionFactory.getCurrentSession();
		if(session.isOpen()){
			return session;
		}
		return sessionFactory.openSession();
	}
	
	protected void closeSession(Session session){
		if(session != null){
			if(session.isOpen()){
				session.close();
			}
		}
	}

}
