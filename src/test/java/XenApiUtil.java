package test.java;

import java.net.URL;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Session;
import com.xensource.xenapi.Task;
import com.xensource.xenapi.Types;

public class XenApiUtil {

	private static XenServerPoolConf conf = XenServerPoolConf.getInstance();
	protected static Connection connection = null;
	protected static Session session = null;

	public static Connection getConnection() throws Exception{
		if (null == connection) {

			//HttpsUtil.trustAllHttpsCertificates(); // Sets the default
													// HostnameVerifier used on
													// all Https connections
													// created after this point

			connection = new Connection(new URL(conf.getHostURL()));
			
			session = Session.loginWithPassword(connection, conf.getUsername(), conf.getPassword());
		}
		return connection;
	}

	public static Session getSession() throws Exception {

		if (null == session) {
			throw new Exception();
		}

		return session;
	}

	public static void waitForTask(Connection c, Task task, int delay)
			throws Exception {
		while (task.getStatus(c) == Types.TaskStatusType.PENDING) {
			System.out.println(task.getProgress(c));
			Thread.sleep(delay);
		}
	}

}
