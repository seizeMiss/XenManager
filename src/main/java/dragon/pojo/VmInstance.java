package main.java.dragon.pojo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="vm_instance")
public class VmInstance {
	
	private String id;
	private String clusterId;
	private String hostId;
	private String imageId;
	private String storageId;
	private String uuid;
	private String name;
	private String vmIp;
	private int status;
	private String powerStatus;
	private Date createTime;
	private Date updateTime;
	private String osType;
	private String osName;
	private int cpu;
	private int memory;
	private int systemDisk;
	private Set<VmStorage> vmStorages;
	private Set<VmNetwork> vmNetWorks;
	
	public VmInstance() {
		// TODO Auto-generated constructor stub
	}
	
	
	public VmInstance(String id, String clusterId, String hostId, String imageId, String storageId, String uuid,
			String name, String vmIp, int status, String powerStatus, Date createTime, Date updateTime, String osType,
			String osName, int cpu, int memory, int systemDisk) {
		super();
		this.id = id;
		this.clusterId = clusterId;
		this.hostId = hostId;
		this.imageId = imageId;
		this.storageId = storageId;
		this.uuid = uuid;
		this.name = name;
		this.vmIp = vmIp;
		this.status = status;
		this.powerStatus = powerStatus;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.osType = osType;
		this.osName = osName;
		this.cpu = cpu;
		this.memory = memory;
		this.systemDisk = systemDisk;
	}


	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="cluster_id",length=50)
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	@Column(name="host_id",length=36)
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	@Column(name="image_id",length=36)
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	@Column(name="storage_id",length=36)
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	@Column(name="uuid",length=36)
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="name",length=20)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Column(name="vm_ip",length=20)
	public String getVmIp() {
		return vmIp;
	}
	public void setVmIp(String vmIp) {
		this.vmIp = vmIp;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="power_status",length=20)
	public String getPowerStatus() {
		return powerStatus;
	}
	public void setPowerStatus(String powerStatus) {
		this.powerStatus = powerStatus;
	}
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="update_time")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name="os_type",length=20)
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	@Column(name="os_name",length=50)
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	@Column(name="cpu")
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	@Column(name="memory")
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	@Column(name="system_disk")
	public int getSystemDisk() {
		return systemDisk;
	}
	public void setSystemDisk(int systemDisk) {
		this.systemDisk = systemDisk;
	}
	@OneToMany(targetEntity=VmNetwork.class,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="vm_id")
	public Set<VmNetwork> getVmNetWorks() {
		return vmNetWorks;
	}
	public void setVmNetWorks(Set<VmNetwork> vmNetWorks) {
		this.vmNetWorks = vmNetWorks;
	}
	@OneToMany(targetEntity=VmStorage.class,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="vm_id")
	public Set<VmStorage> getVmStorages() {
		return vmStorages;
	}
	public void setVmStorages(Set<VmStorage> vmStorages) {
		this.vmStorages = vmStorages;
	}


	@Override
	public String toString() {
		return "VmInstance [id=" + id + ", clusterId=" + clusterId + ", hostId=" + hostId + ", imageId=" + imageId
				+ ", storageId=" + storageId + ", uuid=" + uuid + ", name=" + name + ", vmIp=" + vmIp + ", status="
				+ status + ", powerStatus=" + powerStatus + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", osType=" + osType + ", osName=" + osName + ", cpu=" + cpu + ", memory=" + memory + ", systemDisk="
				+ systemDisk + ", vmStorages=" + vmStorages + ", vmNetWorks=" + vmNetWorks + "]";
	}
	
	

}
