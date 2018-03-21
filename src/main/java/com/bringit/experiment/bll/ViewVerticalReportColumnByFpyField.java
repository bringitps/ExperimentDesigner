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
    @JoinColumn(name="FpyInfoFieldId", unique=false, updatable=true)
	private FirstPassYieldInfoField firstPassYieldInfoField;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fpyRptId", unique=false, updatable=true)
	private FirstPassYieldReport fpyRpt;

	@Column(name="VwVerticalRptColumnByFpyIsDateTimeExpField")
	private Boolean VwVerticalRptColumnByFpyIsDateTimeExpField;

	@Column(name="VwVerticalRptColumnByFpyIsSNExpField")
	private Boolean VwVerticalRptColumnByFpyIsSNExpField;

	@Column(name="VwVerticalRptColumnByFpyIsResultExpField")
	private Boolean VwVerticalRptColumnByFpyIsResultExpField;
	
	
	public ViewVerticalReportColumnByFpyField(Integer vwVerticalRptColumnByFpyFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			FirstPassYieldInfoField firstPassYieldInfoField,
			FirstPassYieldReport fpyRpt,
			Boolean VwVerticalRptColumnByFpyIsDateTimeExpField,
			Boolean VwVerticalRptColumnByFpyIsSNExpField,
			Boolean VwVerticalRptColumnByFpyIsResultExpField) {
		this.vwVerticalRptColumnByFpyFieldId = vwVerticalRptColumnByFpyFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.firstPassYieldInfoField = firstPassYieldInfoField;
		this.fpyRpt = fpyRpt;
		this.VwVerticalRptColumnByFpyIsDateTimeExpField = VwVerticalRptColumnByFpyIsDateTimeExpField;
		this.VwVerticalRptColumnByFpyIsSNExpField = VwVerticalRptColumnByFpyIsSNExpField;
		this.VwVerticalRptColumnByFpyIsResultExpField = VwVerticalRptColumnByFpyIsResultExpField;
	}	

	public ViewVerticalReportColumnByFpyField() {
		this.vwVerticalRptColumnByFpyFieldId = null;
		this.vwVerticalReportColumn = null;
		this.firstPassYieldInfoField = null;
		this.fpyRpt = null;
		this.VwVerticalRptColumnByFpyIsDateTimeExpField = null;
		this.VwVerticalRptColumnByFpyIsSNExpField = null;
		this.VwVerticalRptColumnByFpyIsResultExpField = null;
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
	
	public FirstPassYieldInfoField getFirstPassYieldInfoField() {
		return firstPassYieldInfoField;
	}

	public void setFirstPassYieldInfoField(FirstPassYieldInfoField firstPassYieldInfoField) {
		this.firstPassYieldInfoField = firstPassYieldInfoField;
	}

	public FirstPassYieldReport getFpyRpt() {
		return fpyRpt;
	}

	public void setFpyRpt(FirstPassYieldReport fpyRpt) {
		this.fpyRpt = fpyRpt;
	}

	public Boolean getVwVerticalRptColumnByFpyIsDateTimeExpField() {
		return VwVerticalRptColumnByFpyIsDateTimeExpField;
	}

	public void setVwVerticalRptColumnByFpyIsDateTimeExpField(Boolean VwVerticalRptColumnByFpyIsDateTimeExpField) {
		this.VwVerticalRptColumnByFpyIsDateTimeExpField = VwVerticalRptColumnByFpyIsDateTimeExpField;
	}

	public Boolean getVwVerticalRptColumnByFpyIsSNExpField() {
		return VwVerticalRptColumnByFpyIsSNExpField;
	}

	public void setVwVerticalRptColumnByFpyIsSNExpField(Boolean VwVerticalRptColumnByFpyIsSNExpField) {
		this.VwVerticalRptColumnByFpyIsSNExpField = VwVerticalRptColumnByFpyIsSNExpField;
	}

	public Boolean getVwVerticalRptColumnByFpyIsResultExpField() {
		return VwVerticalRptColumnByFpyIsResultExpField;
	}

	public void setVwVerticalRptColumnByFpyIsResultExpField(Boolean VwVerticalRptColumnByFpyIsResultExpField) {
		this.VwVerticalRptColumnByFpyIsResultExpField = VwVerticalRptColumnByFpyIsResultExpField;
	}
	
}
