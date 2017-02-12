package com.bringit.experiment.bll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ExperimentMeasureFieldValue")
public class ExperimentMeasureFieldValue {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpMeasureFieldValueId")
	private Integer expMeasureFieldValueId;
	
	@Column(name="ExpMeasureValue")
	private String expMeasureValue;
	
	@OneToOne
    @JoinColumn(name="ExpFieldId", unique=false, updatable=false)
	private ExperimentField experimentField;

	@OneToOne
    @JoinColumn(name="ExpMeasureId", unique=false, updatable=false)
	private ExperimentMeasure experimentMeasure;

	public Integer getExpMeasureFieldValueId() {
		return expMeasureFieldValueId;
	}

	public void setExpMeasureFieldValueId(Integer expMeasureFieldValueId) {
		this.expMeasureFieldValueId = expMeasureFieldValueId;
	}

	public String getExpMeasureValue() {
		return expMeasureValue;
	}

	public void setExpMeasureValue(String expMeasureValue) {
		this.expMeasureValue = expMeasureValue;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}

	public ExperimentMeasure getExperimentMeasure() {
		return experimentMeasure;
	}

	public void setExperimentMeasure(ExperimentMeasure experimentMeasure) {
		this.experimentMeasure = experimentMeasure;
	}
	
}
