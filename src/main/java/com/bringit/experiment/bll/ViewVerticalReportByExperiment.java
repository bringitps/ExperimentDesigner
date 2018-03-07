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
@Table(name="ViewVerticalReportByExperiment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportByExperiment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptByExperimentId")
	private Integer vwVerticalRptByExperimentId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewVerticalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExperimentId", unique=false, updatable=true)
	private Experiment experiment;

	public ViewVerticalReportByExperiment(Integer vwVerticalRptByExperimentId, ViewVerticalReport viewVerticalReport,
			Experiment experiment) {
		this.vwVerticalRptByExperimentId = vwVerticalRptByExperimentId;
		this.viewVerticalReport = viewVerticalReport;
		this.experiment = experiment;
	}

	public ViewVerticalReportByExperiment() {
		this.vwVerticalRptByExperimentId = null;
		this.viewVerticalReport = null;
		this.experiment = null;
	}

	public Integer getVwVerticalRptByExperimentId() {
		return vwVerticalRptByExperimentId;
	}

	public void setVwVerticalRptByExperimentId(Integer vwVerticalRptByExperimentId) {
		this.vwVerticalRptByExperimentId = vwVerticalRptByExperimentId;
	}

	public ViewVerticalReport getViewVerticalReport() {
		return viewVerticalReport;
	}

	public void setViewVerticalReport(ViewVerticalReport viewVerticalReport) {
		this.viewVerticalReport = viewVerticalReport;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
	
}
