package main.java.dragon.xenapi;

import java.util.HashMap;
import java.util.Map;

import com.xensource.xenapi.VM;
import com.xensource.xenapi.VMGuestMetrics;

import main.java.dragon.service.impl.ConnectionUtil;

public class VmAPI extends ConnectionUtil{

	public VmAPI() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, String> getOsVersion(VM vm) throws Exception{
		Map<String, String> osVersion = null;
		if(!vm.isNull()){
			VMGuestMetrics vmGuestMetrics = vm.getGuestMetrics(connection);
			osVersion = vmGuestMetrics.getOsVersion(connection);
		}
		return osVersion;
	}

}
