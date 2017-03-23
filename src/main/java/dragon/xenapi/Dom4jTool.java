package main.java.dragon.xenapi;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4jTool {

	public static Document xmlString2Doc(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return doc;
	}

	public static Document xmlFile2Doc(File xmlFile) {

		if (xmlFile == null) {
			return null;
		}
		Document doc = null;
		try {
			SAXReader reader = new SAXReader();
			doc = reader.read(xmlFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static Document xmlFile2Doc(String xmlFilePath) {

		if (xmlFilePath == null) {
			return null;
		}
		Document doc = null;
		try {
			SAXReader reader = new SAXReader();
			doc = reader.read(new File(xmlFilePath));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static Document url2Doc(URL url) {
		if (url == null) {
			return null;
		}

		Document doc = null;
		try {
			SAXReader reader = new SAXReader();
			doc = reader.read(url);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static Document stream2Doc(InputStream in) {
		if (in == null) {
			return null;
		}

		Document doc = null;
		try {
			SAXReader reader = new SAXReader();
			doc = reader.read(in);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static void doc2File(Document doc, String filePath) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileOutputStream(filePath), format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			return;
		}
	}

	public static String doc2XmlString(Node node) {
		return node.asXML();
	}

	public static Node selectSingleNode(Node node, String path) {
		Node selNode = node.selectSingleNode(path);
		return selNode;
	}

	public static List<Node> selectNodes(Node node, String path) {
		List<Node> selNode = node.selectNodes(path);
		return selNode;
	}

	public static String getNodeText(Node node, String path) {
		Node res = node.selectSingleNode(path);
		if (res != null) {
			return res.getText();
		}
		return null;
	}

	public static void setNodeText(Node node, String path, String value) {
		Node res = node.selectSingleNode(path);
		res.setText(value);
	}

	public static Integer xml2File(String xml, String path) {
		try {
			Document doc = DocumentHelper.parseText(xml);
			doc2File(doc, path);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public static Document readXmlFromFile(String path) {
		File f = new File(path);
		if (f == null || !f.exists()) {
			System.out.println("无法找到配置文件");
			return null;
		}
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(f);
			return doc;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Integer str2File(String xml, String path) {
		Document doc = Dom4jTool.xmlString2Doc(xml);
		if (doc == null) {
			return -1;
		}

		Dom4jTool.doc2File(doc, path);
		return 0;
	}

	public static Document readXml(String xml) {
		try {
			return DocumentHelper.parseText(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param document
	 * @param searchPath
	 *            example:"/Root/HeartBeat"
	 * @param placeNodeName
	 */
	public static void replaceDocumentNodes(Document document, String searchPath, String placeNodeName) {
		List list = document.selectNodes(searchPath);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Element noteElement = (Element) iter.next();
			noteElement.setName(placeNodeName);
		}
	}

	/**
	 * 在需要的路径下添加节点并设置值.
	 * 
	 * @param document
	 *            msg.getDocument();
	 * @param parentPath
	 *            所要添加的节点的父级 example:/Root
	 * @param toAddNodeName
	 *            所要添加节点的名称 example: RemoteAddr
	 * @param toAddNodeValue
	 *            所要添加节点的值 example: 127.0.0.1
	 *            根据例子执行的结果就是在Root节点下添加一个RemoteAddr其值为127.0.0.1
	 */
	public static void addNodeByPath(Document document, String parentPath, String toAddNodeName,
			String toAddNodeValue) {
		List<Node> nodes = document.selectNodes(parentPath);
		if (nodes != null && nodes.size() > 0) {
			for (Node node : nodes) {
				Element element = (Element) node;
				Element titleElement = element.addElement(toAddNodeName);
				titleElement.setText(toAddNodeValue);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getListMapByElementName(Document doc, String elementName) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		if (doc == null) {
			return listMap;
		}
		
		if(StringUtils.isBlank(elementName)) {
			return listMap;
		}

		Element root = doc.getRootElement();

		List<Element> elements = root.elements(elementName);
		if (elements == null || elements.size() <= 0) {
			return listMap;
		}

		for (Element element : elements) {
			Map<String, Object> map = buildXmlBody2map(element);
			if (map == null || map.size() <= 0) {
				continue;
			}

			listMap.add(map);
		}

		return listMap;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> buildXmlBody2map(Element body) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (body != null) {
			List<Element> elements = body.elements();
			for (Element element : elements) {
				String key = element.getName();

				if (StringUtils.isNotEmpty(key)) {
					// 这里默认其为string类型
					String text = element.getText().trim();

					// 加入到map
					map.put(key, text);
				}
			}
		}

		return map;
	}

}
