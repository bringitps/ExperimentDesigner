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
@Table(name="ExperimentMeasureFieldValueUpdateLog")
public class ExperimentMeasureFieldValueUpdateLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpMeasureFieldValueUpdateId")
	private Integer expMeasureFieldValueUpdateId;

	@Column(name="ExpMeasureOldValue")
	private String expMeasureOldValue;
	
	@Column(name="ExpMeasureOldCreatedDate")
	private Date expMeasureOldCreatedDate;

	@Column(name="ExpMeasureNewValue")
	private String expMeasureNewValue;
	
	@Column(name="ExpMeasureNewCreatedDate")
	private Date expMeasureNewCreatedDate;
	
	@OneToOne
    @JoinColumn(name="ExpMeasureFieldValueId", unique=false, updatable=false)
	private ExperimentMeasureFieldValue experimentMeasureFieldValue;

	@OneToOne
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;

	public Integer getExpMeasureFieldValueUpdateId() {
		return expMeasureFieldValueUpdateId;
	}

	public void setExpMeasureFieldValueUpdateId(Integer expMeasureFieldValueUpdateId) {
		this.expMeasureFieldValueUpdateId = expMeasureFieldValueUpdateId;
	}

	public String getExpMeasureOldValue() {
		return expMeasureOldValue;
	}

	public void setExpMeasureOldValue(String expMeasureOldValue) {
		this.expMeasureOldValue = expMeasureOldValue;
	}

	public Date getExpMeasureOldCreatedDate() {
		return expMeasureOldCreatedDate;
	}

	public void setExpMeasureOldCreatedDate(Date expMeasureOldCreatedDate) {
		this.expMeasureOldCreatedDate = expMeasureOldCreatedDate;
	}

	public String getExpMeasureNewValue() {
		return expMeasureNewValue;
	}

	public void setExpMeasureNewValue(String expMeasureNewValue) {
		this.expMeasureNewValue = expMeasureNewValue;
	}

	public Date getExpMeasureNewCreatedDate() {
		return expMeasureNewCreatedDate;
	}

	public void setExpMeasureNewCreatedDate(Date expMeasureNewCreatedDate) {
		this.expMeasureNewCreatedDate = expMeasureNewCreatedDate;
	}

	public ExperimentMeasureFieldValue getExperimentMeasureFieldValue() {
		return experimentMeasureFieldValue;
	}

	public void setExperimentMeasureFieldValue(ExperimentMeasureFieldValue experimentMeasureFieldValue) {
		this.experimentMeasureFieldValue = experimentMeasureFieldValue;
	}

	public SysUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}
	
}