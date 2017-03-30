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

import com.bringit.experiment.util.M5Encryption;

@Entity
@Table(name="SysUser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class SysUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserId")
	private Integer userId;

	@Column(name="UserIsActive")
	private Boolean userIsActive;
	
	@Column(name="UserFullName")
	private String userFullName;

	@Column(name="UserEmail")
	private String userEmail;
	
	@Column(name="UserName",unique=true)
	private String userName;
	
	@Column(name="UserPass")
	private String userPass;
	
	@Column(name="IsActiveDirectoryUser")
	private Boolean isActiveDirectoryUser;

	public SysUser() {
		
		this.userId = null;
		this.userIsActive = null;
		this.userFullName = null;
		this.userEmail = null;
		this.userName = null;
		this.userPass = null;
		this.isActiveDirectoryUser = null;
		
	}
	
	public SysUser(Integer userId, Boolean userIsActive, String userFullName, String userEmail, String userName, String userPass,
			Boolean isActiveDirectoryUser) {

		this.userId = userId;
		this.userIsActive = userIsActive;
		this.userFullName = userFullName;
		this.userEmail = userEmail;
		this.userName = userName;
		this.userPass = userPass;
		this.isActiveDirectoryUser = isActiveDirectoryUser;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getUserIsActive() {
		return userIsActive;
	}

	public void setUserIsActive(Boolean userIsActive) {
		this.userIsActive = userIsActive;
	}

	public Boolean getIsActiveDirectoryUser() {
		return isActiveDirectoryUser;
	}

	public void setIsActiveDirectoryUser(Boolean isActiveDirectoryUser) {
		this.isActiveDirectoryUser = isActiveDirectoryUser;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() 
	{
	   	return M5Encryption.decrypt(userPass);
	}

	public void setUserPass(String userPass) {
	    this.userPass = M5Encryption.encrypt(userPass);
	}

}
