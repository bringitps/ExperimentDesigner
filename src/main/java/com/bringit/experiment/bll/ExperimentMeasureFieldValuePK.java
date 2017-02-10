package com.bringit.experiment.bll;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Embeddable
public class ExperimentMeasureFieldValuePK implements Serializable {
	@OneToOne
	@JoinColumn(name="ExpMeasureId", unique=false, updatable=false)
	private ExperimentMeasure experimentMeasure;
	
	@OneToOne
    @JoinColumn(name="ExpFieldId", unique=false, updatable=false)
	private ExperimentField experimentField;
    public ExperimentMeasureFieldValuePK() {}

    public ExperimentMeasureFieldValuePK(ExperimentMeasure experimentMeasure, ExperimentField experimentField) {
        this.experimentMeasure = experimentMeasure;
        this.experimentField = experimentField;
    }

	public ExperimentMeasureFieldValuePK(ExperimentMeasureFieldValuePK experimentMeasureFieldValuePK) {
		this.experimentMeasure = experimentMeasureFieldValuePK.getExperimentMeasure();
        this.experimentField = experimentMeasureFieldValuePK.getExperimentField();
	}

	public ExperimentMeasure getExperimentMeasure() {
		return experimentMeasure;
	}

	public void setExperimentMeasure(ExperimentMeasure experimentMeasure) {
		this.experimentMeasure = experimentMeasure;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExperimentField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}
    
}

