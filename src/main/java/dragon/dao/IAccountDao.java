package main.java.dragon.dao;

import java.util.List;

import main.java.dragon.pojo.Account;

public interface IAccountDao {
	public int insertAccount(Account account);
	public int updateAccount(Account account);
	public Account selectAccountById(String id);
	public Account selectAccountByUsernameAndPassword(Account account);
	public List<Account> selectAllAccount();
	public int deleteAccount(String id);
	public List<Account> selectAccountsByCondition(String name);

}
