package main.java.dragon.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="account")
public class Account {
	private String id;
	private String userName;
	private String realName;
	private String password;
	private String email;
	private Date createTime;
	private Date updateTime;
	private String description;
	
	public Account(){
		
	}
	
	public Account(String id, String userName, String realName, String password, String email, Date createTime,
			Date updateTime, String description) {
		super();
		this.id = id;
		this.userName = userName;
		this.realName = realName;
		this.password = password;
		this.email = email;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.description = description;
	}

	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="user_name",length=36)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="real_name",length=20)
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	@Column(name="password",length=20)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="email",length=20)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	@Column(name="description",length=256)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", userName=" + userName + ", realName=" + realName + ", password=" + password
				+ ", email=" + email + ", createTime=" + createTime + ", updateTime=" + updateTime + ", description="
				+ description + "]";
	}
	
}
