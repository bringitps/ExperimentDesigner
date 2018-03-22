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
@Table(name="ViewHorizontalReportFilterByFpyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportFilterByFpyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptFilterByFpyFieldId")
	private Integer vwHorizontalRptFilterByFpyFieldId;

	@Column(name="VwHorizontalRptFilterByFpyFieldExpression")
	private String vwHorizontalRptFilterByFpyFieldExpression;

	@Column(name="VwHorizontalRptFilterByFpyFieldOperation")
	private String vwHorizontalRptFilterByFpyFieldOperation;
	
	@Column(name="VwHorizontalRptFilterByFpyFieldValue1")
	private String vwHorizontalRptFilterByFpyFieldValue1;

	@Column(name="VwHorizontalRptFilterByFpyFieldValue2")
	private String vwHorizontalRptFilterByFpyFieldValue2;

	@Column(name="VwHorizontalRptFilterByFpyIsDateTimeExpField")
	private Boolean vwHorizontalRptFilterByFpyIsDateTimeExpField;

	@Column(name="VwHorizontalRptFilterByFpyIsSNExpField")
	private Boolean vwHorizontalRptFilterByFpySNExpField;

	@Column(name="VwHorizontalRptFilterByFpyIsResultExpField")
	private Boolean vwHorizontalRptFilterByFpyIsResultExpField;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalFpyRptId", unique=false, updatable=true)
	private ViewHorizontalReportByFpyRpt vwHorizontalFpyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FpyInfoFieldId", unique=false, updatable=true)
	private FirstPassYieldInfoField fpyInfoField;

	public ViewHorizontalReportFilterByFpyField(Integer vwHorizontalRptFilterByFpyFieldId,
			String vwHorizontalRptFilterByFpyFieldExpression, String vwHorizontalRptFilterByFpyFieldOperation,
			String vwHorizontalRptFilterByFpyFieldValue1, String vwHorizontalRptFilterByFpyFieldValue2,
			Boolean vwHorizontalRptFilterByFpyIsDateTimeExpField, Boolean vwHorizontalRptFilterByFpySNExpField,
			Boolean vwHorizontalRptFilterByFpyIsResultExpField, ViewHorizontalReportByFpyRpt vwHorizontalFpyRpt,
			FirstPassYieldInfoField fpyInfoField) {
		this.vwHorizontalRptFilterByFpyFieldId = vwHorizontalRptFilterByFpyFieldId;
		this.vwHorizontalRptFilterByFpyFieldExpression = vwHorizontalRptFilterByFpyFieldExpression;
		this.vwHorizontalRptFilterByFpyFieldOperation = vwHorizontalRptFilterByFpyFieldOperation;
		this.vwHorizontalRptFilterByFpyFieldValue1 = vwHorizontalRptFilterByFpyFieldValue1;
		this.vwHorizontalRptFilterByFpyFieldValue2 = vwHorizontalRptFilterByFpyFieldValue2;
		this.vwHorizontalRptFilterByFpyIsDateTimeExpField = vwHorizontalRptFilterByFpyIsDateTimeExpField;
		this.vwHorizontalRptFilterByFpySNExpField = vwHorizontalRptFilterByFpySNExpField;
		this.vwHorizontalRptFilterByFpyIsResultExpField = vwHorizontalRptFilterByFpyIsResultExpField;
		this.vwHorizontalFpyRpt = vwHorizontalFpyRpt;
		this.fpyInfoField = fpyInfoField;
	}
	
	public ViewHorizontalReportFilterByFpyField() {
		this.vwHorizontalRptFilterByFpyFieldId = null;
		this.vwHorizontalRptFilterByFpyFieldExpression = null;
		this.vwHorizontalRptFilterByFpyFieldOperation = null;
		this.vwHorizontalRptFilterByFpyFieldValue1 = null;
		this.vwHorizontalRptFilterByFpyFieldValue2 = null;
		this.vwHorizontalRptFilterByFpyIsDateTimeExpField = null;
		this.vwHorizontalRptFilterByFpySNExpField = null;
		this.vwHorizontalRptFilterByFpyIsResultExpField = null;
		this.vwHorizontalFpyRpt = null;
		this.fpyInfoField = null;
	}

	public Integer getVwHorizontalRptFilterByFpyFieldId() {
		return vwHorizontalRptFilterByFpyFieldId;
	}

	public void setVwHorizontalRptFilterByFpyFieldId(Integer vwHorizontalRptFilterByFpyFieldId) {
		this.vwHorizontalRptFilterByFpyFieldId = vwHorizontalRptFilterByFpyFieldId;
	}

	public String getVwHorizontalRptFilterByFpyFieldExpression() {
		return vwHorizontalRptFilterByFpyFieldExpression;
	}

	public void setVwHorizontalRptFilterByFpyFieldExpression(String vwHorizontalRptFilterByFpyFieldExpression) {
		this.vwHorizontalRptFilterByFpyFieldExpression = vwHorizontalRptFilterByFpyFieldExpression;
	}

	public String getVwHorizontalRptFilterByFpyFieldOperation() {
		return vwHorizontalRptFilterByFpyFieldOperation;
	}

	public void setVwHorizontalRptFilterByFpyFieldOperation(String vwHorizontalRptFilterByFpyFieldOperation) {
		this.vwHorizontalRptFilterByFpyFieldOperation = vwHorizontalRptFilterByFpyFieldOperation;
	}

	public String getVwHorizontalRptFilterByFpyFieldValue1() {
		return vwHorizontalRptFilterByFpyFieldValue1;
	}

	public void setVwHorizontalRptFilterByFpyFieldValue1(String vwHorizontalRptFilteByFpyFieldValue1) {
		this.vwHorizontalRptFilterByFpyFieldValue1 = vwHorizontalRptFilteByFpyFieldValue1;
	}

	public String getVwHorizontalRptFilterByFpyFieldValue2() {
		return vwHorizontalRptFilterByFpyFieldValue2;
	}

	public void setVwHorizontalRptFilterByFpyFieldValue2(String vwHorizontalRptFilterByFpyFieldValue2) {
		this.vwHorizontalRptFilterByFpyFieldValue2 = vwHorizontalRptFilterByFpyFieldValue2;
	}

	public Boolean getVwHorizontalRptFilterByFpyIsDateTimeExpField() {
		return vwHorizontalRptFilterByFpyIsDateTimeExpField;
	}

	public void setVwHorizontalRptFilterByFpyIsDateTimeExpField(Boolean vwHorizontalRptFilterByFpyIsDateTimeExpField) {
		this.vwHorizontalRptFilterByFpyIsDateTimeExpField = vwHorizontalRptFilterByFpyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalRptFilterByFpySNExpField() {
		return vwHorizontalRptFilterByFpySNExpField;
	}

	public void setVwHorizontalRptFilterByFpySNExpField(Boolean vwHorizontalRptFilterByFpySNExpField) {
		this.vwHorizontalRptFilterByFpySNExpField = vwHorizontalRptFilterByFpySNExpField;
	}

	public Boolean getVwHorizontalRptFilterByFpyIsResultExpField() {
		return vwHorizontalRptFilterByFpyIsResultExpField;
	}

	public void setVwHorizontalRptFilterByFpyIsResultExpField(Boolean vwHorizontalRptFilterByFpyIsResultExpField) {
		this.vwHorizontalRptFilterByFpyIsResultExpField = vwHorizontalRptFilterByFpyIsResultExpField;
	}

	public ViewHorizontalReportByFpyRpt getVwHorizontalFpyRpt() {
		return vwHorizontalFpyRpt;
	}

	public void setVwHorizontalFpyRpt(ViewHorizontalReportByFpyRpt vwHorizontalFpyRpt) {
		this.vwHorizontalFpyRpt = vwHorizontalFpyRpt;
	}

	public FirstPassYieldInfoField getFpyInfoField() {
		return fpyInfoField;
	}

	public void setFpyInfoField(FirstPassYieldInfoField fpyInfoField) {
		this.fpyInfoField = fpyInfoField;
	}
	
	
}
