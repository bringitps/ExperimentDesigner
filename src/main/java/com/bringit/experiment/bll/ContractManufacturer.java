package com.bringit.experiment.bll;

import java.util.Date;

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
@Table(name="ContractManufacturer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ContractManufacturer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CmId")
	private Integer cmId;

	@Column(name="CmName")
	private String cmName;

	@Column(name="CmAbbreviation")
	private String cmAbbreviation;
	
	@Column(name="CmDescription")
	private String cmDescription;

	@Column(name="CmEmail")
	private String cmEmail;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;

	public ContractManufacturer() {
		this.cmId = null;
		this.cmName = null;
		this.cmAbbreviation = null;
		this.cmDescription = null;
		this.cmEmail = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}
	
	public ContractManufacturer(Integer cmId, String cmName, String cmAbbreviation, String cmDescription, String cmEmail, Date createdDate,
			Date modifiedDate, SysUser createdBy, SysUser lastModifiedBy) {
		this.cmId = cmId;
		this.cmName = cmName;
		this.cmAbbreviation = cmAbbreviation;
		this.cmDescription = cmDescription;
		this.cmEmail = cmEmail;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getCmId() {
		return cmId;
	}

	public void setCmId(Integer cmId) {
		this.cmId = cmId;
	}

	public String getCmName() {
		return cmName;
	}

	public void setCmName(String cmName) {
		this.cmName = cmName;
	}

	public String getCmAbbreviation() {
		return cmAbbreviation;
	}

	public void setCmAbbreviation(String cmAbbreviation) {
		this.cmAbbreviation = cmAbbreviation;
	}

	public String getCmDescription() {
		return cmDescription;
	}

	public void setCmDescription(String cmDescription) {
		this.cmDescription = cmDescription;
	}

	public String getCmEmail() {
		return cmEmail;
	}

	public void setCmEmail(String cmEmail) {
		this.cmEmail = cmEmail;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public SysUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}

	public SysUser getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(SysUser lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	
	
}
