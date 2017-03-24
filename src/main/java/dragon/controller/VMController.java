package main.java.dragon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.service.ClusterService;
import main.java.dragon.service.HostService;
import main.java.dragon.utils.StringUtils;

@Controller
public class VMController {
	@Autowired
	private ClusterService clusterService;
	@Autowired
	private HostService hostService;

	@RequestMapping("showVM")
	public String showVM(){
		
		return "jsp/vm/virtual_machine";
	}
	
	@RequestMapping("showClusterAndHost")
	public String showClusterAndHost(Model model){
		List<Cluster> clusters = clusterService.getAllCluster();
		if(!StringUtils.isEmpty(clusters)){
			Cluster cluster = clusters.get(0);
			cluster.setCpuAverage(Double.parseDouble(StringUtils.double2String(cluster.getCpuAverage())));
			String clusterMemoryUsedRate = StringUtils.double2String(cluster.getMemoryUsed()*100.0/cluster.getMemoryTotal());
			String clusterStorageUserRate = StringUtils.double2String(cluster.getStorageUsed()*100.0/cluster.getStorageTotal());
			model.addAttribute("cluster", cluster);
			model.addAttribute("clusterMemoryUsedRate", clusterMemoryUsedRate);
			model.addAttribute("clusterStorageUserRate", clusterStorageUserRate);
		}
		List<HostInstance> hostInstances = hostService.getAllHost();
		if(!StringUtils.isEmpty(hostInstances)){
			HostInstance hostInstance = hostInstances.get(0);
			hostInstance.setCpuAverage(Double.parseDouble(StringUtils.double2String(hostInstance.getCpuAverage())));
			String hostMemoryUsedRate = StringUtils.double2String(hostInstance.getMemoryUsed()*100.0/hostInstance.getMemoryTotal());
			model.addAttribute("hostInstance", hostInstance);
			model.addAttribute("hostMemoryUsedRate", hostMemoryUsedRate);
			Cluster cluster = clusterService.getClusterById(hostInstance.getClusterId());
			model.addAttribute("clusterName",cluster.getName());
		}
		return "jsp/colony_hostcomputer";
	}
}
