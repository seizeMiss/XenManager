package main.java.dragon.pojo;

public class VmNeedInfo {
	private VmInstance vmInstance;
	private double cpuRate;
	private double memoryRate;
	private String clusterName;
	private String hostName;
	private String memoryTotal;
	private String memoryUsed;
	private int showMemoryRate;
	private int showCpuRate;
	private int userDiskSize;
	

	public VmNeedInfo() {
		super();
	}
	
	public VmNeedInfo(VmInstance vmInstance, double cpuRate, double memoryRate, String clusterName, String hostName,
			String memoryTotal, String memoryUsed, int isXenTool, int showCpuRate,int userDiskSize) {
		super();
		this.vmInstance = vmInstance;
		this.cpuRate = cpuRate;
		this.memoryRate = memoryRate;
		this.clusterName = clusterName;
		this.hostName = hostName;
		this.memoryTotal = memoryTotal;
		this.memoryUsed = memoryUsed;
		this.showMemoryRate = isXenTool;
		this.showCpuRate = showCpuRate;
		this.userDiskSize = userDiskSize;
	}
	
	

	public int getUserDiskSize() {
		return userDiskSize;
	}

	public void setUserDiskSize(int userDiskSize) {
		this.userDiskSize = userDiskSize;
	}

	public int getShowMemoryRate() {
		return showMemoryRate;
	}

	public void setShowMemoryRate(int showMemoryRate) {
		this.showMemoryRate = showMemoryRate;
	}

	public int getShowCpuRate() {
		return showCpuRate;
	}

	public void setShowCpuRate(int showCpuRate) {
		this.showCpuRate = showCpuRate;
	}

	public int isXenTool() {
		return showMemoryRate;
	}

	public void setXenTool(int isXenTool) {
		this.showMemoryRate = isXenTool;
	}

	public String getMemoryTotal() {
		return memoryTotal;
	}

	public void setMemoryTotal(String memoryTotal) {
		this.memoryTotal = memoryTotal;
	}

	public String getMemoryUsed() {
		return memoryUsed;
	}

	public void setMemoryUsed(String memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public VmInstance getVmInstance() {
		return vmInstance;
	}
	public void setVmInstance(VmInstance vmInstance) {
		this.vmInstance = vmInstance;
	}
	public double getCpuRate() {
		return cpuRate;
	}
	public void setCpuRate(double cpuRate) {
		this.cpuRate = cpuRate;
	}
	public double getMemoryRate() {
		return memoryRate;
	}
	public void setMemoryRate(double memoryRate) {
		this.memoryRate = memoryRate;
	}

	@Override
	public String toString() {
		return "VmNeedInfo [vmInstance=" + vmInstance + ", cpuRate=" + cpuRate + ", memoryRate=" + memoryRate
				+ ", clusterName=" + clusterName + ", hostName=" + hostName + ", memoryTotal=" + memoryTotal
				+ ", memoryUsed=" + memoryUsed + ", isXenTool=" + showMemoryRate + "]";
	}


	

}
