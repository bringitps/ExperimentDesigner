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
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CsvTemplateId", unique=false, updatable=false)
	private CsvTemplate csvTemplate;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=false)
	private ExperimentField expField;

	public CsvTemplateColumns() {
		this.csvTemplateColumnId = null;
		this.csvTemplateColumnName = null;
		this.csvTemplate = null;
		this.expField = null;
	}
	
	public CsvTemplateColumns(Integer csvTemplateColumnId, String csvTemplateColumnName, CsvTemplate csvTemplate,
			ExperimentField expField) {
		this.csvTemplateColumnId = csvTemplateColumnId;
		this.csvTemplateColumnName = csvTemplateColumnName;
		this.csvTemplate = csvTemplate;
		this.expField = expField;
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
