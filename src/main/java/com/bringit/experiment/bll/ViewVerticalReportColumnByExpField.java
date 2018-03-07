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
    @JoinColumn(name="VwVerticalReportByExperimentId", unique=false, updatable=true)
	private ViewVerticalReportByExperiment vwVerticalReportByExperiment;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	public ViewVerticalReportColumnByExpField(Integer vwVerticalRptColumnByExpFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			ViewVerticalReportByExperiment vwVerticalReportByExperiment,
			ExperimentField experimentField) {
		this.vwVerticalRptColumnByExpFieldId = vwVerticalRptColumnByExpFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.vwVerticalReportByExperiment = vwVerticalReportByExperiment;
		this.experimentField = experimentField;
	}	
	
	public ViewVerticalReportColumnByExpField() {
		this.vwVerticalRptColumnByExpFieldId = null;
		this.vwVerticalReportColumn = null;
		this.vwVerticalReportByExperiment = null;
		this.experimentField = null;
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

	public ViewVerticalReportByExperiment getVwVerticalReportByExperiment() {
		return vwVerticalReportByExperiment;
	}

	public void setVwVerticalReportByExperiment(ViewVerticalReportByExperiment vwVerticalReportByExperiment) {
		this.vwVerticalReportByExperiment = vwVerticalReportByExperiment;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}
}
