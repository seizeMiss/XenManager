package main.java.dragon.service.impl;
import com.xensource.xenapi.*;

import main.java.dragon.xenapi.XenApiUtil;

public class ConnectionUtil {
	public Connection connection = null;
	public static Session session = null;

	public ConnectionUtil() throws Exception {
		connection = XenApiUtil.getConnection();
		session = XenApiUtil.getSession();
	}
	
	
}
