package main.java.dragon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.IAccountDao;
import main.java.dragon.pojo.Account;
import main.java.dragon.service.IAccountService;

@Service
@Transactional
public class AccountServiceImpl implements IAccountService{
	
	@Autowired
	private IAccountDao accountDao;

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
		// TODO Auto-generated method stub
		return accountDao.selectAllAccount();
	}

	@Override
	public Account getAccountById(String id) {
		// TODO Auto-generated method stub
		return accountDao.selectAccountById(id);
	}

	@Override
	public boolean saveUser(Account account) {
		// TODO Auto-generated method stub
		if (accountDao.updateAccount(account) > 0){
			return true;
		}
		return false;
	}
	
	

}
