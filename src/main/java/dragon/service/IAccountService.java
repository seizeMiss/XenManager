package main.java.dragon.service;

import main.java.dragon.pojo.Account;

public interface IAccountService {
	public Account getByUserNameAndPassword(String userName,String password);

}
