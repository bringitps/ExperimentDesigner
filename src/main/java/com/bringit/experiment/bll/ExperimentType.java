package com.bringit.experiment.bll;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="ExperimentType")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class ExperimentType {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ExpTypeId")
	private Integer expTypeId;
	
	@Column(name="ExpTypeName")
	private String expTypeName;

	@Column(name="ExpTypeDescription")
	private String expTypeDescription;

	public ExperimentType() {
		this.expTypeId = null;
		this.expTypeName = null;
		this.expTypeDescription = null;
	}

	public ExperimentType(Integer expTypeId, String expTypeName, String expTypeDescription) {
		this.expTypeId = expTypeId;
		this.expTypeName = expTypeName;
		this.expTypeDescription = expTypeDescription;
	}

	public Integer getExpTypeId() {
		return expTypeId;
	}

	public void setExpTypeId(Integer expTypeId) {
		this.expTypeId = expTypeId;
	}

	public String getExpTypeName() {
		return expTypeName;
	}

	public void setExpTypeName(String expTypeName) {
		this.expTypeName = expTypeName;
	}

	public String getExpTypeDescription() {
		return expTypeDescription;
	}

	public void setExpTypeDescription(String expTypeDescription) {
		this.expTypeDescription = expTypeDescription;
	}
	
}
