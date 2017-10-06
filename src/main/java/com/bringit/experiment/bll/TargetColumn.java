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
	
	@Column(name="TargetColumnPosition")
	private Integer targetColumnPosition;

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
    @JoinColumn(name="TargetColumnWhatIfCurrMinExpFieldId", unique=false, updatable=true)
	private ExperimentField targetColumnWhatIfCurrMinExpField;

	@Column(name="TargetColumnWhatIfCurrentMin")
	private Float targetColumnWhatIfCurrentMin;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetColumnWhatIfCurrMaxExpFieldId", unique=false, updatable=true)
	private ExperimentField targetColumnWhatIfCurrMaxExpField;

	@Column(name="TargetColumnWhatIfCurrentMax")
	private Float targetColumnWhatIfCurrentMax;

	@Column(name="TargetColumnWhatIfNewMin")
	private Float targetColumnWhatIfNewMin;
	
	@Column(name="TargetColumnWhatIfNewMax")
	private Float targetColumnWhatIfNewMax;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField experimentField;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TargetColumnGroupId", unique=false, updatable=true)
	private TargetColumnGroup targetColumnGroup;

	public TargetColumn() {
		this.targetColumnId = null;
		this.targetColumnIsInfo = null;
		this.targetColumnPosition = null;
		this.targetColumnLabel = null;
		this.targetColumnOffset = null;
		this.targetColumnGoalValue = null;
		this.targetColumnMinValue = null;
		this.targetColumnMaxValue = null;
		this.targetColumnWhatIfCurrMinExpField = null;
		this.targetColumnWhatIfCurrentMin = null;
		this.targetColumnWhatIfCurrMaxExpField = null;
		this.targetColumnWhatIfCurrentMax = null;
		this.targetColumnWhatIfNewMin = null;
		this.targetColumnWhatIfNewMax = null;
		this.experimentField = null;
		this.targetColumnGroup = null;
	}

	public TargetColumn(Integer targetColumnId, Boolean targetColumnIsInfo, Integer targetColumnPosition, String targetColumnLabel,
			Float targetColumnOffset, Float targetColumnGoalValue, Float targetColumnMinValue,
			Float targetColumnMaxValue, ExperimentField targetColumnWhatIfCurrMinExpField, Float targetColumnWhatIfCurrentMin,
			ExperimentField targetColumnWhatIfCurrMaxExpField, Float targetColumnWhatIfCurrentMax,
			Float targetColumnWhatIfNewMin, Float targetColumnWhatIfNewMax,
			ExperimentField experimentField, TargetColumnGroup targetColumnGroup) {
		this.targetColumnId = targetColumnId;
		this.targetColumnIsInfo = targetColumnIsInfo;
		this.targetColumnPosition = targetColumnPosition;
		this.targetColumnLabel = targetColumnLabel;
		this.targetColumnOffset = targetColumnOffset;
		this.targetColumnGoalValue = targetColumnGoalValue;
		this.targetColumnMinValue = targetColumnMinValue;
		this.targetColumnMaxValue = targetColumnMaxValue;
		this.targetColumnWhatIfCurrMinExpField = targetColumnWhatIfCurrMinExpField;
		this.targetColumnWhatIfCurrentMin = targetColumnWhatIfCurrentMin;
		this.targetColumnWhatIfCurrMaxExpField = targetColumnWhatIfCurrMaxExpField;
		this.targetColumnWhatIfCurrentMax = targetColumnWhatIfCurrentMax;
		this.targetColumnWhatIfNewMin = targetColumnWhatIfNewMin;
		this.targetColumnWhatIfNewMax = targetColumnWhatIfNewMax;
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

	public Integer getTargetColumnPosition() {
		return targetColumnPosition;
	}

	public void setTargetColumnPosition(Integer targetColumnPosition) {
		this.targetColumnPosition = targetColumnPosition;
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
	
	public ExperimentField getTargetColumnWhatIfCurrMinExpField() {
		return targetColumnWhatIfCurrMinExpField;
	}

	public Float getTargetColumnWhatIfCurrentMin() {
		return targetColumnWhatIfCurrentMin;
	}

	public void setTargetColumnWhatIfCurrentMin(Float targetColumnWhatIfCurrentMin) {
		this.targetColumnWhatIfCurrentMin = targetColumnWhatIfCurrentMin;
	}

	public ExperimentField getTargetColumnWhatIfCurrMaxExpField() {
		return targetColumnWhatIfCurrMaxExpField;
	}

	public void setTargetColumnWhatIfCurrMaxExpField(ExperimentField targetColumnWhatIfCurrMaxExpField) {
		this.targetColumnWhatIfCurrMaxExpField = targetColumnWhatIfCurrMaxExpField;
	}

	public Float getTargetColumnWhatIfCurrentMax() {
		return targetColumnWhatIfCurrentMax;
	}

	public void setTargetColumnWhatIfCurrentMax(Float targetColumnWhatIfCurrentMax) {
		this.targetColumnWhatIfCurrentMax = targetColumnWhatIfCurrentMax;
	}

	public Float getTargetColumnWhatIfNewMin() {
		return targetColumnWhatIfNewMin;
	}

	public void setTargetColumnWhatIfNewMin(Float targetColumnWhatIfNewMin) {
		this.targetColumnWhatIfNewMin = targetColumnWhatIfNewMin;
	}

	public Float getTargetColumnWhatIfNewMax() {
		return targetColumnWhatIfNewMax;
	}

	public void setTargetColumnWhatIfNewMax(Float targetColumnWhatIfNewMax) {
		this.targetColumnWhatIfNewMax = targetColumnWhatIfNewMax;
	}

	public void setTargetColumnWhatIfCurrMinExpField(ExperimentField targetColumnWhatIfCurrMinExpField) {
		this.targetColumnWhatIfCurrMinExpField = targetColumnWhatIfCurrMinExpField;
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

	@Override
	public String toString() {
		return "TargetColumn [targetColumnId=" + targetColumnId + ", targetColumnIsInfo=" + targetColumnIsInfo
				+ ", targetColumnPosition=" + targetColumnPosition + ", targetColumnLabel=" + targetColumnLabel
				+ ", targetColumnOffset=" + targetColumnOffset + ", targetColumnGoalValue=" + targetColumnGoalValue
				+ ", targetColumnMinValue=" + targetColumnMinValue + ", targetColumnMaxValue=" + targetColumnMaxValue
				+ ", experimentField=" + experimentField + ", targetColumnGroup=" + targetColumnGroup + "]";
	}
	
}
