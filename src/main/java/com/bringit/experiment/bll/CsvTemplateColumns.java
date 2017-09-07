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
@Table(name="CsvTemplateColumns")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class CsvTemplateColumns {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CsvTemplateColumnId")
	private Integer csvTemplateColumnId;
	
	@Column(name="CsvTemplateColumnName")
	private String csvTemplateColumnName;
	
	@Column(name="CsvTemplateColumnMandatory")
	private Boolean csvTemplateColumnMandatory;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CsvTemplateId", unique=false, updatable=true)
	private CsvTemplate csvTemplate;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField expField;

	@Column(name="CsvTemplateColumnDatetimeMask")
	private String csvTemplateColumnDatetimeMask;
	
	public CsvTemplateColumns() {
		this.csvTemplateColumnId = null;
		this.csvTemplateColumnName = null;
		this.csvTemplateColumnMandatory = null;
		this.csvTemplate = null;
		this.expField = null;
		this.csvTemplateColumnDatetimeMask = null;
	}
	
	public CsvTemplateColumns(Integer csvTemplateColumnId, String csvTemplateColumnName, 
			Boolean csvTemplateColumnMandatory, CsvTemplate csvTemplate,
			ExperimentField expField, String csvTemplateColumnDatetimeMask) {
		this.csvTemplateColumnId = csvTemplateColumnId;
		this.csvTemplateColumnName = csvTemplateColumnName;
		this.csvTemplateColumnMandatory = csvTemplateColumnMandatory;
		this.csvTemplate = csvTemplate;
		this.expField = expField;
		this.csvTemplateColumnDatetimeMask = csvTemplateColumnDatetimeMask;
	}

	public Integer getCsvTemplateColumnId() {
		return csvTemplateColumnId;
	}

	public void setCsvTemplateColumnId(Integer csvTemplateColumnId) {
		this.csvTemplateColumnId = csvTemplateColumnId;
	}

	public String getCsvTemplateColumnName() {
		return csvTemplateColumnName;
	}

	public void setCsvTemplateColumnName(String csvTemplateColumnName) {
		this.csvTemplateColumnName = csvTemplateColumnName;
	}
	
	public Boolean getCsvTemplateColumnMandatory() {
		return csvTemplateColumnMandatory;
	}

	public void setCsvTemplateColumnMandatory(Boolean csvTemplateColumnMandatory) {
		this.csvTemplateColumnMandatory = csvTemplateColumnMandatory;
	}

	public CsvTemplate getCsvTemplate() {
		return csvTemplate;
	}

	public void setCsvTemplate(CsvTemplate csvTemplate) {
		this.csvTemplate = csvTemplate;
	}

	public ExperimentField getExpField() {
		return expField;
	}

	public void setExpField(ExperimentField expField) {
		this.expField = expField;
	}

	public String getCsvTemplateColumnDatetimeMask() {
		return csvTemplateColumnDatetimeMask;
	}

	public void setCsvTemplateColumnDatetimeMask(String csvTemplateColumnDatetimeMask) {
		this.csvTemplateColumnDatetimeMask = csvTemplateColumnDatetimeMask;
	}	
}
