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
@Table(name="ViewVerticalReportColumnByTargetColumn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewVerticalReportColumnByTargetColumn {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptColumnByTargetColumnId")
	private Integer vwVerticalRptColumnByTargetColumnId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalRptColumnId", unique=false, updatable=true)
	private ViewVerticalReportColumn vwVerticalReportColumn;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportByTargetRptId", unique=false, updatable=true)
	private ViewVerticalReportByTargetRpt vwVerticalReportByTargetRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetReportId", unique=false, updatable=true)
	private TargetReport targetReport;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetColumnId", unique=false, updatable=true)
	private TargetColumn targetColumn;


	@Column(name="VwVerticalRptColumnByTargetColumnIsFixedValue")
	private Boolean vwVerticalRptColumnByTargetColumnIsFixedValue;
	
	@Column(name="VwVerticalRptColumnByTargetColumnFixedValue")
	private String vwVerticalRptColumnByTargetColumnFixedValue;
	
	public ViewVerticalReportColumnByTargetColumn(Integer vwVerticalRptColumnByTargetColumnId,
			ViewVerticalReportColumn vwVerticalReportColumn,
			ViewVerticalReportByTargetRpt vwVerticalReportByTargetRpt,
			TargetReport targetReport, TargetColumn targetColumn, 
			Boolean vwVerticalRptColumnByTargetColumnIsFixedValue,
			String vwVerticalRptColumnByTargetColumnFixedValue) {
		this.vwVerticalRptColumnByTargetColumnId = vwVerticalRptColumnByTargetColumnId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.vwVerticalReportByTargetRpt = vwVerticalReportByTargetRpt;
		this.targetReport = targetReport;
		this.targetColumn = targetColumn;
		this.vwVerticalRptColumnByTargetColumnIsFixedValue = vwVerticalRptColumnByTargetColumnIsFixedValue;
		this.vwVerticalRptColumnByTargetColumnFixedValue = vwVerticalRptColumnByTargetColumnFixedValue;
	}	
	
	public ViewVerticalReportColumnByTargetColumn() {
		this.vwVerticalRptColumnByTargetColumnId = null;
		this.vwVerticalReportColumn = null;
		this.vwVerticalReportByTargetRpt = null;
		this.targetReport = null;
		this.targetColumn = null;
		this.vwVerticalRptColumnByTargetColumnIsFixedValue = null;
		this.vwVerticalRptColumnByTargetColumnFixedValue = null;
	}

	public Integer getVwVerticalRptColumnByTargetColumnId() {
		return vwVerticalRptColumnByTargetColumnId;
	}

	public void setVwVerticalRptColumnByTargetColumnId(Integer vwVerticalRptColumnByTargetColumnId) {
		this.vwVerticalRptColumnByTargetColumnId = vwVerticalRptColumnByTargetColumnId;
	}

	public ViewVerticalReportColumn getVwVerticalReportColumn() {
		return vwVerticalReportColumn;
	}

	public void setVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
		this.vwVerticalReportColumn = vwVerticalReportColumn;
	}

	public ViewVerticalReportByTargetRpt getVwVerticalReportByTargetRpt() {
		return vwVerticalReportByTargetRpt;
	}

	public void setVwVerticalReportByTargetRpt(ViewVerticalReportByTargetRpt vwVerticalReportByTargetRpt) {
		this.vwVerticalReportByTargetRpt = vwVerticalReportByTargetRpt;
	}
	
	public TargetReport getTargetReport() {
		return targetReport;
	}

	public void setTargetReport(TargetReport targetReport) {
		this.targetReport = targetReport;
	}

	public TargetColumn getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(TargetColumn targetColumn) {
		this.targetColumn = targetColumn;
	}

	public Boolean getVwVerticalRptColumnByTargetColumnIsFixedValue() {
		return vwVerticalRptColumnByTargetColumnIsFixedValue;
	}

	public void setVwVerticalRptColumnByTargetColumnIsFixedValue(Boolean vwVerticalRptColumnByTargetColumnIsFixedValue) {
		this.vwVerticalRptColumnByTargetColumnIsFixedValue = vwVerticalRptColumnByTargetColumnIsFixedValue;
	}

	public String getVwVerticalRptColumnByTargetColumnFixedValue() {
		return vwVerticalRptColumnByTargetColumnFixedValue;
	}

	public void setVwVerticalRptColumnByTargetColumnFixedValue(String vwVerticalRptColumnByTargetColumnFixedValue) {
		this.vwVerticalRptColumnByTargetColumnFixedValue = vwVerticalRptColumnByTargetColumnFixedValue;
	}
	
}
