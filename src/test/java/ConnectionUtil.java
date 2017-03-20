package test.java;
import com.xensource.xenapi.*;

public class ConnectionUtil {
	public Connection connection = null;
	public static Session session = null;

	public ConnectionUtil() throws Exception {
		connection = XenApiUtil.getConnection();
		session = XenApiUtil.getSession();
	}
}
