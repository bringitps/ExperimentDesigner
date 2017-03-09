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
@Table(name="TargetField")
public class TargetField {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TargetFieldId")
	private Integer targetFieldId;

	@Column(name="TargetFieldIsInfo")
	private Boolean targetFieldIsInfo;

	@Column(name="TargetFieldLabel")
	private String targetFieldLabel;

	@Column(name="TargetFieldOffset")
	private Float targetFieldOffset;

	@Column(name="TargetFieldGoalValue")
	private Float targetFieldGoalValue;

	@Column(name="TargetFieldMinValue")
	private Float targetFieldMinValue;

	@Column(name="TargetFieldMaxValue")
	private Float targetFieldMaxValue;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetFieldGroupId", unique=false, updatable=true)
	private TargetFieldGroup targetFieldGroup;

	public TargetField() {
		this.targetFieldId = null;
		this.targetFieldIsInfo = null;
		this.targetFieldLabel = null;
		this.targetFieldOffset = null;
		this.targetFieldGoalValue = null;
		this.targetFieldMinValue = null;
		this.targetFieldMaxValue = null;
		this.experimentField = null;
		this.targetFieldGroup = null;
	}
	

	public TargetField(Integer targetFieldId, Boolean targetFieldIsInfo, String targetFieldLabel,
			Float targetFieldOffset, Float targetFieldGoalValue, Float targetFieldMinValue, Float targetFieldMaxValue,
			ExperimentField experimentField, TargetFieldGroup targetFieldGroup) {
		this.targetFieldId = targetFieldId;
		this.targetFieldIsInfo = targetFieldIsInfo;
		this.targetFieldLabel = targetFieldLabel;
		this.targetFieldOffset = targetFieldOffset;
		this.targetFieldGoalValue = targetFieldGoalValue;
		this.targetFieldMinValue = targetFieldMinValue;
		this.targetFieldMaxValue = targetFieldMaxValue;
		this.experimentField = experimentField;
		this.targetFieldGroup = targetFieldGroup;
	}
	
}
