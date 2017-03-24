package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ejb.criteria.expression.SearchedCaseExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.AccountDao;
import main.java.dragon.pojo.Account;
import main.java.dragon.utils.StringUtils;

@Repository
@Transactional
public class AccountDaoImpl extends HibernateUtils implements AccountDao{

	@Override
	public int insertAccount(Account account) {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.save(account);
			session.flush();
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return 0;
	}

	@Override
	public int updateAccount(Account account) {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.update(account);
			session.flush();
			session.getTransaction().commit();
			return 1;
		}catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			closeSession(session);
		}
		return 0;
	}

	@Override
	public Account selectAccountById(String id) {
		Session session = null;
		Account account = null;
		try {
			session = getSession();
			account = (Account) session.get(Account.class, id);
			if (!StringUtils.isEmpty(account)){
				return account;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
		return account;
	}

	@Override
	public Account selectAccountByUsernameAndPassword(Account account) {
		Session session = null;
		List<Account> selectAccounts = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from Account a where a.userName = ? and a.password = ?";
			Query query = queryByParams(session, hql, account.getUserName(),account.getPassword());
			selectAccounts = (List<Account>)query.list();
			session.getTransaction().commit();
			if(!StringUtils.isEmpty(selectAccounts)){
				System.out.println(selectAccounts.get(0));
				return selectAccounts.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
		return null;
	}

	@Override
	public List<Account> selectAllAccount() {
		List<Account> accounts = null;
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from Account";
			Query query = session.createQuery(hql);
			accounts = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
		return accounts;
	}

	@Override
	public int deleteAccount(String id) {
		Session session = null;
		Account account = null;
		try {
			session = getSession();
			session.beginTransaction();
			account = (Account) session.load(Account.class, id);
			session.delete(account);
			session.flush();
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			closeSession(session);
		}
		return 0;
	}

	@Override
	public List<Account> selectAccountsByCondition(String name) {
		Session session = null;
		List<Account> accounts = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from Account where userName like '" + name + "%'";
			System.out.println(hql);
			Query query = session.createQuery(hql);
			accounts = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeSession(session);
		}
		return accounts;
	}

}
