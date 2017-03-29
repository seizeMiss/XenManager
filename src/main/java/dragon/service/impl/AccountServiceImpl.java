package main.java.dragon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.AccountDao;
import main.java.dragon.pojo.Account;
import main.java.dragon.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountDao accountDao;

	@Override
	public Account getByUserNameAndPassword(String userName, String password) {
		Account account = new Account();
		account.setUserName(userName);
		account.setPassword(password);
		if(accountDao.selectAccountByUsernameAndPassword(account) != null){
			return accountDao.selectAccountByUsernameAndPassword(account);
		}
		return null;
	}

	@Override
	public List<Account> getAllAccount() {
		return accountDao.selectAllAccount();
	}

	@Override
	public Account getAccountById(String id) {
		// TODO Auto-generated method stub
		return accountDao.selectAccountById(id);
	}

	@Override
	public boolean saveUser(Account account) {
		if (accountDao.updateAccount(account) > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean addUser(Account account) {
		if(accountDao.insertAccount(account) > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUsers(String ids) {
		String[] idArray = ids.split(",");
		int size = 0;
		for(String id : idArray){
			if(accountDao.deleteAccount(id) > 0){
				size++;
			}
		}
		if(size == idArray.length){
			return true;
		}
		return false;
	}

	@Override
	public List<Account> getAccountsByCondition(String name) {
		// TODO Auto-generated method stub
		return accountDao.selectAccountsByCondition(name);
	}
	
	

}
