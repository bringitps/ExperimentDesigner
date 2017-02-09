package com.bringit.experiment.bll;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ExperimentMeasure")
public class ExperimentMeasure {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpMeasureId")
	private Integer expMeasureId;
	
	@Column(name="ExpMeasureValue")
	private String expMeasureValue;
	
	@Column(name="ExpMeasureComment")
	private String expMeasureComment;
	
	@Column(name="CreatedBy")
	private Integer createdBy;
	
	@Column(name="LastModifiedBy")
	private Integer lastModifiedBy;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;

	@OneToOne
    @JoinColumn(name="ExpFieldId", unique=false, updatable=false)
	private ExperimentField experimentField;
	
	
	public Integer getExpMeasureId() {
		return expMeasureId;
	}

	public void setExpMeasureId(Integer expMeasureId) {
		this.expMeasureId = expMeasureId;
	}

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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public ExperimentField getExperimentField() {
		return experimentField;
	}

	public void setExpField(ExperimentField experimentField) {
		this.experimentField = experimentField;
	}
	
	

}
