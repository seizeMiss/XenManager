package main.java.dragon.service.impl;

import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.Types.XenAPIException;
import com.xensource.xenapi.VM;

import main.java.dragon.service.IndexService;
import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.HostAPI;

@Service(value="indexService")
@Transactional
public class IndexServiceImpl extends ConnectionUtil implements IndexService{
	
	private static String VM_IS_RUNNING = "Running";
	

	public IndexServiceImpl() throws Exception{
		super();
	}
	
	public int getVmCount(boolean isRunning){
		int count = 0;
		Set<VM> vms;
		try {
			vms = VM.getAll(connection);
			for (VM vm : vms){
				VM.Record record = vm.getRecord(connection);
				//快照  控制主机 模板 
				if(record.isASnapshot || record.isControlDomain 
						|| record.isATemplate || record.isSnapshotFromVmpp){
					
				}else{
					if(isRunning){
						if(record.powerState.toString().equals(VM_IS_RUNNING)){
							count++;
						}
					}else{
						count++;
					}
				}
			}
		} catch (XenAPIException | XmlRpcException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public String getStorageTotal(){
		String storageTotal = "";
		long total = 0;
		Set<Host> hosts;
		try {
			hosts = Host.getAll(connection);
			for(Host host : hosts){
				Set<PBD> pbds = host.getPBDs(connection);
				for(PBD pbd : pbds){
					SR sr = pbd.getSR(connection);
					if(sr.getPhysicalSize(connection) > 0){
						total += NumberUtils.formatStorage(sr.getPhysicalSize(connection));
					}
				}
			}
		} catch (XenAPIException | XmlRpcException e) {
			e.printStackTrace();
		}
		storageTotal = String.valueOf(total);
		return storageTotal;
	}
	
	public String getStorageUsedRate(){
		String storageRate = "";
		Set<Host> hosts;
		long total = 0;
		long free = 0;
		try {
			hosts = Host.getAll(connection);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		storageRate = String.valueOf(free*100/total);
		return storageRate;
	}
	public String getMemoryUsedRate(){
		HostAPI hostAPI;
		String memoryRate = "0";
		try {
			hostAPI = new HostAPI();
			Host host = getDefaultHost();
			if(host != null){
				long memoryTotal = hostAPI.getMemoryInHost(host).get(1);
				long memoryUsed = hostAPI.getMemoryInHost(host).get(0);
				memoryRate = memoryUsed*100/memoryTotal + "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memoryRate;
	}
	
	public String getCpuUsedRate(){
		FetchDynamicData data = null;
		String cpuRate = "";
		try {
			data = new FetchDynamicData();
			Double cpuAverage = (Double)data.getIndexNeedInfo().get("cpu_avg");
			cpuRate = StringUtils.double2String(cpuAverage.isNaN() ? 0.0 : cpuAverage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cpuRate;
	}
	
	private Host getDefaultHost() throws Exception{
		Set<Host> hosts = Host.getAll(connection);
		for(Host host : hosts){
			if(null != host && !host.isNull()){
				return host;
			}
		}
		return null;
	}

}
