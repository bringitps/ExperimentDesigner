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
@Table(name="FinalPassYieldReport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FinalPassYieldReport {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FnyReportId")
	private Integer fnyReportId;

	@Column(name="FnyReportIsActive")
	private Boolean fnyReportIsActive;

	@Column(name="FnyReportName")
	private String fnyReportName;

	@Column(name="FnyReportDbRptTableNameId")
	private String fnyReportDbRptTableNameId;

	@Column(name="FnyReportDbRptTableLastUpdate")
	private Date fnyReportDbRptTableLastUpdate;
	
	@Column(name="FnyReportDescription")
	private String fnyReportDescription;
	
	@Column(name="FnyGroupByTimeRange")
	private Boolean fnyGroupByTimeRange;
	
	@Column(name="FnyTimeRangeMin")
	private Integer fnyTimeRangeMin;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpId", unique=false, updatable=true)
	private Experiment experiment;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SerialNumberExpFieldId", unique=false, updatable=true)
	private ExperimentField SerialNumberExpField;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DateTimeExpFieldId", unique=false, updatable=true)
	private ExperimentField DateTimeExpField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ResultExpFieldId", unique=false, updatable=true)
	private ExperimentField ResultExpField;
	
	@Column(name="FnyPassResultValue")
	private String fnyPassResultValue;

	@Column(name="FnyFailResultValue")
	private String fnyFailResultValue;

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
	
	public FinalPassYieldReport() {
		this.fnyReportId = null;
		this.fnyReportIsActive = null;
		this.fnyReportName = null;
		this.fnyReportDbRptTableNameId = null;
		this.fnyReportDbRptTableLastUpdate = null;
		this.fnyReportDescription = null;
		this.fnyGroupByTimeRange = null;
		this.fnyTimeRangeMin = null;
		this.experiment = null;
		this.SerialNumberExpField = null;
		this.DateTimeExpField = null;
		this.ResultExpField = null;
		this.fnyPassResultValue = null;
		this.fnyFailResultValue = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}

	public FinalPassYieldReport(Integer fnyReportId, Boolean fnyReportIsActive, String fnyReportName,
			String fnyReportDbRptTableNameId, Date fnyReportDbRptTableLastUpdate, String fnyReportDescription,
			Boolean fnyGroupByTimeRange, Integer fnyTimeRangeMin, Experiment experiment,
			ExperimentField serialNumberExpField, ExperimentField dateTimeExpField, ExperimentField resultExpField,
			String fnyPassResultValue, String fnyFailResultValue, Date createdDate, Date modifiedDate,
			SysUser createdBy, SysUser lastModifiedBy) {
		this.fnyReportId = fnyReportId;
		this.fnyReportIsActive = fnyReportIsActive;
		this.fnyReportName = fnyReportName;
		this.fnyReportDbRptTableNameId = fnyReportDbRptTableNameId;
		this.fnyReportDbRptTableLastUpdate = fnyReportDbRptTableLastUpdate;
		this.fnyReportDescription = fnyReportDescription;
		this.fnyGroupByTimeRange = fnyGroupByTimeRange;
		this.fnyTimeRangeMin = fnyTimeRangeMin;
		this.experiment = experiment;
		SerialNumberExpField = serialNumberExpField;
		DateTimeExpField = dateTimeExpField;
		ResultExpField = resultExpField;
		this.fnyPassResultValue = fnyPassResultValue;
		this.fnyFailResultValue = fnyFailResultValue;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getFnyReportId() {
		return fnyReportId;
	}

	public void setFnyReportId(Integer fnyReportId) {
		this.fnyReportId = fnyReportId;
	}

	public Boolean getFnyReportIsActive() {
		return fnyReportIsActive;
	}

	public void setFnyReportIsActive(Boolean fnyReportIsActive) {
		this.fnyReportIsActive = fnyReportIsActive;
	}

	public String getFnyReportName() {
		return fnyReportName;
	}

	public void setFnyReportName(String fnyReportName) {
		this.fnyReportName = fnyReportName;
	}

	public String getFnyReportDbRptTableNameId() {
		return fnyReportDbRptTableNameId;
	}

	public void setFnyReportDbRptTableNameId(String fnyReportDbRptTableNameId) {
		this.fnyReportDbRptTableNameId = fnyReportDbRptTableNameId;
	}

	public Date getFnyReportDbRptTableLastUpdate() {
		return fnyReportDbRptTableLastUpdate;
	}

	public void setFnyReportDbRptTableLastUpdate(Date fnyReportDbRptTableLastUpdate) {
		this.fnyReportDbRptTableLastUpdate = fnyReportDbRptTableLastUpdate;
	}

	public String getFnyReportDescription() {
		return fnyReportDescription;
	}

	public void setFnyReportDescription(String fnyReportDescription) {
		this.fnyReportDescription = fnyReportDescription;
	}

	public Boolean getFnyGroupByTimeRange() {
		return fnyGroupByTimeRange;
	}

	public void setFnyGroupByTimeRange(Boolean fnyGroupByTimeRange) {
		this.fnyGroupByTimeRange = fnyGroupByTimeRange;
	}

	public Integer getFnyTimeRangeMin() {
		return fnyTimeRangeMin;
	}

	public void setFnyTimeRangeMin(Integer fnyTimeRangeMin) {
		this.fnyTimeRangeMin = fnyTimeRangeMin;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public ExperimentField getSerialNumberExpField() {
		return SerialNumberExpField;
	}

	public void setSerialNumberExpField(ExperimentField serialNumberExpField) {
		SerialNumberExpField = serialNumberExpField;
	}

	public ExperimentField getDateTimeExpField() {
		return DateTimeExpField;
	}

	public void setDateTimeExpField(ExperimentField dateTimeExpField) {
		DateTimeExpField = dateTimeExpField;
	}

	public ExperimentField getResultExpField() {
		return ResultExpField;
	}

	public void setResultExpField(ExperimentField resultExpField) {
		ResultExpField = resultExpField;
	}

	public String getFnyPassResultValue() {
		return fnyPassResultValue;
	}

	public void setFnyPassResultValue(String fnyPassResultValue) {
		this.fnyPassResultValue = fnyPassResultValue;
	}

	public String getFnyFailResultValue() {
		return fnyFailResultValue;
	}

	public void setFnyFailResultValue(String fnyFailResultValue) {
		this.fnyFailResultValue = fnyFailResultValue;
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
