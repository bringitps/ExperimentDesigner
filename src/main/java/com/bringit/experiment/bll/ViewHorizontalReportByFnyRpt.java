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
@Table(name="ViewHorizontalReportByFnyRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportByFnyRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptByFnyId")
	private Integer vwHorizontalRptByFnyId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalReportId", unique=false, updatable=true)
	private ViewHorizontalReport viewHorizontalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fnyRptId", unique=false, updatable=true)
	private FinalPassYieldReport fnyRpt;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FnyKeyInfoFieldId", unique=false, updatable=true)
	private FinalPassYieldInfoField fnyKeyInfoField;
	
	@Column(name="VwHorizontalFnyKeyIsDateTimeExpField")
	private Boolean vwHorizontalFnyKeyIsDateTimeExpField;

	@Column(name="VwHorizontalFnyKeyIsSNExpField")
	private Boolean vwHorizontalFnyKeyIsSNExpField;

	@Column(name="VwHorizontalFnyKeyIsResultExpField")
	private Boolean vwHorizontalFnyKeyIsResultExpField;

	public ViewHorizontalReportByFnyRpt(Integer vwHorizontalRptByFnyId, ViewHorizontalReport viewHorizontalReport,
			FinalPassYieldReport fnyRpt, FinalPassYieldInfoField fnyKeyInfoField,
			Boolean vwHorizontalFnyKeyIsDateTimeExpField, Boolean vwHorizontalFnyKeyIsSNExpField,
			Boolean vwHorizontalFnyKeyIsResultExpField) {
		this.vwHorizontalRptByFnyId = vwHorizontalRptByFnyId;
		this.viewHorizontalReport = viewHorizontalReport;
		this.fnyRpt = fnyRpt;
		this.fnyKeyInfoField = fnyKeyInfoField;
		this.vwHorizontalFnyKeyIsDateTimeExpField = vwHorizontalFnyKeyIsDateTimeExpField;
		this.vwHorizontalFnyKeyIsSNExpField = vwHorizontalFnyKeyIsSNExpField;
		this.vwHorizontalFnyKeyIsResultExpField = vwHorizontalFnyKeyIsResultExpField;
	}

	public ViewHorizontalReportByFnyRpt() {
		this.vwHorizontalRptByFnyId = null;
		this.viewHorizontalReport = null;
		this.fnyRpt = null;
		this.fnyKeyInfoField = null;
		this.vwHorizontalFnyKeyIsDateTimeExpField = null;
		this.vwHorizontalFnyKeyIsSNExpField = null;
		this.vwHorizontalFnyKeyIsResultExpField = null;
	}

	public Integer getVwHorizontalRptByFnyId() {
		return vwHorizontalRptByFnyId;
	}

	public void setVwHorizontalRptByFnyId(Integer vwHorizontalRptByFnyId) {
		this.vwHorizontalRptByFnyId = vwHorizontalRptByFnyId;
	}

	public ViewHorizontalReport getViewHorizontalReport() {
		return viewHorizontalReport;
	}

	public void setViewHorizontalReport(ViewHorizontalReport viewHorizontalReport) {
		this.viewHorizontalReport = viewHorizontalReport;
	}

	public FinalPassYieldReport getFnyRpt() {
		return fnyRpt;
	}

	public void setFnyRpt(FinalPassYieldReport fnyRpt) {
		this.fnyRpt = fnyRpt;
	}

	public FinalPassYieldInfoField getFnyKeyInfoField() {
		return fnyKeyInfoField;
	}

	public void setFnyKeyInfoField(FinalPassYieldInfoField fnyKeyInfoField) {
		this.fnyKeyInfoField = fnyKeyInfoField;
	}

	public Boolean getVwHorizontalFnyKeyIsDateTimeExpField() {
		return vwHorizontalFnyKeyIsDateTimeExpField;
	}

	public void setVwHorizontalFnyKeyIsDateTimeExpField(Boolean vwHorizontalFnyKeyIsDateTimeExpField) {
		this.vwHorizontalFnyKeyIsDateTimeExpField = vwHorizontalFnyKeyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalFnyKeyIsSNExpField() {
		return vwHorizontalFnyKeyIsSNExpField;
	}

	public void setVwHorizontalFnyKeyIsSNExpField(Boolean vwHorizontalFnyKeyIsSNExpField) {
		this.vwHorizontalFnyKeyIsSNExpField = vwHorizontalFnyKeyIsSNExpField;
	}

	public Boolean getVwHorizontalFnyKeyIsResultExpField() {
		return vwHorizontalFnyKeyIsResultExpField;
	}

	public void setVwHorizontalFnyKeyIsResultExpField(Boolean vwHorizontalFnyKeyIsResultExpField) {
		this.vwHorizontalFnyKeyIsResultExpField = vwHorizontalFnyKeyIsResultExpField;
	}
	
	
}
