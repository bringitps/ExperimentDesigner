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
@Table(name="ViewHorizontalReportByFpyRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportByFpyRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptByFpyId")
	private Integer vwHorizontalRptByFpyId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewHorizontalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fpyRptId", unique=false, updatable=true)
	private FirstPassYieldReport fpyRpt;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FpyKeyInfoFieldId", unique=false, updatable=true)
	private FirstPassYieldInfoField fpyKeyInfoField;
	
	@Column(name="VwHorizontalFpyKeyIsDateTimeExpField")
	private Boolean vwHorizontalFpyKeyIsDateTimeExpField;

	@Column(name="VwHorizontalFpyKeyIsSNExpField")
	private Boolean vwHorizontalFpyKeyIsSNExpField;

	@Column(name="VwHorizontalFtyKeyIsResultExpField")
	private Boolean vwHorizontalFtyKeyIsResultExpField;

	public ViewHorizontalReportByFpyRpt(Integer vwHorizontalRptByFpyId, ViewVerticalReport viewHorizontalReport,
			FirstPassYieldReport fpyRpt, FirstPassYieldInfoField fpyKeyInfoField,
			Boolean vwHorizontalFpyKeyIsDateTimeExpField, Boolean vwHorizontalFpyKeyIsSNExpField,
			Boolean vwHorizontalFtyKeyIsResultExpField) {
		this.vwHorizontalRptByFpyId = vwHorizontalRptByFpyId;
		this.viewHorizontalReport = viewHorizontalReport;
		this.fpyRpt = fpyRpt;
		this.fpyKeyInfoField = fpyKeyInfoField;
		this.vwHorizontalFpyKeyIsDateTimeExpField = vwHorizontalFpyKeyIsDateTimeExpField;
		this.vwHorizontalFpyKeyIsSNExpField = vwHorizontalFpyKeyIsSNExpField;
		this.vwHorizontalFtyKeyIsResultExpField = vwHorizontalFtyKeyIsResultExpField;
	}
	
	public ViewHorizontalReportByFpyRpt() {
		this.vwHorizontalRptByFpyId = null;
		this.viewHorizontalReport = null;
		this.fpyRpt = null;
		this.fpyKeyInfoField = null;
		this.vwHorizontalFpyKeyIsDateTimeExpField = null;
		this.vwHorizontalFpyKeyIsSNExpField = null;
		this.vwHorizontalFtyKeyIsResultExpField = null;
	}

	public Integer getVwHorizontalRptByFpyId() {
		return vwHorizontalRptByFpyId;
	}

	public void setVwHorizontalRptByFpyId(Integer vwHorizontalRptByFpyId) {
		this.vwHorizontalRptByFpyId = vwHorizontalRptByFpyId;
	}

	public ViewVerticalReport getViewHorizontalReport() {
		return viewHorizontalReport;
	}

	public void setViewHorizontalReport(ViewVerticalReport viewHorizontalReport) {
		this.viewHorizontalReport = viewHorizontalReport;
	}

	public FirstPassYieldReport getFpyRpt() {
		return fpyRpt;
	}

	public void setFpyRpt(FirstPassYieldReport fpyRpt) {
		this.fpyRpt = fpyRpt;
	}

	public FirstPassYieldInfoField getFpyKeyInfoField() {
		return fpyKeyInfoField;
	}

	public void setFpyKeyInfoField(FirstPassYieldInfoField fpyKeyInfoField) {
		this.fpyKeyInfoField = fpyKeyInfoField;
	}

	public Boolean getVwHorizontalFpyKeyIsDateTimeExpField() {
		return vwHorizontalFpyKeyIsDateTimeExpField;
	}

	public void setVwHorizontalFpyKeyIsDateTimeExpField(Boolean vwHorizontalFpyKeyIsDateTimeExpField) {
		this.vwHorizontalFpyKeyIsDateTimeExpField = vwHorizontalFpyKeyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalFpyKeyIsSNExpField() {
		return vwHorizontalFpyKeyIsSNExpField;
	}

	public void setVwHorizontalFpyKeyIsSNExpField(Boolean vwHorizontalFpyKeyIsSNExpField) {
		this.vwHorizontalFpyKeyIsSNExpField = vwHorizontalFpyKeyIsSNExpField;
	}

	public Boolean getVwHorizontalFtyKeyIsResultExpField() {
		return vwHorizontalFtyKeyIsResultExpField;
	}

	public void setVwHorizontalFtyKeyIsResultExpField(Boolean vwHorizontalFtyKeyIsResultExpField) {
		this.vwHorizontalFtyKeyIsResultExpField = vwHorizontalFtyKeyIsResultExpField;
	}
	
	
}
