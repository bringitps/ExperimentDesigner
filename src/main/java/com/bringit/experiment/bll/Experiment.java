package com.bringit.experiment.bll;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.bringit.experiment.dao.SysUserDao;

@Entity
@Table(name="Experiment")
public class Experiment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpId")
	private Integer expId;

	@Column(name="ExpDbTableNameId")
	private String expDbTableNameId;
	
	@Column(name="ExpIsActive")
	private Boolean expIsActive;
	
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
	
	@Column(name="ExpImage")
	private byte[] expImage;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;


	public Experiment() {
		this.expId = null;
		this.expDbTableNameId = null;
		this.expIsActive = false;
		this.expName = null;
		this.expComments = null;
		this.expInstructions = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.expImage = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}

	public Experiment(Integer expId, String expDbTableNameId, boolean expIsActive, String expName, String expComments,
			String expInstructions, Date createdDate, Date modifiedDate, byte[] expImage, SysUser createdBy,
			SysUser lastModifiedBy) {
		this.expId = expId;
		this.expDbTableNameId = expDbTableNameId;
		this.expIsActive = expIsActive;
		this.expName = expName;
		this.expComments = expComments;
		this.expInstructions = expInstructions;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.expImage = expImage;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getExpId() {
		return expId;
	}


	public void setExpId(Integer expId) {
		this.expId = expId;
	}

	public String getExpDbTableNameId() {
		return expDbTableNameId;
	}


	public void setExpDbTableNameId(String expDbTableNameId) {
		this.expDbTableNameId = expDbTableNameId;
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


	public byte[] getExpImage() {
		return expImage;
	}


	public void setExpImage(byte[] expImage) {
		this.expImage = expImage;
	}


}
