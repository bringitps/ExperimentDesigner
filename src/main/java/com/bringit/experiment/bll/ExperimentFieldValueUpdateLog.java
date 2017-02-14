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
public class ExperimentFieldValueUpdateLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpFieldValueUpdateId")
	private Integer expFieldValueUpdateId;

	@Column(name="ExpOldValue")
	private String expOldValue;
	
	@Column(name="ExpOldCreatedDate")
	private Date expOldCreatedDate;

	@Column(name="ExpNewValue")
	private String expNewValue;
	
	@Column(name="ExpNewCreatedDate")
	private Date expNewCreatedDate;
	
	@OneToOne
    @JoinColumn(name="ExpFieldId", unique=false, updatable=false)
	private ExperimentField experimentField;

	@Column(name="DbExperimentDataTableRecordId")
	private Integer dbExperimentDataTableRecordId;
	
	@OneToOne
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;

	public Integer getExpFieldValueUpdateId() {
		return expFieldValueUpdateId;
	}

	public void setExpFieldValueUpdateId(Integer expFieldValueUpdateId) {
		this.expFieldValueUpdateId = expFieldValueUpdateId;
	}

	public String getExpOldValue() {
		return expOldValue;
	}

	public void setExpOldValue(String expOldValue) {
		this.expOldValue = expOldValue;
	}

	public Date getExpOldCreatedDate() {
		return expOldCreatedDate;
	}

	public void setExpOldCreatedDate(Date expOldCreatedDate) {
		this.expOldCreatedDate = expOldCreatedDate;
	}

	public String getExpNewValue() {
		return expNewValue;
	}

	public void setExpNewValue(String expNewValue) {
		this.expNewValue = expNewValue;
	}

	public Date getExpNewCreatedDate() {
		return expNewCreatedDate;
	}

	public void setExpNewCreatedDate(Date expNewCreatedDate) {
		this.expNewCreatedDate = expNewCreatedDate;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}

	public Integer getDbExperimentDataTableRecordId() {
		return dbExperimentDataTableRecordId;
	}

	public void setDbExperimentDataTableRecordId(Integer dbExperimentDataTableRecordId) {
		this.dbExperimentDataTableRecordId = dbExperimentDataTableRecordId;
	}

	public SysUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}
	
}