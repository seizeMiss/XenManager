package test.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.SR;

import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.XenConstants;
import main.java.dragon.xenapi.Dom4jTool;
import sun.misc.*;

public class HostDemo extends ConnectionUtil{

	public HostDemo() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//定义标签
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

	
	private long step = 0; //时间间隔
	private long startTime = 0; //获取数据的开始时间
	private long endTime = 0; //获取数据的结束时间
	private int rowCount = 0; //数据的总数
	private int columnCount = 0; //所要获得数据的名称的个数
	
	private long[] times; //
	private double[][] values; //保存数据数值
	// 历史数据，特点是未取到值则由前面一个时间的值填充
	private double[][] latitudeValues;
	
	private int[] lastRowValidRecord;//上一个数据的有效值
	
	private String[] types; // 记录的数据类型 min,or max,or avg
	private String[] objs; // 保存的对象是主机还是虚拟机 host,or vm
	private String[] uuids; // 记录uuid
	private String[] metrics; // memory_target or others

	public int computeHostOrVmCount(String[] uuids,String[] obj,String type){
		Set<String> target = new HashSet<String>();
		for(int i = 0; i < uuids.length; i++){
			if (obj[i].equals(type)){
				target.add(uuids[i]);
			}
		}
		return target.size();
	}
	@Test
	public void getStorage() throws Exception{
		Set<Host> hosts = Host.getAll(connection);
		long total = 0;
		long free = 0;
		for(Host host : hosts){
			Set<PBD> pbds = host.getPBDs(connection);
			for(PBD pbd : pbds){
				SR sr = pbd.getSR(connection);
				if(sr.getPhysicalSize(connection) > 0){
					total += NumberUtils.formatStorage(sr.getPhysicalSize(connection));
					free += NumberUtils.formatStorage(sr.getPhysicalUtilisation(connection));
				}
			}
		}
		System.out.println(String.valueOf(free*100/total));
		
	}
	
	@Test
	public void parseXml() throws Exception {
		ComputerAPI computeApi = new ComputerAPI();
		VM vm = computeApi.getVMbyName("IVYCloud-Server");
		String uuid = vm.getUuid(connection);
		Document document = getDocById("", "rrdUpdate", "update");
		Element root = document.getRootElement();
		parseMetaParams(root);
		parseRRDValues(root);
		System.out.println(computeHostOrVmCount(uuids, objs,"vm"));
		for(int i = 0; i < rowCount; i++){
			double cpuRate = 0.0d;
			double vmCpuTate = 0.0d;
			double host_memory_free = 0.0d;
			double host_memory_total = 0.0d;
			for(int j = 0; j < columnCount; j++){
				String attr = metrics[j];
				double val = 0.0d;
				if(values[i][j] > 0){
					val = values[i][j];
				}else{
					val = latitudeValues[i][j];
				}
				if(objs[j].equals("host")){
					if(attr.equals(XenConstants.XEN_HOST_MEMORY_FREE_KIB)){
						if(val != XenConstants.XEN_METER_BAD_VALUE){
							host_memory_free = val / 1024;
							String hostMemoryFree = String.valueOf(Math.round(host_memory_free));
							System.out.println(XenConstants.XEN_HOST_MEMORY_FREE_KIB + ":" + hostMemoryFree + ":" + hostMemoryFree);
						}
					}else if(attr.equals(XenConstants.XEN_HOST_MEMORY_TOTAL_KIB)){
						if(val != XenConstants.XEN_METER_BAD_VALUE){
							host_memory_total = val / 1024;
							String hostMemoryTotal = String.valueOf(Math.round(host_memory_total));
							System.out.println(XenConstants.XEN_HOST_MEMORY_TOTAL_KIB + ":" + host_memory_total + ":" + hostMemoryTotal);
						}
					}else if(attr.equals(XenConstants.XEN_HOST_CPU_META)){
						double cpu = val;
						System.out.println(XenConstants.XEN_HOST_CPU_META + ":" + cpu);
					}else if(attr.startsWith("cpu")){
						cpuRate += val;
					}
				}
				
				/*if (uuids[j].equals(uuid)){
					if(attr.equals(XenConstants.XEN_VM_MEMORY_FREE_META)){
						if(val != XenConstants.XEN_METER_BAD_VALUE){
							double vm_memory_free = val / 1024;
							String vmMemoryFree = String.valueOf(Math.round(vm_memory_free));
							System.out.println(XenConstants.XEN_VM_MEMORY_FREE_META + ":" + vmMemoryFree + ":" + vmMemoryFree);
						}
					}else if(attr.equals(XenConstants.XEN_VM_MEMORY_TOTAL_META)){
						if(val != XenConstants.XEN_METER_BAD_VALUE){
							double vm_memory_total = val / 1024;
							String vmMemoryTotal = String.valueOf(Math.round(vm_memory_total));
							System.out.println(XenConstants.XEN_VM_MEMORY_TOTAL_META + ":" + vm_memory_total + ":" + vmMemoryTotal);
						}
					}else if(attr.startsWith(XenConstants.XEN_VM_CPU)){
						vmCpuTate += val;
						System.out.println("cpu: " + val);
					}
				}*/
			}
			System.out.println("内存使用率是：" + NumberUtils.computerUsedRate(host_memory_total, host_memory_free, 0));
			cpuRate = cpuRate / 33;
			System.out.println("CPU使用率：" + cpuRate);
			vmCpuTate /= 2;
			System.out.println("VM CPU使用率：" + vmCpuTate);
			break;
		}
		
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
		String ip = "192.168.4.206";
		Date date = new Date();
		String admin = "root";
		String password = "centerm";
		String result = "";
		String urlString = "";
		long startTime = date.getTime() / 1000 - 3600 * 24 * 6 - 3600 * 23;
		if(type.equals("isVM")){
			urlString = "http://" + ip + "/vm_rrd?uuid="+ id;
		}else if(type.equals("isHost")){
			urlString = "http://" + ip +"/host_rrd";
		}else{
			urlString = "http://" + ip + "/rrd_updates?host=true&cf=AVERAGE&interval=" + 60 + "&start=" + startTime;
		}
		try {
			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			String encoding = new BASE64Encoder().encode((admin + ":" + password).getBytes());
			urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			InputStream iStream = urlConnection.getInputStream();
			result = IOUtils.toString(iStream);
			File file = new File("src/"+ pathName + ".xml");
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
