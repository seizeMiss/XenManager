package main.java.dragon.dao;

import java.util.List;

import main.java.dragon.pojo.User;

public interface IUserDao {
	public int insertUser(User user);
	public List<User> selectAllUser();
	public int updateUser(User user);
	public User deleteUser(int id);
	
	public User selectUserById(int id);

}
