package main.java.dragon.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import main.java.dragon.pojo.Account;
import main.java.dragon.utils.StringUtils;
/**
 * 登录拦截器
 * @author chenbaolong
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取请求的URL
		String url = request.getRequestURI();
		if(url.indexOf("login") > 0 || url.indexOf("img") > 0
				|| url.indexOf("script") > 0 || url.indexOf("css") > 0
				|| url.indexOf("bootstrap") > 0){
			return true;
		}
		
		HttpSession session = request.getSession();
		Account account = (Account)session.getAttribute("account");
		if(!StringUtils.isEmpty(account)){
			return true;
		}
		//不符合条件的，跳转到登录界面
		request.getRequestDispatcher("/login.jsp").forward(request, response);
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
