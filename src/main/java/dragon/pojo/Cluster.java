package main.java.dragon.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cluster")
public class Cluster {
	private String id;
	private String name;
	private String clusterIP;
	private int status;
	private double cpuAverage;
	private int memoryTotal;
	private int memoryUsed;
	private int storageTotal;
	private int storageUsed;
	private int storageCount;
	private int hostCount;
	private int vmCount;
	private String description;
	
	public Cluster(){
		
	}
	
	public Cluster(String id, String name, String clusterIP, int status, double cpuAverage, int memoryTotal,
			int memoryUsed, int storageTotal, int storageUsed, int storageCount, int hostCount, int vm_count, String description) {
		super();
		this.id = id;
		this.name = name;
		this.clusterIP = clusterIP;
		this.status = status;
		this.cpuAverage = cpuAverage;
		this.memoryTotal = memoryTotal;
		this.memoryUsed = memoryUsed;
		this.storageTotal = storageTotal;
		this.storageUsed = storageUsed;
		this.storageCount = storageCount;
		this.hostCount = hostCount;
		this.vmCount = vm_count;
		this.description = description;
	}
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="name",length=30)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="cluster_ip",length=20)
	public String getClusterIP() {
		return clusterIP;
	}
	public void setClusterIP(String clusterIP) {
		this.clusterIP = clusterIP;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="cpu_avg",scale=1,precision=3)
	public double getCpuAverage() {
		return cpuAverage;
	}
	public void setCpuAverage(double cpuAverage) {
		this.cpuAverage = cpuAverage;
	}
	@Column(name="memory_total")
	public int getMemoryTotal() {
		return memoryTotal;
	}
	public void setMemoryTotal(int memoryTotal) {
		this.memoryTotal = memoryTotal;
	}
	@Column(name="memory_used")
	public int getMemoryUsed() {
		return memoryUsed;
	}
	public void setMemoryUsed(int memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	@Column(name="storage_total")
	public int getStorageTotal() {
		return storageTotal;
	}
	public void setStorageTotal(int storageTotal) {
		this.storageTotal = storageTotal;
	}
	@Column(name="storage_used")
	public int getStorageUsed() {
		return storageUsed;
	}
	public void setStorageUsed(int storageUsed) {
		this.storageUsed = storageUsed;
	}
	@Column(name="storage_count")
	public int getStorageCount() {
		return storageCount;
	}
	public void setStorageCount(int storageCount) {
		this.storageCount = storageCount;
	}
	@Column(name="host_count")
	public int getHostCount() {
		return hostCount;
	}
	public void setHostCount(int hostCount) {
		this.hostCount = hostCount;
	}
	@Column(name="vm_count")
	public int getVmCount() {
		return vmCount;
	}
	public void setVmCount(int vmCount) {
		this.vmCount = vmCount;
	}
	
	@Column(name="description",length=256)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Cluster cluster = (Cluster)obj;
		if(this.id.equals(cluster.getId()) 
				&& this.cpuAverage == cluster.getCpuAverage() 
				&& this.memoryTotal == cluster.getMemoryTotal() 
				&& this.memoryUsed == cluster.getMemoryUsed() 
				&& this.storageTotal == cluster.getStorageTotal() 
				&& this.storageUsed == cluster.getStorageUsed()){
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Cluster [id=" + id + ", name=" + name + ", clusterIP=" + clusterIP + ", status=" + status
				+ ", cpuAverage=" + cpuAverage + ", memoryTotal=" + memoryTotal + ", memoryUsed=" + memoryUsed
				+ ", storageTotal=" + storageTotal + ", storageUsed=" + storageUsed + ", storageCount=" + storageCount
				+ ", hostCount=" + hostCount + ", description=" + description + "]";
	}
	
	

}
