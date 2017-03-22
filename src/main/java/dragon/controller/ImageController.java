package main.java.dragon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ImageController {
	
	@RequestMapping("showImage")
	public String showImage(){
		
		return "/jsp/image/image";
	}

}
