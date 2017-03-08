package com.bringit.experiment.bll;

import java.sql.Blob;

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
@Table(name="ExperimentImage")
public class ExperimentImage {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpImageId")
	private Integer expImageId;
	
	@Column(name="ExpImagePath")
	private String expImagePath;
	
	@Column(name="ExpImageBase64")
	private Blob expImageBase64;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpId", unique=false, updatable=true)
	private Experiment experiment;

	public ExperimentImage() {
		this.expImageId = null;
		this.expImagePath = null;
		this.expImageBase64 = null;
		this.experiment = null;
	}
	
	public ExperimentImage(Integer expImageId, String expImagePath, Blob expImageBase64, Experiment experiment) {
		this.expImageId = expImageId;
		this.expImagePath = expImagePath;
		this.expImageBase64 = expImageBase64;
		this.experiment = experiment;
	}

	public Integer getExpImageId() {
		return expImageId;
	}

	public void setExpImageId(Integer expImageId) {
		this.expImageId = expImageId;
	}

	public String getExpImagePath() {
		return expImagePath;
	}

	public void setExpImagePath(String expImagePath) {
		this.expImagePath = expImagePath;
	}

	public Blob getExpImageBase64() {
		return expImageBase64;
	}

	public void setExpImageBase64(Blob expImageBase64) {
		this.expImageBase64 = expImageBase64;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

}
