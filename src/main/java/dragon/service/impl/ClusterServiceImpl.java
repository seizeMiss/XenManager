package main.java.dragon.service.impl;

import java.util.Set;

import org.junit.Test;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VM;

import main.java.dragon.service.ClusterService;

public class ClusterServiceImpl extends ConnectionUtil implements ClusterService{

	public ClusterServiceImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	private boolean isAvailableVm(VM vm) throws Exception{
		VM.Record record = vm.getRecord(connection);
		if(record.isASnapshot || record.isControlDomain 
					|| record.isATemplate || record.isSnapshotFromVmpp){
			return false;
		}else{
			return true;
		}
	}
	@Test
	public void demo() throws Exception{
		Set<Pool> pools = Pool.getAll(connection);
		for(Pool pool : pools){
			Host host = pool.getMaster(connection);
			System.out.println(host.getResidentVMs(connection).size());
			System.out.println(host.getAddress(connection));
			Set<PBD> pbds = host.getPBDs(connection);
			for(PBD pbd : pbds){
				SR sr = pbd.getSR(connection);
				if(sr.getPhysicalUtilisation(connection) > 0){
					System.out.println(sr.getNameLabel(connection));
					Set<VDI> vdis = sr.getVDIs(connection);
					for(VDI vdi : vdis){
						Set<VBD> vbds = vdi.getVBDs(connection);
						for(VBD vbd : vbds){
							VM vm = vbd.getVM(connection);
							if(isAvailableVm(vm)){
								System.out.println(vbd.getVM(connection).getNameLabel(connection));
							}
						}
					}
				}
			}
		}
	}
	
	private int getVmCount(Pool pool) throws Exception{
		Host host = pool.getMaster(connection);
		host.getResidentVMs(connection).size();
		return 0;
	}
	@Override
	public void addCluster() {
		
		
		
		
	}

	@Override
	public void updateCluster() {
		
	}

}
