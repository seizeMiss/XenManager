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
import main.java.dragon.utils.ConnectionInfoParseXml;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.HostAPI;

@Service
@Transactional
public class HostServiceImpl extends ConnectionUtil implements HostService {

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
		List<HostInstance> hostInstances = new ArrayList<>();
		String id = StringUtils.generateUUID();
		try {
			hostInstance = getHost(id);
			hostInstances.add(hostInstance);
			hostDao.insertHost(hostInstances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hostInstance;
	}

	private HostInstance getHost(String id) throws Exception {
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
		Map<String, String> connectionInfo = ConnectionInfoParseXml.getXenConenctionInfo();
		
		List<Cluster> clusters = clusterDao.selectAllClusters();
		Cluster cluster = null;

		if (!StringUtils.isEmpty(clusters)) {
			cluster = clusters.get(0);
		}
		String clusterId = cluster.getId();
		Pool pool = hostAPI.getPoolByName(connectionInfo.get("cluster-name"));
		Host host = pool.getMaster(connection);
		String uuid = host.getUuid(connection);
		String name = host.getNameLabel(connection);

		if (host.getEnabled(connection)) {
			status = 1;
			powerStatus = "running";
			cpuTotal = (int) data.getIndexNeedInfoByParseXml().get("cpu_account");
			cpuAverage = (double) data.getIndexNeedInfoByParseXml().get("cpu_avg");
			memoryTotal = hostAPI.getMemoryInHost(host).get(1);
			memoryUsed = hostAPI.getMemoryInHost(host).get(0);
			vmRunningCount = hostAPI.getVmCountInHost(host, true);
			vmTotalCount = hostAPI.getVmCountInHost(host, false);
		}

		description = host.getNameDescription(connection);
		hostInstance = new HostInstance(id, name, uuid, clusterId, status, powerStatus,
				cpuUsed, cpuTotal, cpuAverage, memoryTotal.intValue(), memoryUsed.intValue(),
				diskTotal, diskUsed, vmTotalCount, vmRunningCount, description);

		return hostInstance;

	}

	@Override
	public List<HostInstance> getAllHost() {
		return hostDao.selectAllHost();
	}

	@Override
	public List<HostInstance> saveHost() {
		List<HostInstance> hostInstances = getAllHost();
		if (!StringUtils.isEmpty(hostInstances)) {
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

	public List<HostInstance> updateHostInstance(List<HostInstance> hosts) throws Exception {
		List<HostInstance> hostInstances = new ArrayList<HostInstance>();
		FetchDynamicData data = new FetchDynamicData();
		HostAPI hostAPI = new HostAPI();
		for (HostInstance hostInstance : hosts) {
			int vmTotalCount = 0;
			int vmRunningCount = 0;
			int cpuTotal = 0;
			Double cpuAverage = 0.0d;
			Long memoryTotal = 0l;
			Long memoryUsed = 0l;
			Host host = Host.getByUuid(connection, hostInstance.getUuid());
			List<VmInstance> vmInstances = vmDao.selectVmInstanceByHostId(hostInstance.getId());
			if (!StringUtils.isEmpty(vmInstances)) {
				for (VmInstance vmInstance : vmInstances) {
					if (vmInstance.getPowerStatus().equals(CommonConstants.VM_POWER_START)) {
						vmRunningCount++;
					}
					if (vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS) {
						vmTotalCount++;
					}
				}
			}
			if (host.getEnabled(connection)) {
				Map<String, Object> dataMap = data.getIndexNeedInfoByParseXml();
				cpuTotal = (int) dataMap.get("cpu_account");
				cpuAverage = (Double) dataMap.get("cpu_avg");
				memoryTotal = hostAPI.getMemoryInHost(host).get(1);
				memoryUsed = hostAPI.getMemoryInHost(host).get(0);
				memoryTotal = (long) Math.ceil(memoryTotal * 1.0 / 1024);
				memoryUsed = (long) Math.ceil(memoryUsed * 1.0 / 1024);
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
