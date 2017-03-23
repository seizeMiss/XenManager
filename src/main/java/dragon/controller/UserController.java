package main.java.dragon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.dragon.pojo.User;
import main.java.dragon.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("getUser")
	public String getUserById(HttpSession session, Model model,HttpServletRequest request){
		int id = Integer.parseInt(request.getParameter("id"));
		User user = userService.getUserById(id);
		System.out.println(user);
		model.addAttribute("user", user);
		session.setAttribute("user", user);
		return "/index";
	}
	
	@RequestMapping("insertUser")
	public String saveUser(){
		User user = new User();
		user.setUserName("wangwu");
		user.setPassword("333");
		user.setAge(15);
		userService.addUser(user);
		return "/success";
	}
	
	@RequestMapping("getAllUsers")
	public String getAllUSers(Model model){
		List<User> users = userService.getAllUser();
		System.out.println(users);
		model.addAttribute("users", users);
		return "/alluser";
	}
	
	@RequestMapping("updateUser")
	@ResponseBody
	public Map<String,Object> updateUser(HttpSession session, HttpServletRequest request, Model model){
		Map<String, Object> map = new HashMap<>();
		String password = request.getParameter("password");
		int age = Integer.parseInt(request.getParameter("age"));
		User preUser = (User) session.getAttribute("user");
		preUser.setPassword(password);
		preUser.setAge(age);
		userService.editUser(preUser);
		model.addAttribute("user", preUser);
		map.put("data", "修改成功！");
		return map;
	}
	
	@RequestMapping("deleteUser")
	public String deleteUser(Model model, @RequestParam(value="id")String id){
		int getId = Integer.parseInt(id);
		User user = userService.deleteUser(getId);
		List<User> users = userService.getAllUser();
		model.addAttribute("users", users);
		return "alluser";
	}
	@RequestMapping("getLocalUser")
	public String getLocalUSer(){
		return "jsp/account/add_user";
	}
}
