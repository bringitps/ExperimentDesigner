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
@Table(name="ViewHorizontalReportColumnByFnyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportFilterByFnyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptColumnByFnyFieldId")
	private Integer vwHorizontalRptColumnByFnyFieldId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalRptColumnId", unique=false, updatable=true)
	private ViewHorizontalReportColumn vwHorizontalReportColumn;	
		
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FnyInfoFieldId", unique=false, updatable=true)
	private FinalPassYieldInfoField finalPassYieldInfoField;

	@Column(name="VwHorizontalRptFilterByFnyIsDateTimeExpField")
	private Boolean vwHorizontalRptFilterByFnyIsDateTimeExpField;

	@Column(name="VwHorizontalRptFilterByFnyIsSNExpField")
	private Boolean vwHorizontalRptFilterByFnySNExpField;

	@Column(name="VwHorizontalRptFilterByFnyIsResultExpField")
	private Boolean vwHorizontalRptFilterByFnyIsResultExpField;

	public ViewHorizontalReportFilterByFnyField(Integer vwHorizontalRptColumnByFnyFieldId,
			ViewHorizontalReportColumn vwHorizontalReportColumn, FinalPassYieldInfoField finalPassYieldInfoField,
			Boolean vwHorizontalRptFilterByFnyIsDateTimeExpField, Boolean vwHorizontalRptFilterByFnySNExpField,
			Boolean vwHorizontalRptFilterByFnyIsResultExpField) {
		this.vwHorizontalRptColumnByFnyFieldId = vwHorizontalRptColumnByFnyFieldId;
		this.vwHorizontalReportColumn = vwHorizontalReportColumn;
		this.finalPassYieldInfoField = finalPassYieldInfoField;
		this.vwHorizontalRptFilterByFnyIsDateTimeExpField = vwHorizontalRptFilterByFnyIsDateTimeExpField;
		this.vwHorizontalRptFilterByFnySNExpField = vwHorizontalRptFilterByFnySNExpField;
		this.vwHorizontalRptFilterByFnyIsResultExpField = vwHorizontalRptFilterByFnyIsResultExpField;
	}
	
	public ViewHorizontalReportFilterByFnyField() {
		this.vwHorizontalRptColumnByFnyFieldId = null;
		this.vwHorizontalReportColumn = null;
		this.finalPassYieldInfoField = null;
		this.vwHorizontalRptFilterByFnyIsDateTimeExpField = null;
		this.vwHorizontalRptFilterByFnySNExpField = null;
		this.vwHorizontalRptFilterByFnyIsResultExpField = null;
	}

	public Integer getVwHorizontalRptColumnByFnyFieldId() {
		return vwHorizontalRptColumnByFnyFieldId;
	}

	public void setVwHorizontalRptColumnByFnyFieldId(Integer vwHorizontalRptColumnByFnyFieldId) {
		this.vwHorizontalRptColumnByFnyFieldId = vwHorizontalRptColumnByFnyFieldId;
	}

	public ViewHorizontalReportColumn getVwHorizontalReportColumn() {
		return vwHorizontalReportColumn;
	}

	public void setVwHorizontalReportColumn(ViewHorizontalReportColumn vwHorizontalReportColumn) {
		this.vwHorizontalReportColumn = vwHorizontalReportColumn;
	}

	public FinalPassYieldInfoField getFinalPassYieldInfoField() {
		return finalPassYieldInfoField;
	}

	public void setFinalPassYieldInfoField(FinalPassYieldInfoField finalPassYieldInfoField) {
		this.finalPassYieldInfoField = finalPassYieldInfoField;
	}

	public Boolean getVwHorizontalRptFilterByFnyIsDateTimeExpField() {
		return vwHorizontalRptFilterByFnyIsDateTimeExpField;
	}

	public void setVwHorizontalRptFilterByFnyIsDateTimeExpField(Boolean vwHorizontalRptFilterByFnyIsDateTimeExpField) {
		this.vwHorizontalRptFilterByFnyIsDateTimeExpField = vwHorizontalRptFilterByFnyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalRptFilterByFnySNExpField() {
		return vwHorizontalRptFilterByFnySNExpField;
	}

	public void setVwHorizontalRptFilterByFnySNExpField(Boolean vwHorizontalRptFilterByFnySNExpField) {
		this.vwHorizontalRptFilterByFnySNExpField = vwHorizontalRptFilterByFnySNExpField;
	}

	public Boolean getVwHorizontalRptFilterByFnyIsResultExpField() {
		return vwHorizontalRptFilterByFnyIsResultExpField;
	}

	public void setVwHorizontalRptFilterByFnyIsResultExpField(Boolean vwHorizontalRptFilterByFnyIsResultExpField) {
		this.vwHorizontalRptFilterByFnyIsResultExpField = vwHorizontalRptFilterByFnyIsResultExpField;
	}
	
	
	
}
