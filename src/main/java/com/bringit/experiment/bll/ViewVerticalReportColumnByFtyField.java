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
@Table(name="ViewVerticalReportColumnByFtyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportColumnByFtyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptColumnByFtyFieldId")
	private Integer vwVerticalRptColumnByFtyFieldId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalRptColumnId", unique=false, updatable=true)
	private ViewVerticalReportColumn vwVerticalReportColumn;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FtyInfoFieldId", unique=false, updatable=true)
	private FirstTimeYieldInfoField firstTimeYieldInfoField;


	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ftyRptId", unique=false, updatable=true)
	private FirstTimeYieldReport ftyRpt;
	
	@Column(name="VwVerticalRptColumnByFtyIsDateTimeExpField")
	private Boolean VwVerticalRptColumnByFtyIsDateTimeExpField;

	@Column(name="VwVerticalRptColumnByFtyIsSNExpField")
	private Boolean VwVerticalRptColumnByFtyIsSNExpField;

	@Column(name="VwVerticalRptColumnByFtyIsResultExpField")
	private Boolean VwVerticalRptColumnByFtyIsResultExpField;

	@Column(name="VwVerticalRptColumnByFtyFieldIsFixedValue")
	private Boolean vwVerticalRptColumnByFtyFieldIsFixedValue;
	
	@Column(name="VwVerticalRptColumnByFtyFieldFixedValue")
	private String vwVerticalRptColumnByFtyFieldFixedValue;
	
	public ViewVerticalReportColumnByFtyField(Integer vwVerticalRptColumnByFtyFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			FirstTimeYieldInfoField firstTimeYieldInfoField,
			FirstTimeYieldReport ftyRpt,
			Boolean VwVerticalRptColumnByFtyIsDateTimeExpField,
			Boolean VwVerticalRptColumnByFtyIsSNExpField,
			Boolean VwVerticalRptColumnByFtyIsResultExpField, 
			Boolean vwVerticalRptColumnByFtyFieldIsFixedValue,
			String vwVerticalRptColumnByFtyFieldFixedValue) {
		this.vwVerticalRptColumnByFtyFieldId = vwVerticalRptColumnByFtyFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.firstTimeYieldInfoField = firstTimeYieldInfoField;
		this.ftyRpt = ftyRpt;
		this.VwVerticalRptColumnByFtyIsDateTimeExpField = VwVerticalRptColumnByFtyIsDateTimeExpField;
		this.VwVerticalRptColumnByFtyIsSNExpField = VwVerticalRptColumnByFtyIsSNExpField;
		this.VwVerticalRptColumnByFtyIsResultExpField = VwVerticalRptColumnByFtyIsResultExpField;
		this.vwVerticalRptColumnByFtyFieldIsFixedValue = vwVerticalRptColumnByFtyFieldIsFixedValue;
		this.vwVerticalRptColumnByFtyFieldFixedValue = vwVerticalRptColumnByFtyFieldFixedValue;
	}	

	public ViewVerticalReportColumnByFtyField() {
		this.vwVerticalRptColumnByFtyFieldId = null;
		this.vwVerticalReportColumn = null;
		this.firstTimeYieldInfoField = null;
		this.ftyRpt = null;
		this.VwVerticalRptColumnByFtyIsDateTimeExpField = null;
		this.VwVerticalRptColumnByFtyIsSNExpField = null;
		this.VwVerticalRptColumnByFtyIsResultExpField = null;
		this.vwVerticalRptColumnByFtyFieldIsFixedValue = null;
		this.vwVerticalRptColumnByFtyFieldFixedValue = null;
	}

	public Integer getVwVerticalRptColumnByFtyFieldId() {
		return vwVerticalRptColumnByFtyFieldId;
	}

	public void setVwVerticalRptColumnByFtyFieldId(Integer vwVerticalRptColumnByFtyFieldId) {
		this.vwVerticalRptColumnByFtyFieldId = vwVerticalRptColumnByFtyFieldId;
	}

	public ViewVerticalReportColumn getVwVerticalReportColumn() {
		return vwVerticalReportColumn;
	}

	public void setVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
		this.vwVerticalReportColumn = vwVerticalReportColumn;
	}
	
	public FirstTimeYieldInfoField getFirstTimeYieldInfoField() {
		return firstTimeYieldInfoField;
	}

	public void setFirstTimeYieldInfoField(FirstTimeYieldInfoField firstTimeYieldInfoField) {
		this.firstTimeYieldInfoField = firstTimeYieldInfoField;
	}

	public FirstTimeYieldReport getFtyRpt() {
		return ftyRpt;
	}

	public void setFtyRpt(FirstTimeYieldReport ftyRpt) {
		this.ftyRpt = ftyRpt;
	}

	public Boolean getVwVerticalRptColumnByFtyIsDateTimeExpField() {
		return VwVerticalRptColumnByFtyIsDateTimeExpField;
	}

	public void setVwVerticalRptColumnByFtyIsDateTimeExpField(Boolean VwVerticalRptColumnByFtyIsDateTimeExpField) {
		this.VwVerticalRptColumnByFtyIsDateTimeExpField = VwVerticalRptColumnByFtyIsDateTimeExpField;
	}

	public Boolean getVwVerticalRptColumnByFtyIsSNExpField() {
		return VwVerticalRptColumnByFtyIsSNExpField;
	}

	public void setVwVerticalRptColumnByFtyIsSNExpField(Boolean VwVerticalRptColumnByFtyIsSNExpField) {
		this.VwVerticalRptColumnByFtyIsSNExpField = VwVerticalRptColumnByFtyIsSNExpField;
	}

	public Boolean getVwVerticalRptColumnByFtyIsResultExpField() {
		return VwVerticalRptColumnByFtyIsResultExpField;
	}

	public void setVwVerticalRptColumnByFtyIsResultExpField(Boolean VwVerticalRptColumnByFtyIsResultExpField) {
		this.VwVerticalRptColumnByFtyIsResultExpField = VwVerticalRptColumnByFtyIsResultExpField;
	}

	public Boolean getVwVerticalRptColumnByFtyFieldIsFixedValue() {
		return vwVerticalRptColumnByFtyFieldIsFixedValue;
	}

	public void setVwVerticalRptColumnByFtyFieldIsFixedValue(Boolean vwVerticalRptColumnByFtyFieldIsFixedValue) {
		this.vwVerticalRptColumnByFtyFieldIsFixedValue = vwVerticalRptColumnByFtyFieldIsFixedValue;
	}

	public String getVwVerticalRptColumnByFtyFieldFixedValue() {
		return vwVerticalRptColumnByFtyFieldFixedValue;
	}

	public void setVwVerticalRptColumnByFtyFieldFixedValue(String vwVerticalRptColumnByFtyFieldFixedValue) {
		this.vwVerticalRptColumnByFtyFieldFixedValue = vwVerticalRptColumnByFtyFieldFixedValue;
	}
	

}