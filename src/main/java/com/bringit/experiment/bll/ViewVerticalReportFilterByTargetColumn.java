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
@Table(name="ViewVerticalReportFilterByTargetColumn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewVerticalReportFilterByTargetColumn {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptFilterByTargetColumnId")
	private Integer VwVerticalRptFilterByTargetColumnId;

	@Column(name="VwVerticalRptFilterByTargetColumnFieldExpression")
	private String vwVerticalRptFilterByTargetColumnExpression;

	@Column(name="VwVerticalRptFilterByTargetColumnOperation")
	private String vwVerticalRptFilterByTargetColumnOperation;
	
	@Column(name="VwVerticalRptFilterByTargetColumnValue1")
	private String vwVerticalRptFilterByTargetColumnValue1;

	@Column(name="VwVerticalRptFilterByTargetColumnValue2")
	private String vwVerticalRptFilterByTargetColumnValue2;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalTargetRptId", unique=false, updatable=true)
	private ViewVerticalReportByTargetRpt vwVerticalTargetRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="targetColumnId", unique=false, updatable=true)
	private TargetColumn targetColumn;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListId", unique=false, updatable=true)
	private CustomList customList;


	public ViewVerticalReportFilterByTargetColumn(Integer vwVerticalRptFilterByTargetColumnId,
			String vwVerticalRptFilterByTargetColumnExpression, String vwVerticalRptFilterByTargetColumnOperation,
			String vwVerticalRptFilterByTargetColumnValue1, String vwVerticalRptFilterByTargetColumnValue2,
			ViewVerticalReportByTargetRpt vwVerticalTargetRpt,
			TargetColumn targetColumn, CustomList customList) {
		VwVerticalRptFilterByTargetColumnId = vwVerticalRptFilterByTargetColumnId;
		this.vwVerticalRptFilterByTargetColumnExpression = vwVerticalRptFilterByTargetColumnExpression;
		this.vwVerticalRptFilterByTargetColumnOperation = vwVerticalRptFilterByTargetColumnOperation;
		this.vwVerticalRptFilterByTargetColumnValue1 = vwVerticalRptFilterByTargetColumnValue1;
		this.vwVerticalRptFilterByTargetColumnValue2 = vwVerticalRptFilterByTargetColumnValue2;
		this.vwVerticalTargetRpt = vwVerticalTargetRpt;
		this.targetColumn = targetColumn;
		this.customList = customList;
		this.customList = null;
	}

	public ViewVerticalReportFilterByTargetColumn() {
		VwVerticalRptFilterByTargetColumnId = null;
		this.vwVerticalRptFilterByTargetColumnExpression = null;
		this.vwVerticalRptFilterByTargetColumnOperation = null;
		this.vwVerticalRptFilterByTargetColumnValue1 = null;
		this.vwVerticalRptFilterByTargetColumnValue2 = null;
		this.vwVerticalTargetRpt = null;
		this.targetColumn = null;
	}

	public Integer getVwVerticalRptFilterByTargetColumnId() {
		return VwVerticalRptFilterByTargetColumnId;
	}

	public void setVwVerticalRptFilterByTargetColumnId(Integer vwVerticalRptFilterByTargetColumnId) {
		VwVerticalRptFilterByTargetColumnId = vwVerticalRptFilterByTargetColumnId;
	}

	public String getVwVerticalRptFilterByTargetColumnExpression() {
		return vwVerticalRptFilterByTargetColumnExpression;
	}

	public void setVwVerticalRptFilterByTargetColumnExpression(String vwVerticalRptFilterByTargetColumnExpression) {
		this.vwVerticalRptFilterByTargetColumnExpression = vwVerticalRptFilterByTargetColumnExpression;
	}

	public String getVwVerticalRptFilterByTargetColumnOperation() {
		return vwVerticalRptFilterByTargetColumnOperation;
	}

	public void setVwVerticalRptFilterByTargetColumnOperation(String vwVerticalRptFilterByTargetColumnOperation) {
		this.vwVerticalRptFilterByTargetColumnOperation = vwVerticalRptFilterByTargetColumnOperation;
	}

	public String getVwVerticalRptFilterByTargetColumnValue1() {
		return vwVerticalRptFilterByTargetColumnValue1;
	}

	public void setVwVerticalRptFilterByTargetColumnValue1(String vwVerticalRptFilterByTargetColumnValue1) {
		this.vwVerticalRptFilterByTargetColumnValue1 = vwVerticalRptFilterByTargetColumnValue1;
	}

	public String getVwVerticalRptFilterByTargetColumnValue2() {
		return vwVerticalRptFilterByTargetColumnValue2;
	}

	public void setVwVerticalRptFilterByTargetColumnValue2(String vwVerticalRptFilterByTargetColumnValue2) {
		this.vwVerticalRptFilterByTargetColumnValue2 = vwVerticalRptFilterByTargetColumnValue2;
	}

	public ViewVerticalReportByTargetRpt getVwVerticalTargetRpt() {
		return vwVerticalTargetRpt;
	}

	public void setVwVerticalTargetRpt(ViewVerticalReportByTargetRpt vwVerticalTargetRpt) {
		this.vwVerticalTargetRpt = vwVerticalTargetRpt;
	}

	public TargetColumn getTargetColumn() {
		return targetColumn;
	}

	public void setTargetColumn(TargetColumn targetColumn) {
		this.targetColumn = targetColumn;
	}

	public CustomList getCustomList() {
		return customList;
	}

	public void setCustomList(CustomList customList) {
		this.customList = customList;
	}
}
