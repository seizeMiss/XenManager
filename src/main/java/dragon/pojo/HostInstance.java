package main.java.dragon.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="host_instance")
public class HostInstance {
	private String id;
	private String name;
	private String uuid;
	private String clusterId;
	private int status;
	private String powerStatus;
	private int cpuUsed;
	private int cpuTotal;
	private double cpuAverage;
	private int memoryTotal;
	private int memoryUsed;
	private int diskTotal;
	private int diskUsed;
	private int vmTotalCount;
	private int vmRunningCount;
	private String description;
	public HostInstance(){
		
	}
	
	public HostInstance(String id, String name, String uuid, String clusterId, int status, String powerStatus, int cpuUsed,
			int cpuTotal, double cpuAverage, int memoryTotal, int memoryUsed, int diskTotal, int diskUsed,
			int vmTotalCount, int vmRunningCount, String description) {
		super();
		this.id = id;
		this.name = name;
		this.uuid = uuid;
		this.clusterId = clusterId;
		this.status = status;
		this.powerStatus = powerStatus;
		this.cpuUsed = cpuUsed;
		this.cpuTotal = cpuTotal;
		this.cpuAverage = cpuAverage;
		this.memoryTotal = memoryTotal;
		this.memoryUsed = memoryUsed;
		this.diskTotal = diskTotal;
		this.diskUsed = diskUsed;
		this.vmTotalCount = vmTotalCount;
		this.vmRunningCount = vmRunningCount;
		this.description = description;
	}
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="name",length=36)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="uuid",length=36)
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="cluster_id",length=36)
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="power_status",length=10)
	public String getPowerStatus() {
		return powerStatus;
	}
	public void setPowerStatus(String powerStatus) {
		this.powerStatus = powerStatus;
	}
	@Column(name="cpu_used")
	public int getCpuUsed() {
		return cpuUsed;
	}
	public void setCpuUsed(int cpuUsed) {
		this.cpuUsed = cpuUsed;
	}
	@Column(name="cpu_total")
	public int getCpuTotal() {
		return cpuTotal;
	}
	public void setCpuTotal(int cpuTotal) {
		this.cpuTotal = cpuTotal;
	}
	@Column(name="cpu_avg")
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
	@Column(name="disk_total")
	public int getDiskTotal() {
		return diskTotal;
	}
	public void setDiskTotal(int diskTotal) {
		this.diskTotal = diskTotal;
	}
	@Column(name="disk_used")
	public int getDiskUsed() {
		return diskUsed;
	}
	public void setDiskUsed(int diskUsed) {
		this.diskUsed = diskUsed;
	}
	@Column(name="vm_total_count")
	public int getVmTotalCount() {
		return vmTotalCount;
	}
	public void setVmTotalCount(int vmTotalCount) {
		this.vmTotalCount = vmTotalCount;
	}
	@Column(name="vm_running_count")
	public int getVmRunningCount() {
		return vmRunningCount;
	}
	public void setVmRunningCount(int vmRunningCount) {
		this.vmRunningCount = vmRunningCount;
	}
	@Column(name="description",length=256)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Host [id=" + id + ", name=" + name + ", uuid=" + uuid + ", clusterId=" + clusterId + ", status="
				+ status + ", powerStatus=" + powerStatus + ", cpuUsed=" + cpuUsed + ", cpuTotal=" + cpuTotal
				+ ", cpuAverage=" + cpuAverage + ", memoryTotal=" + memoryTotal + ", memoryUsed=" + memoryUsed
				+ ", diskTotal=" + diskTotal + ", diskUsed=" + diskUsed + ", vmTotalCount=" + vmTotalCount
				+ ", vmRunningCount=" + vmRunningCount + ", description=" + description + "]";
	}
	
}
