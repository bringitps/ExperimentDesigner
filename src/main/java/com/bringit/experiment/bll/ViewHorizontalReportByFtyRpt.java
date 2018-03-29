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
@Table(name="ViewHorizontalReportByFtyRpt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportByFtyRpt {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptByFtyId")
	private Integer vwHorizontalRptByFtyId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalReportId", unique=false, updatable=true)
	private ViewHorizontalReport viewHorizontalReport;	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ftyRptId", unique=false, updatable=true)
	private FirstTimeYieldReport ftyRpt;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FtyKeyInfoFieldId", unique=false, updatable=true)
	private FirstTimeYieldInfoField ftyKeyInfoField;
	
	@Column(name="VwHorizontalFtyKeyIsDateTimeExpField")
	private Boolean vwHorizontalFtyKeyIsDateTimeExpField;

	@Column(name="VwHorizontalFnyKeyIsSNExpField")
	private Boolean vwHorizontalFtyKeyIsSNExpField;

	@Column(name="VwHorizontalFtyKeyIsResultExpField")
	private Boolean vwHorizontalFtyKeyIsResultExpField;

	public ViewHorizontalReportByFtyRpt(Integer vwHorizontalRptByFtyId, ViewHorizontalReport viewHorizontalReport,
			FirstTimeYieldReport ftyRpt, FirstTimeYieldInfoField ftyKeyInfoField,
			Boolean vwHorizontalFtyKeyIsDateTimeExpField, Boolean vwHorizontalFtyKeyIsSNExpField,
			Boolean vwHorizontalFtyKeyIsResultExpField) {
		this.vwHorizontalRptByFtyId = vwHorizontalRptByFtyId;
		this.viewHorizontalReport = viewHorizontalReport;
		this.ftyRpt = ftyRpt;
		this.ftyKeyInfoField = ftyKeyInfoField;
		this.vwHorizontalFtyKeyIsDateTimeExpField = vwHorizontalFtyKeyIsDateTimeExpField;
		this.vwHorizontalFtyKeyIsSNExpField = vwHorizontalFtyKeyIsSNExpField;
		this.vwHorizontalFtyKeyIsResultExpField = vwHorizontalFtyKeyIsResultExpField;
	}
	
	public ViewHorizontalReportByFtyRpt() {
		this.vwHorizontalRptByFtyId = null;
		this.viewHorizontalReport = null;
		this.ftyRpt = null;
		this.ftyKeyInfoField = null;
		this.vwHorizontalFtyKeyIsDateTimeExpField = null;
		this.vwHorizontalFtyKeyIsSNExpField = null;
		this.vwHorizontalFtyKeyIsResultExpField = null;
	}

	public Integer getVwHorizontalRptByFtyId() {
		return vwHorizontalRptByFtyId;
	}

	public void setVwHorizontalRptByFtyId(Integer vwHorizontalRptByFtyId) {
		this.vwHorizontalRptByFtyId = vwHorizontalRptByFtyId;
	}

	public ViewHorizontalReport getViewHorizontalReport() {
		return viewHorizontalReport;
	}

	public void setViewHorizontalReport(ViewHorizontalReport viewHorizontalReport) {
		this.viewHorizontalReport = viewHorizontalReport;
	}

	public FirstTimeYieldReport getFtyRpt() {
		return ftyRpt;
	}

	public void setFtyRpt(FirstTimeYieldReport ftyRpt) {
		this.ftyRpt = ftyRpt;
	}

	public FirstTimeYieldInfoField getFtyKeyInfoField() {
		return ftyKeyInfoField;
	}

	public void setFtyKeyInfoField(FirstTimeYieldInfoField ftyKeyInfoField) {
		this.ftyKeyInfoField = ftyKeyInfoField;
	}

	public Boolean getVwHorizontalFtyKeyIsDateTimeExpField() {
		return vwHorizontalFtyKeyIsDateTimeExpField;
	}

	public void setVwHorizontalFtyKeyIsDateTimeExpField(Boolean vwHorizontalFtyKeyIsDateTimeExpField) {
		this.vwHorizontalFtyKeyIsDateTimeExpField = vwHorizontalFtyKeyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalFtyKeyIsSNExpField() {
		return vwHorizontalFtyKeyIsSNExpField;
	}

	public void setVwHorizontalFtyKeyIsSNExpField(Boolean vwHorizontalFtyKeyIsSNExpField) {
		this.vwHorizontalFtyKeyIsSNExpField = vwHorizontalFtyKeyIsSNExpField;
	}

	public Boolean getVwHorizontalFtyKeyIsResultExpField() {
		return vwHorizontalFtyKeyIsResultExpField;
	}

	public void setVwHorizontalFtyKeyIsResultExpField(Boolean vwHorizontalFtyKeyIsResultExpField) {
		this.vwHorizontalFtyKeyIsResultExpField = vwHorizontalFtyKeyIsResultExpField;
	}
}
