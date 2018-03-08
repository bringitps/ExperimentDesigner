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
@Table(name="ViewVerticalReportByFtyRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportByFtyRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptByFtyId")
	private Integer vwVerticalRptByFtyId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewVerticalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ftyRptId", unique=false, updatable=true)
	private FirstTimeYieldReport ftyRpt;

	public ViewVerticalReportByFtyRpt(Integer vwVerticalRptByFtyId, ViewVerticalReport viewVerticalReport,
			FirstTimeYieldReport ftyRpt) {
		this.vwVerticalRptByFtyId = vwVerticalRptByFtyId;
		this.viewVerticalReport = viewVerticalReport;
		this.ftyRpt = ftyRpt;
	}
	
	public ViewVerticalReportByFtyRpt() {
		this.vwVerticalRptByFtyId = null;
		this.viewVerticalReport = null;
		this.ftyRpt = null;
	}

	public Integer getVwVerticalRptByFtyId() {
		return vwVerticalRptByFtyId;
	}

	public void setVwVerticalRptByFtyId(Integer vwVerticalRptByFtyId) {
		this.vwVerticalRptByFtyId = vwVerticalRptByFtyId;
	}

	public ViewVerticalReport getViewVerticalReport() {
		return viewVerticalReport;
	}

	public void setViewVerticalReport(ViewVerticalReport viewVerticalReport) {
		this.viewVerticalReport = viewVerticalReport;
	}

	public FirstTimeYieldReport getFtyRpt() {
		return ftyRpt;
	}

	public void setFtyRpt(FirstTimeYieldReport firstTimeYieldReport) {
		this.ftyRpt = firstTimeYieldReport;
	}
}
