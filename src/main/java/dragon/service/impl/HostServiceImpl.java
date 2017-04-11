package main.java.dragon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.Types.BadServerResponse;
import com.xensource.xenapi.Types.XenAPIException;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.HostDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.service.HostService;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.HostAPI;

@Service
@Transactional
public class HostServiceImpl extends ConnectionUtil implements HostService{
	
	@Autowired
	private HostDao hostDao;
	
	@Autowired
	private ClusterDao clusterDao;
	
	@Autowired
	private VMDao vmDao;

	public HostServiceImpl() throws Exception {
		super();
	}
	
	@Override
	public HostInstance addHost() {
		HostInstance hostInstance = null;
		
		return hostInstance;
	}

	@Override
	public List<HostInstance> getAllHost() {
		return hostDao.selectAllHost();
	}

	@Override
	public List<HostInstance> saveHost(){
		List<HostInstance> hostInstances = getAllHost();
		if(!StringUtils.isEmpty(hostInstances)){
			try {
				hostInstances = updateHostInstance(hostInstances);
				hostDao.updateHost(hostInstances);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hostInstances;
	}
	@Override
	public HostInstance getHostInstanceById(String id) {
		HostInstance hostInstance = null;
		hostInstance = hostDao.selectHostById(id);
		return hostInstance;
	}
	
	public List<HostInstance> updateHostInstance(List<HostInstance> hosts) throws Exception{
		List<HostInstance> hostInstances = new ArrayList<HostInstance>();
		FetchDynamicData data = new FetchDynamicData();
		HostAPI hostAPI = new HostAPI();
		for(HostInstance hostInstance : hosts){
			int vmTotalCount = 0;
			int vmRunningCount = 0;
			int cpuTotal = 0;
			Double cpuAverage = 0.0d;
			Long memoryTotal = 0l;
			Long memoryUsed = 0l;
			Host host = Host.getByUuid(connection, hostInstance.getUuid());
			List<VmInstance> vmInstances  = vmDao.selectVmInstanceByHostId(hostInstance.getId());
			if(!StringUtils.isEmpty(vmInstances)){
				for(VmInstance vmInstance : vmInstances){
					if(vmInstance.getPowerStatus().equals(CommonConstants.VM_POWER_START)){
						vmRunningCount++;
					}
					if(vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS){
						vmTotalCount++;
					}
				}
			}
			if(host.getEnabled(connection)){
				Map<String, Object> dataMap = data.getIndexNeedInfoByParseXml();
				cpuTotal = (int)dataMap.get("cpu_account");
				cpuAverage = (Double)dataMap.get("cpu_avg");
				memoryTotal = hostAPI.getMemoryInHost(host).get(1);
				memoryUsed = hostAPI.getMemoryInHost(host).get(0);
				memoryTotal = (long) Math.ceil(memoryTotal*1.0/1024);
				memoryUsed = (long)Math.ceil(memoryUsed*1.0/1024);
			}
			hostInstance.setVmRunningCount(vmRunningCount);
			hostInstance.setVmTotalCount(vmTotalCount);
			hostInstance.setCpuAverage(cpuAverage);
			hostInstance.setCpuTotal(cpuTotal);
			
			hostInstance.setMemoryTotal(memoryTotal.intValue());
			hostInstance.setMemoryUsed(memoryUsed.intValue());
			hostInstances.add(hostInstance);
		}
		return hostInstances;
	}
	

}
