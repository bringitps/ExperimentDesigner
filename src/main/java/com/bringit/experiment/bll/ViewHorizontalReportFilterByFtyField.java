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
@Table(name="ViewHorizontalReportFilterByFtyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportFilterByFtyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptFilterByFtyFieldId")
	private Integer vwHorizontalRptFilterByFtyFieldId;

	@Column(name="VwHorizontalRptFilterByFtyFieldExpression")
	private String vwHorizontalRptFilterByFtyFieldExpression;

	@Column(name="VwHorizontalRptFilterByFtyFieldOperation")
	private String vwHorizontalRptFilterByFtyFieldOperation;
	
	@Column(name="VwHorizontalRptFilterByFtyFieldValue1")
	private String vwHorizontalRptFilterByFtyFieldValue1;

	@Column(name="VwHorizontalRptFilterByFtyFieldValue2")
	private String vwHorizontalRptFilterByFtyFieldValue2;

	@Column(name="VwHorizontalRptFilterByFtyIsDateTimeExpField")
	private Boolean vwHorizontalRptFilterByFtyIsDateTimeExpField;

	@Column(name="VwHorizontalRptFilterByFtyIsSNExpField")
	private Boolean vwHorizontalRptFilterByFtySNExpField;

	@Column(name="VwHorizontalRptFilterByFtyIsResultExpField")
	private Boolean vwHorizontalRptFilterByFtyIsResultExpField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalFtyRptId", unique=false, updatable=true)
	private ViewHorizontalReportByFtyRpt vwHorizontalFtyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FtyInfoFieldId", unique=false, updatable=true)
	private FirstTimeYieldInfoField ftyInfoField;
	
	public ViewHorizontalReportFilterByFtyField(Integer vwHorizontalRptFilterByFtyFieldId,
			String vwHorizontalRptFilterByFtyFieldExpression, String vwHorizontalRptFilterByFtyFieldOperation,
			String vwHorizontalRptFilterByFtyFieldValue1, String vwHorizontalRptFilterByFtyFieldValue2,
			Boolean vwHorizontalRptFilterByFtyIsDateTimeExpField, Boolean vwHorizontalRptFilterByFtySNExpField,
			Boolean vwHorizontalRptFilterByFtyIsResultExpField, ViewHorizontalReportByFtyRpt vwHorizontalFtyRpt,
			FirstTimeYieldInfoField ftyInfoField) {
		this.vwHorizontalRptFilterByFtyFieldId = vwHorizontalRptFilterByFtyFieldId;
		this.vwHorizontalRptFilterByFtyFieldExpression = vwHorizontalRptFilterByFtyFieldExpression;
		this.vwHorizontalRptFilterByFtyFieldOperation = vwHorizontalRptFilterByFtyFieldOperation;
		this.vwHorizontalRptFilterByFtyFieldValue1 = vwHorizontalRptFilterByFtyFieldValue1;
		this.vwHorizontalRptFilterByFtyFieldValue2 = vwHorizontalRptFilterByFtyFieldValue2;
		this.vwHorizontalRptFilterByFtyIsDateTimeExpField = vwHorizontalRptFilterByFtyIsDateTimeExpField;
		this.vwHorizontalRptFilterByFtySNExpField = vwHorizontalRptFilterByFtySNExpField;
		this.vwHorizontalRptFilterByFtyIsResultExpField = vwHorizontalRptFilterByFtyIsResultExpField;
		this.vwHorizontalFtyRpt = vwHorizontalFtyRpt;
		this.ftyInfoField = ftyInfoField;
	}

	public ViewHorizontalReportFilterByFtyField() {
		this.vwHorizontalRptFilterByFtyFieldId = null;
		this.vwHorizontalRptFilterByFtyFieldExpression = null;
		this.vwHorizontalRptFilterByFtyFieldOperation = null;
		this.vwHorizontalRptFilterByFtyFieldValue1 = null;
		this.vwHorizontalRptFilterByFtyFieldValue2 = null;
		this.vwHorizontalRptFilterByFtyIsDateTimeExpField = null;
		this.vwHorizontalRptFilterByFtySNExpField = null;
		this.vwHorizontalRptFilterByFtyIsResultExpField = null;
		this.vwHorizontalFtyRpt = null;
		this.ftyInfoField = null;
	}

	public Integer getVwHorizontalRptFilterByFtyFieldId() {
		return vwHorizontalRptFilterByFtyFieldId;
	}

	public void setVwHorizontalRptFilterByFtyFieldId(Integer vwHorizontalRptFilterByFtyFieldId) {
		this.vwHorizontalRptFilterByFtyFieldId = vwHorizontalRptFilterByFtyFieldId;
	}

	public String getVwHorizontalRptFilterByFtyFieldExpression() {
		return vwHorizontalRptFilterByFtyFieldExpression;
	}

	public void setVwHorizontalRptFilterByFtyFieldExpression(String vwHorizontalRptFilterByFtyFieldExpression) {
		this.vwHorizontalRptFilterByFtyFieldExpression = vwHorizontalRptFilterByFtyFieldExpression;
	}

	public String getVwHorizontalRptFilterByFtyFieldOperation() {
		return vwHorizontalRptFilterByFtyFieldOperation;
	}

	public void setVwHorizontalRptFilterByFtyFieldOperation(String vwHorizontalRptFilterByFtyFieldOperation) {
		this.vwHorizontalRptFilterByFtyFieldOperation = vwHorizontalRptFilterByFtyFieldOperation;
	}

	public String getVwHorizontalRptFilterByFtyFieldValue1() {
		return vwHorizontalRptFilterByFtyFieldValue1;
	}

	public void setVwHorizontalRptFilterByFtyFieldValue1(String vwHorizontalRptFilteByFtyFieldValue1) {
		this.vwHorizontalRptFilterByFtyFieldValue1 = vwHorizontalRptFilteByFtyFieldValue1;
	}

	public String getVwHorizontalRptFilterByFtyFieldValue2() {
		return vwHorizontalRptFilterByFtyFieldValue2;
	}

	public void setVwHorizontalRptFilterByFtyFieldValue2(String vwHorizontalRptFilterByFtyFieldValue2) {
		this.vwHorizontalRptFilterByFtyFieldValue2 = vwHorizontalRptFilterByFtyFieldValue2;
	}

	public Boolean getVwHorizontalRptFilterByFtyIsDateTimeExpField() {
		return vwHorizontalRptFilterByFtyIsDateTimeExpField;
	}

	public void setVwHorizontalRptFilterByFtyIsDateTimeExpField(Boolean vwHorizontalRptFilterByFtyIsDateTimeExpField) {
		this.vwHorizontalRptFilterByFtyIsDateTimeExpField = vwHorizontalRptFilterByFtyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalRptFilterByFtySNExpField() {
		return vwHorizontalRptFilterByFtySNExpField;
	}

	public void setVwHorizontalRptFilterByFtySNExpField(Boolean vwHorizontalRptFilterByFtySNExpField) {
		this.vwHorizontalRptFilterByFtySNExpField = vwHorizontalRptFilterByFtySNExpField;
	}

	public Boolean getVwHorizontalRptFilterByFtyIsResultExpField() {
		return vwHorizontalRptFilterByFtyIsResultExpField;
	}

	public void setVwHorizontalRptFilterByFtyIsResultExpField(Boolean vwHorizontalRptFilterByFtyIsResultExpField) {
		this.vwHorizontalRptFilterByFtyIsResultExpField = vwHorizontalRptFilterByFtyIsResultExpField;
	}

	public ViewHorizontalReportByFtyRpt getVwHorizontalFtyRpt() {
		return vwHorizontalFtyRpt;
	}

	public void setVwHorizontalFtyRpt(ViewHorizontalReportByFtyRpt vwHorizontalFtyRpt) {
		this.vwHorizontalFtyRpt = vwHorizontalFtyRpt;
	}

	public FirstTimeYieldInfoField getFtyInfoField() {
		return ftyInfoField;
	}

	public void setFtyInfoField(FirstTimeYieldInfoField ftyInfoField) {
		this.ftyInfoField = ftyInfoField;
	}
	
	
}
