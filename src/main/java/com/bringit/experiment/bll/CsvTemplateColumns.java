package com.bringit.experiment.bll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CsvTemplateColumns")
public class CsvTemplateColumns {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CsvTemplateColumnId")
	private Integer csvTemplateColumnId;
	
	@Column(name="CsvTemplateColumnName")
	private String csvTemplateColumnName;
	
	@OneToOne
    @JoinColumn(name="CsvTemplateId", unique=false, updatable=false)
	private CsvTemplate csvTemplate;
	
	@OneToOne
    @JoinColumn(name="ExpFieldId", unique=false, updatable=false)
	private ExperimentField expField;

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
}