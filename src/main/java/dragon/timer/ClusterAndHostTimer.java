package main.java.dragon.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xensource.xenapi.Host;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.HostDao;
import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.service.impl.ConnectionUtil;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.HostAPI;

@Component
public class ClusterAndHostTimer extends ConnectionUtil{
	

	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private HostDao hostDao;
	
	public ClusterAndHostTimer() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	@Scheduled(cron="0 0 10 * * ?")
	public void refreshClusterAndHostInfo(){
		List<HostInstance> hostInstances = null;
		try {
			hostInstances = getHost();
			if(!StringUtils.isEmpty(hostInstances)){
				hostDao.insertHost(hostInstances);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<HostInstance> getHost() throws Exception{
		List<HostInstance> hostInstances = new ArrayList<HostInstance>();
		boolean isHostExist = false;
		HostAPI hostAPI = new HostAPI();
		int status = 0;
		String powerStatus = "death";
		int cpuTotal = 0;
		int cpuUsed = 0;
		Double cpuAverage = 0.0d;
		Long memoryTotal = 0l;
		Long memoryUsed = 0l;
		int diskTotal = 0;
		int diskUsed = 0;
		String description = "";
		FetchDynamicData data = new FetchDynamicData();
		List<Cluster> clusters = clusterDao.selectAllClusters();
		Cluster cluster = null;
		if(!StringUtils.isEmpty(clusters)){
			cluster = clusters.get(0);
		}
		String clusterId = cluster.getId();
		List<HostInstance> selectHostInstances = hostDao.selectAllHost();
		Set<Host> hosts = Host.getAll(connection);
		for(Host host : hosts){
			String uuid = host.getUuid(connection);
			if(!StringUtils.isEmpty(selectHostInstances)){
				for(HostInstance hostInstance : selectHostInstances){
					if(hostInstance.getUuid().equals(uuid)){
						isHostExist = true;
					}
				}
			}
			//该主机 已存在于数据库
			if(isHostExist){
				isHostExist = false;
				continue;
			}
			String id = StringUtils.generateUUID();
			String name = host.getNameLabel(connection);
			int vmTotalCount = hostAPI.getVmCountInHost(host, false);
			int vmRunningCount = hostAPI.getVmCountInHost(host, true);
			if(host.getEnabled(connection)){
				Map<String, Object> dataMap = data.getIndexNeedInfoByParseXml();
				status = 1;
				powerStatus = "running";
				cpuTotal = (int)dataMap.get("cpu_account");
				cpuAverage = (Double)dataMap.get("cpu_avg");
				memoryTotal = hostAPI.getMemoryInHost(host).get(1);
				memoryUsed = hostAPI.getMemoryInHost(host).get(0);
				memoryTotal = (long) Math.ceil(memoryTotal*1.0/1024);
				memoryUsed = (long)Math.ceil(memoryUsed*1.0/1024);
			}
			description = host.getNameDescription(connection);
			HostInstance hostInstance = new HostInstance(id, name, uuid, clusterId, status, powerStatus, 
					cpuUsed, cpuTotal, cpuAverage.isNaN() ? 0.0 : cpuAverage, memoryTotal.intValue(), memoryUsed.intValue(), 
							diskTotal, diskUsed, vmTotalCount, vmRunningCount, description);
			hostInstances.add(hostInstance);
		}
		return hostInstances;
	}

}
