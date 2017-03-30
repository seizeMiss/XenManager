package main.java.dragon.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.pojo.Storage;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNeedInfo;
import main.java.dragon.service.ClusterService;
import main.java.dragon.service.HostService;
import main.java.dragon.service.StorageService;
import main.java.dragon.service.VMService;
import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;

@Controller
public class VMController {
	@Autowired
	private ClusterService clusterService;
	@Autowired
	private HostService hostService;
	@Autowired
	private VMService vmService;
	@Autowired
	private StorageService storageService;

	@RequestMapping("showVM")
	public String showVM(Model model){
		List<VmInstance> vmInstances = null;
		List<VmNeedInfo> vmNeedInfos = new ArrayList<VmNeedInfo>();
		boolean isShowMemoryRate = false;
		try {
			FetchDynamicData data = new FetchDynamicData();
//			vmService.addVm(null, null, null);
			vmInstances = vmService.getAllVm();
			VmNeedInfo vmNeedInfo = null;
			Cluster cluster = null;
			HostInstance hostInstance = null;
			double memoryTotal = 0.0d;
			double memoryUsed = 0.0d;
			double cpuRate = 0.0d;
			double memoryRate = 0.0d;
			for(VmInstance vmInstance : vmInstances){
				cluster = clusterService.getClusterById(vmInstance.getClusterId());
				hostInstance = hostService.getHostInstanceById(vmInstance.getHostId());
				vmInstance.setMemory(vmInstance.getMemory()/1024);
				if(vmInstance.getPowerStatus().equals("Running")){
					Map<String, Object> map = data.getVmNeedInfoByParseXml(vmInstance.getUuid());
					cpuRate = (double) map.get("cpu_avg")*100;
					isShowMemoryRate = (boolean) map.get("memory_flag");
					memoryUsed = (double)map.get("memory_used");
					memoryTotal = (double)map.get("memory_total");
				}
				if(isShowMemoryRate){
					memoryRate = NumberUtils.computerUsedRate(memoryTotal, memoryTotal-memoryUsed, 1);
				}
				if(StringUtils.isEmpty(cluster, hostInstance)){
					vmNeedInfo = new VmNeedInfo(vmInstance, NumberUtils.setdoubleScal(cpuRate), 
							memoryRate,"-","-",StringUtils.double2String(memoryTotal),
							StringUtils.double2StringKeepScal(memoryUsed),isShowMemoryRate);
				}else{
					vmNeedInfo = new VmNeedInfo(vmInstance, NumberUtils.setdoubleScal(cpuRate), 
							memoryRate,cluster.getName(),hostInstance.getName(),
							StringUtils.double2String(memoryTotal),StringUtils.double2StringKeepScal(memoryUsed),isShowMemoryRate);
				}
				vmNeedInfos.add(vmNeedInfo);
			}
			model.addAttribute("vmNeedInfos", vmNeedInfos);
			model.addAttribute("isShowMemoryRate", isShowMemoryRate ? 1 : 0);
			model.addAttribute("vmCount", vmNeedInfos.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "jsp/vm/virtual_machine";
	}
	
	@RequestMapping("showClusterAndHost")
	public String showClusterAndHost(Model model){
		Cluster cluster = clusterService.saveCluster();
		if(!StringUtils.isEmpty(cluster)){
			cluster.setCpuAverage(Double.parseDouble(StringUtils.double2String(cluster.getCpuAverage())));
			String clusterMemoryUsedRate = StringUtils.double2String(cluster.getMemoryUsed()*100.0/cluster.getMemoryTotal());
			String clusterStorageUserRate = StringUtils.double2String(cluster.getStorageUsed()*100.0/cluster.getStorageTotal());
			model.addAttribute("cluster", cluster);
			model.addAttribute("clusterMemoryUsedRate", clusterMemoryUsedRate);
			model.addAttribute("clusterStorageUserRate", clusterStorageUserRate);
		}
		HostInstance hostInstance = hostService.saveHost();
		if(!StringUtils.isEmpty(hostInstance)){
			hostInstance.setCpuAverage(Double.parseDouble(StringUtils.double2String(hostInstance.getCpuAverage())));
			String hostMemoryUsedRate = StringUtils.double2String(hostInstance.getMemoryUsed()*100.0/hostInstance.getMemoryTotal());
			model.addAttribute("hostInstance", hostInstance);
			model.addAttribute("hostMemoryUsedRate", hostMemoryUsedRate);
			Cluster clusterByHost = clusterService.getClusterById(hostInstance.getClusterId());
			model.addAttribute("clusterName",clusterByHost.getName());
		}
		return "jsp/colony_hostcomputer";
	}
	@RequestMapping("showAddVm")
	public String showAddVm(Model model){
		List<Storage> storages = storageService.getAllStorage();
		List<Cluster> clusters = clusterService.getAllCluster();
		if(!StringUtils.isEmpty(storages) && !StringUtils.isEmpty(clusters)){
			model.addAttribute("storages", storages);
			model.addAttribute("clusters", clusters);
		}
		return "jsp/vm/add_vm";
	}
	
	@RequestMapping("searchVmByName")
	@ResponseBody
	public List<VmInstance> searchVmByName(Model model,HttpServletRequest request){
		List<VmInstance> instances = null;
		String name = request.getParameter("searchContent");
		instances = vmService.getVmInstanceByName(name);
		for(VmInstance vmInstance : instances){
			vmInstance.setVmStorages(null);
			vmInstance.setVmNetWorks(null);
		}
		return instances;
	}
}
