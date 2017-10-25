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
@Table(name="FirstPassYieldInfoField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FirstPassYieldInfoField {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FpyInfoFieldId")
	private Integer fpyInfoFieldId;

	@Column(name="FpyInfoFieldLabel")
	private String fpyInfoFieldLabel;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FpyReportId", unique=false, updatable=true)
	private FirstPassYieldReport fpyReport;
	
	public FirstPassYieldInfoField() {
		this.fpyInfoFieldId = null;
		this.fpyInfoFieldLabel = null;
		this.experimentField = null;
		this.fpyReport = null;
	}

	public FirstPassYieldInfoField(Integer fpyInfoFieldId, String fpyInfoFieldLabel, ExperimentField experimentField, FirstPassYieldReport fpyReport) {
		this.fpyInfoFieldId = fpyInfoFieldId;
		this.fpyInfoFieldLabel = fpyInfoFieldLabel;
		this.experimentField = experimentField;
		this.fpyReport = fpyReport;
	}

	public Integer getFpyInfoFieldId() {
		return fpyInfoFieldId;
	}

	public void setFpyInfoFieldId(Integer fpyInfoFieldId) {
		this.fpyInfoFieldId = fpyInfoFieldId;
	}

	public String getFpyInfoFieldLabel() {
		return fpyInfoFieldLabel;
	}

	public void setFpyInfoFieldLabel(String fpyInfoFieldLabel) {
		this.fpyInfoFieldLabel = fpyInfoFieldLabel;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}

	public FirstPassYieldReport getFpyReport() {
		return fpyReport;
	}

	public void setFpyReport(FirstPassYieldReport fpyReport) {
		this.fpyReport = fpyReport;
	}
}
