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

	@Column(name="VwVerticalRptFilterByFtyIsDateTimeExpField")
	private Boolean vwVerticalRptFilterByFtyIsDateTimeExpField;

	@Column(name="VwVerticalRptFilterByFtyIsSNExpField")
	private Boolean vwVerticalRptFilterByFtySNExpField;

	@Column(name="VwVerticalRptFilterByFtyIsResultExpField")
	private Boolean vwVerticalRptFilterByFtyIsResultExpField;
	
	public ViewVerticalReportColumnByFtyField(Integer vwVerticalRptColumnByFtyFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			FirstTimeYieldInfoField firstTimeYieldInfoField,
			Boolean vwVerticalRptFilterByFtyIsDateTimeExpField,
			Boolean vwVerticalRptFilterByFtySNExpField,
			Boolean vwVerticalRptFilterByFtyIsResultExpField) {
		this.vwVerticalRptColumnByFtyFieldId = vwVerticalRptColumnByFtyFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.firstTimeYieldInfoField = firstTimeYieldInfoField;
		this.vwVerticalRptFilterByFtyIsDateTimeExpField = vwVerticalRptFilterByFtyIsDateTimeExpField;
		this.vwVerticalRptFilterByFtySNExpField = vwVerticalRptFilterByFtySNExpField;
		this.vwVerticalRptFilterByFtyIsResultExpField = vwVerticalRptFilterByFtyIsResultExpField;
	}	

	public ViewVerticalReportColumnByFtyField() {
		this.vwVerticalRptColumnByFtyFieldId = null;
		this.vwVerticalReportColumn = null;
		this.firstTimeYieldInfoField = null;
		this.vwVerticalRptFilterByFtyIsDateTimeExpField = null;
		this.vwVerticalRptFilterByFtySNExpField = null;
		this.vwVerticalRptFilterByFtyIsResultExpField = null;
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

	public Boolean getVwVerticalRptFilterByFtyIsDateTimeExpField() {
		return vwVerticalRptFilterByFtyIsDateTimeExpField;
	}

	public void setVwVerticalRptFilterByFtyIsDateTimeExpField(Boolean vwVerticalRptFilterByFtyIsDateTimeExpField) {
		this.vwVerticalRptFilterByFtyIsDateTimeExpField = vwVerticalRptFilterByFtyIsDateTimeExpField;
	}

	public Boolean getVwVerticalRptFilterByFtySNExpField() {
		return vwVerticalRptFilterByFtySNExpField;
	}

	public void setVwVerticalRptFilterByFtySNExpField(Boolean vwVerticalRptFilterByFtySNExpField) {
		this.vwVerticalRptFilterByFtySNExpField = vwVerticalRptFilterByFtySNExpField;
	}

	public Boolean getVwVerticalRptFilterByFtyIsResultExpField() {
		return vwVerticalRptFilterByFtyIsResultExpField;
	}

	public void setVwVerticalRptFilterByFtyIsResultExpField(Boolean vwVerticalRptFilterByFtyIsResultExpField) {
		this.vwVerticalRptFilterByFtyIsResultExpField = vwVerticalRptFilterByFtyIsResultExpField;
	}
	

}