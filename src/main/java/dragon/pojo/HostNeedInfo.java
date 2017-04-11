package main.java.dragon.pojo;

public class HostNeedInfo {
	
	private HostInstance hostInstance;
	private String clusterName;
	private String memoryUsedRate;
	
	public HostNeedInfo() {
		super();
	}
	public HostNeedInfo(HostInstance hostInstance, String clusterName, String memoryUsedRate) {
		super();
		this.hostInstance = hostInstance;
		this.clusterName = clusterName;
		this.memoryUsedRate = memoryUsedRate;
	}
	public HostInstance getHostInstance() {
		return hostInstance;
	}
	public void setHostInstance(HostInstance hostInstance) {
		this.hostInstance = hostInstance;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getMemoryUsedRate() {
		return memoryUsedRate;
	}
	public void setMemoryUsedRate(String memoryUsedRate) {
		this.memoryUsedRate = memoryUsedRate;
	}
	@Override
	public String toString() {
		return "HostNeedInfo [hostInstance=" + hostInstance + ", clusterName=" + clusterName + ", memoryUsedRate="
				+ memoryUsedRate + "]";
	}
	
}
