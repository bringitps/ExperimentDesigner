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
@Table(name="TargetColumnGroup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class TargetColumnGroup {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TargetColumnGroupId")
	private Integer targetColumnGroup;

	@Column(name="TargetColumnGroupName")
	private String targetColumnGroupName;
	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetReportId", unique=false, updatable=true)
	private TargetReport targetReport;


	public TargetColumnGroup() {
		this.targetColumnGroup = null;
		this.targetColumnGroupName = null;
		this.targetReport = null;
	}

	public TargetColumnGroup(Integer targetColumnGroup, String targetColumnGroupName, TargetReport targetReport) {
		this.targetColumnGroup = targetColumnGroup;
		this.targetColumnGroupName = targetColumnGroupName;
		this.targetReport = targetReport;
	}

	public Integer getTargetColumnGroup() {
		return targetColumnGroup;
	}

	public void setTargetColumnGroup(Integer targetColumnGroup) {
		this.targetColumnGroup = targetColumnGroup;
	}

	public String getTargetColumnGroupName() {
		return targetColumnGroupName;
	}

	public void setTargetColumnGroupName(String targetColumnGroupName) {
		this.targetColumnGroupName = targetColumnGroupName;
	}

	public TargetReport getTargetReport() {
		return targetReport;
	}

	public void setTargetReport(TargetReport targetReport) {
		this.targetReport = targetReport;
	}

	
}
