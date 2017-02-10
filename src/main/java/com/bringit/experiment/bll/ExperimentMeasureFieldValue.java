package com.bringit.experiment.bll;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ExperimentMeasurefieldValue")
public class ExperimentMeasureFieldValue implements Serializable {

    @EmbeddedId
    private ExperimentMeasureFieldValuePK experimentMeasureFieldValuePK;
	
	@Column(name="ExpMeasureValue")
	private String expMeasureValue;
	
	@Column(name="ExpMeasureComment")
	private String expMeasureComment;


	public String getExpMeasureValue() {
		return expMeasureValue;
	}

	public void setExpMeasureValue(String expMeasureValue) {
		this.expMeasureValue = expMeasureValue;
	}

	public String getExpMeasureComment() {
		return expMeasureComment;
	}

	public void setExpMeasureComment(String expMeasureComment) {
		this.expMeasureComment = expMeasureComment;
	}

	public ExperimentMeasureFieldValuePK getExperimentMeasureFieldValuePK() {
		return experimentMeasureFieldValuePK;
	}

	public void setExperimentMeasureFieldValuePK(ExperimentMeasureFieldValuePK experimentMeasureFieldValuePK) {
		this.experimentMeasureFieldValuePK = experimentMeasureFieldValuePK;
	}
	
}
