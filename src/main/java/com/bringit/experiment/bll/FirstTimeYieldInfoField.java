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
@Table(name="FirstTimeYieldInfoField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FirstTimeYieldInfoField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FtyInfoFieldId")
	private Integer ftyInfoFieldId;

	@Column(name="FtyInfoFieldLabel")
	private String ftyInfoFieldLabel;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FtyReportId", unique=false, updatable=true)
	private FirstTimeYieldReport ftyReport;
	
	public FirstTimeYieldInfoField() {
		this.ftyInfoFieldId = null;
		this.ftyInfoFieldLabel = null;
		this.experimentField = null;
		this.ftyReport = null;
	}

	public FirstTimeYieldInfoField(Integer ftyInfoFieldId, String ftyInfoFieldLabel, ExperimentField experimentField, FirstTimeYieldReport ftyReport) {
		this.ftyInfoFieldId = ftyInfoFieldId;
		this.ftyInfoFieldLabel = ftyInfoFieldLabel;
		this.experimentField = experimentField;
		this.ftyReport = ftyReport;
	}

	public Integer getFtyInfoFieldId() {
		return ftyInfoFieldId;
	}

	public void setFtyInfoFieldId(Integer ftyInfoFieldId) {
		this.ftyInfoFieldId = ftyInfoFieldId;
	}

	public String getFtyInfoFieldLabel() {
		return ftyInfoFieldLabel;
	}

	public void setFtyInfoFieldLabel(String ftyInfoFieldLabel) {
		this.ftyInfoFieldLabel = ftyInfoFieldLabel;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}

	public FirstTimeYieldReport getFtyReport() {
		return ftyReport;
	}

	public void setFtyReport(FirstTimeYieldReport ftyReport) {
		this.ftyReport = ftyReport;
	}
}
