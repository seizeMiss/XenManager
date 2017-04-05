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
@Table(name="vm_network")
public class VmNetwork {
	private String id;
	private String uuid;
	private String vmId;
	private String networkId;
	private String networkName;
	private String macAddress;
//	private VmInstance vmInstance;
	
	public VmNetwork(String id, String uuid, String vmId, String networkId, String networkName, String macAddress) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.vmId = vmId;
		this.networkId = networkId;
		this.networkName = networkName;
		this.macAddress = macAddress;
	}
	public VmNetwork() {
		super();
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
	@Column(name="vm_id",length=36)
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	@Column(name="network_id",length=40)
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	@Column(name="network_name",length=20)
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	@Column(name="mac_addr",length=30)
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
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
		return "VmNetwork [id=" + id + ", uuid=" + uuid + ", vmId=" + vmId + ", networkId=" + networkId
				+ ", networkName=" + networkName + ", macAddress=" + macAddress + "]";
	}
	
	

}
