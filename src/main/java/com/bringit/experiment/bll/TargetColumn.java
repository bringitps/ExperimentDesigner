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
@Table(name="TargetColumn")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class TargetColumn {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TargetColumnId")
	private Integer targetColumnId;

	@Column(name="TargetColumnIsInfo")
	private Boolean targetColumnIsInfo;

	@Column(name="TargetColumnLabel")
	private String targetColumnLabel;

	@Column(name="TargetColumnOffset")
	private Float targetColumnOffset;

	@Column(name="TargetColumnGoalValue")
	private Float targetColumnGoalValue;

	@Column(name="TargetColumnMinValue")
	private Float targetColumnMinValue;

	@Column(name="TargetColumnMaxValue")
	private Float targetColumnMaxValue;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetColumnGroupId", unique=false, updatable=true)
	private TargetColumnGroup targetColumnGroup;

	public TargetColumn() {
		this.targetColumnId = null;
		this.targetColumnIsInfo = null;
		this.targetColumnLabel = null;
		this.targetColumnOffset = null;
		this.targetColumnGoalValue = null;
		this.targetColumnMinValue = null;
		this.targetColumnMaxValue = null;
		this.experimentField = null;
		this.targetColumnGroup = null;
	}

	public TargetColumn(Integer targetColumnId, Boolean targetColumnIsInfo, String targetColumnLabel,
			Float targetColumnOffset, Float targetColumnGoalValue, Float targetColumnMinValue,
			Float targetColumnMaxValue, ExperimentField experimentField, TargetColumnGroup targetColumnGroup) {
		this.targetColumnId = targetColumnId;
		this.targetColumnIsInfo = targetColumnIsInfo;
		this.targetColumnLabel = targetColumnLabel;
		this.targetColumnOffset = targetColumnOffset;
		this.targetColumnGoalValue = targetColumnGoalValue;
		this.targetColumnMinValue = targetColumnMinValue;
		this.targetColumnMaxValue = targetColumnMaxValue;
		this.experimentField = experimentField;
		this.targetColumnGroup = targetColumnGroup;
	}

	public Integer getTargetColumnId() {
		return targetColumnId;
	}

	public void setTargetColumnId(Integer targetColumnId) {
		this.targetColumnId = targetColumnId;
	}

	public Boolean getTargetColumnIsInfo() {
		return targetColumnIsInfo;
	}

	public void setTargetColumnIsInfo(Boolean targetColumnIsInfo) {
		this.targetColumnIsInfo = targetColumnIsInfo;
	}

	public String getTargetColumnLabel() {
		return targetColumnLabel;
	}

	public void setTargetColumnLabel(String targetColumnLabel) {
		this.targetColumnLabel = targetColumnLabel;
	}

	public Float getTargetColumnOffset() {
		return targetColumnOffset;
	}

	public void setTargetColumnOffset(Float targetColumnOffset) {
		this.targetColumnOffset = targetColumnOffset;
	}

	public Float getTargetColumnGoalValue() {
		return targetColumnGoalValue;
	}

	public void setTargetColumnGoalValue(Float targetColumnGoalValue) {
		this.targetColumnGoalValue = targetColumnGoalValue;
	}

	public Float getTargetColumnMinValue() {
		return targetColumnMinValue;
	}

	public void setTargetColumnMinValue(Float targetColumnMinValue) {
		this.targetColumnMinValue = targetColumnMinValue;
	}

	public Float getTargetColumnMaxValue() {
		return targetColumnMaxValue;
	}

	public void setTargetColumnMaxValue(Float targetColumnMaxValue) {
		this.targetColumnMaxValue = targetColumnMaxValue;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}

	public TargetColumnGroup getTargetColumnGroup() {
		return targetColumnGroup;
	}

	public void setTargetColumnGroup(TargetColumnGroup targetColumnGroup) {
		this.targetColumnGroup = targetColumnGroup;
	}
	
}
