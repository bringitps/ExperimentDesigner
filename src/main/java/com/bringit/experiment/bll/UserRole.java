package com.bringit.experiment.bll;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="UserRole")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class UserRole {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserRoleId")
	private Integer userRoleId;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="UserId", unique=false, updatable=true)
	private SysUser sysUser;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="RoleId", unique=false, updatable=true)
	private SysRole sysRole;

	@Column(name="IsDefaultRole")
	private boolean isDefaultRole;

	public UserRole() {
		this.userRoleId = null;
		this.sysUser = null;
		this.sysRole = null;
	}

	public UserRole(Integer userRoleId, SysUser user, SysRole role) {
		this.sysRole = role;
		this.userRoleId = userRoleId;
		this.sysUser = user;
	}


	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public SysRole getSysRole() {
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

	public boolean isDefaultRole() {
		return isDefaultRole;
	}

	public void setDefaultRole(boolean isDefaultRole) {
		this.isDefaultRole = isDefaultRole;
	}



	
}
