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
@Table(name="FirstPassYieldReport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FirstPassYieldReport {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FpyReportId")
	private Integer fpyReportId;

	@Column(name="FpyReportIsActive")
	private Boolean fpyReportIsActive;

	@Column(name="FpyReportName")
	private String fpyReportName;

	@Column(name="FpyReportDbRptTableNameId")
	private String fpyReportDbRptTableNameId;

	@Column(name="FpyReportDescription")
	private String fpyReportDescription;
	
	@Column(name="FpyGroupByTimeRange")
	private Boolean fpyGroupByTimeRange;
	
	@Column(name="FpyTimeRangeMin")
	private Integer fpyTimeRangeMin;
	
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
	
	@Column(name="FpyPassResultValue")
	private String fpyPassResultValue;

	@Column(name="FpyFailResultValue")
	private String fpyFailResultValue;

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
	
	public FirstPassYieldReport() {
		this.fpyReportId = null;
		this.fpyReportIsActive = null;
		this.fpyReportName = null;
		this.fpyReportDbRptTableNameId = null;
		this.fpyReportDescription = null;
		this.fpyGroupByTimeRange = null;
		this.fpyTimeRangeMin = null;
		this.experiment = null;
		this.SerialNumberExpField = null;
		this.DateTimeExpField = null;
		this.ResultExpField = null;
		this.fpyPassResultValue = null;
		this.fpyFailResultValue = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}
	
	public FirstPassYieldReport(Integer fpyReportId, Boolean fpyReportIsActive, String fpyReportName,
			String fpyReportDbRptTableNameId, String fpyReportDescription, Boolean fpyGroupByTimeRange, Integer fpyTimeRangeMin, Experiment experiment,
			ExperimentField serialNumberExpField, ExperimentField dateTimeExpField, ExperimentField resultExpField,
			String fpyPassResultValue, String fpyFailResultValue, Date createdDate, Date modifiedDate, SysUser createdBy, SysUser lastModifiedBy) {
		this.fpyReportId = fpyReportId;
		this.fpyReportIsActive = fpyReportIsActive;
		this.fpyReportName = fpyReportName;
		this.fpyReportDbRptTableNameId = fpyReportDbRptTableNameId;
		this.fpyReportDescription = fpyReportDescription;
		this.fpyGroupByTimeRange = fpyGroupByTimeRange;
		this.fpyTimeRangeMin = fpyTimeRangeMin;
		this.experiment = experiment;
		this.SerialNumberExpField = serialNumberExpField;
		this.DateTimeExpField = dateTimeExpField;
		this.ResultExpField = resultExpField;
		this.fpyPassResultValue = fpyPassResultValue;
		this.fpyFailResultValue = fpyFailResultValue;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getFpyReportId() {
		return fpyReportId;
	}

	public void setFpyReportId(Integer fpyReportId) {
		this.fpyReportId = fpyReportId;
	}

	public Boolean getFpyReportIsActive() {
		return fpyReportIsActive;
	}

	public void setFpyReportIsActive(Boolean fpyReportIsActive) {
		this.fpyReportIsActive = fpyReportIsActive;
	}

	public String getFpyReportName() {
		return fpyReportName;
	}

	public void setFpyReportName(String fpyReportName) {
		this.fpyReportName = fpyReportName;
	}

	public String getFpyReportDbRptTableNameId() {
		return fpyReportDbRptTableNameId;
	}

	public void setFpyReportDbRptTableNameId(String fpyReportDbRptTableNameId) {
		this.fpyReportDbRptTableNameId = fpyReportDbRptTableNameId;
	}

	public String getFpyReportDescription() {
		return fpyReportDescription;
	}

	public void setFpyReportDescription(String fpyReportDescription) {
		this.fpyReportDescription = fpyReportDescription;
	}

	public Boolean getFpyGroupByTimeRange() {
		return fpyGroupByTimeRange;
	}

	public void setFpyGroupByTimeRange(Boolean fpyGroupByTimeRange) {
		this.fpyGroupByTimeRange = fpyGroupByTimeRange;
	}

	public Integer getFpyTimeRangeMin() {
		return fpyTimeRangeMin;
	}

	public void setFpyTimeRangeMin(Integer fpyTimeRangeMin) {
		this.fpyTimeRangeMin = fpyTimeRangeMin;
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
		this.SerialNumberExpField = serialNumberExpField;
	}

	public ExperimentField getDateTimeExpField() {
		return DateTimeExpField;
	}

	public void setDateTimeExpField(ExperimentField dateTimeExpField) {
		this.DateTimeExpField = dateTimeExpField;
	}

	public ExperimentField getResultExpField() {
		return ResultExpField;
	}

	public void setResultExpField(ExperimentField resultExpField) {
		this.ResultExpField = resultExpField;
	}

	public String getFpyPassResultValue() {
		return fpyPassResultValue;
	}

	public void setFpyPassResultValue(String fpyPassResultValue) {
		this.fpyPassResultValue = fpyPassResultValue;
	}

	public String getFpyFailResultValue() {
		return fpyFailResultValue;
	}

	public void setFpyFailResultValue(String fpyFailResultValue) {
		this.fpyFailResultValue = fpyFailResultValue;
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
