package main.java.dragon.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.HostInstance;
import main.java.dragon.pojo.HostNeedInfo;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.Storage;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNeedInfo;
import main.java.dragon.service.ClusterService;
import main.java.dragon.service.HostService;
import main.java.dragon.service.ImageService;
import main.java.dragon.service.StorageService;
import main.java.dragon.service.VMService;
import main.java.dragon.utils.CommonConstants;
import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.HostAPI;

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
	public String showVM(Model model) {
		vmService.addVm();
		List<VmInstance> vmInstances = vmService.getAllVm();
		List<VmNeedInfo> vmNeedInfos = null;
		Set<String> vmOsTypes = null;
		if (!StringUtils.isEmpty(vmInstances)) {
			vmOsTypes = getVmOsTypes();
			try {
				vmNeedInfos = initVmNeedInfo(vmInstances, false);
				model.addAttribute("vmCount", vmNeedInfos.size());
				model.addAttribute("vmOsTypes", vmOsTypes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("vmNeedInfos", vmNeedInfos);
		return "jsp/vm/virtual_machine";
	}

	@RequestMapping("showClusterAndHost")
	public String showClusterAndHost(Model model) {
		Cluster cluster = clusterService.saveCluster();
		if (!StringUtils.isEmpty(cluster)) {
			cluster.setCpuAverage(Double.parseDouble(StringUtils.double2String(cluster.getCpuAverage())));
			String clusterMemoryUsedRate = StringUtils
					.double2String(cluster.getMemoryUsed() * 100.0 / cluster.getMemoryTotal());
			String clusterStorageUserRate = StringUtils
					.double2String(cluster.getStorageUsed() * 100.0 / cluster.getStorageTotal());
			model.addAttribute("cluster", cluster);
			model.addAttribute("clusterMemoryUsedRate", clusterMemoryUsedRate);
			model.addAttribute("clusterStorageUserRate", clusterStorageUserRate);
		}
		List<HostInstance> hostInstances = hostService.saveHost();
		List<HostNeedInfo> hostNeedInfos = new ArrayList<HostNeedInfo>();
		if (!StringUtils.isEmpty(hostInstances)) {
			for(HostInstance hostInstance : hostInstances){
				hostInstance.setCpuAverage(Double.parseDouble(StringUtils.double2String(hostInstance.getCpuAverage())));
				String hostMemoryUsedRate = StringUtils
						.double2String(hostInstance.getMemoryUsed() * 100.0 / hostInstance.getMemoryTotal());
				Cluster clusterByHost = clusterService.getClusterById(hostInstance.getClusterId());
				HostNeedInfo hostNeedInfo = new HostNeedInfo(hostInstance, clusterByHost.getName(), hostMemoryUsedRate);
				hostNeedInfos.add(hostNeedInfo);
			}
			model.addAttribute("hostNeedInfos", hostNeedInfos);
			model.addAttribute("host_size", hostNeedInfos.size());
		}
		return "jsp/colony_hostcomputer";
	}

	@RequestMapping("showAddVm")
	public String showAddVm(Model model) {
		List<Storage> storages = storageService.getAllStorage();
		List<Cluster> clusters = clusterService.getAllCluster();
		List<Image> images = imageService.getAllImages();
		if (!StringUtils.isEmpty(storages) && !StringUtils.isEmpty(clusters) && !StringUtils.isEmpty(images)) {
			model.addAttribute("images", images);
			model.addAttribute("storages", storages);
			model.addAttribute("clusters", clusters);
		}
		return "jsp/vm/add_vm";
	}

	@RequestMapping("searchVmByName")
	@ResponseBody
	public List<VmInstance> searchVmByName(Model model, HttpServletRequest request) {
		List<VmInstance> instances = null;
		String name = request.getParameter("searchContent");
		instances = vmService.getVmInstanceByName(name);
		List<VmInstance> filterVmInstances = new ArrayList<VmInstance>();
		for (VmInstance vmInstance : instances) {
			if(vmInstance.getPowerStatus().equals(CommonConstants.VM_POWER_CLOSED)){
				vmInstance.setVmStorages(null);
				vmInstance.setVmNetWorks(null);
				filterVmInstances.add(vmInstance);
			}
		}
		return filterVmInstances;
	}

	private Set<String> getVmOsTypes() {
		Set<String> vmOsTypes = new HashSet<>();
		List<VmInstance> vmInstances = vmService.getAllVm();
		if (!StringUtils.isEmpty(vmInstances)) {
			for (VmInstance vmInstance : vmInstances) {
				vmOsTypes.add(vmInstance.getOsType());
			}
		}
		return vmOsTypes;
	}

	@RequestMapping("searchVm")
	public String searchVmsByCondition(HttpServletRequest request, Model model) {
		String vmName = request.getParameter("condition-name");
		String vmStatus = request.getParameter("selected-state");
		String vmOsType = request.getParameter("selected-os");
		List<VmInstance> vmInstances = vmService.getVmInstanceByCondition(vmName, vmStatus, vmOsType);
		List<VmNeedInfo> vmNeedInfos = null;
		Set<String> vmOsTypes = null;
		if (!StringUtils.isEmpty(vmInstances)) {
			vmOsTypes = getVmOsTypes();
			try {
				vmNeedInfos = initVmNeedInfo(vmInstances, false);
				model.addAttribute("vmCount", vmNeedInfos.size());
				model.addAttribute("vmOsTypes", vmOsTypes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("vmNeedInfos", vmNeedInfos);
		return "jsp/vm/virtual_machine";
	}

	@RequestMapping("openVmByselected")
	@ResponseBody
	public VmInstance openVmByselected(HttpServletRequest request) {
		VmInstance vmInstance = null;
		String id = request.getParameter("vid");
		vmInstance = vmService.startVm(id);
		vmInstance.setVmStorages(null);
		vmInstance.setVmNetWorks(null);
		return vmInstance;
	}

	@RequestMapping("restartVmByselected")
	@ResponseBody
	public VmInstance restartVmByselected(HttpServletRequest request) {
		VmInstance vmInstance = null;
		String id = request.getParameter("vid");
		vmInstance = vmService.restartVm(id);
		vmInstance.setVmStorages(null);
		vmInstance.setVmNetWorks(null);
		return vmInstance;
	}

	@RequestMapping("closeVmByselected")
	@ResponseBody
	public VmInstance closeVmByselected(HttpServletRequest request) {
		VmInstance vmInstance = null;
		String id = request.getParameter("vid");
		vmInstance = vmService.closeVm(id);
		vmInstance.setVmStorages(null);
		vmInstance.setVmNetWorks(null);
		return vmInstance;
	}

	@RequestMapping("addVm")
	@ResponseBody
	public Map<String, String> addVm(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		String vmName = request.getParameter("nameRule");
		// String vmNumber = request.getParameter("vmNumber");
		String clusterId = request.getParameter("clusterId");
		String imageId = request.getParameter("imageId");
		String cpuNumber = request.getParameter("cpuNumber");
		String memorySize = request.getParameter("memoryNumber");
		String storageId = request.getParameter("storageId");
		String userDisk = request.getParameter("userDisk");
		int backType = vmService.addVm(vmName, clusterId, imageId, cpuNumber, memorySize, storageId, userDisk);
		if (backType == 1) {
			map.put("data", "success");
		} else if (backType == 2) {
			map.put("data", "cpu-failure");
		} else if (backType == 3) {
			map.put("data", "storage-failure");
		}
		return map;
	}

	@RequestMapping("showEditVm")
	public String showEditVm(Model model, HttpServletRequest request, HttpSession session) {
		String vid = request.getParameter("vid");
		VmInstance vmInstance = vmService.getVmInstanceById(vid);
		if (!StringUtils.isEmpty(vmInstance)) {
			Image image = imageService.getImageById(vmInstance.getImageId());
			session.setAttribute("session_vmInstance", vmInstance);
			model.addAttribute("vmInstance", vmInstance);
			model.addAttribute("imageName", image.getName());
		}
		return "jsp/vm/edit_vm";
	}

	@RequestMapping("editVmBySelected")
	@ResponseBody
	public Map<String, String> editVmBySelected(HttpServletRequest request, HttpSession session) {
		Map<String, String> map = new HashMap<String, String>();
		String cpu = request.getParameter("cpuNumber");
		String memory = request.getParameter("memoryNumber");
		VmInstance vmInstance = (VmInstance) session.getAttribute("session_vmInstance");
		int result = vmService.modifyVm(cpu, memory, vmInstance);
		if (result == -1) {
			map.put("data", "cpu_failure");
		} else {
			map.put("data", "success");
		}
		return map;
	}

	@RequestMapping("deleteVms")
	@ResponseBody
	public List<VmInstance> deleteVms(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		List<VmInstance> vmInstances = vmService.deleteVms(ids);
		for (VmInstance vmInstance : vmInstances) {
			vmInstance.setVmNetWorks(null);
			vmInstance.setVmStorages(null);
		}
		return vmInstances;
	}

	@RequestMapping("refreshVmData")
	@ResponseBody
	public List<VmNeedInfo> refreshVmData(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		List<VmNeedInfo> VmNeedInfos = null;
		if(!StringUtils.isEmpty(ids)){
			List<VmInstance> vmInstances = vmService.getVmInstancesByIds(ids);
			for (VmInstance vmInstance : vmInstances) {
				vmInstance.setVmNetWorks(null);
				vmInstance.setVmStorages(null);
			}
			if(!StringUtils.isEmpty(vmInstances)){
				try {
					VmNeedInfos = initVmNeedInfo(vmInstances, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return VmNeedInfos;
	}
	
	private List<VmNeedInfo> initVmNeedInfo(List<VmInstance> vmInstances, boolean isAll) throws Exception {
		List<VmNeedInfo> vmNeedInfos = new ArrayList<VmNeedInfo>();
		boolean isShowMemoryRate = false;
		boolean isShowCpuRate = true;
		VmNeedInfo vmNeedInfo = null;
		Cluster cluster = null;
		HostAPI hostAPI = new HostAPI();
		HostInstance hostInstance = null;
		for (VmInstance vmInstance : vmInstances) {
			if(vmInstance.getStatus() != CommonConstants.VM_DELETED_STATUS || isAll){
				cluster = clusterService.getClusterById(vmInstance.getClusterId());
				hostInstance = hostService.getHostInstanceById(vmInstance.getHostId());
				String ipAddress = hostAPI.getHostIpAddress(hostInstance.getUuid());
				FetchDynamicData data = new FetchDynamicData(ipAddress);
				vmInstance.setMemory(vmInstance.getMemory() / 1024);
				int userDiskSize = vmService.getUserDiskSize(vmInstance.getId());
				if (vmInstance.getPowerStatus().equals("Running")) {
					double memoryRate = 0.0d;
					Map<String, Object> map = data.getVmNeedInfoByParseXml(vmInstance.getUuid());
					double cpuRate = (double) map.get("cpu_avg") * 100;
					isShowMemoryRate = (boolean) map.get("memory_flag");
					double memoryUsed = (double) map.get("memory_used");
					double memoryTotal = (double) map.get("memory_total");
					if (Double.isNaN(memoryTotal) || Double.isNaN(memoryUsed)) {
						isShowMemoryRate = false;
						memoryTotal = 0.0;
						memoryUsed = 0.0;
					} else {
						memoryRate = NumberUtils.computerUsedRate(memoryTotal, memoryTotal - memoryUsed, 1);
					}
					if (Double.isNaN(cpuRate)) {
						isShowCpuRate = false;
						cpuRate = 0.0;
					}
					vmNeedInfo = new VmNeedInfo(vmInstance, NumberUtils.setdoubleScal(cpuRate), memoryRate, cluster.getName(), hostInstance.getName(),
							StringUtils.double2String(memoryTotal), StringUtils.double2StringKeepScal(memoryUsed),
							isShowMemoryRate ? 1 : 0, isShowCpuRate ? 1 : 0, userDiskSize);
					
				} else {
					vmNeedInfo = new VmNeedInfo(vmInstance, 0.0d, 0.0d, cluster.getName(), hostInstance.getName(), "", "",
							0, 0, userDiskSize);
				}
				vmNeedInfos.add(vmNeedInfo);
			}
		}
		return vmNeedInfos;
	}

}
