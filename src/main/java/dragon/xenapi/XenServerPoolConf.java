package main.java.dragon.xenapi;

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

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	private XenServerPoolConf() {
		this.hostURL = "http://192.168.4.206";
		this.username = "root";
		this.password = "centerm";
	}

}
