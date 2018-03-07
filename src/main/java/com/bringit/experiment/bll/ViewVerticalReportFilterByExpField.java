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
@Table(name="ViewVerticalReportFilterByExpField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewVerticalReportFilterByExpField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptFilterByExpFieldId")
	private Integer vwVerticalRptFilterByExpFieldId;

	@Column(name="VwVerticalRptFilterByExpFieldExpression")
	private String vwVerticalRptFilterByExpFieldExpression;

	@Column(name="VwVerticalRptFilterByExpFieldOperation")
	private String vwVerticalRptFilterByExpFieldOperation;
	
	@Column(name="VwVerticalRptFilterByExpFieldValue1")
	private String vwVerticalRptFilteByExpFieldValue1;

	@Column(name="VwVerticalRptFilterByExpFieldValue2")
	private String vwVerticalRptFilterByExpFieldValue2;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportByExperimentId", unique=false, updatable=true)
	private ViewVerticalReportByExperiment vwVerticalReportByExperiment;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField expField;

	public ViewVerticalReportFilterByExpField(Integer vwVerticalRptFilterByExpFieldId,
			String vwVerticalRptFilterByExpFieldExpression, String vwVerticalRptFilterByExpFieldOperation,
			String vwVerticalRptFilteByExpFieldValue1, String vwVerticalRptFilterByExpFieldValue2,
			ViewVerticalReportByExperiment vwVerticalReportByExperiment,
			ExperimentField expField) {
		this.vwVerticalRptFilterByExpFieldId = vwVerticalRptFilterByExpFieldId;
		this.vwVerticalRptFilterByExpFieldExpression = vwVerticalRptFilterByExpFieldExpression;
		this.vwVerticalRptFilterByExpFieldOperation = vwVerticalRptFilterByExpFieldOperation;
		this.vwVerticalRptFilteByExpFieldValue1 = vwVerticalRptFilteByExpFieldValue1;
		this.vwVerticalRptFilterByExpFieldValue2 = vwVerticalRptFilterByExpFieldValue2;
		this.vwVerticalReportByExperiment = vwVerticalReportByExperiment;
		this.expField = expField;
	}	

	public ViewVerticalReportFilterByExpField() {
		this.vwVerticalRptFilterByExpFieldId = null;
		this.vwVerticalRptFilterByExpFieldExpression = null;
		this.vwVerticalRptFilterByExpFieldOperation = null;
		this.vwVerticalRptFilteByExpFieldValue1 = null;
		this.vwVerticalRptFilterByExpFieldValue2 = null;
		this.vwVerticalReportByExperiment = null;
		this.expField = null;
	}

	public Integer getVwVerticalRptFilterByExpFieldId() {
		return vwVerticalRptFilterByExpFieldId;
	}

	public void setVwVerticalRptFilterByExpFieldId(Integer vwVerticalRptFilterByExpFieldId) {
		this.vwVerticalRptFilterByExpFieldId = vwVerticalRptFilterByExpFieldId;
	}

	public String getVwVerticalRptFilterByExpFieldExpression() {
		return vwVerticalRptFilterByExpFieldExpression;
	}

	public void setVwVerticalRptFilterByExpFieldExpression(String vwVerticalRptFilterByExpFieldExpression) {
		this.vwVerticalRptFilterByExpFieldExpression = vwVerticalRptFilterByExpFieldExpression;
	}

	public String getVwVerticalRptFilterByExpFieldOperation() {
		return vwVerticalRptFilterByExpFieldOperation;
	}

	public void setVwVerticalRptFilterByExpFieldOperation(String vwVerticalRptFilterByExpFieldOperation) {
		this.vwVerticalRptFilterByExpFieldOperation = vwVerticalRptFilterByExpFieldOperation;
	}

	public String getVwVerticalRptFilteByExpFieldValue1() {
		return vwVerticalRptFilteByExpFieldValue1;
	}

	public void setVwVerticalRptFilteByExpFieldValue1(String vwVerticalRptFilteByExpFieldValue1) {
		this.vwVerticalRptFilteByExpFieldValue1 = vwVerticalRptFilteByExpFieldValue1;
	}

	public String getVwVerticalRptFilterByExpFieldValue2() {
		return this.vwVerticalRptFilterByExpFieldValue2;
	}

	public void setVwVerticalRptFilterByExpFieldValue2(String vwVerticalRptFilterByExpFieldValue2) {
		this.vwVerticalRptFilterByExpFieldValue2 = vwVerticalRptFilterByExpFieldValue2;
	}

	public ViewVerticalReportByExperiment getVwVerticalReportByExperiment() {
		return vwVerticalReportByExperiment;
	}

	public void setVwVerticalReportByExperiment(ViewVerticalReportByExperiment vwVerticalReportByExperiment) {
		this.vwVerticalReportByExperiment = vwVerticalReportByExperiment;
	}

	public ExperimentField getExpField() {
		return expField;
	}

	public void setExpField(ExperimentField expField) {
		this.expField = expField;
	}
}
