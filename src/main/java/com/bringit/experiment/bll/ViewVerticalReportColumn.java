package com.bringit.experiment.bll;

import java.util.Date;

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
@Table(name="ViewVerticalReportColumn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportColumn {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptColumnId")
	private Integer vwVerticalRptColumnId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalReportId", unique=false, updatable=true)
	private ViewVerticalReport viewVerticalReport;	

	@Column(name="VwVerticalRptColumnName")
	private String vwVerticalRptColumnName;

	@Column(name="VwVerticalRptColumnDbId")
	private String vwVerticalRptColumnDbId;

	public ViewVerticalReportColumn(Integer vwVerticalRptColumnId, ViewVerticalReport viewVerticalReport,
			String vwVerticalRptColumnName, String vwVerticalRptColumnDbId) {
		this.vwVerticalRptColumnId = vwVerticalRptColumnId;
		this.viewVerticalReport = viewVerticalReport;
		this.vwVerticalRptColumnName = vwVerticalRptColumnName;
		this.vwVerticalRptColumnDbId = vwVerticalRptColumnDbId;
	}

	public ViewVerticalReportColumn() {
		this.vwVerticalRptColumnId = null;
		this.viewVerticalReport = null;
		this.vwVerticalRptColumnName = null;
		this.vwVerticalRptColumnDbId = null;
	}

	public Integer getVwVerticalRptColumnId() {
		return vwVerticalRptColumnId;
	}

	public void setVwVerticalRptColumnId(Integer vwVerticalRptColumnId) {
		this.vwVerticalRptColumnId = vwVerticalRptColumnId;
	}

	public ViewVerticalReport getViewVerticalReport() {
		return viewVerticalReport;
	}

	public void setViewVerticalReport(ViewVerticalReport viewVerticalReport) {
		this.viewVerticalReport = viewVerticalReport;
	}

	public String getVwVerticalRptColumnName() {
		return vwVerticalRptColumnName;
	}

	public void setVwVerticalRptColumnName(String vwVerticalRptColumnName) {
		this.vwVerticalRptColumnName = vwVerticalRptColumnName;
	}

	public String getVwVerticalRptColumnDbId() {
		return vwVerticalRptColumnDbId;
	}

	public void setVwVerticalRptColumnDbId(String vwVerticalRptColumnDbId) {
		this.vwVerticalRptColumnDbId = vwVerticalRptColumnDbId;
	}
	
}
