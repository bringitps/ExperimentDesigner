package com.bringit.experiment.bll;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Experiment")
public class Experiment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpId")
	private Integer expId;

	@Column(name="ExpIsActive")
	private boolean expIsActive;
	
	@Column(name="ExpName")
	private String expName;

	@Column(name="ExpComments")
	private String expComments;

	@Column(name="ExpInstructions")
	private String expInstructions;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@OneToOne
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;
	
	public Integer getExpId() {
		return expId;
	}


	public void setExpId(Integer expId) {
		this.expId = expId;
	}


	public boolean isExpIsActive() {
		return expIsActive;
	}


	public void setExpIsActive(boolean expIsActive) {
		this.expIsActive = expIsActive;
	}


	public String getExpName() {
		return expName;
	}


	public void setExpName(String expName) {
		this.expName = expName;
	}


	public String getExpComments() {
		return expComments;
	}


	public void setExpComments(String expComments) {
		this.expComments = expComments;
	}


	public String getExpInstructions() {
		return expInstructions;
	}


	public void setExpInstructions(String expInstructions) {
		this.expInstructions = expInstructions;
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
