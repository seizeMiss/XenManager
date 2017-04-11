package main.java.dragon.xenapi;

import java.util.Map;

import main.java.dragon.utils.ConnectionInfoParseXml;

class XenServerPoolConf {
	private String hostURL;
	private String username;
	private String password;
	private static XenServerPoolConf instance = new XenServerPoolConf();

	public static XenServerPoolConf getInstance() {
		return instance;
	}

	public String getHostURL() {
		return this.hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	private XenServerPoolConf() {
		Map<String, String> connectionInfo = ConnectionInfoParseXml.getXenConenctionInfo();
		this.hostURL = connectionInfo.get("host-url");
		this.username = connectionInfo.get("user");
		this.password = connectionInfo.get("password");
		
	}

}
