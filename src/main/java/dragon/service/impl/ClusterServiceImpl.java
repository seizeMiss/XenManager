package main.java.dragon.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.HostCpu;
import com.xensource.xenapi.HostMetrics;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VMAppliance;
import com.xensource.xenapi.VMGuestMetrics;
import com.xensource.xenapi.VMMetrics;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.service.ClusterService;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.HostAPI;

@Service
@Transactional
public class ClusterServiceImpl extends ConnectionUtil implements ClusterService {
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private VMDao vmDao;

	public ClusterServiceImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 判断虚拟机是否是用户创建的
	 * 
	 * @param vm
	 * @return
	 * @throws Exception
	 */
	private boolean isAvailableVm(VM vm) throws Exception {
		VM.Record record = vm.getRecord(connection);
		if (record.isASnapshot || record.isControlDomain || record.isATemplate || record.isSnapshotFromVmpp) {
			return false;
		} else {
			return true;
		}
	}

	public Cluster getCluster(String id) throws Exception{
		Cluster cluster = null;
		Pool pool = null;
		String name = "4.206";
		String ipAddress = "";
		int status = 0;
		double cpuAverage = 0.0d;
		Long memoryTotal = 0l;
		Long memoryUsed = 0l;
		int storageTotal = 0;
		int storageUsed = 0;
		int storageCount = 0;
		int hostCount = 0;
		int vmCount = 0;
		String description = "";

		HostAPI hostAPI = new HostAPI();
		pool = hostAPI.getPoolByName(name);
		FetchDynamicData data = new FetchDynamicData();
		Host host = pool.getMaster(connection);
		ipAddress = host.getAddress(connection);
		if (host.getEnabled(connection)) {
			status = 1;
		}
		cpuAverage = (double) data.getIndexNeedInfoByParseXml().get("cpu_avg");
		memoryTotal = hostAPI.getMemoryInHost(host).get(1);
		memoryUsed = hostAPI.getMemoryInHost(host).get(0);
		memoryTotal = (long) Math.ceil(memoryTotal*1.0/1024);
		memoryUsed = (long)Math.ceil(memoryUsed*1.0/1024);
		storageCount = hostAPI.getStorageInHost(host).get(0);
		storageTotal = hostAPI.getStorageInHost(host).get(1);
		storageUsed = hostAPI.getStorageInHost(host).get(2);
		hostCount = 1;
		List<VmInstance> vmInstances = vmDao.selectVmInstanceByClusterId(id);
		for(VmInstance vmInstance : vmInstances){
			if(vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS){
				vmCount++;
			}
		}
		description = pool.getNameDescription(connection);
		cluster = new Cluster(id, name, ipAddress, status, cpuAverage, memoryTotal.intValue(), memoryUsed.intValue(),
				storageTotal, storageUsed, storageCount, hostCount, vmCount, description);
		return cluster;
	}

	public Cluster addCluster() {
		Cluster cluster = null;
		String id = StringUtils.generateUUID();
		try {
			cluster = getCluster(id);
			clusterDao.insertCluster(cluster);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cluster;
	}

	@Override
	public Cluster saveCluster() {
		List<Cluster> clusters = getAllCluster();
		if (!StringUtils.isEmpty(clusters)) {
			try {
				Cluster cluster = getCluster(clusters.get(0).getId());
				clusterDao.updateCluster(cluster);
				return cluster;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Cluster getClusterById(String id) {
		// TODO Auto-generated method stub
		Cluster cluster = null;
		cluster = clusterDao.selectClusterById(id);
		return cluster;
	}

	@Override
	public List<Cluster> getAllCluster() {
		// TODO Auto-generated method stub
		return clusterDao.selectAllClusters();
	}

}
