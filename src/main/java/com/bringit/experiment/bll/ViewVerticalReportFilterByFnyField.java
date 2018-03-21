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
@Table(name="ViewVerticalReportFilterByFnyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportFilterByFnyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptFilterByFnyFieldId")
	private Integer vwVerticalRptFilterByFnyFieldId;

	@Column(name="VwVerticalRptFilterByFnyFieldExpression")
	private String vwVerticalRptFilterByFnyFieldExpression;

	@Column(name="VwVerticalRptFilterByFnyFieldOperation")
	private String vwVerticalRptFilterByFnyFieldOperation;
	
	@Column(name="VwVerticalRptFilterByFnyFieldValue1")
	private String vwVerticalRptFilterByFnyFieldValue1;

	@Column(name="VwVerticalRptFilterByFnyFieldValue2")
	private String vwVerticalRptFilterByFnyFieldValue2;

	@Column(name="VwVerticalRptFilterByFnyIsDateTimeExpField")
	private Boolean vwVerticalRptFilterByFnyIsDateTimeExpField;

	@Column(name="VwVerticalRptFilterByFnyIsSNExpField")
	private Boolean vwVerticalRptFilterByFnySNExpField;

	@Column(name="VwVerticalRptFilterByFnyIsResultExpField")
	private Boolean vwVerticalRptFilterByFnyIsResultExpField;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalFnyRptId", unique=false, updatable=true)
	private ViewVerticalReportByFnyRpt vwVerticalFnyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FnyInfoFieldId", unique=false, updatable=true)
	private FinalPassYieldInfoField fnyInfoField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListId", unique=false, updatable=true)
	private CustomList customList;

	public ViewVerticalReportFilterByFnyField(Integer vwVerticalRptFilterByFnyFieldId,
			String vwVerticalRptFilterByFnyFieldExpression, String vwVerticalRptFilterByFnyFieldOperation,
			String vwVerticalRptFilterByFnyFieldValue1, String vwVerticalRptFilterByFnyFieldValue2,
			Boolean vwVerticalRptFilterByFnyIsDateTimeExpField, Boolean vwVerticalRptFilterByFnySNExpField,
			Boolean vwVerticalRptFilterByFnyIsResultExpField, ViewVerticalReportByFnyRpt vwVerticalFnyRpt,
			FinalPassYieldInfoField fnyInfoField, CustomList customList) {
		this.vwVerticalRptFilterByFnyFieldId = vwVerticalRptFilterByFnyFieldId;
		this.vwVerticalRptFilterByFnyFieldExpression = vwVerticalRptFilterByFnyFieldExpression;
		this.vwVerticalRptFilterByFnyFieldOperation = vwVerticalRptFilterByFnyFieldOperation;
		this.vwVerticalRptFilterByFnyFieldValue1 = vwVerticalRptFilterByFnyFieldValue1;
		this.vwVerticalRptFilterByFnyFieldValue2 = vwVerticalRptFilterByFnyFieldValue2;
		this.vwVerticalRptFilterByFnyIsDateTimeExpField = vwVerticalRptFilterByFnyIsDateTimeExpField;
		this.vwVerticalRptFilterByFnySNExpField = vwVerticalRptFilterByFnySNExpField;
		this.vwVerticalRptFilterByFnyIsResultExpField = vwVerticalRptFilterByFnyIsResultExpField;
		this.vwVerticalFnyRpt = vwVerticalFnyRpt;
		this.fnyInfoField = fnyInfoField;
		this.customList = customList;
	}	

	public ViewVerticalReportFilterByFnyField() {
		this.vwVerticalRptFilterByFnyFieldId = null;
		this.vwVerticalRptFilterByFnyFieldExpression = null;
		this.vwVerticalRptFilterByFnyFieldOperation = null;
		this.vwVerticalRptFilterByFnyFieldValue1 = null;
		this.vwVerticalRptFilterByFnyFieldValue2 = null;
		this.vwVerticalRptFilterByFnyIsDateTimeExpField = null;
		this.vwVerticalRptFilterByFnySNExpField = null;
		this.vwVerticalRptFilterByFnyIsResultExpField = null;
		this.vwVerticalFnyRpt = null;
		this.fnyInfoField = null;
		this.customList = null;
	}

	public Integer getVwVerticalRptFilterByFnyFieldId() {
		return vwVerticalRptFilterByFnyFieldId;
	}

	public void setVwVerticalRptFilterByFnyFieldId(Integer vwVerticalRptFilterByFnyFieldId) {
		this.vwVerticalRptFilterByFnyFieldId = vwVerticalRptFilterByFnyFieldId;
	}

	public String getVwVerticalRptFilterByFnyFieldExpression() {
		return vwVerticalRptFilterByFnyFieldExpression;
	}

	public void setVwVerticalRptFilterByFnyFieldExpression(String vwVerticalRptFilterByFnyFieldExpression) {
		this.vwVerticalRptFilterByFnyFieldExpression = vwVerticalRptFilterByFnyFieldExpression;
	}

	public String getVwVerticalRptFilterByFnyFieldOperation() {
		return vwVerticalRptFilterByFnyFieldOperation;
	}

	public void setVwVerticalRptFilterByFnyFieldOperation(String vwVerticalRptFilterByFnyFieldOperation) {
		this.vwVerticalRptFilterByFnyFieldOperation = vwVerticalRptFilterByFnyFieldOperation;
	}

	public String getVwVerticalRptFilterByFnyFieldValue1() {
		return vwVerticalRptFilterByFnyFieldValue1;
	}

	public void setVwVerticalRptFilterByFnyFieldValue1(String vwVerticalRptFilterByFnyFieldValue1) {
		this.vwVerticalRptFilterByFnyFieldValue1 = vwVerticalRptFilterByFnyFieldValue1;
	}

	public String getVwVerticalRptFilterByFnyFieldValue2() {
		return vwVerticalRptFilterByFnyFieldValue2;
	}

	public void setVwVerticalRptFilterByFnyFieldValue2(String vwVerticalRptFilterByFnyFieldValue2) {
		this.vwVerticalRptFilterByFnyFieldValue2 = vwVerticalRptFilterByFnyFieldValue2;
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

	public ViewVerticalReportByFnyRpt getVwVerticalFnyRpt() {
		return vwVerticalFnyRpt;
	}

	public void setVwVerticalFnyRpt(ViewVerticalReportByFnyRpt vwVerticalFnyRpt) {
		this.vwVerticalFnyRpt = vwVerticalFnyRpt;
	}

	public FinalPassYieldInfoField getFnyInfoField() {
		return fnyInfoField;
	}

	public void setFnyInfoField(FinalPassYieldInfoField fnyInfoField) {
		this.fnyInfoField = fnyInfoField;
	}

	public CustomList getCustomList() {
		return customList;
	}

	public void setCustomList(CustomList customList) {
		this.customList = customList;
	}
	
}
