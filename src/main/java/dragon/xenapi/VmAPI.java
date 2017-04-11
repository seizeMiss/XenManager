package main.java.dragon.xenapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VMGuestMetrics;

import main.java.dragon.pojo.VmInstance;
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
	
	public Host getHostByVm(VM vm) throws Exception{
		Host host = null;
		Set<VBD> vbds = vm.getVBDs(connection);
		for(VBD vbd : vbds){
			if( vbd!= null && !vbd.isNull()){
				VDI vdi = vbd.getVDI(connection);
				if(vdi != null && !vdi.isNull()){
					SR sr = vdi.getSR(connection);
					Set<PBD> pbds = sr.getPBDs(connection);
					for(PBD pbd : pbds){
						if(pbd != null && !pbd.isNull()){
							host = pbd.getHost(connection);
						}
					}
				}
			}
			
		}
		return host;
	}
	
	public String getSrUuidByVm(VM vm) throws Exception{
		String uuid = "";
		Set<VBD> vbds = vm.getVBDs(connection);
		for(VBD vbd : vbds){
			if( vbd!= null && !vbd.isNull()){
				VDI vdi = vbd.getVDI(connection);
				if(vdi != null && !vdi.isNull()){
					SR sr = vdi.getSR(connection);
					uuid = sr.getUuid(connection);
				}
				
			}
		}
		return uuid;
	}
	
}
