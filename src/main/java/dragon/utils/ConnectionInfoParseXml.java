package main.java.dragon.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import main.java.dragon.xenapi.Dom4jTool;

public class ConnectionInfoParseXml {
	
	public static String realFilePath(String path){
		StringBuffer realPath = new StringBuffer();
		String[] paths = path.split("\\\\");
		for(String s : paths){
			if(s.equals(paths[paths.length-1])){
				realPath.append("resources\\" + s);
			}else{
				realPath.append(s+"\\");
			}
		}
		return realPath.toString();
	}
	
	public static Map<String, String> getXenConenctionInfo(){
		String hostUrl = "http://";
		String userName = "";
		String password = "";
		Map<String, String> connectionInfo = new HashMap<String, String>();
		File file = new File("xen-connection.xml");
		String wenAppPath = System.getProperty("vmmanager-webapp");
		String realpath = wenAppPath + "WEB-INF\\classes\\" + file.getName();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(realpath));
			Document document = Dom4jTool.stream2Doc(fis);
			Element rootElement = document.getRootElement();
			Element conElement = rootElement.element("connection");
			Element hostElement = conElement.element("hosturl");
			Element userElement = conElement.element("user");
			Element psdElement = conElement.element("password");
			
			hostUrl = hostUrl + hostElement.getText();
			userName = userElement.getText();
			password = psdElement.getText();
			connectionInfo.put("host-url", hostUrl);
			connectionInfo.put("user", userName);
			connectionInfo.put("password", password);
			connectionInfo.put("ip-address", hostElement.getText());
			fis.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return connectionInfo;
	}
	
	@Test
	public void demo(){
		String hostUrl = "http://";
		String userName = "";
		String password = "";
		File file = new File("/conf/xen-connection.xml");
		System.out.println(file.getAbsolutePath());
		System.out.println(realFilePath(file.getAbsolutePath()));
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(realFilePath(file.getAbsolutePath())));
			Document document = Dom4jTool.stream2Doc(fis);
			System.out.println(document.getName());
			Element rootElement = document.getRootElement();
			System.out.println(rootElement.getName());
			Element conElement = rootElement.element("connection");
			System.out.println(conElement.getName());
			Element hostElement = conElement.element("hosturl");
			Element userElement = conElement.element("user");
			Element psdElement = conElement.element("password");
			System.out.println(hostElement.getText());
			System.out.println(userElement.getText());
			System.out.println(psdElement.getText());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
}
