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
import org.hibernate.annotations.Type;

@Entity
@Table(name="SysRole")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class SysRole {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="RoleId")
	private Integer roleId;

	@Column(name="RoleName",unique=true)
	private String roleName;

	@Column(name="RoleDescription")
	private String roleDescription;
	
	@Column(name="RoleMenuAccess")
	@Type(type="text")
	private String roleMenuAccess;

	public SysRole() {
		this.roleId = null;
		this.roleName = null;
		this.roleDescription = null;
		this.roleMenuAccess = null;
	}

	public SysRole(Integer roleId, String roleName, String roleDesc, String roleMenuAccess) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleDescription = roleDesc;
		this.roleMenuAccess = roleMenuAccess;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDesc) {
		this.roleDescription = roleDesc;
	}

	public String getRoleMenuAccess() {
		return roleMenuAccess;
	}

	public void setRoleMenuAccess(String roleMenuAccess) {
		this.roleMenuAccess = roleMenuAccess;
	}
	
}
