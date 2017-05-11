package test.java;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateTest{
	
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
	@Before
	public void init(){
		Configuration configuration  = new Configuration().configure();
		
		ServiceRegistry serviceRegistry = (ServiceRegistry) new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}
	
	@After
	public void destroy(){
		transaction.commit();
		session.close();
		sessionFactory.close();
	}

	@Test
	public void demo(){
		
	}
}
