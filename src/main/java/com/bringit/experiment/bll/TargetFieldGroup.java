package com.bringit.experiment.bll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TargetFieldGroup")
public class TargetFieldGroup {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TargetFieldGroupId")
	private Integer targetFieldGroup;

	@Column(name="TargetFieldGroupName")
	private String targetFieldGroupName;
	

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetReportId", unique=false, updatable=true)
	private TargetReport targetReport;


	public TargetFieldGroup() {
		this.targetFieldGroup = null;
		this.targetFieldGroupName = null;
		this.targetReport = null;
	}

	public TargetFieldGroup(Integer targetFieldGroup, String targetFieldGroupName, TargetReport targetReport) {
		this.targetFieldGroup = targetFieldGroup;
		this.targetFieldGroupName = targetFieldGroupName;
		this.targetReport = targetReport;
	}

	public Integer getTargetFieldGroup() {
		return targetFieldGroup;
	}

	public void setTargetFieldGroup(Integer targetFieldGroup) {
		this.targetFieldGroup = targetFieldGroup;
	}

	public String getTargetFieldGroupName() {
		return targetFieldGroupName;
	}

	public void setTargetFieldGroupName(String targetFieldGroupName) {
		this.targetFieldGroupName = targetFieldGroupName;
	}

	public TargetReport getTargetReport() {
		return targetReport;
	}

	public void setTargetReport(TargetReport targetReport) {
		this.targetReport = targetReport;
	}
	
	
}
