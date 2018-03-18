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
@Table(name="ViewHorizontalReportFilterByExpField")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportFilterByExpField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptFilterByExpFieldId")
	private Integer vwHorizontalRptFilterByExpFieldId;

	@Column(name="VwHorizontalRptFilterByExpFieldExpression")
	private String vwHorizontalRptFilterByExpFieldExpression;

	@Column(name="VwHorizontalRptFilterByExpFieldOperation")
	private String vwHorizontalRptFilterByExpFieldOperation;
	
	@Column(name="VwHorizontalRptFilterByExpFieldValue1")
	private String vwHorizontalRptFilteByExpFieldValue1;

	@Column(name="VwHorizontalRptFilterByExpFieldValue2")
	private String vwHorizontalRptFilterByExpFieldValue2;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalReportByExperimentId", unique=false, updatable=true)
	private ViewVerticalReportByExperiment vwHorizontalReportByExperiment;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField expField;

	public ViewHorizontalReportFilterByExpField(Integer vwHorizontalRptFilterByExpFieldId,
			String vwHorizontalRptFilterByExpFieldExpression, String vwHorizontalRptFilterByExpFieldOperation,
			String vwHorizontalRptFilteByExpFieldValue1, String vwHorizontalRptFilterByExpFieldValue2,
			ViewVerticalReportByExperiment vwHorizontalReportByExperiment, ExperimentField expField) {
		this.vwHorizontalRptFilterByExpFieldId = vwHorizontalRptFilterByExpFieldId;
		this.vwHorizontalRptFilterByExpFieldExpression = vwHorizontalRptFilterByExpFieldExpression;
		this.vwHorizontalRptFilterByExpFieldOperation = vwHorizontalRptFilterByExpFieldOperation;
		this.vwHorizontalRptFilteByExpFieldValue1 = vwHorizontalRptFilteByExpFieldValue1;
		this.vwHorizontalRptFilterByExpFieldValue2 = vwHorizontalRptFilterByExpFieldValue2;
		this.vwHorizontalReportByExperiment = vwHorizontalReportByExperiment;
		this.expField = expField;
	}
	
	public ViewHorizontalReportFilterByExpField() {
		this.vwHorizontalRptFilterByExpFieldId = null;
		this.vwHorizontalRptFilterByExpFieldExpression = null;
		this.vwHorizontalRptFilterByExpFieldOperation = null;
		this.vwHorizontalRptFilteByExpFieldValue1 = null;
		this.vwHorizontalRptFilterByExpFieldValue2 = null;
		this.vwHorizontalReportByExperiment = null;
		this.expField = null;
	}

	public Integer getVwHorizontalRptFilterByExpFieldId() {
		return vwHorizontalRptFilterByExpFieldId;
	}

	public void setVwHorizontalRptFilterByExpFieldId(Integer vwHorizontalRptFilterByExpFieldId) {
		this.vwHorizontalRptFilterByExpFieldId = vwHorizontalRptFilterByExpFieldId;
	}

	public String getVwHorizontalRptFilterByExpFieldExpression() {
		return vwHorizontalRptFilterByExpFieldExpression;
	}

	public void setVwHorizontalRptFilterByExpFieldExpression(String vwHorizontalRptFilterByExpFieldExpression) {
		this.vwHorizontalRptFilterByExpFieldExpression = vwHorizontalRptFilterByExpFieldExpression;
	}

	public String getVwHorizontalRptFilterByExpFieldOperation() {
		return vwHorizontalRptFilterByExpFieldOperation;
	}

	public void setVwHorizontalRptFilterByExpFieldOperation(String vwHorizontalRptFilterByExpFieldOperation) {
		this.vwHorizontalRptFilterByExpFieldOperation = vwHorizontalRptFilterByExpFieldOperation;
	}

	public String getVwHorizontalRptFilteByExpFieldValue1() {
		return vwHorizontalRptFilteByExpFieldValue1;
	}

	public void setVwHorizontalRptFilteByExpFieldValue1(String vwHorizontalRptFilteByExpFieldValue1) {
		this.vwHorizontalRptFilteByExpFieldValue1 = vwHorizontalRptFilteByExpFieldValue1;
	}

	public String getVwHorizontalRptFilterByExpFieldValue2() {
		return vwHorizontalRptFilterByExpFieldValue2;
	}

	public void setVwHorizontalRptFilterByExpFieldValue2(String vwHorizontalRptFilterByExpFieldValue2) {
		this.vwHorizontalRptFilterByExpFieldValue2 = vwHorizontalRptFilterByExpFieldValue2;
	}

	public ViewVerticalReportByExperiment getVwHorizontalReportByExperiment() {
		return vwHorizontalReportByExperiment;
	}

	public void setVwHorizontalReportByExperiment(ViewVerticalReportByExperiment vwHorizontalReportByExperiment) {
		this.vwHorizontalReportByExperiment = vwHorizontalReportByExperiment;
	}

	public ExperimentField getExpField() {
		return expField;
	}

	public void setExpField(ExperimentField expField) {
		this.expField = expField;
	}
	
	
}
