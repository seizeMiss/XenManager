package main.java.dragon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.UserDao;
import main.java.dragon.pojo.User;
import main.java.dragon.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;

	@Override
	public boolean addUser(User user) {
		userDao.insertUser(user);
		return true;
	}

	@Override
	public List<User> getAllUser() {
		return userDao.selectAllUser();
	}

	@Override
	public boolean editUser(User user) {
		userDao.updateUser(user);
		return false;
	}

	@Override
	public User deleteUser(int id) {
		User user = userDao.deleteUser(id);
		return user;
	}

	@Override
	public User getUserById(int id) {
		return userDao.selectUserById(id);
	}
	

}
