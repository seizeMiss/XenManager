package main.java.dragon.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.dragon.pojo.Cluster;
import main.java.dragon.pojo.Image;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.service.ClusterService;
import main.java.dragon.service.ImageService;
import main.java.dragon.service.VMService;
import main.java.dragon.utils.CommonConstants;
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
	public String showImage(Model model){
		List<Image> images = imageService.getAllImages();
		if(!StringUtils.isEmpty(images)){
			Set<String> imageOsNames = getImageSystemName(images);
			model.addAttribute("images", images);
			model.addAttribute("imageCount", images.size());
			model.addAttribute("imageOsNames", imageOsNames);
		}
		return "/jsp/image/image";
	}
	@RequestMapping("showAddImage")
	public String showAddImage(Model model){
		List<Cluster> clusters = clusterService.getAllCluster();
		List<VmInstance> vmInstances = vmService.getAllVm();
		List<VmInstance> filterVmInstances = new ArrayList<VmInstance>();
		if(!StringUtils.isEmpty(clusters,vmInstances)){
			for(VmInstance vmInstance : vmInstances){
				if(vmInstance.getPowerStatus().equals(CommonConstants.VM_POWER_CLOSED)){
					filterVmInstances.add(vmInstance);
				}
			}
			model.addAttribute("clusters", clusters);
			model.addAttribute("vmInstances", filterVmInstances);
		}
		return "jsp/image/add_image";
	}
	@RequestMapping("addImage")
	@ResponseBody
	public Map<String, String> addImage(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		String imageName = StringUtils.setEncodeString(request.getParameter("imageName"));
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
	private Set<String> getImageSystemName(List<Image> images){
		Set<String> imageSystemNames = new HashSet<String>();
		for(Image image : images){
			imageSystemNames.add(image.getOsType());
		}
		return imageSystemNames;
	}
	
	@RequestMapping("searchImageBycondition")
	public String searchImageBycondition(Model model,HttpServletRequest request){
		String imageName = StringUtils.setEncodeString(request.getParameter("condition-name"));
		String imageStatus = StringUtils.setEncodeString(request.getParameter("selected-state"));
		String imageOsType = StringUtils.setEncodeString(request.getParameter("selected-os"));
		List<Image> imagesbyCondition = imageService.getImagesByCondition(imageName, imageStatus, imageOsType);
		List<Image> allImages = imageService.getAllImages();
		if(!StringUtils.isEmpty(imagesbyCondition)){
			Set<String> imageOsNames = getImageSystemName(allImages);
			model.addAttribute("images", imagesbyCondition);
			model.addAttribute("imageCount", imagesbyCondition.size());
			model.addAttribute("imageOsNames", imageOsNames);
		}
		return "/jsp/image/image";
	}
	
	
	@RequestMapping("deleteImages")
	@ResponseBody
	public Map<String, String> deleteImages(@RequestParam("tid")String ids){
		Map<String, String> map = new HashMap<>();
		if(imageService.deleteImages(ids)){
			map.put("data", "success");
		}
		return map;
	}
	
	@RequestMapping("searchImageByName")
	@ResponseBody
	public List<Image> searchImageByName(@RequestParam("searchContent")String searchContent){
		List<Image> imagesByName = imageService.getImagesByName(searchContent);
		return imagesByName;
	}
	
	@RequestMapping("refreshImageData")
	@ResponseBody
	public List<Image> refreshImageData(@RequestParam("ids")String ids){
		List<Image> images = null;
		if(!StringUtils.isEmpty(ids)){
			images = imageService.getImagesByIds(ids);
		}
		return images;
	}

}
