package main.java.dragon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VMController {

	@RequestMapping("showVM")
	public String showVM(){
		
		return "jsp/vm/virtual_machine";
	}
	
	@RequestMapping("showClusterAndHost")
	public String showClusterAndHost(){
		
		return "jsp/colony_hostcomputer";
	}
}
