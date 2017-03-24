package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.UserDao;
import main.java.dragon.pojo.User;

@Repository
@Transactional
public class UserDaoImpl extends HibernateUtils implements UserDao{
	
	@Override
	public int insertUser(User user) {
		Session session = getSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		return 0;
	}

	@Override
	public List<User> selectAllUser() {
		List<User> users = null;
		Session session = getSession();
		session.beginTransaction();
		String hql = "from User";
		Query query = session.createQuery(hql);
		//设置查询的最大结果集
//		query.setMaxResults(2);
		users = query.list();
		session.getTransaction().commit();
		closeSession(session);
		return users;
	}

	@Override
	public int updateUser(User user) {
		Session session = null;
		try {
			session =  getSession();
			session.beginTransaction();
			User getUser = (User) session.load(User.class, user.getId());
			getUser.setAge(user.getAge());
			getUser.setPassword(user.getPassword());
			session.update(getUser);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
		return 0;
	}

	@Override
	public User deleteUser(int id) {
		Session session = null;
		User user = null;
		try{
			session = getSession();
			session.beginTransaction();
			user = (User)session.load(User.class, id);
			session.delete(user);
			session.flush();
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return user;
	}

	@Override
	public User selectUserById(int id) {
//		Session session = null;
//		User user = null;
//		try {
//			session = getSession();
//			session.beginTransaction();
//		    user = (User)session.load(User.class, id);
//		    session.getTransaction().commit();
//		} catch (Exception e) {
//			session.getTransaction().rollback();
//			e.printStackTrace();
//		}finally{
//			closeSession(session);
//		}
		Session session = getSession();
		User user = (User) session.load(User.class, id);
		return user;
	}

}
