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
@Table(name="FinalPassYieldJobData")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FinalPassYieldReportJobData {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FnyJobId")
	private Integer fnyJobId;

	@Column(name="CreatedDate")
	private Date createdDate;

	@Column(name="ModifiedDate")
	private Date lastModifiedDate;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;

	@Column(name="IsAutoScheduler")
	private Boolean isAutoScheduler;  //0:Manual, 1:Auto

	@Column(name="IsCompleted")
	private Boolean isCompleted; //0:Pending, 1:Completed

	@Column(name="Status")
	private String status;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FnyReportId", unique=false, updatable=false)
	private FinalPassYieldReport fnyReport;

	public Integer getFnyJobId() {
		return fnyJobId;
	}

	public void setFnyJobId(Integer fnyJobId) {
		this.fnyJobId = fnyJobId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public SysUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}

	public Boolean getIsAutoScheduler() {
		return isAutoScheduler;
	}

	public void setIsAutoScheduler(Boolean isAutoScheduler) {
		this.isAutoScheduler = isAutoScheduler;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FinalPassYieldReport getFinalPassYieldReport() {
		return fnyReport;
	}

	public void setFinalPassYieldReport(FinalPassYieldReport fnyReport) {
		this.fnyReport = fnyReport;
	}
}
