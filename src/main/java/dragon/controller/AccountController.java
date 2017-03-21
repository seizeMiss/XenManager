package main.java.dragon.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.dragon.pojo.Account;
import main.java.dragon.service.IAccountService;

@Controller
public class AccountController {
	@Autowired
	private IAccountService accountService;
	
	@RequestMapping("login")
	@ResponseBody
	public Map<String,Object> login(HttpSession session,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		Account account =null;
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		if(accountService.getByUserNameAndPassword(userName, password) != null){
			account = accountService.getByUserNameAndPassword(userName, password);
			session.setAttribute("account", account);
			map.put("data", "success");
		}
		return map;
	}
	
	@RequestMapping("loginOut")
	public String loginOut(HttpSession session){
		session.invalidate();
		return "login";
	}
	
	@RequestMapping("showIndex")
	public String showInde(Model model){
		
		return "/jsp/index";
	}
	
	@RequestMapping("showLocalUser")
	public String showLocalUser(HttpSession session,Model model){
		Account account = (Account)session.getAttribute("account");
		model.addAttribute("account", account);
		System.out.println(account);
		return "/jsp/account/loacl_user";
	}
	
	
	
}
