package com.bringit.experiment.bll;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	@Column(name="ExpId")
	private Integer expId;

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

	public Integer getExpId() {
		return expId;
	}

	public void setExpId(Integer expId) {
		this.expId = expId;
	}
	
	
	
	

}
