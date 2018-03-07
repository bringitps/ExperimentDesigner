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
    @JoinColumn(name="VwVerticalReportByFtyRptId", unique=false, updatable=true)
	private ViewVerticalReportByFtyRpt vwVerticalFtyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FtyInfoFieldId", unique=false, updatable=true)
	private FirstTimeYieldInfoField firstTimeYieldInfoField;

	public ViewVerticalReportColumnByFtyField(Integer vwVerticalRptColumnByFtyFieldId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			ViewVerticalReportByFtyRpt vwVerticalFtyRpt,
			FirstTimeYieldInfoField firstTimeYieldInfoField) {
		this.vwVerticalRptColumnByFtyFieldId = vwVerticalRptColumnByFtyFieldId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.vwVerticalFtyRpt = vwVerticalFtyRpt;
		this.firstTimeYieldInfoField = firstTimeYieldInfoField;
	}	

	public ViewVerticalReportColumnByFtyField() {
		this.vwVerticalRptColumnByFtyFieldId = null;
		this.vwVerticalReportColumn = null;
		this.vwVerticalFtyRpt = null;
		this.firstTimeYieldInfoField = null;
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

	public ViewVerticalReportByFtyRpt getVwVerticalFtyRpt() {
		return vwVerticalFtyRpt;
	}

	public void setVwVerticalFtyRpt(ViewVerticalReportByFtyRpt vwVerticalFtyRpt) {
		this.vwVerticalFtyRpt = vwVerticalFtyRpt;
	}

	public FirstTimeYieldInfoField getFirstTimeYieldInfoField() {
		return firstTimeYieldInfoField;
	}

	public void setFirstTimeYieldInfoField(FirstTimeYieldInfoField firstTimeYieldInfoField) {
		this.firstTimeYieldInfoField = firstTimeYieldInfoField;
	}

}