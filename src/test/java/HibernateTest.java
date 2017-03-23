package test.java;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import main.java.dragon.pojo.User;
import main.java.dragon.service.UserService;

public class HibernateTest {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void insertUser(){
		User user = new User();
		user.setUserName("zhangsan");
		user.setPassword("111");
		user.setAge(12);
		System.out.println(user);
		if(userService.addUser(user)){
			System.out.println("添加成功");
		}else{
			System.out.println("添加失败");
		}
	}

}
