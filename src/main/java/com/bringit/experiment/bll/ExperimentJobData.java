package com.bringit.experiment.bll;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
import java.util.Date;

@Entity
@Table(name="ExperimentJobData")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ExperimentJobData {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpJobId")
	private Integer expJobId;

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
	private Boolean isCompleted;  //0:Manual, 1:Auto

	@Column(name="Status")
	private String status;

	public Integer getExpJobId() {
		return expJobId;
	}

	public void setExpJobId(Integer expJobId) {
		this.expJobId = expJobId;
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
}
