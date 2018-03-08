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
@Table(name="ViewVerticalReportFilterByFpyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportFilterByFpyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptFilterByFpyFieldId")
	private Integer vwVerticalRptFilterByFpyFieldId;

	@Column(name="VwVerticalRptFilterByFpyFieldExpression")
	private String vwVerticalRptFilterByFpyFieldExpression;

	@Column(name="VwVerticalRptFilterByFpyFieldOperation")
	private String vwVerticalRptFilterByFpyFieldOperation;
	
	@Column(name="VwVerticalRptFilterByFpyFieldValue1")
	private String vwVerticalRptFilteByFpyFieldValue1;

	@Column(name="VwVerticalRptFilterByFpyFieldValue2")
	private String vwVerticalRptFilterByFpyFieldValue2;

	@Column(name="VwVerticalRptFilterByFpyIsDateTimeExpField")
	private Boolean vwVerticalRptFilterByFpyIsDateTimeExpField;

	@Column(name="VwVerticalRptFilterByFpyIsSNExpField")
	private Boolean vwVerticalRptFilterByFpySNExpField;

	@Column(name="VwVerticalRptFilterByFpyIsResultExpField")
	private Boolean vwVerticalRptFilterByFpyIsResultExpField;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalFpyRptId", unique=false, updatable=true)
	private ViewVerticalReportByFpyRpt vwVerticalFpyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FpyInfoFieldId", unique=false, updatable=true)
	private FirstPassYieldInfoField fpyInfoField;

	public ViewVerticalReportFilterByFpyField(Integer vwVerticalRptFilterByFpyFieldId,
			String vwVerticalRptFilterByFpyFieldExpression, String vwVerticalRptFilterByFpyFieldOperation,
			String vwVerticalRptFilteByFpyFieldValue1, String vwVerticalRptFilterByFpyFieldValue2,
			Boolean vwVerticalRptFilterByFpyIsDateTimeExpField, Boolean vwVerticalRptFilterByFpySNExpField, 
			Boolean vwVerticalRptFilterByFpyIsResultExpField, ViewVerticalReportByFpyRpt vwVerticalFpyRpt,
			FirstPassYieldInfoField fpyInfoField) {
		this.vwVerticalRptFilterByFpyFieldId = vwVerticalRptFilterByFpyFieldId;
		this.vwVerticalRptFilterByFpyFieldExpression = vwVerticalRptFilterByFpyFieldExpression;
		this.vwVerticalRptFilterByFpyFieldOperation = vwVerticalRptFilterByFpyFieldOperation;
		this.vwVerticalRptFilteByFpyFieldValue1 = vwVerticalRptFilteByFpyFieldValue1;
		this.vwVerticalRptFilterByFpyFieldValue2 = vwVerticalRptFilterByFpyFieldValue2;
		this.vwVerticalRptFilterByFpyIsDateTimeExpField = vwVerticalRptFilterByFpyIsDateTimeExpField;
		this.vwVerticalRptFilterByFpySNExpField = vwVerticalRptFilterByFpySNExpField;
		this.vwVerticalRptFilterByFpyIsResultExpField = vwVerticalRptFilterByFpyIsResultExpField;
		this.vwVerticalFpyRpt = vwVerticalFpyRpt;
		this.fpyInfoField = fpyInfoField;
	}	

	public ViewVerticalReportFilterByFpyField() {
		this.vwVerticalRptFilterByFpyFieldId = null;
		this.vwVerticalRptFilterByFpyFieldExpression = null;
		this.vwVerticalRptFilterByFpyFieldOperation = null;
		this.vwVerticalRptFilteByFpyFieldValue1 = null;
		this.vwVerticalRptFilterByFpyFieldValue2 = null;
		this.vwVerticalRptFilterByFpyIsDateTimeExpField = null;
		this.vwVerticalRptFilterByFpySNExpField = null;
		this.vwVerticalRptFilterByFpyIsResultExpField = null;
		this.vwVerticalFpyRpt = null;
		this.fpyInfoField = null;
	}

	public Integer getVwVerticalRptFilterByFpyFieldId() {
		return vwVerticalRptFilterByFpyFieldId;
	}

	public void setVwVerticalRptFilterByFpyFieldId(Integer vwVerticalRptFilterByFpyFieldId) {
		this.vwVerticalRptFilterByFpyFieldId = vwVerticalRptFilterByFpyFieldId;
	}

	public String getVwVerticalRptFilterByFpyFieldExpression() {
		return vwVerticalRptFilterByFpyFieldExpression;
	}

	public void setVwVerticalRptFilterByFpyFieldExpression(String vwVerticalRptFilterByFpyFieldExpression) {
		this.vwVerticalRptFilterByFpyFieldExpression = vwVerticalRptFilterByFpyFieldExpression;
	}

	public String getVwVerticalRptFilterByFpyFieldOperation() {
		return vwVerticalRptFilterByFpyFieldOperation;
	}

	public void setVwVerticalRptFilterByFpyFieldOperation(String vwVerticalRptFilterByFpyFieldOperation) {
		this.vwVerticalRptFilterByFpyFieldOperation = vwVerticalRptFilterByFpyFieldOperation;
	}

	public String getVwVerticalRptFilteByFpyFieldValue1() {
		return vwVerticalRptFilteByFpyFieldValue1;
	}

	public void setVwVerticalRptFilteByFpyFieldValue1(String vwVerticalRptFilteByFpyFieldValue1) {
		this.vwVerticalRptFilteByFpyFieldValue1 = vwVerticalRptFilteByFpyFieldValue1;
	}

	public String getVwVerticalRptFilterByFpyFieldValue2() {
		return vwVerticalRptFilterByFpyFieldValue2;
	}

	public void setVwVerticalRptFilterByFpyFieldValue2(String vwVerticalRptFilterByFpyFieldValue2) {
		this.vwVerticalRptFilterByFpyFieldValue2 = vwVerticalRptFilterByFpyFieldValue2;
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

	public ViewVerticalReportByFpyRpt getVwVerticalFpyRpt() {
		return vwVerticalFpyRpt;
	}

	public void setVwVerticalFpyRpt(ViewVerticalReportByFpyRpt vwVerticalFpyRpt) {
		this.vwVerticalFpyRpt = vwVerticalFpyRpt;
	}

	public FirstPassYieldInfoField getFpyInfoField() {
		return fpyInfoField;
	}

	public void setFpyInfoField(FirstPassYieldInfoField fpyInfoField) {
		this.fpyInfoField = fpyInfoField;
	}

}