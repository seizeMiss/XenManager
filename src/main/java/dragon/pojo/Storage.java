package main.java.dragon.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="storage")
public class Storage {
	private String id;
	private String uuid;
	private String clusterId;
	private String name;
	private int storageTotal;
	private int storageUsed;
	private String storageType;
	private int status;
	private String ipAddress;
	
	public Storage() {
		super();
	}
	
	public Storage(String id, String uuid, String clusterId, String name, int storageTotal, int storageUsed,
			String storageType, int status, String ipAddress) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.clusterId = clusterId;
		this.name = name;
		this.storageTotal = storageTotal;
		this.storageUsed = storageUsed;
		this.storageType = storageType;
		this.status = status;
		this.ipAddress = ipAddress;
	}

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	@Column(name="name",length=20)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	@Column(name="storage_type",length=20)
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="ip_address",length=20)
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Override
	public String toString() {
		return "Storage [id=" + id + ", uuid=" + uuid + ", clusterId=" + clusterId + ", name=" + name
				+ ", storageTotal=" + storageTotal + ", storageUsed=" + storageUsed + ", storageType=" + storageType
				+ ", status=" + status + ", ipAddress=" + ipAddress + "]";
	}
	
	

}
