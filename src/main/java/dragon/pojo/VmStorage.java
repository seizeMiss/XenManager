package main.java.dragon.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="vm_storage")
public class VmStorage {
	
	private String id;
	private String name;
	private String vmId;
	private String storageType;
	private String storageId;
	private String description;
//	private VmInstance vmInstance;
	
	public VmStorage() {
		super();
	}
	public VmStorage(String id, String name, String vmId, String storageType, String storageId, String description) {
		super();
		this.id = id;
		this.name = name;
		this.vmId = vmId;
		this.storageType = storageType;
		this.storageId = storageId;
		this.description = description;
	}
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="name",length=20)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="vm_id",length=36)
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	@Column(name="storage_type",length=20)
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	@Column(name="storage_id",length=36)
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	@Column(name="description",length=256)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
//	@ManyToOne(targetEntity=VmInstance.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
//	public VmInstance getVmInstance() {
//		return vmInstance;
//	}
//	public void setVmInstance(VmInstance vmInstance) {
//		this.vmInstance = vmInstance;
//	}
	@Override
	public String toString() {
		return "VmStorage [id=" + id + ", name=" + name + ", vmId=" + vmId + ", storageType=" + storageType
				+ ", storageId=" + storageId + ", description=" + description + "]";
	}
	
	

}
