package main.java.dragon.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

public class ConnectionUtils {
	
	@Autowired
	private static SessionFactory factory;

	/*
	 * 打开Session
	 */

	public static Session getSession() {
		return factory.getCurrentSession();
	}
	/*
	 * 关闭Session
	 */

	public static void closeSession(Session session) {
		if (session != null) {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	public static SessionFactory getSessionFactory() {
		return factory;
	}

}
