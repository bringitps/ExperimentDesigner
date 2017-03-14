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
@Table(name="ExperimentMeasureFieldValueUpdateLog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
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
	

	@Column(name="DbExperimentDataTableRecordId")
	private Integer dbExperimentDataTableRecordId;
	
	@Column(name="DbTableRecordCommentsUpdate")
	private Boolean dbTableRecordCommentsUpdate;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpId", unique=false, updatable=true)
	private Experiment experiment;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;
	
	@OneToOne
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;

	public ExperimentFieldValueUpdateLog() {
		this.expFieldValueUpdateId = null;
		this.expOldValue = null;
		this.expOldCreatedDate = null;
		this.expNewValue = null;
		this.expNewCreatedDate = null;
		this.experiment = null;
		this.experimentField = null;
		this.dbExperimentDataTableRecordId = null;
		this.dbTableRecordCommentsUpdate = null;
		this.createdBy = null;
	}
	
	public ExperimentFieldValueUpdateLog(Integer expFieldValueUpdateId, String expOldValue, Date expOldCreatedDate,
			String expNewValue, Date expNewCreatedDate, Experiment experiment, ExperimentField experimentField,
			Integer dbExperimentDataTableRecordId, boolean dbTableRecordCommentsUpdate, SysUser createdBy) {
		this.expFieldValueUpdateId = expFieldValueUpdateId;
		this.expOldValue = expOldValue;
		this.expOldCreatedDate = expOldCreatedDate;
		this.expNewValue = expNewValue;
		this.expNewCreatedDate = expNewCreatedDate;
		this.experiment = experiment;
		this.experimentField = experimentField;
		this.dbExperimentDataTableRecordId = dbExperimentDataTableRecordId;
		this.dbTableRecordCommentsUpdate = dbTableRecordCommentsUpdate;
		this.createdBy = createdBy;
	}

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

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
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

	public boolean isDbTableRecordCommentsUpdate() {
		return dbTableRecordCommentsUpdate;
	}

	public void setDbTableRecordCommentsUpdate(boolean dbTableRecordCommentsUpdate) {
		this.dbTableRecordCommentsUpdate = dbTableRecordCommentsUpdate;
	}

	public SysUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}
	
}