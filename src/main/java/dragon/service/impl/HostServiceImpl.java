package main.java.dragon.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.Pool;

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
		// TODO Auto-generated constructor stub
	}
	public HostInstance getHost(String id) throws Exception{
		HostInstance hostInstance = null;
		
		int status = 0;
		String powerStatus = "death";
		int cpuTotal = 0;
		int cpuUsed = 0;
		double cpuAverage = 0.0d;
		Long memoryTotal = 0l;
		Long memoryUsed = 0l;
		int diskTotal = 0;
		int diskUsed = 0;
		int vmTotalCount = 0;
		int vmRunningCount = 0;
		String description = "";
		HostAPI hostAPI = new HostAPI();
		FetchDynamicData data = new FetchDynamicData();
		List<Cluster> clusters = clusterDao.selectAllClusters();
		Cluster cluster = null;
		if(!StringUtils.isEmpty(clusters)){
			cluster = clusters.get(0);
		}
		String clusterId = cluster.getId();
		Pool pool = hostAPI.getPoolByName("4.206");
		Host host = pool.getMaster(connection);
		String uuid = host.getUuid(connection);
		String name = host.getNameLabel(connection);
		if(host.getEnabled(connection)){
			List<VmInstance> vmInstances  = vmDao.selectVmInstanceByHostId(id);
			Map<String, Object> dataMap = data.getIndexNeedInfoByParseXml();
			status = 1;
			powerStatus = "running";
			cpuTotal = (int)dataMap.get("cpu_account");
			cpuAverage = (double)dataMap.get("cpu_avg");
			memoryTotal = hostAPI.getMemoryInHost(host).get(1);
			memoryUsed = hostAPI.getMemoryInHost(host).get(0);
			memoryTotal = (long) Math.ceil(memoryTotal*1.0/1024);
			memoryUsed = (long)Math.ceil(memoryUsed*1.0/1024);
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
		}
		description = host.getNameDescription(connection);
		hostInstance = new HostInstance(id, name, uuid, clusterId, status, powerStatus, 
				cpuUsed, cpuTotal, cpuAverage, memoryTotal.intValue(), memoryUsed.intValue(), 
				diskTotal, diskUsed, vmTotalCount, vmRunningCount, description);
		return hostInstance;
	}

	@Override
	public HostInstance addHost() {
		// TODO Auto-generated method stub
		String id = StringUtils.generateUUID();
		HostInstance hostInstance = null;
		try {
			hostInstance = getHost(id);
			hostDao.insertHost(hostInstance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hostInstance;
	}

	@Override
	public List<HostInstance> getAllHost() {
		// TODO Auto-generated method stub
		return hostDao.selectAllHost();
	}

	@Override
	public HostInstance saveHost(){
		// TODO Auto-generated method stub
		List<HostInstance> hostInstances = getAllHost();
		HostInstance hostInstance = null;
		if(!StringUtils.isEmpty(hostInstances)){
			try {
				hostInstance = getHost(hostInstances.get(0).getId());
				hostDao.updateHost(hostInstance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hostInstance;
	}
	@Override
	public HostInstance getHostInstanceById(String id) {
		// TODO Auto-generated method stub
		HostInstance hostInstance = null;
		hostInstance = hostDao.selectHostById(id);
		return hostInstance;
	}
	

}
