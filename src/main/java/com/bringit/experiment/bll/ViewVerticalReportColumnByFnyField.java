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

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fnyRptId", unique=false, updatable=true)
	private FinalPassYieldReport fnyRpt;

	@Column(name="VwVerticalRptColumnByFnyIsDateTimeExpField")
	private Boolean VwVerticalRptColumnByFnyIsDateTimeExpField;

	@Column(name="VwVerticalRptColumnByFnyIsSNExpField")
	private Boolean VwVerticalRptColumnByFnyIsSNExpField;

	@Column(name="VwVerticalRptColumnByFnyIsResultExpField")
	private Boolean VwVerticalRptColumnByFnyIsResultExpField;

	@Column(name="VwVerticalRptColumnByFnyFieldIsFixedValue")
	private Boolean vwVerticalRptColumnByFnyFieldIsFixedValue;
	
	@Column(name="VwVerticalRptColumnByFnyFieldFixedValue")
	private String vwVerticalRptColumnByFnyFieldFixedValue;
	
	
	public ViewVerticalReportColumnByFnyField(Integer vwVerticalRptColumnByFnyFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			FinalPassYieldInfoField finalPassYieldInfoField,
			FinalPassYieldReport fnyRpt,
			Boolean VwVerticalRptColumnByFnyIsDateTimeExpField,
			Boolean VwVerticalRptColumnByFnyIsSNExpField,
			Boolean VwVerticalRptColumnByFnyIsResultExpField, 
			Boolean vwVerticalRptColumnByFnyFieldIsFixedValue,
			String vwVerticalRptColumnByFnyFieldFixedValue) {
		this.vwVerticalRptColumnByFnyFieldId = vwVerticalRptColumnByFnyFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.finalPassYieldInfoField = finalPassYieldInfoField;
		this.fnyRpt = fnyRpt;
		this.VwVerticalRptColumnByFnyIsDateTimeExpField = VwVerticalRptColumnByFnyIsDateTimeExpField;
		this.VwVerticalRptColumnByFnyIsSNExpField = VwVerticalRptColumnByFnyIsSNExpField;
		this.VwVerticalRptColumnByFnyIsResultExpField = VwVerticalRptColumnByFnyIsResultExpField;
		this.vwVerticalRptColumnByFnyFieldIsFixedValue = vwVerticalRptColumnByFnyFieldIsFixedValue;
		this.vwVerticalRptColumnByFnyFieldFixedValue = vwVerticalRptColumnByFnyFieldFixedValue;
	}	

	public ViewVerticalReportColumnByFnyField() {
		this.vwVerticalRptColumnByFnyFieldId = null;
		this.vwVerticalReportColumn = null;
		this.finalPassYieldInfoField = null;
		this.fnyRpt = null;
		this.VwVerticalRptColumnByFnyIsDateTimeExpField = null;
		this.VwVerticalRptColumnByFnyIsSNExpField = null;
		this.VwVerticalRptColumnByFnyIsResultExpField = null;
		this.vwVerticalRptColumnByFnyFieldIsFixedValue = null;
		this.vwVerticalRptColumnByFnyFieldFixedValue = null;
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
	
	public FinalPassYieldReport getFnyRpt() {
		return fnyRpt;
	}

	public void setFnyRpt(FinalPassYieldReport fnyRpt) {
		this.fnyRpt = fnyRpt;
	}

	public Boolean getVwVerticalRptColumnByFnyIsDateTimeExpField() {
		return VwVerticalRptColumnByFnyIsDateTimeExpField;
	}

	public void setVwVerticalRptColumnByFnyIsDateTimeExpField(Boolean VwVerticalRptColumnByFnyIsDateTimeExpField) {
		this.VwVerticalRptColumnByFnyIsDateTimeExpField = VwVerticalRptColumnByFnyIsDateTimeExpField;
	}

	public Boolean getVwVerticalRptColumnByFnyIsSNExpField() {
		return VwVerticalRptColumnByFnyIsSNExpField;
	}

	public void setVwVerticalRptColumnByFnyIsSNExpField(Boolean VwVerticalRptColumnByFnyIsSNExpField) {
		this.VwVerticalRptColumnByFnyIsSNExpField = VwVerticalRptColumnByFnyIsSNExpField;
	}

	public Boolean getVwVerticalRptColumnByFnyIsResultExpField() {
		return VwVerticalRptColumnByFnyIsResultExpField;
	}

	public void setVwVerticalRptColumnByFnyIsResultExpField(Boolean VwVerticalRptColumnByFnyIsResultExpField) {
		this.VwVerticalRptColumnByFnyIsResultExpField = VwVerticalRptColumnByFnyIsResultExpField;
	}

	public Boolean getVwVerticalRptColumnByFnyFieldIsFixedValue() {
		return vwVerticalRptColumnByFnyFieldIsFixedValue;
	}

	public void setVwVerticalRptColumnByFnyFieldIsFixedValue(Boolean vwVerticalRptColumnByFnyFieldIsFixedValue) {
		this.vwVerticalRptColumnByFnyFieldIsFixedValue = vwVerticalRptColumnByFnyFieldIsFixedValue;
	}

	public String getVwVerticalRptColumnByFnyFieldFixedValue() {
		return vwVerticalRptColumnByFnyFieldFixedValue;
	}

	public void setVwVerticalRptColumnByFnyFieldFixedValue(String vwVerticalRptColumnByFnyFieldFixedValue) {
		this.vwVerticalRptColumnByFnyFieldFixedValue = vwVerticalRptColumnByFnyFieldFixedValue;
	}
}
