package test.java;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.xensource.xenapi.Host;
import com.xensource.xenapi.HostCpu;
import com.xensource.xenapi.HostMetrics;
import com.xensource.xenapi.Network;
import com.xensource.xenapi.PBD;
import com.xensource.xenapi.PIF;
import com.xensource.xenapi.Pool;
import com.xensource.xenapi.SR;
import com.xensource.xenapi.VBD;
import com.xensource.xenapi.VDI;
import com.xensource.xenapi.VIF;
import com.xensource.xenapi.VM;
import com.xensource.xenapi.VMGuestMetrics;
import com.xensource.xenapi.VMMetrics;

import main.java.dragon.pojo.Cluster;


public class Demo extends ConnectionUtil{
	
	public Demo() throws Exception {
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
			
			/*Pool.Record poolRecord = pool.getRecord(connection);
			System.out.println("---------poolRecord---------");
			//
			System.out.println(" guiConfig:" + poolRecord.guiConfig);
			//
			System.out.println(" otherConfig:" + poolRecord.otherConfig);
			// 限制
			System.out.println(" restrictions:" + poolRecord.restrictions);
			//虚拟交换器的地址
			System.out.println(" vswitchController:" + poolRecord.vswitchController);*/
			
			Host host = pool.getMaster(connection);
			
			Host.Record record = host.getRecord(connection);
			System.out.println("---------hostRecord---------");
			System.out.println(record.powerOnConfig);
			//获得主机的ip地址
			System.out.println("address:" + record.address);
			//获得主机的个数
			System.out.println("host count:" + record.residentVMs.size());
			//能力
			System.out.println("capabilities:" + record.capabilities);
			System.out.println("cpuConfiguration:" + record.cpuConfiguration);
			System.out.println("cpuInfo:" + record.cpuInfo);
			System.out.println("edition:" + record.edition);
			
			//主机是否可以使用
			System.out.println("enabled:" + record.enabled);
			//内存开销
			System.out.println("memoryOverhead:" + record.memoryOverhead/1024/1024);
			//otherConfig
			System.out.println("otherConfig:" + record.otherConfig);
			//softwareVersion
			System.out.println("softwareVersion:" + record.softwareVersion);
			
			HostMetrics hostMetrics = host.getMetrics(connection);
			System.out.println("---------hostMetrics---------");
			//空闲内存
			System.out.println(" memoryFree:" + hostMetrics.getMemoryFree(connection)/1024/1024);
			//总内存
			System.out.println(" memoryTotal:" + hostMetrics.getMemoryTotal(connection)/1024/1024);
			//
			System.out.println(" otherConfig:" + hostMetrics.getOtherConfig(connection));
			
			/*Set<HostCpu> hostCpus = host.getHostCPUs(connection);
			for (HostCpu hostCpu : hostCpus){
				HostCpu.Record hostCpuRecord = hostCpu.getRecord(connection);
				System.out.println("---------hostCpuRecord---------");
				// 物理CPU的个数
				System.out.println(" family:" + hostCpuRecord.family);
				// 特征
				System.out.println(" features:" + hostCpuRecord.features);
				// 
				System.out.println(" flags:" + hostCpuRecord.flags);
				//
				System.out.println(" otherConfig:" + hostCpuRecord.otherConfig);
				// 个数
				System.out.println(" number:" + hostCpuRecord.number);
				// 使用率
				System.out.println(" utilisation:" + hostCpuRecord.utilisation);
				// 厂商
				System.out.println(" vendor:" + hostCpuRecord.vendor);
			}*/
			
			
			Set<PBD> pbds = host.getPBDs(connection);
			for(PBD pbd : pbds){
				SR sr = pbd.getSR(connection);
				if(sr.getPhysicalUtilisation(connection) > 0){
					System.out.println(sr.getNameLabel(connection));
					System.out.println(sr.getPhysicalSize(connection)/1024/1024/1024);
					System.out.println(sr.getPhysicalUtilisation(connection)/1024/1024/1024);
					Set<VDI> vdis = sr.getVDIs(connection);
					for(VDI vdi : vdis){
						Set<VBD> vbds = vdi.getVBDs(connection);
						for(VBD vbd : vbds){
							VM vm = vbd.getVM(connection);
							if(isAvailableVm(vm)){
								
//								System.out.println(vbd.getVM(connection).getNameLabel(connection));
								
								/*if(vm.getNameLabel(connection).equals("IVYCloud-Server")){
									VM.Record vmRecord = vm.getRecord(connection);
									System.out.println("------record---------");
									System.out.println("version:" + vmRecord.version);
									System.out.println("VCPUsParams:" + vmRecord.VCPUsParams);
									//获得虚拟机所在的主机
									System.out.println("host:" + vmRecord.residentOn.getNameLabel(connection));
									//获得虚拟机的运行状态
									System.out.println("powerState:" + vmRecord.powerState.toString());
									//特殊平台
									System.out.println("platform:" + vmRecord.platform);
									//其他配置
									System.out.println("otherConfig:" + vmRecord.otherConfig);
									//总内存
									System.out.println("memoryTarget:" + vmRecord.memoryTarget/1024/1024);
									//domain id 判断VM是否可用
									System.out.println(vmRecord.domid);
									
									System.out.println("currentOperations:" + vmRecord.currentOperations);
									System.out.println("allowedOperations:" + vmRecord.allowedOperations);
									
									
									Map<String, String> map = vm.getMetrics(connection).getOtherConfig(connection);
									VMMetrics vmMetrics = vm.getMetrics(connection);
									System.out.println("------vmMetrics---------");
									//实际内存大小 单位byte
									System.out.println(vmMetrics.getMemoryActual(connection)/1024/1024);
									//虚拟机状态
									System.out.println(vmMetrics.getState(connection));
									//VCPU个数
									System.out.println(vmMetrics.getVCPUsNumber(connection));
									//当前虚拟cpu的使用率
									Map<Long, Double> utilisation = vmMetrics.getVCPUsUtilisation(connection);
									System.out.println("utilisation:" + utilisation);
									Map<Long, Long> vcpusCpu = vmMetrics.getVCPUsCPU(connection);
									System.out.println("vcpusCpu:" + vcpusCpu);
									
									Map<Long, Set<String>> cpuFlag = vmMetrics.getVCPUsFlags(connection);
									
									System.out.println("cpuFlag:" + cpuFlag);
									//装置
//									VMAppliance vmAppliance = vm.getAppliance(connection);
//									System.out.println("allowed operation:" + vmAppliance.getAllowedOperations(connection));
									System.out.println("otherConfig:" + map);
									
									
									System.out.println("------vmGuestMetrics---------");
									VMGuestMetrics vmGuestMetrics = vm.getGuestMetrics(connection);
									System.out.println("disk:" + vmGuestMetrics.getDisks(connection));
									System.out.println("memory:" + vmGuestMetrics.getMemory(connection));
									System.out.println("osVersion" + vmGuestMetrics.getOsVersion(connection));
									System.out.println("network:" + vmGuestMetrics.getNetworks(connection));
									
								}*/
								
							}
						}
					}
				}
			}
		}
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
