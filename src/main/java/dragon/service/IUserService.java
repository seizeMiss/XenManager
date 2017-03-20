package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.User;

public interface IUserService {
	public boolean addUser(User user);
	
	public List<User> getAllUser();
	
	public boolean editUser(User user);
	
	public User deleteUser(int id);
	
	public User getUserById(int id);

}
