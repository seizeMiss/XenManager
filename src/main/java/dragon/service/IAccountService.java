package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.Account;

public interface IAccountService {
	public Account getByUserNameAndPassword(String userName,String password);
	public List<Account> getAllAccount();
	public Account getAccountById(String id);
	public boolean saveUser(Account account);
	public boolean addUser(Account account);
	public boolean deleteUsers(String ids);
	public List<Account> getAccountsByCondition(String name);
}
