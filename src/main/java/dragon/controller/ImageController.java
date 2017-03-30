package main.java.dragon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.Image;
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
	@RequestMapping("addImage")
	@ResponseBody
	public Map<String, String> addImage(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		String imageName = request.getParameter("imageName");
		String clusterId = request.getParameter("clusterId");
		String imageDecs = request.getParameter("imageDecs");
		String vmUuid = request.getParameter("imageVmUuid");
		Image image = new Image();
		image.setName(imageName);
		image.setClusterId(clusterId);
		image.setDescription(imageDecs);
		image.setUuid(vmUuid);
		if(imageService.addImage(image)){
			map.put("data", "success");
		}
		return map;
	}
	

}
