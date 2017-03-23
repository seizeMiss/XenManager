package test.java;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.xensource.xenapi.*;

/*
 * XenServer API简单封装，实现Compute接口。该接口会与Image，Volume接口存在调用关系。
 * 这里面目前只是进行比较简单的API调用的验证和分析。部分的调用方式存在效率较低的问题，比如有些数据重复获取了。
 * 在设计时需要对这部分的数据进行关联存储。
 */
public class ComputerAPI extends ConnectionUtil {
	public ComputerAPI() throws Exception {

	}

	public Host getHostByName(String name) throws Exception {
		return Host.getByNameLabel(connection, name).iterator().next();
	}

	/*
	 * 获取集群所有主机的信息 其中 HostInfo 是自己定义类，封装上层所关注的字段 Host,Host.Record
	 * 都是思杰的定义的类，Host能够对主机进行操作，Record是主机的一些属性值。
	 */
	/*public Set<HostInfo> getAllHostInfo() throws Exception {
		Set<HostInfo> hostInfoSet = new HashSet<HostInfo>();
		// 通过AllRecords 可以获取到所有主机及主机属性的对于关系，效率比较高。
		Map<Host, Host.Record> hsRecords = Host.getAllRecords(connection);

		for (Host.Record record : hsRecords.values()) {
			HostInfo info = new HostInfo();
			info.address = record.address;
			info.name = record.nameLabel;
			info.uuid = record.uuid;
			info.cpuSpeed = record.cpuInfo.get("speed");
			info.cpuCount = record.cpuInfo.get("cpu_count");

			// 内存的使用信息，没有在主机属性中存在，因此这里需要通过meterics接口去获取。
			Map<String, Long> meter = getHostMeterics(record.metrics);
			info.memory = meter.get("memory_total");
			info.memoryUsed = info.memory - meter.get("memory_free");

			// 存储信息，也没有在主机属性中，同样需要通过meterics接口获取。
			Map<String, Long> localDiskInfo = getLocalStorageSize(Host
					.getByUuid(connection, record.uuid));

			info.disk = localDiskInfo.get("disk_total");
			info.diskUsed = localDiskInfo.get("disk_used");

			hostInfoSet.add(info);
		}
		return hostInfoSet;
	}*/

	/*
	 * 获取主机内存使用信息（单位字节）
	 */
	public Map<String, Long> getHostMeterics(HostMetrics meterics)
			throws Exception {
		Map<String, Long> meter = new HashMap<String, Long>();

		HostMetrics.Record record = meterics.getRecord(connection);
		meter.put("memory_total", record.memoryTotal);
		meter.put("memory_free", record.memoryFree);

		return meter;
	}

	/*
	 * 获取本地存储使用信息（单位字节） 在思杰，存储分为PBD，VBD，SR，VDI
	 * PBD为物理存储块设备，VBD为虚拟机存储块设备。SR为存储卷，VDI为虚拟机的磁盘。 虚拟机通过 VBD 实现与VDI的关联。主机通过PBD
	 * 实现与 SR的关联。VDI存储在SR上。 因此如果我们要查询主机所对应的存储的信息，需要通过PBD找到SR。
	 */
	public Map<String, Long> getLocalStorageSize(Host host) throws Exception {

		Set<PBD> pbds = new HashSet<PBD>();
		long diskTotal = 0;
		long diskUsed = 0;
		Map<String, Long> diskSize = new HashMap<String, Long>();

		// 这里是获取Host所关联的所有pbd
		pbds = host.getPBDs(connection);

		for (PBD pbd : pbds) {
			SR sr = pbd.getSR(connection);
			SR.Record record = sr.getRecord(connection);
			// 由于我们这里只关心本地存储的使用情况，因此这里跳过对存储卷的管理。
			if (record.shared) {
				continue;
			}
			diskTotal += record.physicalSize;
			diskUsed += record.physicalUtilisation;
		}

		diskSize.put("disk_total", diskTotal);
		diskSize.put("disk_used", diskUsed);
		return diskSize;
	}

	/*
	 * 获取集群信息 XenServer的一个Pool就是一个集群，集群与集群之间是相互隔离的，不能进行统一的管理。
	 * 通过连接任意一台Host都能获取到集群的属性，但不能对集群及集群下的主机等进行管理。
	 * 只能通过Master主机对集群进行管理，因此集群的IP也就是Master的IP。
	 */
	/*public Set<PoolInfo> getAllPoolInfo() throws Exception {
		Set<PoolInfo> poolInfoSet = new HashSet<PoolInfo>();
		Map<Pool, Pool.Record> poolRecords = Pool.getAllRecords(connection);

		for (Pool.Record record : poolRecords.values()) {

			PoolInfo info = new PoolInfo();
			info.uuid = record.uuid;
			info.name = record.nameLabel;
			info.address = record.master.getAddress(connection);
			System.out.println("poolName:"+info.name);
			poolInfoSet.add(info);
		}

		return poolInfoSet;
	}*/
	
	/*
	 * 获取集群信息 XenServer的一个Pool就是一个集群，集群与集群之间是相互隔离的，不能进行统一的管理。
	 * 通过连接任意一台Host都能获取到集群的属性，但不能对集群及集群下的主机等进行管理。
	 * 只能通过Master主机对集群进行管理，因此集群的IP也就是Master的IP。
	 */
	public void join() throws Exception {
		
		Pool.join(connection, "192.168.98.115", "root", "centerm");

	}

	/*
	 * 获取虚拟机网络信息 虚机的网络是通过VIF进行管理，通过VIF可以查询到MAC地址，IP地址，以及关联到的网络（后续提供网络服务后会使用到）。
	 * 虚机网络的NetworkName 需要手动在XenServer上配置，否则会导致名称获取不正确。
	 * 虚机在没有安装XenTools情况下也无法获取到网卡IP地址。
	 */
	/*public Set<VmInfo.NetInfo> getVMNetInfo(VM vm, Set<VIF> vifs)
			throws Exception {

		Set<VmInfo.NetInfo> netInfoSet = new HashSet<VmInfo.NetInfo>();
		Map<String, String> networks = new HashMap<String, String>();
		try {
			VMGuestMetrics guestMetrics = vm.getGuestMetrics(connection);
			networks = guestMetrics.getNetworks(connection);
		} catch (Exception e) {
		}

		for (VIF vif : vifs) {

			VIF.Record record = vif.getRecord(connection);
			VmInfo.NetInfo info = new VmInfo.NetInfo();
			info.mac = record.MAC;
			info.networkId = record.network.getUuid(connection);
			String ipKey = record.device + "/ip";
			if (networks.size() > 0) {
				info.ip = networks.get(ipKey);
			}
			netInfoSet.add(info);
		}

		return netInfoSet;
	}*/

	/*
	 * 直接查询虚机的磁盘列表，会发现部分虚机对系统盘和数据盘的标识存在错误的情况。
	 * 最好还是通过RAL实现层，在数据库中，保存虚机的磁盘信息。存储中存在的非关联的磁盘，RAL层统一进行删除。 本例子是通过bootable 和
	 * vidType判断是否用户磁盘。小概率情况下会识别错误。
	 */
	public Set<String> getVmVolume(VM vm) throws Exception {

		Set<String> volumeSet = new HashSet<String>();
		Set<VBD> vbdSet = new HashSet<VBD>();

		// 获取虚机的VBD。
		vbdSet = vm.getVBDs(connection);

		for (VBD vbd : vbdSet) {
			VBD.Record vbdRecord = vbd.getRecord(connection);

			// 过滤bootable属性及非DISK设备
			if (vbdRecord.type != Types.VbdType.DISK || vbdRecord.bootable) {
				continue;
			}

			// 根据VBD,查询到VDI，并查询VDI属性。
			try {
				VDI.Record record = vbd.getVDI(connection)
						.getRecord(connection);
				if (record.type != Types.VdiType.USER) {
					continue;
				}
				volumeSet.add(record.nameLabel);
			} catch (Exception e) {
			}
			;
		}

		return volumeSet;
	}

	/*
	 * 获取虚机信息，如果非用户虚机则不获取。
	 */
	/*public VmInfo getVmInfo(VM vm) throws Exception {

		VmInfo info = new VmInfo();
		VM.Record record = vm.getRecord(connection);

		// 过滤掉非用户虚机
		if (record.isASnapshot || record.isATemplate || record.isControlDomain
				|| record.isSnapshotFromVmpp) {
			return null;
		}

		info.uuid = record.uuid;
		info.name = record.nameLabel;
		info.cpu = record.VCPUsMax.toString();
		try {
			info.hostUuid = vm.getAffinity(connection).getUuid(connection)
					.toString();
		} catch (Exception e) {
		}
		info.ram = record.memoryStaticMax.toString();
		info.imageUuid = "内部标识".toString();
		info.powerState = record.powerState.toString();
		info.net = getVMNetInfo(vm, record.VIFs);
		info.os = record.otherConfig.get("base_template_name");
		info.volumes = getVmVolume(vm);
		info.persistent = getVmPersist(vm);

		return info;
	}*/

	/*
	 * 获取所有虚拟机信息，XenServer上，vm包括了快照，镜像。 这里我们只关心和我们有关的虚机。对于镜像，需要自己建立映射关系。
	 */
	/*public Set<VmInfo> getAllVmInfo() throws Exception {
		Set<VM> vms = VM.getAll(connection);
		Set<VmInfo> vmInfoSet = new HashSet<VmInfo>();

		for (VM vm : vms) {

			VmInfo info = getVmInfo(vm);
			if (null != info) {
				vmInfoSet.add(info);
			}
		}
		return vmInfoSet;
	}*/

	/*
	 * 获取虚机的系统盘，这种方式对于正常的VM都能获取到。但存在小概率的特殊虚机，无法获取到系统盘。
	 * 对于系统盘，后续有镜像创建，并在RAL层进行统一的维护。同时在VDI的文件命名上，或属性上，应当增加标签。
	 */
	public VDI getVmSystemDisk(VM vm) throws Exception {

		Set<VBD> vbdSet = vm.getVBDs(connection);
		for (VBD vbd : vbdSet) {
			VBD.Record vbdRecord = vbd.getRecord(connection);
			// 如果是DISK,且bootable为 ture 则为启动盘。
			if (vbdRecord.type != Types.VbdType.DISK) {
				continue;
			}

			if (vbdRecord.bootable) {
				return vbd.getVDI(connection);
			}
			// 根据VBD,查询到VDI，并查询VDI属性，如果vid属性为SYSTEM则为系统盘。
			VDI vdi = vbd.getVDI(connection);
			VDI.Record record = vdi.getRecord(connection);
			if (record.type == Types.VdiType.SYSTEM) {
				return vdi;
			}
		}
		return null;
	}

	public VM getVMbyName(String name) throws Exception {
		return VM.getByNameLabel(connection, name).iterator().next();
	}

	/*
	 * 查询虚机是否为持久化
	 */
	public boolean getVmPersist(VM vm) throws Exception {
		VDI vdi = getVmSystemDisk(vm);
		if (null == vdi) {
			return true;
		}
		return vdi.getOnBoot(connection) == Types.OnBoot.RESET ? false : true;
	}

	/*
	 * 设置虚机为非持久化,也就是将虚机的系统盘的 VDI onboot 属性设置为RESET。 开机时无法设置
	 */
	public void setVMPersist(VM vm, boolean persist) throws Exception {

		VDI vdi = getVmSystemDisk(vm);
		vdi.setOnBoot(connection, persist ? Types.OnBoot.PERSIST
				: Types.OnBoot.RESET);
	}

	/*
	 * 关闭虚机
	 */
	public boolean stopVmByName(String name) throws Exception {

		VM vm = getVMbyName(name);

		return stopVMByName(vm, false);
	}

	/*
	 * 强制关闭虚机
	 */
	public boolean hardStopVmByName(String name) throws Exception {

		VM vm = getVMbyName(name);

		return stopVMByName(vm, true);
	}

	/*
	 * 关闭虚机
	 */
	private boolean stopVMByName(VM vm, boolean force) throws Exception {

		if (force) {
			vm.hardShutdown(connection);
		} else {
			vm.shutdown(connection);
		}
		return true;
	}

	/*
	 * 启动虚机
	 */
	public Boolean startVMByName(String name) throws Exception {

		VM vm = getVMbyName(name);
		vm.start(connection, false, false);
		return true;
	}

	// 删除虚拟机
	public void deleteVm(VM vm) throws Exception {

		vm.destroy(connection);

	}

	// 根据传入的OS获取系统模板，用于创建虚拟机，现不再通过该方式进行虚机的创建
	public VM getSystemTemplate(String os) throws Exception {

		Map<VM, VM.Record> allVMs = VM.getAllRecords(connection);

		for (Map.Entry<VM, VM.Record> e : allVMs.entrySet()) {
			if (e.getValue().isATemplate == true
					&& e.getValue().otherConfig.containsKey("default_template")
					&& e.getValue().nameLabel.equals(os)) {

				System.out.println("template:" + e.getValue().nameLabel);
				return e.getKey();
			}
		}

		return null;
	}

	// 创建CDROM并挂载到VM上。
	private VBD makeCDDrive(VM vm) throws Exception {
		VBD.Record vbdrecord = new VBD.Record();

		vbdrecord.VM = vm;
		vbdrecord.VDI = null;
		vbdrecord.userdevice = "3";
		vbdrecord.mode = Types.VbdMode.RO;
		vbdrecord.type = Types.VbdType.CD;
		vbdrecord.empty = true;

		return VBD.create(connection, vbdrecord);
	}
	
	private void createSystemDisk(Connection connection, VDI srcDisk, String vmUUid, String vmName) {
		VDI sysDisk = null;

		try {
			sysDisk = cloneNewVDI(connection, srcDisk, "system_disk_" + vmName);
			attachDisk(VM.getByUuid(connection, vmUUid), sysDisk, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static VDI cloneNewVDI(Connection connection, VDI vdi, String name) throws Exception {
		VDI dstVdi = null;

		if (!vdi.isNull()) {
			dstVdi = vdi.createClone(connection, new HashMap<String, String>());
			dstVdi.setNameLabel(connection, name);
		}

		return dstVdi;
	}

	// 创建空的磁盘,并挂载到VM上
	private void createDisk(VM vm, SR sr, String name, long size)
			throws Exception {
		VDI.Record vdir = new VDI.Record();
		vdir.type = Types.VdiType.USER;
		//代表将放置 VDI 的位的物理存储
		vdir.SR = sr;
		vdir.nameLabel = name;
		vdir.virtualSize = size * 1024 * 1024 * 1024;
		vdir.readOnly = false;
		//创建空白磁盘映像
		VDI vdi = VDI.create(connection, vdir);
		attachDisk(vm,vdi,false);
	}
	
	private void attachDisk(VM vm , VDI vdi,boolean isSystemDisk) throws Exception{
		VBD.Record vbdRecord = new VBD.Record();
		vbdRecord.VM = vm;
		vbdRecord.VDI = vdi;
		//指定 VDI 是以只读方式还是读写方式连接。
		vbdRecord.mode = Types.VbdMode.RW;
		//指定 VDI 是应当作为常规磁盘还是作为 CD 显示在 VM 中
		vbdRecord.type = Types.VbdType.DISK;
		if(isSystemDisk) {
			vbdRecord.bootable = true;
			vbdRecord.userdevice = "0";
		} else {
			//指定来宾系统中的块设备，通过该设备正在 VM 中运行的应用程序将能读/写 VDI 的位。
			vbdRecord.userdevice = getVbdDeviceAvailablePosition(connection,vm);
		}
		vbdRecord.device = "xvda";
		VBD.create(connection, vbdRecord);
	}
	
	/**
	 * 获取磁盘挂载的position
	 */
	public static String getVbdDeviceAvailablePosition(Connection connection, VM vm) throws Exception {
		Set<VBD> vbds = vm.getVBDs(connection);
		Set<String> devicePositionSet = new HashSet<String>();
		for (VBD vbd : vbds) {
			VBD.Record vbdRecord = vbd.getRecord(connection);
			devicePositionSet.add(vbdRecord.userdevice);
		}

		for (int i = 1; i < 16; i++) {
			if (!devicePositionSet.contains(String.valueOf(i))) {
				return String.valueOf(i);
			}
		}
		throw new Exception("vm can't find valid user_device position");
	}

	// 通过名称查找VDI ,如果重名则返回第一个。
	public VDI getVDIByName(String name) throws Exception {

		return VDI.getByNameLabel(connection, name).iterator().next();
	}

	// 获取可挂载的设备位置
	public String getVbdDevicePosition(VM vm) throws Exception {
		Set<VBD> vbdSet = vm.getVBDs(connection);
		Set<String> devicePositionSet = new HashSet<String>();
		for (VBD vbd : vbdSet) {
			VBD.Record record = vbd.getRecord(connection);
			devicePositionSet.add(record.userdevice);
		}

		for (int i = 1; i < 16; i++) {
			if (!devicePositionSet.contains(String.valueOf(i))) {
				return String.valueOf(i);
			}
		}

		throw new Exception("Can't find vaild device position");

	}

	// 挂载VDI到VM上
	private void attachVDI(VM vm, VDI vdi, boolean systemDisk) throws Exception {

		VBD.Record vbdRecord = new VBD.Record();
		vbdRecord.VM = vm;
		vbdRecord.VDI = vdi;
		vbdRecord.mode = Types.VbdMode.RW;
		vbdRecord.type = Types.VbdType.DISK;
		// 系统盘的，userdevice必须为"0"
		vbdRecord.userdevice = systemDisk ? "0" : getVbdDevicePosition(vm);
		VBD.create(connection, vbdRecord);
	}

	// 挂载VDI到VM上，
	private void attachVDI(VM vm, String vdiName, boolean systemDisk)
			throws Exception {
		VDI vdi = getVDIByName(vdiName);
		attachVDI(vm, vdi, systemDisk);
	}

	// 获取第一个网络
	private Network getDefaultNetwork() throws Exception {

		Set<Network> networks = Network.getAll(connection);

		for (Network i : networks) {
			return i;
		}

		throw new Exception("No networks found!");
	}

	// 创建一个网卡并挂载到VM上。
	private VIF makeVIF(VM newVm, Network network, String device)
			throws Exception {
		VIF.Record newvifrecord = new VIF.Record();
		// These three parameters are used in the command line VIF creation
		newvifrecord.VM = newVm;
		newvifrecord.network = network;
		newvifrecord.device = device;
		newvifrecord.MTU = 1500L;
		newvifrecord.lockingMode = Types.VifLockingMode.NETWORK_DEFAULT;

		return VIF.create(connection, newvifrecord);
	}

	// 根据传入的参数生成虚机的属性。
	private VM.Record createVmRecord(String name, long ram, long cpu) {

		// 该值为Windows平台的配置。
		Map<String, String> platform = new HashMap<String, String>();
		platform.put("viridian", "true");
		platform.put("viridian_time_ref_count", "true");
		platform.put("viridian_reference_tsc", "true");
		platform.put("acpi", "1");
		platform.put("apic", "true");
		platform.put("pae", "true");
		platform.put("nx", "true");
		platform.put("cores-per-socket", "1");
		platform.put("timeoffset", "28806");
		platform.put("device_id", "0002");
		VM.Record vmRecord = new VM.Record();
		vmRecord.memoryStaticMax = ram * 1024 * 1024;
		vmRecord.memoryStaticMin = ram * 1024 * 1024;
		vmRecord.memoryDynamicMax = ram * 1024 * 1024;
		vmRecord.memoryDynamicMin = ram * 1024 * 1024;
		vmRecord.memoryTarget = 0L;
		vmRecord.recommendations = "<restrictions><restriction field=\"memory-static-max\" max=\"137438953472\" /><restriction field=\"vcpus-max\" max=\"16\" /><restriction property=\"number-of-vbds\" max=\"16\" /><restriction property=\"number-of-vifs\" max=\"7\" /></restrictions>";
		vmRecord.VCPUsMax = cpu;
		vmRecord.VCPUsAtStartup = cpu;
		vmRecord.nameLabel = name;
		vmRecord.otherConfig = new HashMap<String, String>();
		vmRecord.platform = platform;
		vmRecord.HVMShadowMultiplier = 1.0d;
		vmRecord.HVMBootPolicy = "BIOS order";
		vmRecord.actionsAfterCrash = Types.OnCrashBehaviour.RESTART;
		vmRecord.actionsAfterReboot = Types.OnNormalExit.RESTART;
		vmRecord.actionsAfterShutdown = Types.OnNormalExit.DESTROY;

		return vmRecord;
	}
	public static void main(String[] args) {
		try {
			new ComputerAPI().getVdi();
//			new ComputerAPI().createVMByImage("cbl-vm", "zyq-test1", 1024, 2);
//			System.out.println("创建成功！");
//			new ComputerAPI().rebootVM();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("创建失败！");
		}
	}
	public void closeVM() throws Exception{
		Set<VM> vms = VM.getAll(connection);
		for(VM vm : vms){
			if(vm.getNameLabel(connection).equals("cbl-vm")){
				vm.shutdown(connection);
				System.out.println("虚拟机关闭成功");
			}
		}
	}
	//强制启动
	public void rebootVM() throws Exception{
		VM vm = getVMbyName("cbl-vm");
		vm.hardReboot(connection);
//		vm.cleanReboot(connection);
		System.out.println("虚拟机重新启动成功");
	}
	public void openVM() throws Exception{
//		Set<VM> vms = VM.getAll(connection);
		VM vm = VM.getByUuid(connection, "ab170550-b3e9-863d-f609-baf0408b7195");
		vm.start(connection, false, false);
		System.out.println("虚拟机启动成功！");
//		for(VM vm : vms){
//			if(vm.getNameLabel(connection).equals("cbl-vm")){
//				vm.start(connection, false, true);
//				System.out.println("虚拟机启动成功！");
//				Set<Host> host = Host.getAll(connection);
//			}
//		}
	}
	@Test
	public void getVdi() throws Exception{
		Set<Pool> pools = Pool.getAll(connection);
		for(Pool pool : pools){
			System.out.println(pool.getNameLabel(connection));
		}
		
		Set<Network> networks = Network.getAll(connection);
		for(Network network : networks){
			if(!network.isNull()){
				System.out.println(network.getNameLabel(connection));
			}
		}
		
		VDI vdi = VDI.getByUuid(connection, "4993dc66-5ac4-4569-9722-2d2f8498c3d8");
		System.out.println(vdi.getNameLabel(connection));
		System.out.println(vdi.getSR(connection).getNameLabel(connection));
//		Set<VDI> vdis = VDI.getByNameLabel(connection, "WindowsAD模板");
//		for(VDI vdi2 : vdis){
//			System.out.println(vdi2.getNameLabel(connection) + " " + vdi2.getUuid(connection));
//		}
//		Set<SR> srs = SR.getByNameLabel(connection, "Volume-1( 3T)");
//		for(SR sr : srs){
//			Set<VDI> vdis = sr.getVDIs(connection);
//			for(VDI vdi2 : vdis){
//				if(vdi2.getNameLabel(connection).equals("Windows 7 (64-bit)-lijing")){
//					System.out.println(vdi2.getUuid(connection));
//				}
//			}
//		}
		VolumeAPI volumeAPI = new VolumeAPI();
		VDI vdi2 = volumeAPI.getVDIByName("IVYClient-W732");
		System.out.println(vdi2.getNameLabel(connection)+ " " + vdi2.getType(connection));
		
	}
	

	// 创建裸虚拟机
	public void createVm(String name, long ram, long cpu) throws Exception {

		VolumeAPI volAPI = new VolumeAPI();
		VM.Record vmRecord = createVmRecord(name, ram, cpu);
		// 根据虚机属性创建虚拟机
		VM newVM = VM.create(connection, vmRecord);
		SR sr = volAPI.getDefaultSR();
		//创建系统盘
		createSystemDisk(connection, VDI.getByUuid(connection, "4993dc66-5ac4-4569-9722-2d2f8498c3d8"), newVM.getUuid(connection), name);
		
		// 创建磁盘并挂载到虚机上。
		createDisk(newVM, sr, "testVM_userDisk_5G", 5);
		// 创建虚拟光驱
		makeCDDrive(newVM);
		// 创建网络并挂载到虚机上
		// 网卡有时候有一一对应关系，所以要指定对应网卡
		// 一般管理对应一个网卡、存储对应一个网卡
		makeVIF(newVM, getDefaultNetwork(), "0");
	}

	// 将现有的VDI文件挂载到虚机上。
	public void createTemplateVM(String name, String vdiName, long ram, long cpu)
			throws Exception {

		// VM vmTemplate = getSystemTemplate(os);
		VM.Record vmRecord = createVmRecord(name, ram, cpu);

		// 根据虚机属性创建虚拟机
		VM newVM = VM.create(connection, vmRecord);

		// 创建磁盘并挂载到虚机上。
		attachVDI(newVM, vdiName, true);
		// 创建虚拟光驱
		makeCDDrive(newVM);

		// 创建网络并挂载到虚机上
		makeVIF(newVM, getDefaultNetwork(), "0");
	}

	// 根据镜像创建出虚机
	public void createVMByImage(String name, String imageName, long ram,
			long cpu) throws Exception {

		VolumeAPI volAPI = new VolumeAPI();
		// VM vmTemplate = getSystemTemplate(os);
		VM.Record vmRecord = createVmRecord(name, ram, cpu);
		// 根据虚机属性创建虚拟机
		VM newVM = VM.create(connection, vmRecord);
		VDI srcVDI = volAPI.getVDIByName(imageName);

		VDI newVdi = volAPI.cloneNewVDI(srcVDI, name + "_COPY_" + imageName);

		attachVDI(newVM, newVdi, true);

		// 创建虚拟光驱
		makeCDDrive(newVM);

		// 创建网络并挂载到虚机上
		makeVIF(newVM, getDefaultNetwork(), "0");
	}

	// 修改虚拟机配置
	public void modfiyVmConfig(VM vm, long memory, long cpu) throws Exception {

		long memorySize = memory * 1024 * 1024;
		vm.setMemoryLimits(connection, memorySize, memorySize, memorySize,
				memorySize);
		VM.Record record = vm.getRecord(connection);

		// 修改CPU配置时，需要对CPU的个数进行判断，并确定配置的先后顺序。
		if (record.VCPUsAtStartup > cpu) {
			vm.setVCPUsAtStartup(connection, cpu);
			vm.setVCPUsMax(connection, cpu);
		} else {
			vm.setVCPUsMax(connection, cpu);
			vm.setVCPUsAtStartup(connection, cpu);
		}
	}

	public void rebuildVm(VM vm, VDI vdi) throws Exception {
		VDI systemDisk = getVmSystemDisk(vm);
		Set<VBD> vbdSet = systemDisk.getVBDs(connection);
		for (VBD vbd : vbdSet) {
			vbd.destroy(connection);
		}
		systemDisk.destroy(connection);
		VolumeAPI volApi = new VolumeAPI();
		VDI newVdi = volApi.cloneNewVDI(vdi, vm.getNameLabel(connection)
				+ "_Copy");
		attachVDI(vm, newVdi, true);
		vbdSet = newVdi.getVBDs(connection);

		for (VBD vbd : vbdSet) {
			vbd.setBootable(connection, true);
		}
	}

	// 通过虚机创建镜像
	public void createImageByVM(VM vm, String name) throws Exception {
		// 首先创建快照，避免虚机运行对镜像产生干扰。
		VM newVm = vm.snapshot(connection, "ral_snapshot");
		// 获取快照虚机的系统盘
		VDI vdi = getVmSystemDisk(newVm);
		// 对vdi仅限拷贝（耗时较高，后续优化阶段评估是否采用快照）
		Task task = vdi.copyAsync(connection, vdi.getSR(connection));
		XenApiUtil.waitForTask(connection, task, 2000);
		VDI newVdi = Types.toVDI(task, connection);
		newVdi.setNameLabel(connection, name);
		// 最后销毁不在使用的快照虚机，及磁盘。
		Set<VBD> vbdSet = newVm.getVBDs(connection);
		for (VBD vbd : vbdSet) {
			VDI vmVdi = vbd.getVDI(connection);
			vmVdi.destroy(connection);
			vbd.destroy(connection);
		}
		newVm.destroy(connection);
	}

	public void rebootHost(Host host) throws Exception {
		host.disable(connection);
		host.reboot(connection);
	}

	public void shutdownHost(Host host) throws Exception {
		host.disable(connection);
		host.shutdown(connection);
	}

	public void startHost(Host host) throws Exception {
		host.powerOn(connection);
	}

}
