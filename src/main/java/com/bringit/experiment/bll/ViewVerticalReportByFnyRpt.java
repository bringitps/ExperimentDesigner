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
@Table(name="ViewVerticalReportByFnyRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewVerticalReportByFnyRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptByFnyId")
	private Integer vwVerticalRptByFnyId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewVerticalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fnyRptId", unique=false, updatable=true)
	private FinalPassYieldReport fnyRpt;
	
	public ViewVerticalReportByFnyRpt(Integer vwVerticalRptByFnyId, ViewVerticalReport viewVerticalReport,
			FinalPassYieldReport fnyRpt) {
		this.vwVerticalRptByFnyId = vwVerticalRptByFnyId;
		this.viewVerticalReport = viewVerticalReport;
		this.fnyRpt = fnyRpt;
	}

	public ViewVerticalReportByFnyRpt() {
		this.vwVerticalRptByFnyId = null;
		this.viewVerticalReport = null;
		this.fnyRpt = null;
	}

	public Integer getVwVerticalRptByFnyId() {
		return vwVerticalRptByFnyId;
	}

	public void setVwVerticalRptByFnyId(Integer vwVerticalRptByFnyId) {
		this.vwVerticalRptByFnyId = vwVerticalRptByFnyId;
	}

	public ViewVerticalReport getViewVerticalReport() {
		return viewVerticalReport;
	}

	public void setViewVerticalReport(ViewVerticalReport viewVerticalReport) {
		this.viewVerticalReport = viewVerticalReport;
	}

	public FinalPassYieldReport getFnyRpt() {
		return fnyRpt;
	}

	public void setFnyRpt(FinalPassYieldReport fnyRpt) {
		this.fnyRpt = fnyRpt;
	}
	
}
