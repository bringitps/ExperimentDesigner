package com.bringit.experiment.bll;

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
	
	@Column(name="FtyBatchGroupMin")
	private Integer ftyBatchGroupMin;
	
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

	public FirstTimeYieldReport(Integer ftyReportId, Boolean ftyReportIsActive, String ftyReportName,
			String ftyReportDbRptTableNameId, Integer ftyBatchGroupMin, Experiment experiment,
			ExperimentField serialNumberExpField, ExperimentField dateTimeExpField, ExperimentField resultExpField) {
		this.ftyReportId = ftyReportId;
		this.ftyReportIsActive = ftyReportIsActive;
		this.ftyReportName = ftyReportName;
		this.ftyReportDbRptTableNameId = ftyReportDbRptTableNameId;
		this.ftyBatchGroupMin = ftyBatchGroupMin;
		this.experiment = experiment;
		this.SerialNumberExpField = serialNumberExpField;
		this.DateTimeExpField = dateTimeExpField;
		this.ResultExpField = resultExpField;
	}

	public FirstTimeYieldReport() {
		this.ftyReportId = null;
		this.ftyReportIsActive = null;
		this.ftyReportName = null;
		this.ftyReportDbRptTableNameId = null;
		this.ftyBatchGroupMin = null;
		this.experiment = null;
		this.SerialNumberExpField = null;
		this.DateTimeExpField = null;
		this.ResultExpField = null;
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

	public Integer getFtyBatchGroupMin() {
		return ftyBatchGroupMin;
	}

	public void setFtyBatchGroupMin(Integer ftyBatchGroupMin) {
		this.ftyBatchGroupMin = ftyBatchGroupMin;
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
	
}
