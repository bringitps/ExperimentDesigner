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
@Table(name="ViewVerticalReportFilterByFtyField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportFilterByFtyField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptFilterByFtyFieldId")
	private Integer vwVerticalRptFilterByFtyFieldId;

	@Column(name="VwVerticalRptFilterByFtyFieldExpression")
	private String vwVerticalRptFilterByFtyFieldExpression;

	@Column(name="VwVerticalRptFilterByFtyFieldOperation")
	private String vwVerticalRptFilterByFtyFieldOperation;
	
	@Column(name="VwVerticalRptFilterByFtyFieldValue1")
	private String vwVerticalRptFilterByFtyFieldValue1;

	@Column(name="VwVerticalRptFilterByFtyFieldValue2")
	private String vwVerticalRptFilterByFtyFieldValue2;

	@Column(name="VwVerticalRptFilterByFtyIsDateTimeExpField")
	private Boolean vwVerticalRptFilterByFtyIsDateTimeExpField;

	@Column(name="VwVerticalRptFilterByFtyIsSNExpField")
	private Boolean vwVerticalRptFilterByFtySNExpField;

	@Column(name="VwVerticalRptFilterByFtyIsResultExpField")
	private Boolean vwVerticalRptFilterByFtyIsResultExpField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalFtyRptId", unique=false, updatable=true)
	private ViewVerticalReportByFtyRpt vwVerticalFtyRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FtyInfoFieldId", unique=false, updatable=true)
	private FirstTimeYieldInfoField ftyInfoField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListId", unique=false, updatable=true)
	private CustomList customList;


	public ViewVerticalReportFilterByFtyField(Integer vwVerticalRptFilterByFtyFieldId,
			String vwVerticalRptFilterByFtyFieldExpression, String vwVerticalRptFilterByFtyFieldOperation,
			String vwVerticalRptFilterByFtyFieldValue1, String vwVerticalRptFilterByFtyFieldValue2,
			Boolean vwVerticalRptFilterByFpyIsDateTimeExpField, Boolean vwVerticalRptFilterByFpySNExpField, 
			Boolean vwVerticalRptFilterByFpyIsResultExpField, 
			ViewVerticalReportByFtyRpt vwVerticalFtyRpt,
			FirstTimeYieldInfoField ftyInfoField, CustomList customList) {
		this.vwVerticalRptFilterByFtyFieldId = vwVerticalRptFilterByFtyFieldId;
		this.vwVerticalRptFilterByFtyFieldExpression = vwVerticalRptFilterByFtyFieldExpression;
		this.vwVerticalRptFilterByFtyFieldOperation = vwVerticalRptFilterByFtyFieldOperation;
		this.vwVerticalRptFilterByFtyFieldValue1 = vwVerticalRptFilterByFtyFieldValue1;
		this.vwVerticalRptFilterByFtyFieldValue2 = vwVerticalRptFilterByFtyFieldValue2;
		this.vwVerticalRptFilterByFtyIsDateTimeExpField = vwVerticalRptFilterByFpyIsDateTimeExpField;
		this.vwVerticalRptFilterByFtySNExpField = vwVerticalRptFilterByFpySNExpField;
		this.vwVerticalRptFilterByFtyIsResultExpField = vwVerticalRptFilterByFpyIsResultExpField;
		this.vwVerticalFtyRpt = vwVerticalFtyRpt;
		this.ftyInfoField = ftyInfoField;
		this.customList = customList;
	}	

	public ViewVerticalReportFilterByFtyField() {
		this.vwVerticalRptFilterByFtyFieldId = null;
		this.vwVerticalRptFilterByFtyFieldExpression = null;
		this.vwVerticalRptFilterByFtyFieldOperation = null;
		this.vwVerticalRptFilterByFtyFieldValue1 = null;
		this.vwVerticalRptFilterByFtyFieldValue2 = null;
		this.vwVerticalRptFilterByFtyIsDateTimeExpField = null;
		this.vwVerticalRptFilterByFtySNExpField = null;
		this.vwVerticalRptFilterByFtyIsResultExpField = null;
		this.vwVerticalFtyRpt = null;
		this.ftyInfoField = null;
		this.customList = null;
	}

	public Integer getVwVerticalRptFilterByFtyFieldId() {
		return vwVerticalRptFilterByFtyFieldId;
	}

	public void setVwVerticalRptFilterByFtyFieldId(Integer vwVerticalRptFilterByFtyFieldId) {
		this.vwVerticalRptFilterByFtyFieldId = vwVerticalRptFilterByFtyFieldId;
	}

	public String getVwVerticalRptFilterByFtyFieldExpression() {
		return vwVerticalRptFilterByFtyFieldExpression;
	}

	public void setVwVerticalRptFilterByFtyFieldExpression(String vwVerticalRptFilterByFtyFieldExpression) {
		this.vwVerticalRptFilterByFtyFieldExpression = vwVerticalRptFilterByFtyFieldExpression;
	}

	public String getVwVerticalRptFilterByFtyFieldOperation() {
		return vwVerticalRptFilterByFtyFieldOperation;
	}

	public void setVwVerticalRptFilterByFtyFieldOperation(String vwVerticalRptFilterByFtyFieldOperation) {
		this.vwVerticalRptFilterByFtyFieldOperation = vwVerticalRptFilterByFtyFieldOperation;
	}

	public String getVwVerticalRptFilterByFtyFieldValue1() {
		return vwVerticalRptFilterByFtyFieldValue1;
	}

	public void setVwVerticalRptFilterByFtyFieldValue1(String vwVerticalRptFilterByFtyFieldValue1) {
		this.vwVerticalRptFilterByFtyFieldValue1 = vwVerticalRptFilterByFtyFieldValue1;
	}

	public String getVwVerticalRptFilterByFtyFieldValue2() {
		return vwVerticalRptFilterByFtyFieldValue2;
	}

	public void setVwVerticalRptFilterByFtyFieldValue2(String vwVerticalRptFilterByFtyFieldValue2) {
		this.vwVerticalRptFilterByFtyFieldValue2 = vwVerticalRptFilterByFtyFieldValue2;
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

	public ViewVerticalReportByFtyRpt getVwVerticalFtyRpt() {
		return vwVerticalFtyRpt;
	}

	public void setVwVerticalFtyRpt(ViewVerticalReportByFtyRpt vwVerticalFtyRpt) {
		this.vwVerticalFtyRpt = vwVerticalFtyRpt;
	}

	public FirstTimeYieldInfoField getFtyInfoField() {
		return ftyInfoField;
	}

	public void setFtyInfoField(FirstTimeYieldInfoField ftyInfoField) {
		this.ftyInfoField = ftyInfoField;
	}

	public CustomList getCustomList() {
		return customList;
	}

	public void setCustomList(CustomList customList) {
		this.customList = customList;
	}
	
}