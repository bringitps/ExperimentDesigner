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
@Table(name="FirstTimeYieldReport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FirstTimeYieldReport {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FtyReportId")
	private Integer ftyReportId;

	@Column(name="FtyReportIsActive")
	private Boolean ftyReportIsActive;

	@Column(name="FtyReportName")
	private String ftyReportName;

	@Column(name="FtyReportDbRptTableNameId")
	private String ftyReportDbRptTableNameId;

	@Column(name="FtyReportDbRptTableLastUpdate")
	private Date ftyReportDbRptTableLastUpdate;
	
	@Column(name="FtyReportDescription")
	private String ftyReportDescription;
	
	@Column(name="FtyGroupByTimeRange")
	private Boolean ftyGroupByTimeRange;
	
	@Column(name="FtyTimeRangeMin")
	private Integer ftyTimeRangeMin;
	
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
	
	@Column(name="FtyPassResultValue")
	private String ftyPassResultValue;

	@Column(name="FtyFailResultValue")
	private String ftyFailResultValue;

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
	
	public FirstTimeYieldReport() {
		this.ftyReportId = null;
		this.ftyReportIsActive = null;
		this.ftyReportName = null;
		this.ftyReportDbRptTableNameId = null;
		this.ftyReportDbRptTableLastUpdate = null;
		this.ftyReportDescription = null;
		this.ftyGroupByTimeRange = null;
		this.ftyTimeRangeMin = null;
		this.experiment = null;
		this.SerialNumberExpField = null;
		this.DateTimeExpField = null;
		this.ResultExpField = null;
		this.ftyPassResultValue = null;
		this.ftyFailResultValue = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}
	
	public FirstTimeYieldReport(Integer ftyReportId, Boolean ftyReportIsActive, String ftyReportName,
			String ftyReportDbRptTableNameId, Date ftyReportDbRptTableLastUpdate, String ftyReportDescription, Boolean ftyGroupByTimeRange, Integer ftyTimeRangeMin, Experiment experiment,
			ExperimentField serialNumberExpField, ExperimentField dateTimeExpField, ExperimentField resultExpField,
			String ftyPassResultValue, String ftyFailResultValue, Date createdDate, Date modifiedDate, SysUser createdBy, SysUser lastModifiedBy) {
		this.ftyReportId = ftyReportId;
		this.ftyReportIsActive = ftyReportIsActive;
		this.ftyReportName = ftyReportName;
		this.ftyReportDbRptTableNameId = ftyReportDbRptTableNameId;
		this.ftyReportDbRptTableLastUpdate = ftyReportDbRptTableLastUpdate;
		this.ftyReportDescription = ftyReportDescription;
		this.ftyGroupByTimeRange = ftyGroupByTimeRange;
		this.ftyTimeRangeMin = ftyTimeRangeMin;
		this.experiment = experiment;
		this.SerialNumberExpField = serialNumberExpField;
		this.DateTimeExpField = dateTimeExpField;
		this.ResultExpField = resultExpField;
		this.ftyPassResultValue = ftyPassResultValue;
		this.ftyFailResultValue = ftyFailResultValue;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getFtyReportId() {
		return ftyReportId;
	}

	public void setFtyReportId(Integer ftyReportId) {
		this.ftyReportId = ftyReportId;
	}

	public Boolean getFtyReportIsActive() {
		return ftyReportIsActive;
	}

	public void setFtyReportIsActive(Boolean ftyReportIsActive) {
		this.ftyReportIsActive = ftyReportIsActive;
	}

	public String getFtyReportName() {
		return ftyReportName;
	}

	public void setFtyReportName(String ftyReportName) {
		this.ftyReportName = ftyReportName;
	}

	public String getFtyReportDbRptTableNameId() {
		return ftyReportDbRptTableNameId;
	}

	public void setFtyReportDbRptTableNameId(String ftyReportDbRptTableNameId) {
		this.ftyReportDbRptTableNameId = ftyReportDbRptTableNameId;
	}
	
	public Date getFtyReportDbRptTableLastUpdate() {
		return ftyReportDbRptTableLastUpdate;
	}

	public void setFtyReportDbRptTableLastUpdate(Date ftyReportDbRptTableLastUpdate) {
		this.ftyReportDbRptTableLastUpdate = ftyReportDbRptTableLastUpdate;
	}

	public String getFtyReportDescription() {
		return ftyReportDescription;
	}

	public void setFtyReportDescription(String ftyReportDescription) {
		this.ftyReportDescription = ftyReportDescription;
	}

	public Boolean getFpyGroupByTimeRange() {
		return ftyGroupByTimeRange;
	}

	public void setFtyGroupByTimeRange(Boolean ftyGroupByTimeRange) {
		this.ftyGroupByTimeRange = ftyGroupByTimeRange;
	}

	public Integer getFtyTimeRangeMin() {
		return ftyTimeRangeMin;
	}

	public void setFtyTimeRangeMin(Integer ftyTimeRangeMin) {
		this.ftyTimeRangeMin = ftyTimeRangeMin;
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

	public String getFtyPassResultValue() {
		return ftyPassResultValue;
	}

	public void setFtyPassResultValue(String ftyPassResultValue) {
		this.ftyPassResultValue = ftyPassResultValue;
	}

	public String getFtyFailResultValue() {
		return ftyFailResultValue;
	}

	public void setFtyFailResultValue(String ftyFailResultValue) {
		this.ftyFailResultValue = ftyFailResultValue;
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
