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
@Table(name="ViewHorizontalReportColumn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewHorizontalReportColumn {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptColumnId")
	private Integer vwHorizontalRptColumnId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewHorizontalReport;	
	
	@Column(name="VwHorizontalRptColumnName")
	private String vwHorizontalRptColumnName;

	@Column(name="VwHorizontalRptColumnDbId")
	private String vwHorizontalRptColumnDbId;
	
	@Column(name="VwVerticalRptColumnDataType")
	private String vwVerticalRptColumnDataType;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FnyInfoFieldId", unique=false, updatable=true)
	private FinalPassYieldInfoField fnyInfoField;
	
	@Column(name="VwHorizontalFnyIsDateTimeExpField")
	private Boolean vwHorizontalFnyIsDateTimeExpField;

	@Column(name="VwHorizontalFnyIsSNExpField")
	private Boolean vwHorizontalFnyIsSNExpField;

	@Column(name="VwHorizontalFnyIsResultExpField")
	private Boolean vwHorizontalFnyIsResultExpField;
	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FpyInfoFieldId", unique=false, updatable=true)
	private FirstPassYieldInfoField fpyInfoField;
	
	@Column(name="VwHorizontalFpyIsDateTimeExpField")
	private Boolean vwHorizontalFpyIsDateTimeExpField;

	@Column(name="VwHorizontalFpyIsSNExpField")
	private Boolean vwHorizontalFpyIsSNExpField;

	@Column(name="VwHorizontalFpyIsResultExpField")
	private Boolean vwHorizontalFpyIsResultExpField;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FtyInfoFieldId", unique=false, updatable=true)
	private FirstTimeYieldInfoField ftyInfoField;
	
	@Column(name="VwHorizontalFtyIsDateTimeExpField")
	private Boolean vwHorizontalFtyIsDateTimeExpField;

	@Column(name="VwHorizontalFtyIsSNExpField")
	private Boolean vwHorizontalFtyIsSNExpField;

	@Column(name="VwHorizontalFtyIsResultExpField")
	private Boolean vwHorizontalFtyIsResultExpField;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="targetColumnId", unique=false, updatable=true)
	private TargetColumn targetColumn;

	public ViewHorizontalReportColumn(Integer vwHorizontalRptColumnId, ViewVerticalReport viewHorizontalReport,
			String vwHorizontalRptColumnName, String vwHorizontalRptColumnDbId, String vwVerticalRptColumnDataType,
			FinalPassYieldInfoField fnyInfoField, Boolean vwHorizontalFnyIsDateTimeExpField,
			Boolean vwHorizontalFnyIsSNExpField, Boolean vwHorizontalFnyIsResultExpField,
			FirstPassYieldInfoField fpyInfoField, Boolean vwHorizontalFpyIsDateTimeExpField,
			Boolean vwHorizontalFpyIsSNExpField, Boolean vwHorizontalFpyIsResultExpField,
			FirstTimeYieldInfoField ftyInfoField, Boolean vwHorizontalFtyIsDateTimeExpField,
			Boolean vwHorizontalFtyIsSNExpField, Boolean vwHorizontalFtyIsResultExpField, TargetColumn targetColumn) {
		this.vwHorizontalRptColumnId = vwHorizontalRptColumnId;
		this.viewHorizontalReport = viewHorizontalReport;
		this.vwHorizontalRptColumnName = vwHorizontalRptColumnName;
		this.vwHorizontalRptColumnDbId = vwHorizontalRptColumnDbId;
		this.vwVerticalRptColumnDataType = vwVerticalRptColumnDataType;
		this.fnyInfoField = fnyInfoField;
		this.vwHorizontalFnyIsDateTimeExpField = vwHorizontalFnyIsDateTimeExpField;
		this.vwHorizontalFnyIsSNExpField = vwHorizontalFnyIsSNExpField;
		this.vwHorizontalFnyIsResultExpField = vwHorizontalFnyIsResultExpField;
		this.fpyInfoField = fpyInfoField;
		this.vwHorizontalFpyIsDateTimeExpField = vwHorizontalFpyIsDateTimeExpField;
		this.vwHorizontalFpyIsSNExpField = vwHorizontalFpyIsSNExpField;
		this.vwHorizontalFpyIsResultExpField = vwHorizontalFpyIsResultExpField;
		this.ftyInfoField = ftyInfoField;
		this.vwHorizontalFtyIsDateTimeExpField = vwHorizontalFtyIsDateTimeExpField;
		this.vwHorizontalFtyIsSNExpField = vwHorizontalFtyIsSNExpField;
		this.vwHorizontalFtyIsResultExpField = vwHorizontalFtyIsResultExpField;
		this.targetColumn = targetColumn;
	}
	
	public ViewHorizontalReportColumn() {
		this.vwHorizontalRptColumnId = null;
		this.viewHorizontalReport = null;
		this.vwHorizontalRptColumnName = null;
		this.vwHorizontalRptColumnDbId = null;
		this.vwVerticalRptColumnDataType = null;
		this.fnyInfoField = null;
		this.vwHorizontalFnyIsDateTimeExpField = null;
		this.vwHorizontalFnyIsSNExpField = null;
		this.vwHorizontalFnyIsResultExpField = null;
		this.fpyInfoField = null;
		this.vwHorizontalFpyIsDateTimeExpField = null;
		this.vwHorizontalFpyIsSNExpField = null;
		this.vwHorizontalFpyIsResultExpField = null;
		this.ftyInfoField = null;
		this.vwHorizontalFtyIsDateTimeExpField = null;
		this.vwHorizontalFtyIsSNExpField = null;
		this.vwHorizontalFtyIsResultExpField = null;
		this.targetColumn = null;
	}

	public Integer getVwHorizontalRptColumnId() {
		return vwHorizontalRptColumnId;
	}

	public void setVwHorizontalRptColumnId(Integer vwHorizontalRptColumnId) {
		this.vwHorizontalRptColumnId = vwHorizontalRptColumnId;
	}

	public ViewVerticalReport getViewHorizontalReport() {
		return viewHorizontalReport;
	}

	public void setViewHorizontalReport(ViewVerticalReport viewHorizontalReport) {
		this.viewHorizontalReport = viewHorizontalReport;
	}

	public String getVwHorizontalRptColumnName() {
		return vwHorizontalRptColumnName;
	}

	public void setVwHorizontalRptColumnName(String vwHorizontalRptColumnName) {
		this.vwHorizontalRptColumnName = vwHorizontalRptColumnName;
	}

	public String getVwHorizontalRptColumnDbId() {
		return vwHorizontalRptColumnDbId;
	}

	public void setVwHorizontalRptColumnDbId(String vwHorizontalRptColumnDbId) {
		this.vwHorizontalRptColumnDbId = vwHorizontalRptColumnDbId;
	}

	public String getVwVerticalRptColumnDataType() {
		return vwVerticalRptColumnDataType;
	}

	public void setVwVerticalRptColumnDataType(String vwVerticalRptColumnDataType) {
		this.vwVerticalRptColumnDataType = vwVerticalRptColumnDataType;
	}

	public FinalPassYieldInfoField getFnyInfoField() {
		return fnyInfoField;
	}

	public void setFnyInfoField(FinalPassYieldInfoField fnyInfoField) {
		this.fnyInfoField = fnyInfoField;
	}

	public Boolean getVwHorizontalFnyIsDateTimeExpField() {
		return vwHorizontalFnyIsDateTimeExpField;
	}

	public void setVwHorizontalFnyIsDateTimeExpField(Boolean vwHorizontalFnyIsDateTimeExpField) {
		this.vwHorizontalFnyIsDateTimeExpField = vwHorizontalFnyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalFnyIsSNExpField() {
		return vwHorizontalFnyIsSNExpField;
	}

	public void setVwHorizontalFnyIsSNExpField(Boolean vwHorizontalFnyIsSNExpField) {
		this.vwHorizontalFnyIsSNExpField = vwHorizontalFnyIsSNExpField;
	}

	public Boolean getVwHorizontalFnyIsResultExpField() {
		return vwHorizontalFnyIsResultExpField;
	}

	public void setVwHorizontalFnyIsResultExpField(Boolean vwHorizontalFnyIsResultExpField) {
		this.vwHorizontalFnyIsResultExpField = vwHorizontalFnyIsResultExpField;
	}

	public FirstPassYieldInfoField getFpyInfoField() {
		return fpyInfoField;
	}

	public void setFpyInfoField(FirstPassYieldInfoField fpyInfoField) {
		this.fpyInfoField = fpyInfoField;
	}

	public Boolean getVwHorizontalFpyIsDateTimeExpField() {
		return vwHorizontalFpyIsDateTimeExpField;
	}

	public void setVwHorizontalFpyIsDateTimeExpField(Boolean vwHorizontalFpyIsDateTimeExpField) {
		this.vwHorizontalFpyIsDateTimeExpField = vwHorizontalFpyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalFpyIsSNExpField() {
		return vwHorizontalFpyIsSNExpField;
	}

	public void setVwHorizontalFpyIsSNExpField(Boolean vwHorizontalFpyIsSNExpField) {
		this.vwHorizontalFpyIsSNExpField = vwHorizontalFpyIsSNExpField;
	}

	public Boolean getVwHorizontalFpyIsResultExpField() {
		return vwHorizontalFpyIsResultExpField;
	}

	public void setVwHorizontalFpyIsResultExpField(Boolean vwHorizontalFpyIsResultExpField) {
		this.vwHorizontalFpyIsResultExpField = vwHorizontalFpyIsResultExpField;
	}

	public FirstTimeYieldInfoField getFtyInfoField() {
		return ftyInfoField;
	}

	public void setFtyInfoField(FirstTimeYieldInfoField ftyInfoField) {
		this.ftyInfoField = ftyInfoField;
	}

	public Boolean getVwHorizontalFtyIsDateTimeExpField() {
		return vwHorizontalFtyIsDateTimeExpField;
	}

	public void setVwHorizontalFtyIsDateTimeExpField(Boolean vwHorizontalFtyIsDateTimeExpField) {
		this.vwHorizontalFtyIsDateTimeExpField = vwHorizontalFtyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalFtyIsSNExpField() {
		return vwHorizontalFtyIsSNExpField;
	}

	public void setVwHorizontalFtyIsSNExpField(Boolean vwHorizontalFtyIsSNExpField) {
		this.vwHorizontalFtyIsSNExpField = vwHorizontalFtyIsSNExpField;
	}

	public Boolean getVwHorizontalFtyIsResultExpField() {
		return vwHorizontalFtyIsResultExpField;
	}

	public void setVwHorizontalFtyIsResultExpField(Boolean vwHorizontalFtyIsResultExpField) {
		this.vwHorizontalFtyIsResultExpField = vwHorizontalFtyIsResultExpField;
	}

	public TargetColumn getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(TargetColumn targetColumn) {
		this.targetColumn = targetColumn;
	}
}
