package main.java.dragon.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.dragon.dao.StorageDao;
import main.java.dragon.pojo.Account;
import main.java.dragon.service.AccountService;
import main.java.dragon.service.IndexService;
import main.java.dragon.service.StorageService;
import main.java.dragon.utils.StringUtils;

@Controller
public class AccountController{
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private StorageService storageService;
	
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
	public String showIndex(Model model){
		try {
			int vmCount = indexService.getVmCount(false);
			int activeVmCount = indexService.getVmCount(true);
			String cpuUsedRate = indexService.getCpuUsedRate();
			String memoryUsedRate = indexService.getMemoryUsedRate();
			String storageUsedRate = indexService.getStorageUsedRate();
			String storageTotal = indexService.getStorageTotal();
			storageService.addStorage();
			if(!StringUtils.isEmpty(vmCount,activeVmCount,cpuUsedRate,memoryUsedRate,storageTotal,storageUsedRate)){
				model.addAttribute("vmCount", vmCount);
				model.addAttribute("activeVmCount", activeVmCount);
				model.addAttribute("cpuUsedRate", cpuUsedRate);
				model.addAttribute("memoryUsedRate", memoryUsedRate);
				model.addAttribute("storageUsedRate", storageUsedRate);
				model.addAttribute("storageTotal", storageTotal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
		account = accountService.getAccountById(account.getId());
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
		int size = 0;
		if(!StringUtils.isEmpty(accounts)){
			size = accounts.size();
			model.addAttribute("accounts", accounts);
			model.addAttribute("size", size);
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
	/**
	 * 修改密码
	 * @param session
	 * @param request
	 * @return
	 */
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
	public Map<String, Object> editUser(HttpServletRequest request,HttpSession session){
		Map<String, Object> map = new HashMap<>();
		String id = request.getParameter("id");
		String realName = StringUtils.setEncodeString(request.getParameter("realName"));
		String email = request.getParameter("email");
		String description = StringUtils.setEncodeString(request.getParameter("description"));
		Account account = accountService.getAccountById(id);
		account.setRealName(realName);
		account.setEmail(email);
		account.setDescription(description);
		account.setUpdateTime(new Date());
		if(accountService.saveUser(account)){
			Account localAccount = (Account)session.getAttribute("account");
			if(localAccount.getId().equals(account.getId())){
				map.put("data", 1);
			}else{
				map.put("data", 0);
			}
		}
		return map;
	}
	
	@RequestMapping("showAddUser")
	public String showAddUser(){
		return "jsp/account/add_user";
	}
	
	@RequestMapping("addUser")
	@ResponseBody
	public Map<String, Object> addUser(HttpServletRequest request){
		String userName = request.getParameter("userName");
		String realName = request.getParameter("realName");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String description = "";
		try {
			description = new String(request.getParameter("description").getBytes("iso-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Map<String,Object> map = new HashMap<>();
		String uuid = StringUtils.generateUUID();
		Account account = new Account(uuid, userName, realName, password, email, new Date(), new Date(), description);
		if(accountService.addUser(account)){
			map.put("data", "success");
		}
		return map;
	}
	/**
	 * 删除用户信息
	 * @param ids
	 * @return
	 */
	@RequestMapping("deleteUsers")
	@ResponseBody
	public Map<String, Object> deleteUsers(@RequestParam(value="tid")String ids){
		Map<String, Object> map = new HashMap<>();
		if(accountService.deleteUsers(ids)){
			map.put("data", "success");
		}
		return map;
	}
	
	@RequestMapping("searchAccount")
	public String searchAccount(Model model, HttpServletRequest request){
		String name = request.getParameter("condition-name");
		List<Account> accounts = accountService.getAccountsByCondition(name);
		if(!StringUtils.isEmpty(accounts)){
			model.addAttribute("accounts", accounts);
			model.addAttribute("size", accounts.size());
		}
		return "jsp/account/admin_user";
	}
	
	@RequestMapping("showAbout")
	public String showAcount(){
		
		return "jsp/about";
	}
	
}
