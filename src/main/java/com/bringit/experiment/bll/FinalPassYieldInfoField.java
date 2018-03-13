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
@Table(name="FinalPassYieldInfoField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FinalPassYieldInfoField {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FnyInfoFieldId")
	private Integer fnyInfoFieldId;

	@Column(name="FnyInfoFieldLabel")
	private String fnyInfoFieldLabel;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FnyReportId", unique=false, updatable=true)
	private FinalPassYieldReport fnyReport;
	
	public FinalPassYieldInfoField() {
		this.fnyInfoFieldId = null;
		this.fnyInfoFieldLabel = null;
		this.experimentField = null;
		this.fnyReport = null;
	}

	public FinalPassYieldInfoField(Integer fnyInfoFieldId, String fnyInfoFieldLabel, ExperimentField experimentField, FinalPassYieldReport fnyReport) {
		this.fnyInfoFieldId = fnyInfoFieldId;
		this.fnyInfoFieldLabel = fnyInfoFieldLabel;
		this.experimentField = experimentField;
		this.fnyReport = fnyReport;
	}

	public Integer getFnyInfoFieldId() {
		return fnyInfoFieldId;
	}

	public void setFnyInfoFieldId(Integer fnyInfoFieldId) {
		this.fnyInfoFieldId = fnyInfoFieldId;
	}

	public String getFnyInfoFieldLabel() {
		return fnyInfoFieldLabel;
	}

	public void setFnyInfoFieldLabel(String fnyInfoFieldLabel) {
		this.fnyInfoFieldLabel = fnyInfoFieldLabel;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}

	public FinalPassYieldReport getFnyReport() {
		return fnyReport;
	}

	public void setFnyReport(FinalPassYieldReport fnyReport) {
		this.fnyReport = fnyReport;
	}
}
