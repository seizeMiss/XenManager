package main.java.dragon.service.impl;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.common.util.StringHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xensource.xenapi.Network;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;

import main.java.dragon.dao.ClusterDao;
import main.java.dragon.dao.VMDao;
import main.java.dragon.pojo.VmInstance;
import main.java.dragon.pojo.VmNetwork;
import main.java.dragon.pojo.VmStorage;
import main.java.dragon.service.VMService;
import main.java.dragon.utils.NumberUtils;
import main.java.dragon.utils.StringUtils;
import main.java.dragon.xenapi.FetchDynamicData;
import main.java.dragon.xenapi.XenApiUtil;

@Service
@Transactional
public class VMServiceImpl extends ConnectionUtil implements VMService {
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private VMDao vmDao;

	public VMServiceImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	private List<VmInstance> getVmInstance() throws Exception {
		
		String clusterId = "22f7d051-d8ea-4646-8692-7de4d189b8c4";
		String hostId = "e120960b-d35b-405b-81c1-49ca8599edc0";
		String imageId = "6e7beda7-9fb9-45c7-ab5a-6e4bb17fbcea";
		String storageId = "44c9f5e8-9d29-43ac-b866-6079b440ce67";
		String uuid = "";
		String name = "";
		String vmIp = "";
		int status = 0;
		String powerStatus = "";
		Date createTime = new Date();
		Date updateTime = new Date();
		String osType = "";
		String osName = "";
		int cpu = 0;
		int memory = 0;
		int systemDisk = 20;
		List<VmInstance> vmInstances = new ArrayList<VmInstance>();
		Set<VM> vms = VM.getAll(connection);
		for (VM vm : vms) {
			if (XenApiUtil.isAvailableVm(vm)) {
				if(!vm.getNameLabel(connection).equals("cbl-vm-1")){
					String id = StringUtils.generateUUID();
					uuid = vm.getUuid(connection);
					name = vm.getNameLabel(connection);
					if(vm.getDomid(connection) > 0){
						status = 1;
					}
					powerStatus = vm.getPowerState(connection).toString();
					cpu = vm.getMetrics(connection).getVCPUsNumber(connection).intValue();
					memory = (int) (vm.getMemoryTarget(connection)/1024/1024);
					VmInstance vmInstance = new VmInstance(id, clusterId, hostId, imageId, 
							storageId, uuid, name, vmIp, status, powerStatus, 
							createTime, updateTime, osType, osName, cpu, memory, systemDisk);
					vmInstance.setVmNetWorks(getVmNetwork(vm, id));
					vmInstance.setVmStorages(getVmStorage(vm, id));
					vmInstances.add(vmInstance);
				}
			}
		}
		return vmInstances;
	}
//	@Test
//	public void printSR() throws Exception{
//		Set<VM> vms = VM.getAll(connection);
//		for (VM vm : vms) {
//			if (XenApiUtil.isAvailableVm(vm)) {
//				System.out.println(vm.getNameLabel(connection));
//				if(!vm.getNameLabel(connection).equals("cbl-vm-1")){
//					getVmStorage(vm,"");
//				}
//			}
//		}
//	}


	private List<VmStorage> getVmStorage(VM vm, String vmId) throws Exception{
		List<VmStorage> vmStorages = new ArrayList<>();
		Set<VBD> vbds = vm.getVBDs(connection);
		for(VBD vbd : vbds){
			VmStorage vmStorage = new VmStorage();
			VDI vdi = vbd.getVDI(connection);
			if(!vdi.isNull() && vdi.getVirtualSize(connection) > 0){
				vmStorage.setId(StringUtils.generateUUID());
				vmStorage.setName(vdi.getNameLabel(connection));
				vmStorage.setStorageType(vdi.getType(connection).toString());
//				vmStorage.setVmId(vmId);
				vmStorage.setDescription(vdi.getNameDescription(connection));
				vmStorage.setStorageId("44c9f5e8-9d29-43ac-b866-6079b440ce67");
				vmStorages.add(vmStorage);
			}
		}
		return vmStorages;
	}

	private List<VmNetwork> getVmNetwork(VM vm, String vmId) throws Exception{
		List<VmNetwork> vmNetworks = new ArrayList<>();
		Set<VIF> vifs = vm.getVIFs(connection);
		for(VIF vif : vifs){
			VmNetwork vmNetwork = new VmNetwork();
			Network network = vif.getNetwork(connection);
			String id = StringUtils.generateUUID();
			vmNetwork.setId(id);
			vmNetwork.setMacAddress(vif.getMAC(connection));
			vmNetwork.setNetworkId(network.getUuid(connection));
			vmNetwork.setNetworkName(network.getNameLabel(connection));
//			vmNetwork.setVmId(vmId);
			vmNetworks.add(vmNetwork);
		}
		return vmNetworks;
	}

	@Override
	public void addVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork) throws Exception{
		// TODO Auto-generated method stub
		List<VmInstance> vmInstances = getVmInstance();
		for(int i = 0; i < vmInstances.size(); i++){
			vmDao.insertVm(vmInstances.get(i));
		}
	}

	@Override
	public void saveVm(VmInstance vmInstance, List<VmStorage> vmStorage, List<VmNetwork> vmNetwork) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<VmInstance> getAllVm() {
		// TODO Auto-generated method stub
		List<VmInstance> vmInstances = vmDao.selectAllVm();
		return vmInstances;
	}

	@Override
	public void deleteVm(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<VmInstance> getVmInstanceByName(String name) {
		// TODO Auto-generated method stub
		return vmDao.selectVmInstanceByName(name);
	}

}
