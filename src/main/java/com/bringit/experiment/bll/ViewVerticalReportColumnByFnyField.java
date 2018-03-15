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
@Table(name="ViewVerticalReportColumnByFnyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewVerticalReportColumnByFnyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptColumnByFnyFieldId")
	private Integer vwVerticalRptColumnByFnyFieldId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalRptColumnId", unique=false, updatable=true)
	private ViewVerticalReportColumn vwVerticalReportColumn;	
		
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FnyInfoFieldId", unique=false, updatable=true)
	private FinalPassYieldInfoField finalPassYieldInfoField;

	@Column(name="VwVerticalRptFilterByFnyIsDateTimeExpField")
	private Boolean vwVerticalRptFilterByFnyIsDateTimeExpField;

	@Column(name="VwVerticalRptFilterByFnyIsSNExpField")
	private Boolean vwVerticalRptFilterByFnySNExpField;

	@Column(name="VwVerticalRptFilterByFnyIsResultExpField")
	private Boolean vwVerticalRptFilterByFnyIsResultExpField;
	
	
	public ViewVerticalReportColumnByFnyField(Integer vwVerticalRptColumnByFnyFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			FinalPassYieldInfoField finalPassYieldInfoField,
			Boolean vwVerticalRptFilterByFnyIsDateTimeExpField,
			Boolean vwVerticalRptFilterByFnySNExpField,
			Boolean vwVerticalRptFilterByFnyIsResultExpField) {
		this.vwVerticalRptColumnByFnyFieldId = vwVerticalRptColumnByFnyFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.finalPassYieldInfoField = finalPassYieldInfoField;
		this.vwVerticalRptFilterByFnyIsDateTimeExpField = vwVerticalRptFilterByFnyIsDateTimeExpField;
		this.vwVerticalRptFilterByFnySNExpField = vwVerticalRptFilterByFnySNExpField;
		this.vwVerticalRptFilterByFnyIsResultExpField = vwVerticalRptFilterByFnyIsResultExpField;
	}	

	public ViewVerticalReportColumnByFnyField() {
		this.vwVerticalRptColumnByFnyFieldId = null;
		this.vwVerticalReportColumn = null;
		this.finalPassYieldInfoField = null;
		this.vwVerticalRptFilterByFnyIsDateTimeExpField = null;
		this.vwVerticalRptFilterByFnySNExpField = null;
		this.vwVerticalRptFilterByFnyIsResultExpField = null;
	}

	public Integer getVwVerticalRptColumnByFnyFieldId() {
		return vwVerticalRptColumnByFnyFieldId;
	}

	public void setVwVerticalRptColumnByFnyFieldId(Integer vwVerticalRptColumnByFnyFieldId) {
		this.vwVerticalRptColumnByFnyFieldId = vwVerticalRptColumnByFnyFieldId;
	}

	public ViewVerticalReportColumn getVwVerticalReportColumn() {
		return vwVerticalReportColumn;
	}

	public void setVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
		this.vwVerticalReportColumn = vwVerticalReportColumn;
	}

	public FinalPassYieldInfoField getFinalPassYieldInfoField() {
		return finalPassYieldInfoField;
	}

	public void setFinalPassYieldInfoField(FinalPassYieldInfoField finalPassYieldInfoField) {
		this.finalPassYieldInfoField = finalPassYieldInfoField;
	}

	public Boolean getVwVerticalRptFilterByFnyIsDateTimeExpField() {
		return vwVerticalRptFilterByFnyIsDateTimeExpField;
	}

	public void setVwVerticalRptFilterByFnyIsDateTimeExpField(Boolean vwVerticalRptFilterByFnyIsDateTimeExpField) {
		this.vwVerticalRptFilterByFnyIsDateTimeExpField = vwVerticalRptFilterByFnyIsDateTimeExpField;
	}

	public Boolean getVwVerticalRptFilterByFnySNExpField() {
		return vwVerticalRptFilterByFnySNExpField;
	}

	public void setVwVerticalRptFilterByFnySNExpField(Boolean vwVerticalRptFilterByFnySNExpField) {
		this.vwVerticalRptFilterByFnySNExpField = vwVerticalRptFilterByFnySNExpField;
	}

	public Boolean getVwVerticalRptFilterByFnyIsResultExpField() {
		return vwVerticalRptFilterByFnyIsResultExpField;
	}

	public void setVwVerticalRptFilterByFnyIsResultExpField(Boolean vwVerticalRptFilterByFnyIsResultExpField) {
		this.vwVerticalRptFilterByFnyIsResultExpField = vwVerticalRptFilterByFnyIsResultExpField;
	}
}
