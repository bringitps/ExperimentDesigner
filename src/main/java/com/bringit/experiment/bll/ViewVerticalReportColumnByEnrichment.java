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
@Table(name="ViewVerticalReportColumnByEnrichment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable

public class ViewVerticalReportColumnByEnrichment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwVerticalRptColumnByEnrichmentId")
	private Integer vwVerticalRptColumnByEnrichmentId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwVerticalRptColumnId", unique=false, updatable=true)
	private ViewVerticalReportColumn vwVerticalReportColumn;	

	@Column(name="VwVerticalRptColumnEnrichmentOperation")
	private String vwVerticalRptColumnEnrichmentOperation;

	@Column(name="VwVerticalRptColumnEnrichmentValue")
	private String vwVerticalRptColumnEnrichmentValue;
	
	@Column(name="VwVerticalRptColumnEnrichmentType")
	private String vwVerticalRptColumnEnrichmentType;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListValueId", unique=false, updatable=true)
	private CustomListValue customListValue ;

	@Column(name="vwVerticalRptColumnEnrichmentStaticValue")
	private String vwVerticalRptColumnEnrichmentStaticValue;

	public ViewVerticalReportColumnByEnrichment(Integer vwVerticalRptColumnByEnrichmentId,
			ViewVerticalReportColumn vwVerticalReportColumn, String vwVerticalRptColumnEnrichmentOperation,
			String vwVerticalRptColumnEnrichmentValue, String vwVerticalRptColumnEnrichmentType,
			CustomListValue customListValue, String vwVerticalRptColumnEnrichmentStaticValue) {
		this.vwVerticalRptColumnByEnrichmentId = vwVerticalRptColumnByEnrichmentId;
		this.vwVerticalReportColumn = vwVerticalReportColumn;
		this.vwVerticalRptColumnEnrichmentOperation = vwVerticalRptColumnEnrichmentOperation;
		this.vwVerticalRptColumnEnrichmentValue = vwVerticalRptColumnEnrichmentValue;
		this.vwVerticalRptColumnEnrichmentType = vwVerticalRptColumnEnrichmentType;
		this.customListValue = customListValue;
		this.vwVerticalRptColumnEnrichmentStaticValue = vwVerticalRptColumnEnrichmentStaticValue;
	}

	public ViewVerticalReportColumnByEnrichment() {
		this.vwVerticalRptColumnByEnrichmentId = null;
		this.vwVerticalReportColumn = null;
		this.vwVerticalRptColumnEnrichmentOperation = null;
		this.vwVerticalRptColumnEnrichmentValue = null;
		this.vwVerticalRptColumnEnrichmentType = null;
		this.customListValue = null;
		this.vwVerticalRptColumnEnrichmentStaticValue = null;
	}

	public Integer getVwVerticalRptColumnByEnrichmentId() {
		return vwVerticalRptColumnByEnrichmentId;
	}

	public void setVwVerticalRptColumnByEnrichmentId(Integer vwVerticalRptColumnByEnrichmentId) {
		this.vwVerticalRptColumnByEnrichmentId = vwVerticalRptColumnByEnrichmentId;
	}

	public ViewVerticalReportColumn getVwVerticalReportColumn() {
		return vwVerticalReportColumn;
	}

	public void setVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
		this.vwVerticalReportColumn = vwVerticalReportColumn;
	}

	public String getVwVerticalRptColumnEnrichmentOperation() {
		return vwVerticalRptColumnEnrichmentOperation;
	}

	public void setVwVerticalRptColumnEnrichmentOperation(String vwVerticalRptColumnEnrichmentOperation) {
		this.vwVerticalRptColumnEnrichmentOperation = vwVerticalRptColumnEnrichmentOperation;
	}

	public String getVwVerticalRptColumnEnrichmentValue() {
		return vwVerticalRptColumnEnrichmentValue;
	}

	public void setVwVerticalRptColumnEnrichmentValue(String vwVerticalRptColumnEnrichmentValue) {
		this.vwVerticalRptColumnEnrichmentValue = vwVerticalRptColumnEnrichmentValue;
	}

	public String getVwVerticalRptColumnEnrichmentType() {
		return vwVerticalRptColumnEnrichmentType;
	}

	public void setVwVerticalRptColumnEnrichmentType(String vwVerticalRptColumnEnrichmentType) {
		this.vwVerticalRptColumnEnrichmentType = vwVerticalRptColumnEnrichmentType;
	}

	public CustomListValue getCustomListValue() {
		return customListValue;
	}

	public void setCustomListValue(CustomListValue customListValue) {
		this.customListValue = customListValue;
	}

	public String getVwVerticalRptColumnEnrichmentStaticValue() {
		return vwVerticalRptColumnEnrichmentStaticValue;
	}

	public void setVwVerticalRptColumnEnrichmentStaticValue(String vwVerticalRptColumnEnrichmentStaticValue) {
		this.vwVerticalRptColumnEnrichmentStaticValue = vwVerticalRptColumnEnrichmentStaticValue;
	}
	
}
