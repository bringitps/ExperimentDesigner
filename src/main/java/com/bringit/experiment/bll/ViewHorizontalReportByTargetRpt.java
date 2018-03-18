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
@Table(name="ViewHorizontalReportByTargetRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportByTargetRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptByTargetRptId")
	private Integer vwHorizontalRptByTargetRptId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewHorizontalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetRptId", unique=false, updatable=true)
	private TargetReport targetRpt;

	public ViewHorizontalReportByTargetRpt(Integer vwHorizontalRptByTargetRptId,
			ViewVerticalReport viewHorizontalReport, TargetReport targetRpt) {
		this.vwHorizontalRptByTargetRptId = vwHorizontalRptByTargetRptId;
		this.viewHorizontalReport = viewHorizontalReport;
		this.targetRpt = targetRpt;
	}
	
	public ViewHorizontalReportByTargetRpt() {
		this.vwHorizontalRptByTargetRptId = null;
		this.viewHorizontalReport = null;
		this.targetRpt = null;
	}

	public Integer getVwHorizontalRptByTargetRptId() {
		return vwHorizontalRptByTargetRptId;
	}

	public void setVwHorizontalRptByTargetRptId(Integer vwHorizontalRptByTargetRptId) {
		this.vwHorizontalRptByTargetRptId = vwHorizontalRptByTargetRptId;
	}

	public ViewVerticalReport getViewHorizontalReport() {
		return viewHorizontalReport;
	}

	public void setViewHorizontalReport(ViewVerticalReport viewHorizontalReport) {
		this.viewHorizontalReport = viewHorizontalReport;
	}

	public TargetReport getTargetRpt() {
		return targetRpt;
	}

	public void setTargetRpt(TargetReport targetRpt) {
		this.targetRpt = targetRpt;
	}
}
