package main.java.dragon.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="image")
public class Image {
	
	private String id;
	private String uuid;
	private String clusterId;
	private int status;
	private String name;
	private int imageSize;
	private String osName;
	private String osType;
	private Date createTime;
	private String description;
	
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
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="name",length=20)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="image_size")
	public int getImageSize() {
		return imageSize;
	}
	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	@Column(name="os_name",length=60)
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	@Column(name="os_type",length=10)
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
		return "Image [id=" + id + ", uuid=" + uuid + ", clusterId=" + clusterId + ", status=" + status + ", name="
				+ name + ", imageSize=" + imageSize + ", osName=" + osName + ", osType=" + osType + ", createTime="
				+ createTime + ", description=" + description + "]";
	}
	
	

}
