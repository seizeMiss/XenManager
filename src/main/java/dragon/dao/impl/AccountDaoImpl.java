package main.java.dragon.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.IAccountDao;
import main.java.dragon.pojo.Account;
import main.java.dragon.utils.StringUtils;

@Repository
@Transactional
public class AccountDaoImpl extends HibernateUtils implements IAccountDao{

	@Override
	public int insertAccount(Account account) {
		
		return 0;
	}

	@Override
	public int updateAccount(Account account) {
		
		return 0;
	}

	@Override
	public int selectAccountById(String id) {
		
		return 0;
	}

	@Override
	public Account selectAccountByUsernameAndPassword(Account account) {
		Session session = null;
		List<Account> selectAccounts = null;
		try {
			session = getSession();
			session.beginTransaction();
			String hql = "from Account a where a.userName = ? and a.password = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, account.getUserName());
			query.setParameter(1, account.getPassword());
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
		
		return null;
	}

	@Override
	public int deleteAccount(String id) {
		
		return 0;
	}

}
