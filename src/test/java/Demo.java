package test.java;

import java.util.List;
import java.util.Set;

import javax.transaction.HeuristicCommitException;

import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.apache.xmlrpc.XmlRpcException;
import org.junit.Test;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.PIF;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.Types.BadServerResponse;
import com.xensource.xenapi.Types.XenAPIException;

import main.java.dragon.pojo.Cluster;

import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;


public class Demo extends ConnectionUtil{
	
	public Demo() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void demo(){
		System.out.println("hello world");
	}
	private VIF getDefaultVIF(VM vm) throws Exception{
		VIF defaultVIF = null;
		Set<VIF> vifs = vm.getVIFs(connection);
		for (VIF vif : vifs){
			if(vif != null){
				defaultVIF = vif;
				return defaultVIF;
			}
		}
		return defaultVIF;
	}
	
	private Pool getPoolByVM(VM vm){
		Pool pool = null;
		try {
			VIF vif = getDefaultVIF(vm);
			Network network = vif.getNetwork(connection);
			Set<PIF> pifs = network.getPIFs(connection);
			Host host = null;
			for(PIF pif : pifs){
				host = pif.getHost(connection);
				if(host != null){
					break;
				}
			}
			host.getRecord(connection);
			pool = Pool.getAll(connection).iterator().next();
			System.out.println(pool.getNameLabel(connection));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pool;
	}
	@Test
	public void getPoolInfo() throws Exception{
		Set<Pool> pools = Pool.getAll(connection);
		for(Pool pool : pools){
			Cluster cluster = new Cluster();
			Pool.Record record = pool.getRecord(connection);
			cluster.setName(record.nameLabel);
			cluster.setClusterIP(record.master.getAddress(connection));
			cluster.setDescription(record.nameDescription);
			record.master.getHostname(connection);
			System.out.println(cluster);
		}
	}
	
	@Test
	public void getAllVm() throws Exception{
		Set<VM> vms = VM.getAll(connection);
		System.out.println(vms.size());
		for (VM vm : vms){
			VM.Record record = vm.getRecord(connection);
			//快照  控制主机 模板 
			if(record.isASnapshot || record.isControlDomain 
					|| record.isATemplate || record.isSnapshotFromVmpp){
				
			}else{
				System.out.println(record.powerState.toString());
				
			}
		}
	}
}
