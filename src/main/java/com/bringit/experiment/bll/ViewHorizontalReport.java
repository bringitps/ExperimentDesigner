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
@Table(name="ViewHorizontalReport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReport {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptId")
	private Integer vwHorizontalRptId;
	
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

	@Column(name="VwHorizontalRptIsActive")
	private Boolean vwHorizontalRptIsActive;

	@Column(name="VwHorizontalRptName")
	private String vwHorizontalRptName;

	@Column(name="VwHorizontalRptDbTableNameId")
	private String vwHorizontalRptDbTableNameId;

	@Column(name="VwHorizontalRptDbTableLastUpdate")
	private Date vwHorizontalRptDbTableLastUpdate;
	
	@Column(name="vwHorizontalRptDescription")
	private String vwHorizontalRptDescription;
	
	@Column(name="vwHorizontalRptKeyColumnName")
	private String vwHorizontalRptKeyColumnName;

	@Column(name="vwHorizontalRptKeyColumnDbId")
	private String vwHorizontalRptKeyColumnDbId;

	@Column(name="vwHorizontalRptKeyColumnDataType")
	private String vwHorizontalRptKeyColumnDataType;

	public ViewHorizontalReport(Integer vwHorizontalRptId, Date createdDate, Date modifiedDate, SysUser createdBy,
			SysUser lastModifiedBy, Boolean vwHorizontalRptIsActive, String vwHorizontalRptName,
			String vwHorizontalRptDbTableNameId, Date vwHorizontalRptDbTableLastUpdate,
			String vwHorizontalRptDescription, String vwHorizontalRptKeyColumnName,
			String vwHorizontalRptKeyColumnDbId, String vwHorizontalRptKeyColumnDataType) {
		this.vwHorizontalRptId = vwHorizontalRptId;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
		this.vwHorizontalRptIsActive = vwHorizontalRptIsActive;
		this.vwHorizontalRptName = vwHorizontalRptName;
		this.vwHorizontalRptDbTableNameId = vwHorizontalRptDbTableNameId;
		this.vwHorizontalRptDbTableLastUpdate = vwHorizontalRptDbTableLastUpdate;
		this.vwHorizontalRptDescription = vwHorizontalRptDescription;
		this.vwHorizontalRptKeyColumnName = vwHorizontalRptKeyColumnName;
		this.vwHorizontalRptKeyColumnDbId = vwHorizontalRptKeyColumnDbId;
		this.vwHorizontalRptKeyColumnDataType = vwHorizontalRptKeyColumnDataType;
	}
	
	public ViewHorizontalReport() {
		this.vwHorizontalRptId = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
		this.vwHorizontalRptIsActive = null;
		this.vwHorizontalRptName = null;
		this.vwHorizontalRptDbTableNameId = null;
		this.vwHorizontalRptDbTableLastUpdate = null;
		this.vwHorizontalRptDescription = null;
		this.vwHorizontalRptKeyColumnName = null;
		this.vwHorizontalRptKeyColumnDbId = null;
		this.vwHorizontalRptKeyColumnDataType = null;
	}

	public Integer getVwHorizontalRptId() {
		return vwHorizontalRptId;
	}

	public void setVwHorizontalRptId(Integer vwHorizontalRptId) {
		this.vwHorizontalRptId = vwHorizontalRptId;
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

	public Boolean getVwHorizontalRptIsActive() {
		return vwHorizontalRptIsActive;
	}

	public void setVwHorizontalRptIsActive(Boolean vwHorizontalRptIsActive) {
		this.vwHorizontalRptIsActive = vwHorizontalRptIsActive;
	}

	public String getVwHorizontalRptName() {
		return vwHorizontalRptName;
	}

	public void setVwHorizontalRptName(String vwHorizontalRptName) {
		this.vwHorizontalRptName = vwHorizontalRptName;
	}

	public String getVwHorizontalRptDbTableNameId() {
		return vwHorizontalRptDbTableNameId;
	}

	public void setVwHorizontalRptDbTableNameId(String vwHorizontalRptDbTableNameId) {
		this.vwHorizontalRptDbTableNameId = vwHorizontalRptDbTableNameId;
	}

	public Date getVwHorizontalRptDbTableLastUpdate() {
		return vwHorizontalRptDbTableLastUpdate;
	}

	public void setVwHorizontalRptDbTableLastUpdate(Date vwHorizontalRptDbTableLastUpdate) {
		this.vwHorizontalRptDbTableLastUpdate = vwHorizontalRptDbTableLastUpdate;
	}

	public String getVwHorizontalRptDescription() {
		return vwHorizontalRptDescription;
	}

	public void setVwHorizontalRptDescription(String vwHorizontalRptDescription) {
		this.vwHorizontalRptDescription = vwHorizontalRptDescription;
	}

	public String getVwHorizontalRptKeyColumnName() {
		return vwHorizontalRptKeyColumnName;
	}

	public void setVwHorizontalRptKeyColumnName(String vwHorizontalRptKeyColumnName) {
		this.vwHorizontalRptKeyColumnName = vwHorizontalRptKeyColumnName;
	}
	
	public String getVwHorizontalRptKeyColumnDbId() {
		return vwHorizontalRptKeyColumnDbId;
	}

	public void setVwHorizontalRptKeyColumnDbId(String vwHorizontalRptKeyColumnDbId) {
		this.vwHorizontalRptKeyColumnDbId = vwHorizontalRptKeyColumnDbId;
	}

	public String getVwHorizontalRptKeyColumnDataType() {
		return vwHorizontalRptKeyColumnDataType;
	}

	public void setVwHorizontalRptKeyColumnDataType(String vwHorizontalRptKeyColumnDataType) {
		this.vwHorizontalRptKeyColumnDataType = vwHorizontalRptKeyColumnDataType;
	}
	
	
	
}
