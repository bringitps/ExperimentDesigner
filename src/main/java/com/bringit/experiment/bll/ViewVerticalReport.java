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
@Table(name="ViewVerticalReport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewVerticalReport {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptId")
	private Integer vwVerticalRptId;
	
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

	@Column(name="VwVerticalRptIsActive")
	private Boolean vwVerticalRptIsActive;

	@Column(name="VwVerticalRptName")
	private String vwVerticalRptName;

	@Column(name="VwVerticalRptDbTableNameId")
	private String vwVerticalRptDbTableNameId;

	@Column(name="VwVerticalRptDbTableLastUpdate")
	private Date vwVerticalRptDbTableLastUpdate;
	
	@Column(name="vwVerticalRptDescription")
	private String vwVerticalRptDescription;

	public ViewVerticalReport(Integer vwVerticalRptId, Date createdDate, Date modifiedDate, SysUser createdBy,
			SysUser lastModifiedBy, Boolean vwVerticalRptIsActive, String vwVerticalRptName,
			String vwVerticalRptDbTableNameId, Date vwVerticalRptDbTableLastUpdate, String vwVerticalRptDescription) {
		this.vwVerticalRptId = vwVerticalRptId;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
		this.vwVerticalRptIsActive = vwVerticalRptIsActive;
		this.vwVerticalRptName = vwVerticalRptName;
		this.vwVerticalRptDbTableNameId = vwVerticalRptDbTableNameId;
		this.vwVerticalRptDbTableLastUpdate = vwVerticalRptDbTableLastUpdate;
		this.vwVerticalRptDescription = vwVerticalRptDescription;
	}

	public ViewVerticalReport() {
		this.vwVerticalRptId = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
		this.vwVerticalRptIsActive = null;
		this.vwVerticalRptName = null;
		this.vwVerticalRptDbTableNameId = null;
		this.vwVerticalRptDbTableLastUpdate = null;
		this.vwVerticalRptDescription = null;
	}

	public Integer getVwVerticalRptId() {
		return vwVerticalRptId;
	}

	public void setVwVerticalRptId(Integer vwVerticalRptId) {
		this.vwVerticalRptId = vwVerticalRptId;
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

	public Boolean getVwVerticalRptIsActive() {
		return vwVerticalRptIsActive;
	}

	public void setVwVerticalRptIsActive(Boolean vwVerticalRptIsActive) {
		this.vwVerticalRptIsActive = vwVerticalRptIsActive;
	}

	public String getVwVerticalRptName() {
		return vwVerticalRptName;
	}

	public void setVwVerticalRptName(String vwVerticalRptName) {
		this.vwVerticalRptName = vwVerticalRptName;
	}

	public String getVwVerticalRptDbTableNameId() {
		return vwVerticalRptDbTableNameId;
	}

	public void setVwVerticalRptDbTableNameId(String vwVerticalRptDbTableNameId) {
		this.vwVerticalRptDbTableNameId = vwVerticalRptDbTableNameId;
	}

	public Date getVwVerticalRptDbTableLastUpdate() {
		return vwVerticalRptDbTableLastUpdate;
	}

	public void setVwVerticalRptDbTableLastUpdate(Date vwVerticalRptDbTableLastUpdate) {
		this.vwVerticalRptDbTableLastUpdate = vwVerticalRptDbTableLastUpdate;
	}

	public String getVwVerticalRptDescription() {
		return vwVerticalRptDescription;
	}

	public void setVwVerticalRptDescription(String vwVerticalRptDescription) {
		this.vwVerticalRptDescription = vwVerticalRptDescription;
	}

}
