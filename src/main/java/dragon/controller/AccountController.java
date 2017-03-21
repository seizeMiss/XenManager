package main.java.dragon.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
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

import main.java.dragon.pojo.Account;
import main.java.dragon.service.IAccountService;
import main.java.dragon.utils.StringUtils;

@Controller
public class AccountController {
	@Autowired
	private IAccountService accountService;
	
	/**
	 * 登录
	 * @param session
	 * @param request
	 * @return
	 */
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
	/**
	 * 注销系统
	 * @param session
	 * @return
	 */
	@RequestMapping("loginOut")
	public String loginOut(HttpSession session){
		session.invalidate();
		return "login";
	}
	/**
	 * 显示首页
	 * @param model
	 * @return
	 */
	@RequestMapping("showIndex")
	public String showInde(Model model){
		
		return "/jsp/index";
	}
	/**
	 * 显示本地用户
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("showLocalUser")
	public String showLocalUser(HttpSession session,Model model){
		Account account = (Account)session.getAttribute("account");
		model.addAttribute("account", account);
		return "/jsp/account/local_user";
	}
	/**
	 * 显示管理员用户
	 * @param model
	 * @return
	 */
	@RequestMapping("showAdminUser")
	public String showAdminUser(Model model){
		List<Account> accounts = accountService.getAllAccount();
		if(!StringUtils.isEmpty(accounts)){
			model.addAttribute("accounts", accounts);
			System.out.println(model.toString());
		}
		return "/jsp/account/admin_user";
	}
	/**
	 * 显示需要修改密码的用户
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("showModifyPassword")
	public String showModifyPassword(Model model,
			@RequestParam(value="tid")String id){
		Account localAccount = accountService.getAccountById(id);
		if(localAccount != null){
			model.addAttribute("localAccount", localAccount);
		}
		return "/jsp/account/modify_password";
	}
	
	@RequestMapping("modifyPassword")
	@ResponseBody
	public Map<String, Object> modifyPassword(HttpSession session, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		Account localAccount = (Account)session.getAttribute("account");
		String password = request.getParameter("password");
		localAccount.setPassword(password);
		if(accountService.saveUser(localAccount)){
			session.invalidate();
			map.put("data", "success");
		}
		return map;
	}
	/**
	 * 显示修改用户信息界面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("showEditUser")
	public String showEditUser(Model model,@RequestParam(value="tid")String id){
		Account localAccount = accountService.getAccountById(id);
		if(localAccount != null){
			model.addAttribute("localAccount", localAccount);
		}
		return "/jsp/account/edit_user";
	}
	/**
	 * 编辑用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("editUser")
	@ResponseBody
	public Map<String, Object> edit(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String id = request.getParameter("id");
		String realName = request.getParameter("realName");
		String email = request.getParameter("email");
		String description = "";
		try {
			description = new String(request.getParameter("description").getBytes("iso-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Account account = accountService.getAccountById(id);
		account.setRealName(realName);
		account.setEmail(email);
		account.setDescription(description);
		account.setUpdateTime(new Date());
		if(accountService.saveUser(account)){
			map.put("data", "success");
		}
		return map;
	}
	
}
