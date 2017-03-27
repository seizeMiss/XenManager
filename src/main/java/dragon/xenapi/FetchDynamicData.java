package main.java.dragon.xenapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import com.xensource.xenapi.VM;

import main.java.dragon.service.impl.ConnectionUtil;
import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.XenConstants;
import sun.misc.BASE64Encoder;

public class FetchDynamicData extends ConnectionUtil {

	public FetchDynamicData() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	private static final String IP = "192.168.4.206";
	private static final String HOST_NAME = "root";
	private static final String HOST_PASSWORD = "centerm";

	// 定义标签
	private static final String RRD_META_STR = "meta";
	private static final String RRD_DATA_STR = "data";
	private static final String RRD_START_STR = "start";
	private static final String RRD_END_STR = "end";
	private static final String RRD_ROWS_STR = "rows";
	private static final String RRD_COLUMNS_STR = "columns";
	private static final String RRD_LEGEND_STR = "legend";
	private static final String RRD_ENTRY_STR = "entry";
	private static final String RRD_ROW_STR = "row";
	private static final String RRD_STEP_STR = "step";
	private static final String RRD_TIME_STR = "t";
	private static final String RRD_VALUE_STR = "v";
	private static final String RRD_ENTRY_SPLIT_STR = ":";
	private static final int RRD_ENTRY_SPLIT_SIZE = 4;

	private long step = 0; // 时间间隔
	private long startTime = 0; // 获取数据的开始时间
	private long endTime = 0; // 获取数据的结束时间
	private int rowCount = 0; // 数据的总数
	private int columnCount = 0; // 所要获得数据的名称的个数

	private long[] times; //
	private double[][] values; // 保存数据数值
	// 历史数据，特点是未取到值则由前面一个时间的值填充
	private double[][] latitudeValues;

	private int[] lastRowValidRecord;// 上一个数据的有效值

	private String[] types; // 记录的数据类型 min,or max,or avg
	private String[] objs; // 保存的对象是主机还是虚拟机 host,or vm
	private String[] uuids; // 记录uuid
	private String[] metrics; // memory_target or others
	
	/*private boolean isAvailableVm(VM vm) throws Exception {
		VM.Record record = vm.getRecord(connection);
		if (record.isASnapshot || record.isControlDomain || record.isATemplate || record.isSnapshotFromVmpp) {
			return false;
		} else {
			return true;
		}
	}
	
	@Test
	public void printInfo() throws Exception{
		Set<VM> vms = VM.getAll(connection);
		for(VM vm : vms){
			if(isAvailableVm(vm) && vm.getNameLabel(connection).equals("IVYCloud-AD")){
				System.out.println(vm.getUuid(connection));
				getVmNeedInfoByParseXml(vm.getUuid(connection));
			}
		}
	}*/
	
	public Map<String, Object> getVmNeedInfoByParseXml(String uuid) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Document document = getDocById(uuid, "isVM", "vm");
		Element root = document.getRootElement();
		String[] metrics = getMetrics(document);
		
		List<Element> rraElements = root.elements("rra");
		Element rraElement = rraElements.get(0);
		Element dataElement = rraElement.element("database");
		List<Element> rowElements = dataElement.elements("row");
		Element rowElement = rowElements.get(0);
		List<Element> vElements = rowElement.elements("v");
		int size = metrics.length;
		double memory_internal_free = 0.0d;
		double memory_target = 0.0d;
		double memory = 0.0d;
		double cpu = 0.0d;
		for(int i = 0; i < size; i++){
			String attr = metrics[i];
			Element element = vElements.get(i);
			if (attr.equals("memory_internal_free")){
				memory_internal_free = Double.parseDouble(element.getText())/1024;
				System.out.println("memory_internal_free:" + memory_internal_free);
			}else if(attr.equals("memory_target")){
				memory_target = Double.parseDouble(element.getText())/1024/1024;
				System.out.println("memory_target:" + memory_target);
			}else if(attr.equals("memory")){
				memory = Double.parseDouble(element.getText());
				System.out.println("memory:" + memory/1024/1024);
			}else if(attr.startsWith("cpu0")){
				cpu = Double.parseDouble(element.getText());
			}
		}
		System.out.println("cpu_avg:" + cpu*100);
		map.put("cpu_avg", cpu);
		map.put("memory_used", memory_target - memory_target);
		map.put("memoty_total", memory_target);
		return map;
	}
	
	public Map<String, Object> getIndexNeedInfoByParseXml() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		File file = new File("E:\\update.xml");
		FileInputStream is = new FileInputStream(file);
		Document document = Dom4jTool.stream2Doc(is);
		Element root = document.getRootElement();
		parseMetaParams(root);
		parseRRDValues(root);
		
		for(int i = 0; i < rowCount;){
			double host_memory_free = 0.0d;
			double host_memory_total = 0.0d;
			double cpuAvg = 0.0d;
			int cpuAccount = 0;
			for(int j = 0; j < columnCount; j++){
				String attr = metrics[j];
				double val = 0.0d;
				if(values[i][j] > 0){
					val = values[i][j];
				}else{
					val = latitudeValues[i][j];
				}
				if(objs[j].equals("host")){
					if(attr.equals(XenConstants.XEN_HOST_MEMORY_FREE_KIB)
							&& val != XenConstants.XEN_METER_BAD_VALUE){
						host_memory_free = val / 1024;
					}else if(attr.equals(XenConstants.XEN_HOST_MEMORY_TOTAL_KIB)
							&& val != XenConstants.XEN_METER_BAD_VALUE){
						host_memory_total = val / 1024;
					}else if(attr.equals(XenConstants.XEN_HOST_CPU_META)){
						cpuAvg = val;
					}else if(attr.startsWith("cpu")){
						cpuAccount++;
					}
				}
			}
			map.put("cpu_avg", cpuAvg*100);
			map.put("memory_used", NumberUtils.computerUsedRate(host_memory_total, host_memory_free, 0));
			map.put("cpu_account", cpuAccount-1);
			break;
		}
		is.close();
		return map;
	}
	
	

	public Map<String, Object> getIndexNeedInfo() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		
		Document document = getDocById("", "rrdUpdate", "update");
		Element root = document.getRootElement();
		parseMetaParams(root);
		parseRRDValues(root);
		
		for(int i = 0; i < rowCount;){
			double host_memory_free = 0.0d;
			double host_memory_total = 0.0d;
			double cpuAvg = 0.0d;
			for(int j = 0; j < columnCount; j++){
				String attr = metrics[j];
				double val = 0.0d;
				if(values[i][j] > 0){
					val = values[i][j];
				}else{
					val = latitudeValues[i][j];
				}
				if(objs[j].equals("host")){
					if(attr.equals(XenConstants.XEN_HOST_MEMORY_FREE_KIB)
							&& val != XenConstants.XEN_METER_BAD_VALUE){
						host_memory_free = val / 1024;
					}else if(attr.equals(XenConstants.XEN_HOST_MEMORY_TOTAL_KIB)
							&& val != XenConstants.XEN_METER_BAD_VALUE){
						host_memory_total = val / 1024;
					}else if(attr.equals(XenConstants.XEN_HOST_CPU_META)){
						cpuAvg = val;
					}
				}
			}
			map.put("cpu_avg", cpuAvg*100);
			map.put("memory_used", NumberUtils.computerUsedRate(host_memory_total, host_memory_free, 0));
			break;
		}
		
		return map;
	}
	
	private String[] getMetrics(Document document){
		Element rootElement = document.getRootElement();
		List<Element> dsElements = rootElement.elements("ds");
		int size = dsElements.size();
		int count = 0;
		String[] metrics = new String[size];
		for(Element element : dsElements){
			Element nameElement = element.element("name");
			String name = nameElement.getText();
			metrics[count] = name;
			count++;
		}
		return metrics;
	}
	
	public int computeHostOrVmCount(String[] uuids,String[] obj,String type){
		Set<String> target = new HashSet<String>();
		for(int i = 0; i < uuids.length; i++){
			if (obj[i].equals(type)){
				target.add(uuids[i]);
			}
		}
		return target.size();
	}
	
	/**
	 * 
	 * @param root
	 * @throws Exception
	 */
	private void parseRRDValues(Element root) throws Exception {
		Iterator it = root.elementIterator(RRD_DATA_STR);
		while (it.hasNext()) {
			Element dataElement = (Element) it.next();
			parseData(dataElement);
		}

	}
	/**
	 * 解析数据
	 * @param dataElement 数据元素
	 * @throws Exception
	 */
	private void parseData(Element dataElement) throws Exception {
		times = new long[this.rowCount];
		values = new double[this.rowCount][this.columnCount];
		latitudeValues = new double[this.rowCount][this.columnCount];

		int i = 0;
		int j = 0;
		Iterator it = dataElement.elementIterator(RRD_ROW_STR);
		while (it.hasNext()) {
			Element rowElement = (Element) it.next();

			times[i] = Long.parseLong(rowElement.element(RRD_TIME_STR).getText());

			Iterator itValue = rowElement.elementIterator(RRD_VALUE_STR);
			while (itValue.hasNext()) {
				Element valueElement = (Element) itValue.next();

				String var = valueElement.getText();

				if (!var.startsWith("-") && !var.equals("nan") && !var.equals("infinity")) {
					values[i][j] = Double.parseDouble(var);
					latitudeValues[i][j] = Double.parseDouble(var);
				} else {
					// 未获取到正常数据，返回固定值,交由上层调用者进行处理
					values[i][j] = XenConstants.XEN_METER_BAD_VALUE;
					if (i == this.rowCount - 1) {
						latitudeValues[i][j] = 0.0d;
					} else {
						latitudeValues[i][j] = XenConstants.XEN_METER_BAD_VALUE;
					}
				}
				j++;
			}
			j = 0;
			i++;
		}
		handleLatitude(latitudeValues);
	}

	/**
	 * 因为解析xml是时间从前向后，因此需要做断点延续处理
	 * @param a
	 */
	private void handleLatitude(double[][] a) {
		lastRowValidRecord = new int[this.columnCount];
		for (int i = this.rowCount - 1; i >= 0; i--) {
			for (int j = this.columnCount - 1; j >= 0; j--) {
				if (latitudeValues[i][j] == XenConstants.XEN_METER_BAD_VALUE) {
					latitudeValues[i][j] = latitudeValues[lastRowValidRecord[j]][j];
				} else {
					lastRowValidRecord[j] = i;
				}
			}
		}
	}
	/**
	 * 初始化数据
	 * @param root
	 * @throws Exception
	 */
	private void parseMetaParams(Element root) throws Exception {
		Iterator it = root.elementIterator(RRD_META_STR);
		while (it.hasNext()) {
			Element metaElement = (Element) it.next();

			Element startElement = metaElement.element(RRD_START_STR);
			this.startTime = Long.parseLong(startElement.getText());

			Element stepElement = metaElement.element(RRD_STEP_STR);
			this.step = Long.parseLong(stepElement.getText());

			Element endElement = metaElement.element(RRD_END_STR);
			this.endTime = Long.parseLong(endElement.getText());

			Element rowsElement = metaElement.element(RRD_ROWS_STR);
			this.rowCount = Integer.parseInt(rowsElement.getText());

			Element columnsElement = metaElement.element(RRD_COLUMNS_STR);
			this.columnCount = Integer.parseInt(columnsElement.getText());

			 Element legendElement = metaElement.element(RRD_LEGEND_STR);
			 parseMetaLegend(legendElement);
		}
	}
	/**
	 *  解析legend标签下的entry中的值，并将其中的值进行划分
	 *  type: 类型 max、min、avg 一遍为avg
	 *  obj: 记录的是主机还是虚拟机
	 *  uuid: 这个对象的uuid
	 *  metrics: 需要获得数据的名称
	 * @param legendElement 存储需要获取数据的名称的标签
	 * @throws Exception
	 */
	private void parseMetaLegend(Element legendElement) throws Exception {
		types = new String[this.columnCount];
		objs = new String[this.columnCount];
		uuids = new String[this.columnCount];
		metrics = new String[this.columnCount];

		int i = 0;
		Iterator it = legendElement.elementIterator(RRD_ENTRY_STR);
		while (it.hasNext()) {
			Element metaElement = (Element) it.next();

			String[] array = metaElement.getText().split(RRD_ENTRY_SPLIT_STR);
			if (array.length >= RRD_ENTRY_SPLIT_SIZE) {
				types[i] = array[0];
				objs[i] = array[1];
				uuids[i] = array[2];
				metrics[i] = array[3];
			}
			i++;
		}
	}
	/**
	 * 获得xml数据源，并将该数据源转换为Dom
	 * @param id 虚拟机的uuid
	 * @param type 要查询的类型
	 * @param pathName 将该文件存储的位置
	 * @return
	 */
	private Document getDocById(String id, String type, String pathName){
		Document  document = null;
		Date date = new Date();
		String result = "";
		String urlString = "";
		long startTime = date.getTime() / 1000 - 3600 * 24 * 6 - 3600 * 23;
		if(type.equals("isVM")){
			urlString = "http://" + IP + "/vm_rrd?uuid="+ id;
		}else if(type.equals("isHost")){
			urlString = "http://" + IP +"/host_rrd";
		}else{
			urlString = "http://" + IP + "/rrd_updates?host=true&cf=AVERAGE&interval=" + 60 + "&start=" + startTime;
		}
		try {
			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			String encoding = new BASE64Encoder().encode((HOST_NAME + ":" + HOST_PASSWORD).getBytes());
			urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			InputStream iStream = urlConnection.getInputStream();
			result = IOUtils.toString(iStream);
			
			File file = new File("E:\\"+ pathName + ".xml");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file);
			fWriter.append(result);
			fWriter.close();
			InputStream is = new FileInputStream(file);
			document = Dom4jTool.stream2Doc(is);
			is.close();
			iStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}
}
