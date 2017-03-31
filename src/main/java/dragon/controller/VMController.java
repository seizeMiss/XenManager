package main.java.dragon.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.Storage;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNeedInfo;
import main.java.dragon.service.ClusterService;
import main.java.dragon.service.HostService;
import main.java.dragon.service.ImageService;
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
	@Autowired
	private ImageService imageService;

	@RequestMapping("showVM")
	public String showVM(Model model){
		List<VmInstance> vmInstances = null;
		List<VmNeedInfo> vmNeedInfos = new ArrayList<VmNeedInfo>();
		boolean isShowMemoryRate = false;
		boolean isShowCpuRate = true;
		try {
			FetchDynamicData data = new FetchDynamicData();
//			vmService.addVm(null, null, null);
			vmInstances = vmService.getAllVm();
			VmNeedInfo vmNeedInfo = null;
			Set<String> vmOsTypes = new HashSet<>();
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
					if(Double.isNaN(memoryTotal) || Double.isNaN(memoryUsed)){
						isShowMemoryRate = false;
					}else{
						memoryRate = NumberUtils.computerUsedRate(memoryTotal, memoryTotal-memoryUsed, 1);
					}
					if(Double.isNaN(cpuRate)){
						isShowCpuRate = false;
					}
				}
				if(StringUtils.isEmpty(cluster, hostInstance)){
					vmNeedInfo = new VmNeedInfo(vmInstance, NumberUtils.setdoubleScal(Double.isNaN(cpuRate) ? 0.0d : cpuRate), 
							memoryRate,"-","-",StringUtils.double2String(memoryTotal),
							StringUtils.double2StringKeepScal(memoryUsed),isShowMemoryRate ? 1 : 0, isShowCpuRate ? 1 : 0);
				}else{
					vmNeedInfo = new VmNeedInfo(vmInstance, NumberUtils.setdoubleScal(Double.isNaN(cpuRate) ? 0.0d : cpuRate), 
							memoryRate,cluster.getName(),hostInstance.getName(),
							StringUtils.double2String(memoryTotal),StringUtils.double2StringKeepScal(memoryUsed),
							isShowMemoryRate ? 1 : 0, isShowCpuRate ? 1 : 0);
				}
				vmNeedInfos.add(vmNeedInfo);
				vmOsTypes.add(vmInstance.getOsType());
			}
			model.addAttribute("vmNeedInfos", vmNeedInfos);
			model.addAttribute("isShowMemoryRate", isShowMemoryRate ? 1 : 0);
			model.addAttribute("isShowCpuRate", isShowCpuRate ? 1 : 0);
			model.addAttribute("vmCount", vmNeedInfos.size());
			model.addAttribute("vmOsTypes", vmOsTypes);
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
		List<Image> images = imageService.getAllImages();
		if(!StringUtils.isEmpty(storages) && !StringUtils.isEmpty(clusters)
				&& !StringUtils.isEmpty(images)){
			model.addAttribute("images", images);
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
	private Set<String> getVmOsTypes(){
		Set<String> vmOsTypes = new HashSet<>();
		List<VmInstance> vmInstances = vmService.getAllVm();
		if(!StringUtils.isEmpty(vmInstances)){
			for(VmInstance vmInstance : vmInstances){
				vmOsTypes.add(vmInstance.getOsType());
			}
		}
		return vmOsTypes;
	}
	@RequestMapping("searchVm")
	public String searchVmsByCondition(HttpServletRequest request,Model model){
		String vmName = request.getParameter("condition-name");
		String vmStatus = request.getParameter("selected-state");
		String vmOsType = request.getParameter("selected-os");
		List<VmInstance> vmInstances = vmService.getVmInstanceByCondition(vmName, vmStatus, vmOsType);
		List<VmNeedInfo> vmNeedInfos = null;
		boolean isShowMemoryRate = false;
		boolean isShowCpuRate = true;
		try {
			FetchDynamicData data = new FetchDynamicData();
			VmNeedInfo vmNeedInfo = null;
			Set<String> vmOsTypes = getVmOsTypes();
			Cluster cluster = null;
			HostInstance hostInstance = null;
			double memoryTotal = 0.0d;
			double memoryUsed = 0.0d;
			double cpuRate = 0.0d;
			double memoryRate = 0.0d;
			if(!StringUtils.isEmpty(vmInstances)){
				vmNeedInfos = new ArrayList<VmNeedInfo>();
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
						if(Double.isNaN(memoryTotal) || Double.isNaN(memoryUsed)){
							isShowMemoryRate = false;
						}else{
							memoryRate = NumberUtils.computerUsedRate(memoryTotal, memoryTotal-memoryUsed, 1);
						}
						if(Double.isNaN(cpuRate)){
							isShowCpuRate = false;
						}
					}
					if(StringUtils.isEmpty(cluster, hostInstance)){
						vmNeedInfo = new VmNeedInfo(vmInstance, NumberUtils.setdoubleScal(Double.isNaN(cpuRate) ? 0.0d : cpuRate), 
								memoryRate,"-","-",StringUtils.double2String(memoryTotal),
								StringUtils.double2StringKeepScal(memoryUsed),isShowMemoryRate ? 1 : 0, isShowCpuRate ? 1 : 0);
					}else{
						vmNeedInfo = new VmNeedInfo(vmInstance, NumberUtils.setdoubleScal(Double.isNaN(cpuRate) ? 0.0d : cpuRate), 
								memoryRate,cluster.getName(),hostInstance.getName(),
								StringUtils.double2String(memoryTotal),StringUtils.double2StringKeepScal(memoryUsed),
								isShowMemoryRate ? 1 : 0, isShowCpuRate ? 1 : 0);
					}
					vmNeedInfos.add(vmNeedInfo);
				}
			}
			model.addAttribute("vmNeedInfos", vmNeedInfos);
			model.addAttribute("isShowMemoryRate", isShowMemoryRate ? 1 : 0);
			model.addAttribute("isShowCpuRate", isShowCpuRate ? 1 : 0);
			model.addAttribute("vmCount", vmNeedInfos == null ? 0 : vmNeedInfos.size());
			model.addAttribute("vmOsTypes", vmOsTypes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "jsp/vm/virtual_machine";
		
	}
	
	@RequestMapping("openVmByselected")
	@ResponseBody
	public VmInstance openVmByselected(HttpServletRequest request){
		VmInstance vmInstance = null;
		String id = request.getParameter("vid");
		vmInstance = vmService.startVm(id);
		vmInstance.setVmStorages(null);
		vmInstance.setVmNetWorks(null);
		return vmInstance;
	}
	@RequestMapping("restartVmByselected")
	@ResponseBody
	public VmInstance restartVmByselected(HttpServletRequest request){
		VmInstance vmInstance = null;
		String id = request.getParameter("vid");
		vmInstance = vmService.restartVm(id);
		vmInstance.setVmStorages(null);
		vmInstance.setVmNetWorks(null);
		return vmInstance;
	}
	@RequestMapping("closeVmByselected")
	@ResponseBody
	public VmInstance closeVmByselected(HttpServletRequest request){
		VmInstance vmInstance = null;
		String id = request.getParameter("vid");
		vmInstance = vmService.closeVm(id);
		vmInstance.setVmStorages(null);
		vmInstance.setVmNetWorks(null);
		return vmInstance;
	}
}
