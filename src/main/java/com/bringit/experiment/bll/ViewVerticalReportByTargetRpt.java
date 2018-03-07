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
@Table(name="ViewVerticalReportByTargetRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportByTargetRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptByTargetRptId")
	private Integer vwVerticalRptByTargetRptId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewVerticalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetRptId", unique=false, updatable=true)
	private TargetReport targetRpt;

	public ViewVerticalReportByTargetRpt(Integer vwVerticalRptByTargetRptId, ViewVerticalReport viewVerticalReport,
			TargetReport targetRpt) {
		this.vwVerticalRptByTargetRptId = vwVerticalRptByTargetRptId;
		this.viewVerticalReport = viewVerticalReport;
		this.targetRpt = targetRpt;
	}

	public ViewVerticalReportByTargetRpt() {
		this.vwVerticalRptByTargetRptId = null;
		this.viewVerticalReport = null;
		this.targetRpt = null;
	}

	public Integer getVwVerticalRptByTargetRptId() {
		return vwVerticalRptByTargetRptId;
	}

	public void setVwVerticalRptByTargetRptId(Integer vwVerticalRptByTargetRptId) {
		this.vwVerticalRptByTargetRptId = vwVerticalRptByTargetRptId;
	}

	public ViewVerticalReport getViewVerticalReport() {
		return viewVerticalReport;
	}

	public void setViewVerticalReport(ViewVerticalReport viewVerticalReport) {
		this.viewVerticalReport = viewVerticalReport;
	}

	public TargetReport getTargetRpt() {
		return targetRpt;
	}

	public void setTargetRpt(TargetReport targetRpt) {
		this.targetRpt = targetRpt;
	}
}
