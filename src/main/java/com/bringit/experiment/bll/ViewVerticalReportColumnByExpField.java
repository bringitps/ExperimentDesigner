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
@Table(name="ViewVerticalReportColumnByExpField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewVerticalReportColumnByExpField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptColumnByExpFieldId")
	private Integer vwVerticalRptColumnByExpFieldId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalRptColumnId", unique=false, updatable=true)
	private ViewVerticalReportColumn vwVerticalReportColumn;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpId", unique=false, updatable=true)
	private Experiment experiment;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	@Column(name="VwVerticalRptColumnByExpFieldIsFixedValue")
	private Boolean vwVerticalRptColumnByExpFieldIsFixedValue;
	
	@Column(name="VwVerticalRptColumnByExpFieldFixedValue")
	private String vwVerticalRptColumnByExpFieldFixedValue;
	
	public ViewVerticalReportColumnByExpField(Integer vwVerticalRptColumnByExpFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn, Experiment experiment,
			ExperimentField experimentField, Boolean vwVerticalRptColumnByExpFieldIsFixedValue,
			String vwVerticalRptColumnByExpFieldFixedValue) {
		this.vwVerticalRptColumnByExpFieldId = vwVerticalRptColumnByExpFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.experiment = experiment;
		this.experimentField = experimentField;
		this.vwVerticalRptColumnByExpFieldIsFixedValue = vwVerticalRptColumnByExpFieldIsFixedValue;
		this.vwVerticalRptColumnByExpFieldFixedValue = vwVerticalRptColumnByExpFieldFixedValue;
	}	
	
	public ViewVerticalReportColumnByExpField() {
		this.vwVerticalRptColumnByExpFieldId = null;
		this.vwVerticalReportColumn = null;
		this.experiment = null;
		this.experimentField = null;
		this.vwVerticalRptColumnByExpFieldIsFixedValue = null;
		this.vwVerticalRptColumnByExpFieldFixedValue = null;
	}

	public Integer getVwVerticalRptColumnByExpFieldId() {
		return vwVerticalRptColumnByExpFieldId;
	}

	public void setVwVerticalRptColumnByExpFieldId(Integer vwVerticalRptColumnByExpFieldId) {
		this.vwVerticalRptColumnByExpFieldId = vwVerticalRptColumnByExpFieldId;
	}

	public ViewVerticalReportColumn getVwVerticalReportColumn() {
		return vwVerticalReportColumn;
	}

	public void setVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
		this.vwVerticalReportColumn = vwVerticalReportColumn;
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

	public Boolean getVwVerticalRptColumnByExpFieldIsFixedValue() {
		return vwVerticalRptColumnByExpFieldIsFixedValue;
	}

	public void setVwVerticalRptColumnByExpFieldIsFixedValue(Boolean vwVerticalRptColumnByExpFieldIsFixedValue) {
		this.vwVerticalRptColumnByExpFieldIsFixedValue = vwVerticalRptColumnByExpFieldIsFixedValue;
	}

	public String getVwVerticalRptColumnByExpFieldFixedValue() {
		return vwVerticalRptColumnByExpFieldFixedValue;
	}

	public void setVwVerticalRptColumnByExpFieldFixedValue(String vwVerticalRptColumnByExpFieldFixedValue) {
		this.vwVerticalRptColumnByExpFieldFixedValue = vwVerticalRptColumnByExpFieldFixedValue;
	}	
}
