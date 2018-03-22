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
@Table(name="ViewHorizontalReportColumnByFnyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportFilterByFnyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptFilterByFnyFieldId")
	private Integer vwHorizontalRptFilterByFnyFieldId;
	
	@Column(name="VwHorizontalRptFilterByFnyFieldExpression")
	private String vwHorizontalRptFilterByFnyFieldExpression;

	@Column(name="VwHorizontalRptFilterByFnyFieldOperation")
	private String vwHorizontalRptFilterByFnyFieldOperation;
	
	@Column(name="VwHorizontalRptFilterByFnyFieldValue1")
	private String vwHorizontalRptFilterByFnyFieldValue1;

	@Column(name="VwHorizontalRptFilterByFnyFieldValue2")
	private String vwHorizontalRptFilterByFnyFieldValue2;

	@Column(name="VwHorizontalRptFilterByFnyIsDateTimeExpField")
	private Boolean vwHorizontalRptFilterByFnyIsDateTimeExpField;

	@Column(name="VwHorizontalRptFilterByFnyIsSNExpField")
	private Boolean vwHorizontalRptFilterByFnySNExpField;

	@Column(name="VwHorizontalRptFilterByFnyIsResultExpField")
	private Boolean vwHorizontalRptFilterByFnyIsResultExpField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalFnyRptId", unique=false, updatable=true)
	private ViewHorizontalReportByFnyRpt vwHorizontalFnyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FnyInfoFieldId", unique=false, updatable=true)
	private FinalPassYieldInfoField FnyInfoField;
	
	public ViewHorizontalReportFilterByFnyField(Integer vwHorizontalRptFilterByFnyFieldId,
			String vwHorizontalRptFilterByFnyFieldExpression, String vwHorizontalRptFilterByFnyFieldOperation,
			String vwHorizontalRptFilterByFnyFieldValue1, String vwHorizontalRptFilterByFnyFieldValue2,
			Boolean vwHorizontalRptFilterByFnyIsDateTimeExpField, Boolean vwHorizontalRptFilterByFnySNExpField,
			Boolean vwHorizontalRptFilterByFnyIsResultExpField, ViewHorizontalReportByFnyRpt vwHorizontalFnyRpt,
			FinalPassYieldInfoField FnyInfoField) {
		this.vwHorizontalRptFilterByFnyFieldId = vwHorizontalRptFilterByFnyFieldId;
		this.vwHorizontalRptFilterByFnyFieldExpression = vwHorizontalRptFilterByFnyFieldExpression;
		this.vwHorizontalRptFilterByFnyFieldOperation = vwHorizontalRptFilterByFnyFieldOperation;
		this.vwHorizontalRptFilterByFnyFieldValue1 = vwHorizontalRptFilterByFnyFieldValue1;
		this.vwHorizontalRptFilterByFnyFieldValue2 = vwHorizontalRptFilterByFnyFieldValue2;
		this.vwHorizontalRptFilterByFnyIsDateTimeExpField = vwHorizontalRptFilterByFnyIsDateTimeExpField;
		this.vwHorizontalRptFilterByFnySNExpField = vwHorizontalRptFilterByFnySNExpField;
		this.vwHorizontalRptFilterByFnyIsResultExpField = vwHorizontalRptFilterByFnyIsResultExpField;
		this.vwHorizontalFnyRpt = vwHorizontalFnyRpt;
		this.FnyInfoField = FnyInfoField;
	}

	public ViewHorizontalReportFilterByFnyField() {
		this.vwHorizontalRptFilterByFnyFieldId = null;
		this.vwHorizontalRptFilterByFnyFieldExpression = null;
		this.vwHorizontalRptFilterByFnyFieldOperation = null;
		this.vwHorizontalRptFilterByFnyFieldValue1 = null;
		this.vwHorizontalRptFilterByFnyFieldValue2 = null;
		this.vwHorizontalRptFilterByFnyIsDateTimeExpField = null;
		this.vwHorizontalRptFilterByFnySNExpField = null;
		this.vwHorizontalRptFilterByFnyIsResultExpField = null;
		this.vwHorizontalFnyRpt = null;
		this.FnyInfoField = null;
	}

	public Integer getVwHorizontalRptFilterByFnyFieldId() {
		return vwHorizontalRptFilterByFnyFieldId;
	}

	public void setVwHorizontalRptFilterByFnyFieldId(Integer vwHorizontalRptFilterByFnyFieldId) {
		this.vwHorizontalRptFilterByFnyFieldId = vwHorizontalRptFilterByFnyFieldId;
	}

	public String getVwHorizontalRptFilterByFnyFieldExpression() {
		return vwHorizontalRptFilterByFnyFieldExpression;
	}

	public void setVwHorizontalRptFilterByFnyFieldExpression(String vwHorizontalRptFilterByFnyFieldExpression) {
		this.vwHorizontalRptFilterByFnyFieldExpression = vwHorizontalRptFilterByFnyFieldExpression;
	}

	public String getVwHorizontalRptFilterByFnyFieldOperation() {
		return vwHorizontalRptFilterByFnyFieldOperation;
	}

	public void setVwHorizontalRptFilterByFnyFieldOperation(String vwHorizontalRptFilterByFnyFieldOperation) {
		this.vwHorizontalRptFilterByFnyFieldOperation = vwHorizontalRptFilterByFnyFieldOperation;
	}

	public String getVwHorizontalRptFilterByFnyFieldValue1() {
		return vwHorizontalRptFilterByFnyFieldValue1;
	}

	public void setVwHorizontalRptFilterByFnyFieldValue1(String vwHorizontalRptFilteByFnyFieldValue1) {
		this.vwHorizontalRptFilterByFnyFieldValue1 = vwHorizontalRptFilteByFnyFieldValue1;
	}

	public String getVwHorizontalRptFilterByFnyFieldValue2() {
		return vwHorizontalRptFilterByFnyFieldValue2;
	}

	public void setVwHorizontalRptFilterByFnyFieldValue2(String vwHorizontalRptFilterByFnyFieldValue2) {
		this.vwHorizontalRptFilterByFnyFieldValue2 = vwHorizontalRptFilterByFnyFieldValue2;
	}

	public Boolean getVwHorizontalRptFilterByFnyIsDateTimeExpField() {
		return vwHorizontalRptFilterByFnyIsDateTimeExpField;
	}

	public void setVwHorizontalRptFilterByFnyIsDateTimeExpField(Boolean vwHorizontalRptFilterByFnyIsDateTimeExpField) {
		this.vwHorizontalRptFilterByFnyIsDateTimeExpField = vwHorizontalRptFilterByFnyIsDateTimeExpField;
	}

	public Boolean getVwHorizontalRptFilterByFnySNExpField() {
		return vwHorizontalRptFilterByFnySNExpField;
	}

	public void setVwHorizontalRptFilterByFnySNExpField(Boolean vwHorizontalRptFilterByFnySNExpField) {
		this.vwHorizontalRptFilterByFnySNExpField = vwHorizontalRptFilterByFnySNExpField;
	}

	public Boolean getVwHorizontalRptFilterByFnyIsResultExpField() {
		return vwHorizontalRptFilterByFnyIsResultExpField;
	}

	public void setVwHorizontalRptFilterByFnyIsResultExpField(Boolean vwHorizontalRptFilterByFnyIsResultExpField) {
		this.vwHorizontalRptFilterByFnyIsResultExpField = vwHorizontalRptFilterByFnyIsResultExpField;
	}

	public ViewHorizontalReportByFnyRpt getVwHorizontalFnyRpt() {
		return vwHorizontalFnyRpt;
	}

	public void setVwHorizontalFnyRpt(ViewHorizontalReportByFnyRpt vwHorizontalFnyRpt) {
		this.vwHorizontalFnyRpt = vwHorizontalFnyRpt;
	}

	public FinalPassYieldInfoField getFnyInfoField() {
		return FnyInfoField;
	}

	public void setFnyInfoField(FinalPassYieldInfoField FnyInfoField) {
		this.FnyInfoField = FnyInfoField;
	}
	
	
}
