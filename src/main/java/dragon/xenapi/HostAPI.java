package main.java.dragon.xenapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.HostMetrics;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VM;

import main.java.dragon.service.impl.ConnectionUtil;

public class HostAPI extends ConnectionUtil{
	
	public HostAPI() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	private boolean isAvailableVm(VM vm) throws Exception{
		VM.Record record = vm.getRecord(connection);
		if(record.isASnapshot || record.isControlDomain 
					|| record.isATemplate || record.isSnapshotFromVmpp){
			return false;
		}else{
			return true;
		}
	}
	
	public int getVmCountInHost(Host host,boolean isRunning) throws Exception{
		int vmCount = 0;
		if(isRunning){
			Set<VM> vms = host.getResidentVMs(connection);
			for (VM vm : vms){
				if(vm.getPowerState(connection).equals("Running")){
					vmCount++;
				}
			}
		}else{
			vmCount = host.getResidentVMs(connection).size();
		}
		
		return vmCount;
	}
	public List<Long> getMemoryInHost(Host host) throws Exception{
		List<Long> memoryArg = new ArrayList<Long>();
		Long memoryTotal = 0l;
		Long memoryFree = 0l;
		HostMetrics hostMetrics = host.getMetrics(connection);
		memoryFree = hostMetrics.getMemoryFree(connection)/1024/1024;
		memoryTotal = hostMetrics.getMemoryTotal(connection)/1024/1024;
		memoryArg.add(memoryTotal-memoryFree);
		memoryArg.add(memoryTotal);
		return memoryArg;
	}

	public List<Integer> getStorageInHost(Host host) throws Exception{
		List<Integer> storageArg = new ArrayList<Integer>();
		int srCount = 0;
		int storageTotal =0;
		int storageUsed = 0;
		Set<PBD> pbds = host.getPBDs(connection);
		for(PBD pbd : pbds){
			SR sr = pbd.getSR(connection);
			if(sr.getPhysicalUtilisation(connection) > 0){
				srCount++;
				storageTotal += sr.getPhysicalSize(connection)/1024/1024/1024;
				storageUsed += sr.getPhysicalUtilisation(connection)/1024/1024/1024;
			}
		}
		storageArg.add(srCount);
		storageArg.add(storageTotal);
		storageArg.add(storageUsed);
		return storageArg;
	}
	public Pool getPoolByName(String name) throws Exception {
		Pool targetPool = null;
		Set<Pool> pools = Pool.getAll(connection);
		for (Pool pool : pools) {
			if (pool.getNameLabel(connection).equals(name)) {
				targetPool = pool;
			}
		}
		return targetPool;
	}
	
	
}
