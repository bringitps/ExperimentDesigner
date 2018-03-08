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
@Table(name="ViewVerticalReportColumnByFpyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportColumnByFpyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptColumnByFpyFieldId")
	private Integer vwVerticalRptColumnByFpyFieldId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalRptColumnId", unique=false, updatable=true)
	private ViewVerticalReportColumn vwVerticalReportColumn;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportByFpyRptId", unique=false, updatable=true)
	private ViewVerticalReportByFpyRpt vwVerticalFpyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FpyInfoFieldId", unique=false, updatable=true)
	private FirstPassYieldInfoField firstPassYieldInfoField;

	@Column(name="VwVerticalRptFilterByFpyIsDateTimeExpField")
	private Boolean vwVerticalRptFilterByFpyIsDateTimeExpField;

	@Column(name="VwVerticalRptFilterByFpyIsSNExpField")
	private Boolean vwVerticalRptFilterByFpySNExpField;

	@Column(name="VwVerticalRptFilterByFpyIsResultExpField")
	private Boolean vwVerticalRptFilterByFpyIsResultExpField;
	
	
	public ViewVerticalReportColumnByFpyField(Integer vwVerticalRptColumnByFpyFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			ViewVerticalReportByFpyRpt vwVerticalRptColumnByFpyFieldVwVerticalFpyRpt,
			FirstPassYieldInfoField firstPassYieldInfoField,
			Boolean vwVerticalRptFilterByFpyIsDateTimeExpField,
			Boolean vwVerticalRptFilterByFpySNExpField,
			Boolean vwVerticalRptFilterByFpyIsResultExpField) {
		this.vwVerticalRptColumnByFpyFieldId = vwVerticalRptColumnByFpyFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.vwVerticalFpyRpt = vwVerticalRptColumnByFpyFieldVwVerticalFpyRpt;
		this.firstPassYieldInfoField = firstPassYieldInfoField;
		this.vwVerticalRptFilterByFpyIsDateTimeExpField = vwVerticalRptFilterByFpyIsDateTimeExpField;
		this.vwVerticalRptFilterByFpySNExpField = vwVerticalRptFilterByFpySNExpField;
		this.vwVerticalRptFilterByFpyIsResultExpField = vwVerticalRptFilterByFpyIsResultExpField;
	}	

	public ViewVerticalReportColumnByFpyField() {
		this.vwVerticalRptColumnByFpyFieldId = null;
		this.vwVerticalReportColumn = null;
		this.vwVerticalFpyRpt = null;
		this.firstPassYieldInfoField = null;
		this.vwVerticalRptFilterByFpyIsDateTimeExpField = null;
		this.vwVerticalRptFilterByFpySNExpField = null;
		this.vwVerticalRptFilterByFpyIsResultExpField = null;
	}

	public Integer getVwVerticalRptColumnByFpyFieldId() {
		return vwVerticalRptColumnByFpyFieldId;
	}

	public void setVwVerticalRptColumnByFpyFieldId(Integer vwVerticalRptColumnByFpyFieldId) {
		this.vwVerticalRptColumnByFpyFieldId = vwVerticalRptColumnByFpyFieldId;
	}

	public ViewVerticalReportColumn getVwVerticalReportColumn() {
		return vwVerticalReportColumn;
	}

	public void setVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
		this.vwVerticalReportColumn = vwVerticalReportColumn;
	}

	public ViewVerticalReportByFpyRpt getVwVerticalFpyRpt() {
		return vwVerticalFpyRpt;
	}

	public void setVwVerticalFpyRpt(ViewVerticalReportByFpyRpt vwVerticalFpyRpt) {
		this.vwVerticalFpyRpt = vwVerticalFpyRpt;
	}

	public FirstPassYieldInfoField getFirstPassYieldInfoField() {
		return firstPassYieldInfoField;
	}

	public void setFirstPassYieldInfoField(FirstPassYieldInfoField firstPassYieldInfoField) {
		this.firstPassYieldInfoField = firstPassYieldInfoField;
	}

	public Boolean getVwVerticalRptFilterByFpyIsDateTimeExpField() {
		return vwVerticalRptFilterByFpyIsDateTimeExpField;
	}

	public void setVwVerticalRptFilterByFpyIsDateTimeExpField(Boolean vwVerticalRptFilterByFpyIsDateTimeExpField) {
		this.vwVerticalRptFilterByFpyIsDateTimeExpField = vwVerticalRptFilterByFpyIsDateTimeExpField;
	}

	public Boolean getVwVerticalRptFilterByFpySNExpField() {
		return vwVerticalRptFilterByFpySNExpField;
	}

	public void setVwVerticalRptFilterByFpySNExpField(Boolean vwVerticalRptFilterByFpySNExpField) {
		this.vwVerticalRptFilterByFpySNExpField = vwVerticalRptFilterByFpySNExpField;
	}

	public Boolean getVwVerticalRptFilterByFpyIsResultExpField() {
		return vwVerticalRptFilterByFpyIsResultExpField;
	}

	public void setVwVerticalRptFilterByFpyIsResultExpField(Boolean vwVerticalRptFilterByFpyIsResultExpField) {
		this.vwVerticalRptFilterByFpyIsResultExpField = vwVerticalRptFilterByFpyIsResultExpField;
	}
	
}
