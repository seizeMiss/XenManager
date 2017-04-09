package main.java.dragon.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VM;

import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.HostAPI;

@Service
public class IndexService extends ConnectionUtil{
	
	private static String VM_IS_RUNNING = "Running";
	private static String VM_IS_HALTED = "Halted";
	

	public IndexService() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private Pool getDefaultPool(){
		Pool defaultPool = null;
		try {
			Set<Pool> pools = Pool.getAll(connection);
			for(Pool pool : pools){
				if(!StringUtils.isEmpty(pool)){
					defaultPool = pool;
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return defaultPool;
	}
	
	public int getVmCount(boolean isRunning) throws Exception{
		Set<VM> vms = VM.getAll(connection);
		int count = 0;
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
		return count;
	}
	
	public String getStorageTotal() throws Exception{
		String storageTotal = "";
		Set<Host> hosts = Host.getAll(connection);
		long total = 0;
		for(Host host : hosts){
			Set<PBD> pbds = host.getPBDs(connection);
			for(PBD pbd : pbds){
				SR sr = pbd.getSR(connection);
				if(sr.getPhysicalSize(connection) > 0){
					total += NumberUtils.formatStorage(sr.getPhysicalSize(connection));
				}
			}
		}
		storageTotal = String.valueOf(total);
		return storageTotal;
	}
	
	public String getStorageUsedRate() throws Exception{
		String storageRate = "";
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
		storageRate = String.valueOf(free*100/total);
		return storageRate;
	}
	public String getMemoryUsedRate() throws Exception{
		HostAPI hostAPI = new HostAPI();
		Host host = getDefaultHost();
		String memoryRate = "0";
		if(host != null){
			long memoryTotal = hostAPI.getMemoryInHost(host).get(1);
			long memoryUsed = hostAPI.getMemoryInHost(host).get(0);
			memoryRate = memoryUsed*100/memoryTotal + "";
		}
		return memoryRate;
	}
	
	public String getCpuUsedRate() throws Exception{
		FetchDynamicData data = new FetchDynamicData();
		String cpuRate = StringUtils.double2String((double)data.getIndexNeedInfo().get("cpu_avg"));
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
