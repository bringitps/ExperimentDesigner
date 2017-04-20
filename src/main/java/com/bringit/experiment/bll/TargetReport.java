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
import org.hibernate.annotations.Type;

@Entity
@Table(name="TargetReport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class TargetReport {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TargetReportId")
	private Integer targetReportId;

	@Column(name="TargetReportIsActive")
	private Boolean targetReportIsActive;

	@Column(name="TargetReportName")
	private String targetReportName;

	@Column(name="TargetReportDbRptTableNameId")
	private String targetReportDbRptTableNameId;
	
	@Column(name="TargetReportDbRptTableLastUpdate")
	private String targetReportDbRptTableLastUpdate;
	
	@Column(name="TargetReportDescription")
	@Type(type="text")
	private String targetReportDescription;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpId", unique=false, updatable=true)
	private Experiment experiment;

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

	public TargetReport() {
		this.targetReportId = null;
		this.targetReportIsActive = null;
		this.targetReportName = null;
		this.targetReportDescription = null;
		this.experiment = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}
	
	public TargetReport(Integer targetReportId, Boolean targetReportIsActive, String targetReportName,
			String targetReportDescription, Experiment experiment, Date createdDate, Date modifiedDate,
			SysUser createdBy, SysUser lastModifiedBy) {
		this.targetReportId = targetReportId;
		this.targetReportIsActive = targetReportIsActive;
		this.targetReportName = targetReportName;
		this.targetReportDescription = targetReportDescription;
		this.experiment = experiment;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getTargetReportId() {
		return targetReportId;
	}

	public void setTargetReportId(Integer targetReportId) {
		this.targetReportId = targetReportId;
	}

	public Boolean getTargetReportIsActive() {
		return targetReportIsActive;
	}

	public void setTargetReportIsActive(Boolean targetReportIsActive) {
		this.targetReportIsActive = targetReportIsActive;
	}

	public String getTargetReportName() {
		return targetReportName;
	}

	public void setTargetReportName(String targetReportName) {
		this.targetReportName = targetReportName;
	}

	public String getTargetReportDbRptTableNameId() {
		return targetReportDbRptTableNameId;
	}

	public void setTargetReportDbRptTableNameId(String targetReportDbRptTableNameId) {
		this.targetReportDbRptTableNameId = targetReportDbRptTableNameId;
	}

	public String getTargetReportDbRptTableLastUpdate() {
		return targetReportDbRptTableLastUpdate;
	}

	public void setTargetReportDbRptTableLastUpdate(String targetReportDbRptTableLastUpdate) {
		this.targetReportDbRptTableLastUpdate = targetReportDbRptTableLastUpdate;
	}

	public String getTargetReportDescription() {
		return targetReportDescription;
	}

	public void setTargetReportDescription(String targetReportDescription) {
		this.targetReportDescription = targetReportDescription;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
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

	@Override
	public String toString() {
		return "TargetReport [targetReportId=" + targetReportId + ", targetReportIsActive=" + targetReportIsActive
				+ ", targetReportName=" + targetReportName + ", targetReportDescription=" + targetReportDescription
				+ ", experiment=" + experiment + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate
				+ ", createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
	
	
}
