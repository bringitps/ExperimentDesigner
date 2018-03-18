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
@Table(name="ViewHorizontalReportColumnByEnrichment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ViewHorizontalReportColumnByEnrichment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VwHorizontalRptColumnByEnrichmentId")
	private Integer vwHorizontalRptColumnByEnrichmentId;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="VwHorizontalRptColumnId", unique=false, updatable=true)
	private ViewHorizontalReportColumn vwHorizontalReportColumn;	

	@Column(name="VwHorizontalRptColumnEnrichmentOperation")
	private String vwHorizontalRptColumnEnrichmentOperation;

	@Column(name="VwHorizontalRptColumnEnrichmentValue")
	private String vwHorizontalRptColumnEnrichmentValue;
	
	@Column(name="VwHorizontalRptColumnEnrichmentType")
	private String vwHorizontalRptColumnEnrichmentType;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListValueId", unique=false, updatable=true)
	private CustomListValue customListValue ;

	@Column(name="vwHorizontalRptColumnEnrichmentStaticValue")
	private String vwHorizontalRptColumnEnrichmentStaticValue;

	public ViewHorizontalReportColumnByEnrichment(Integer vwHorizontalRptColumnByEnrichmentId,
			ViewHorizontalReportColumn vwHorizontalReportColumn, String vwHorizontalRptColumnEnrichmentOperation,
			String vwHorizontalRptColumnEnrichmentValue, String vwHorizontalRptColumnEnrichmentType,
			CustomListValue customListValue, String vwHorizontalRptColumnEnrichmentStaticValue) {
		this.vwHorizontalRptColumnByEnrichmentId = vwHorizontalRptColumnByEnrichmentId;
		this.vwHorizontalReportColumn = vwHorizontalReportColumn;
		this.vwHorizontalRptColumnEnrichmentOperation = vwHorizontalRptColumnEnrichmentOperation;
		this.vwHorizontalRptColumnEnrichmentValue = vwHorizontalRptColumnEnrichmentValue;
		this.vwHorizontalRptColumnEnrichmentType = vwHorizontalRptColumnEnrichmentType;
		this.customListValue = customListValue;
		this.vwHorizontalRptColumnEnrichmentStaticValue = vwHorizontalRptColumnEnrichmentStaticValue;
	}
	
	public ViewHorizontalReportColumnByEnrichment() {
		this.vwHorizontalRptColumnByEnrichmentId = null;
		this.vwHorizontalReportColumn = null;
		this.vwHorizontalRptColumnEnrichmentOperation = null;
		this.vwHorizontalRptColumnEnrichmentValue = null;
		this.vwHorizontalRptColumnEnrichmentType = null;
		this.customListValue = null;
		this.vwHorizontalRptColumnEnrichmentStaticValue = null;
	}

	public Integer getVwHorizontalRptColumnByEnrichmentId() {
		return vwHorizontalRptColumnByEnrichmentId;
	}

	public void setVwHorizontalRptColumnByEnrichmentId(Integer vwHorizontalRptColumnByEnrichmentId) {
		this.vwHorizontalRptColumnByEnrichmentId = vwHorizontalRptColumnByEnrichmentId;
	}

	public ViewHorizontalReportColumn getVwHorizontalReportColumn() {
		return vwHorizontalReportColumn;
	}

	public void setVwHorizontalReportColumn(ViewHorizontalReportColumn vwHorizontalReportColumn) {
		this.vwHorizontalReportColumn = vwHorizontalReportColumn;
	}

	public String getVwHorizontalRptColumnEnrichmentOperation() {
		return vwHorizontalRptColumnEnrichmentOperation;
	}

	public void setVwHorizontalRptColumnEnrichmentOperation(String vwHorizontalRptColumnEnrichmentOperation) {
		this.vwHorizontalRptColumnEnrichmentOperation = vwHorizontalRptColumnEnrichmentOperation;
	}

	public String getVwHorizontalRptColumnEnrichmentValue() {
		return vwHorizontalRptColumnEnrichmentValue;
	}

	public void setVwHorizontalRptColumnEnrichmentValue(String vwHorizontalRptColumnEnrichmentValue) {
		this.vwHorizontalRptColumnEnrichmentValue = vwHorizontalRptColumnEnrichmentValue;
	}

	public String getVwHorizontalRptColumnEnrichmentType() {
		return vwHorizontalRptColumnEnrichmentType;
	}

	public void setVwHorizontalRptColumnEnrichmentType(String vwHorizontalRptColumnEnrichmentType) {
		this.vwHorizontalRptColumnEnrichmentType = vwHorizontalRptColumnEnrichmentType;
	}

	public CustomListValue getCustomListValue() {
		return customListValue;
	}

	public void setCustomListValue(CustomListValue customListValue) {
		this.customListValue = customListValue;
	}

	public String getVwHorizontalRptColumnEnrichmentStaticValue() {
		return vwHorizontalRptColumnEnrichmentStaticValue;
	}

	public void setVwHorizontalRptColumnEnrichmentStaticValue(String vwHorizontalRptColumnEnrichmentStaticValue) {
		this.vwHorizontalRptColumnEnrichmentStaticValue = vwHorizontalRptColumnEnrichmentStaticValue;
	}
	
	
}
