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
@Table(name="ViewVerticalReportByFpyRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportByFpyRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptByFpyId")
	private Integer vwVerticalRptByFpyId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewVerticalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fpyRptId", unique=false, updatable=true)
	private FirstPassYieldReport fpyRpt;

	public ViewVerticalReportByFpyRpt(Integer vwVerticalRptByFpyId, ViewVerticalReport viewVerticalReport,
			FirstPassYieldReport fpyRpt) {
		this.vwVerticalRptByFpyId = vwVerticalRptByFpyId;
		this.viewVerticalReport = viewVerticalReport;
		this.fpyRpt = fpyRpt;
	}

	public ViewVerticalReportByFpyRpt() {
		this.vwVerticalRptByFpyId = null;
		this.viewVerticalReport = null;
		this.fpyRpt = null;
	}

	public Integer getVwVerticalRptByFpyId() {
		return vwVerticalRptByFpyId;
	}

	public void setVwVerticalRptByFpyId(Integer vwVerticalRptByFpyId) {
		this.vwVerticalRptByFpyId = vwVerticalRptByFpyId;
	}

	public ViewVerticalReport getViewVerticalReport() {
		return viewVerticalReport;
	}

	public void setViewVerticalReport(ViewVerticalReport viewVerticalReport) {
		this.viewVerticalReport = viewVerticalReport;
	}

	public FirstPassYieldReport getFpyRpt() {
		return fpyRpt;
	}

	public void setFpyRpt(FirstPassYieldReport fpyRpt) {
		this.fpyRpt = fpyRpt;
	}
	
}
