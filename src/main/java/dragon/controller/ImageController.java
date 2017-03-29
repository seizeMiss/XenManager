package main.java.dragon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.service.ClusterService;
import main.java.dragon.service.ImageService;
import main.java.dragon.service.VMService;
import main.java.dragon.utils.StringUtils;

@Controller
public class ImageController {
	@Autowired
	private ClusterService clusterService;
	@Autowired
	private VMService vmService;
	@Autowired
	private ImageService imageService;
	
	@RequestMapping("showImage")
	public String showImage(){
		
		return "/jsp/image/image";
	}
	@RequestMapping("showAddImage")
	public String showAddImage(Model model){
		List<Cluster> clusters = clusterService.getAllCluster();
		List<VmInstance> vmInstances = vmService.getAllVm();
		if(!StringUtils.isEmpty(clusters,vmInstances)){
			model.addAttribute("clusters", clusters);
			model.addAttribute("vmInstances", vmInstances);
		}
		return "jsp/image/add_image";
	}

}
