package com.bringit.experiment.bll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ExperimentMeasure")
public class UnitOfMeasure {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UomId")
	private Integer uomId;
	
	@Column(name="UomName")
	private String uomName;
	
	@Column(name="uomAbbreviation")
	private String uomAbbreviation;

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getUomAbbreviation() {
		return uomAbbreviation;
	}

	public void setUomAbbreviation(String uomAbbreviation) {
		this.uomAbbreviation = uomAbbreviation;
	}
	

}
