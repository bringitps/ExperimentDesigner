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

import org.hibernate.annotations.BatchSize;

@Entity
@Table(name="ExperimentField")
public class ExperimentField {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpFieldId")
	private Integer expFieldId;

	@Column(name="ExpDbFieldNameId")
	private String expDbFieldNameId;
	
	@Column(name="ExpFieldIsActive")
	private Boolean expFieldIsActive;
	
	@Column(name="ExpFieldName")
	private String expFieldName;
	
	@Column(name="ExpFieldType")
	private String expFieldType;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpId", unique=false, updatable=true)
	private Experiment experiment;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="UomId", unique=false, updatable=true)
	private UnitOfMeasure unitOfMeasure;

	public ExperimentField() {
		this.expFieldId = null;
		this.expDbFieldNameId = null;
		this.expFieldIsActive = null;
		this.expFieldName = null;
		this.expFieldType = null;
		this.experiment = null;
		this.unitOfMeasure = null;
	}

	public ExperimentField(Integer expFieldId, String expDbFieldNameId, boolean expFieldIsActive, String expFieldName,
			String expFieldType, Experiment experiment, UnitOfMeasure unitOfMeasure) {
		this.expFieldId = expFieldId;
		this.expDbFieldNameId = expDbFieldNameId;
		this.expFieldIsActive = expFieldIsActive;
		this.expFieldName = expFieldName;
		this.expFieldType = expFieldType;
		this.experiment = experiment;
		this.unitOfMeasure = unitOfMeasure;
	}

	public Integer getExpFieldId() {
		return expFieldId;
	}

	public void setExpFieldId(Integer expFieldId) {
		this.expFieldId = expFieldId;
	}

	public String getExpDbFieldNameId() {
		return expDbFieldNameId;
	}

	public void setExpDbFieldNameId(String expDbFieldNameId) {
		this.expDbFieldNameId = expDbFieldNameId;
	}

	public boolean isExpFieldIsActive() {
		return expFieldIsActive;
	}

	public void setExpFieldIsActive(boolean expFieldIsActive) {
		this.expFieldIsActive = expFieldIsActive;
	}

	public String getExpFieldName() {
		return expFieldName;
	}

	public void setExpFieldName(String expFieldName) {
		this.expFieldName = expFieldName;
	}

	public String getExpFieldType() {
		return expFieldType;
	}

	public void setExpFieldType(String expFieldType) {
		this.expFieldType = expFieldType;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

}
