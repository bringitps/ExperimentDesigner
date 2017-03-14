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
	private Integer targetColumnGroupId;

	@Column(name="TargetColumnGroupName")
	private String targetColumnGroupName;

	@Column(name="TargetColumnGroupIsFail")
	private Boolean targetColumnGroupIsFail;
	
	@Column(name="TargetColumnGroupPosition")
	private Integer targetColumnGroupPosition;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetReportId", unique=false, updatable=true)
	private TargetReport targetReport;


	public TargetColumnGroup() {
		this.targetColumnGroupId = null;
		this.targetColumnGroupName = null;
		this.targetColumnGroupIsFail = null;
		this.targetColumnGroupPosition = null;
		this.targetReport = null;
	}

	public TargetColumnGroup(Integer targetColumnGroup, String targetColumnGroupName, Boolean targetColumnGroupIsFail, Integer targetColumnGroupPosition, TargetReport targetReport) {
		this.targetColumnGroupId = targetColumnGroup;
		this.targetColumnGroupName = targetColumnGroupName;
		this.targetColumnGroupIsFail = targetColumnGroupIsFail;
		this.targetColumnGroupPosition = targetColumnGroupPosition;
		this.targetReport = targetReport;
	}

	public Integer getTargetColumnGroupId() {
		return targetColumnGroupId;
	}

	public void setTargetColumnGroupId(Integer targetColumnGroupId) {
		this.targetColumnGroupId = targetColumnGroupId;
	}

	public String getTargetColumnGroupName() {
		return targetColumnGroupName;
	}

	public void setTargetColumnGroupName(String targetColumnGroupName) {
		this.targetColumnGroupName = targetColumnGroupName;
	}

	public Boolean getTargetColumnGroupIsFail() {
		return targetColumnGroupIsFail;
	}

	public void setTargetColumnGroupIsFail(Boolean targetColumnGroupIsFail) {
		this.targetColumnGroupIsFail = targetColumnGroupIsFail;
	}

	public Integer getTargetColumnGroupPosition() {
		return targetColumnGroupPosition;
	}

	public void setTargetColumnGroupPosition(Integer targetColumnGroupPosition) {
		this.targetColumnGroupPosition = targetColumnGroupPosition;
	}

	public TargetReport getTargetReport() {
		return targetReport;
	}

	public void setTargetReport(TargetReport targetReport) {
		this.targetReport = targetReport;
	}

	@Override
	public String toString() {
		return "TargetColumnGroup [targetColumnGroupId=" + targetColumnGroupId + ", targetColumnGroupName="
				+ targetColumnGroupName + ", targetColumnGroupIsFail=" + targetColumnGroupIsFail
				+ ", targetColumnGroupPosition=" + targetColumnGroupPosition + ", targetReport=" + targetReport + "]";
	}

	
}
