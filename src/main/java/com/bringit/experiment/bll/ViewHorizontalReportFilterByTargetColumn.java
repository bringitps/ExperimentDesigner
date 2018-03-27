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
@Table(name="ViewHorizontalReportFilterByTargetColumn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportFilterByTargetColumn {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptFilterByTargetColumnId")
	private Integer VwHorizontalRptFilterByTargetColumnId;

	@Column(name="VwHorizontalRptFilterByTargetColumnFieldExpression")
	private String vwHorizontalRptFilterByTargetColumnExpression;

	@Column(name="VwHorizontalRptFilterByTargetColumnOperation")
	private String vwHorizontalRptFilterByTargetColumnOperation;
	
	@Column(name="VwHorizontalRptFilterByTargetColumnValue1")
	private String vwHorizontalRptFilterByTargetColumnValue1;

	@Column(name="VwHorizontalRptFilterByTargetColumnValue2")
	private String vwHorizontalRptFilterByTargetColumnValue2;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalTargetRptId", unique=false, updatable=true)
	private ViewHorizontalReportByTargetRpt vwHorizontalTargetRpt;	
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="targetColumnId", unique=false, updatable=true)
	private TargetColumn targetColumn;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListId", unique=false, updatable=true)
	private CustomList customList;
	
	public ViewHorizontalReportFilterByTargetColumn(Integer vwHorizontalRptFilterByTargetColumnId,
			String vwHorizontalRptFilterByTargetColumnExpression, String vwHorizontalRptFilterByTargetColumnOperation,
			String vwHorizontalRptFilterByTargetColumnValue1, String vwHorizontalRptFilterByTargetColumnValue2,
			ViewHorizontalReportByTargetRpt vwHorizontalTargetRpt, TargetColumn targetColumn, CustomList customList) {
		VwHorizontalRptFilterByTargetColumnId = vwHorizontalRptFilterByTargetColumnId;
		this.vwHorizontalRptFilterByTargetColumnExpression = vwHorizontalRptFilterByTargetColumnExpression;
		this.vwHorizontalRptFilterByTargetColumnOperation = vwHorizontalRptFilterByTargetColumnOperation;
		this.vwHorizontalRptFilterByTargetColumnValue1 = vwHorizontalRptFilterByTargetColumnValue1;
		this.vwHorizontalRptFilterByTargetColumnValue2 = vwHorizontalRptFilterByTargetColumnValue2;
		this.vwHorizontalTargetRpt = vwHorizontalTargetRpt;
		this.targetColumn = targetColumn;
		this.customList = customList;
	}

	public ViewHorizontalReportFilterByTargetColumn() {
		VwHorizontalRptFilterByTargetColumnId = null;
		this.vwHorizontalRptFilterByTargetColumnExpression = null;
		this.vwHorizontalRptFilterByTargetColumnOperation = null;
		this.vwHorizontalRptFilterByTargetColumnValue1 = null;
		this.vwHorizontalRptFilterByTargetColumnValue2 = null;
		this.vwHorizontalTargetRpt = null;
		this.targetColumn = null;
		this.customList = null;
	}

	public Integer getVwHorizontalRptFilterByTargetColumnId() {
		return VwHorizontalRptFilterByTargetColumnId;
	}

	public void setVwHorizontalRptFilterByTargetColumnId(Integer vwHorizontalRptFilterByTargetColumnId) {
		VwHorizontalRptFilterByTargetColumnId = vwHorizontalRptFilterByTargetColumnId;
	}

	public String getVwHorizontalRptFilterByTargetColumnExpression() {
		return vwHorizontalRptFilterByTargetColumnExpression;
	}

	public void setVwHorizontalRptFilterByTargetColumnExpression(String vwHorizontalRptFilterByTargetColumnExpression) {
		this.vwHorizontalRptFilterByTargetColumnExpression = vwHorizontalRptFilterByTargetColumnExpression;
	}

	public String getVwHorizontalRptFilterByTargetColumnOperation() {
		return vwHorizontalRptFilterByTargetColumnOperation;
	}

	public void setVwHorizontalRptFilterByTargetColumnOperation(String vwHorizontalRptFilterByTargetColumnOperation) {
		this.vwHorizontalRptFilterByTargetColumnOperation = vwHorizontalRptFilterByTargetColumnOperation;
	}

	public String getVwHorizontalRptFilterByTargetColumnValue1() {
		return vwHorizontalRptFilterByTargetColumnValue1;
	}

	public void setVwHorizontalRptFilterByTargetColumnValue1(String vwHorizontalRptFilterByTargetColumnValue1) {
		this.vwHorizontalRptFilterByTargetColumnValue1 = vwHorizontalRptFilterByTargetColumnValue1;
	}

	public String getVwHorizontalRptFilterByTargetColumnValue2() {
		return vwHorizontalRptFilterByTargetColumnValue2;
	}

	public void setVwHorizontalRptFilterByTargetColumnValue2(String vwHorizontalRptFilterByTargetColumnValue2) {
		this.vwHorizontalRptFilterByTargetColumnValue2 = vwHorizontalRptFilterByTargetColumnValue2;
	}

	public ViewHorizontalReportByTargetRpt getVwHorizontalTargetRpt() {
		return vwHorizontalTargetRpt;
	}

	public void setVwHorizontalTargetRpt(ViewHorizontalReportByTargetRpt vwHorizontalTargetRpt) {
		this.vwHorizontalTargetRpt = vwHorizontalTargetRpt;
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
