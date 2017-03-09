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
@Table(name="ExperimentImage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ExperimentImage {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpImageId")
	private Integer expImageId;
	
	@Column(name="ExpImagePath")
	private String expImagePath;
	
	@Column(name="ExpImage", length = 100000)
	private byte[] expImage;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpId", unique=false, updatable=true)
	private Experiment experiment;

	public ExperimentImage() {
		this.expImageId = null;
		this.expImagePath = null;
		this.expImage = null;
		this.experiment = null;
	}
	
	public ExperimentImage(Integer expImageId, String expImagePath, byte[] expImage, Experiment experiment) {
		this.expImageId = expImageId;
		this.expImagePath = expImagePath;
		this.expImage = expImage;
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

	public byte[] getExpImage() {
		return expImage;
	}

	public void setExpImage(byte[] expImage) {
		this.expImage = expImage;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

}
