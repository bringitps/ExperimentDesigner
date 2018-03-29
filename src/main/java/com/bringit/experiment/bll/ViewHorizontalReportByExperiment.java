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
@Table(name="ViewHorizontalReportByExperiment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportByExperiment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptByExperimentId")
	private Integer vwHorizontalRptByExperimentId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalReportId", unique=false, updatable=true)
	private ViewHorizontalReport viewHorizontalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExperimentId", unique=false, updatable=true)
	private Experiment experiment;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExperimentKeyFieldId", unique=false, updatable=true)
	private ExperimentField expKeyField;

	public ViewHorizontalReportByExperiment(Integer vwHorizontalRptByExperimentId,
			ViewHorizontalReport viewHorizontalReport, Experiment experiment, ExperimentField expKeyField) {
		this.vwHorizontalRptByExperimentId = vwHorizontalRptByExperimentId;
		this.viewHorizontalReport = viewHorizontalReport;
		this.experiment = experiment;
		this.expKeyField = expKeyField;
	}
	
	public ViewHorizontalReportByExperiment() {
		this.vwHorizontalRptByExperimentId = null;
		this.viewHorizontalReport = null;
		this.experiment = null;
		this.expKeyField = null;
	}

	public Integer getVwHorizontalRptByExperimentId() {
		return vwHorizontalRptByExperimentId;
	}

	public void setVwHorizontalRptByExperimentId(Integer vwHorizontalRptByExperimentId) {
		this.vwHorizontalRptByExperimentId = vwHorizontalRptByExperimentId;
	}

	public ViewHorizontalReport getViewHorizontalReport() {
		return viewHorizontalReport;
	}

	public void setViewHorizontalReport(ViewHorizontalReport viewHorizontalReport) {
		this.viewHorizontalReport = viewHorizontalReport;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public ExperimentField getExpKeyField() {
		return expKeyField;
	}

	public void setExpKeyField(ExperimentField expKeyField) {
		this.expKeyField = expKeyField;
	}	
}
