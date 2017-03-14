package com.bringit.experiment.bll;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="SysUser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class SysUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserId")
	private Integer userId;

	@Column(name="UserName",unique=true)
	private String UserName;
	
	@Column(name="UserPass")
	private String UserPass;
	
	@Column(name="IsActiveDirectoryUser")
	private boolean isActiveDirectoryUser;

	public SysUser() {
		this.userId = null;
		this.UserName = null;
		this.UserPass = null;
		this.isActiveDirectoryUser = false;
	}

	public SysUser(Integer userId, String userName, String userPass, boolean isActiveDirectoryUser) {
		this.userId = userId;
		this.UserName = userName;
		this.UserPass = userPass;
		this.isActiveDirectoryUser = isActiveDirectoryUser;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserPass() {
		return UserPass;
	}

	public void setUserPass(String userPass) {
		UserPass = userPass;
	}

	public boolean isActiveDirectoryUser() {
		return isActiveDirectoryUser;
	}

	public void setActiveDirectoryUser(boolean isActiveDirectoryUser) {
		this.isActiveDirectoryUser = isActiveDirectoryUser;
	}
	
}
