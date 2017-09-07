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
@Table(name="CsvTemplateEnrichment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class CsvTemplateEnrichment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CsvTemplateEnrichmentId")
	private Integer csvTemplateEnrichmentId;
	
	@Column(name="CsvTemplateEnrichmentColumnNameSource")
	private String csvTemplateEnrichmentColumnNameSource;
	
	@Column(name="CsvTemplateEnrichmentOperation")
	private String csvTemplateEnrichmentOperation;

	@Column(name="CsvTemplateEnrichmentValue")
	private String csvTemplateEnrichmentValue;
	
	@Column(name="CsvTemplateEnrichmentType")
	private String csvTemplateEnrichmentType;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListValueId", unique=false, updatable=true)
	private CustomListValue customListValue ;

	@Column(name="CsvTemplateEnrichmentStaticValue")
	private String csvTemplateEnrichmentStaticValue;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CsvTemplateId", unique=false, updatable=true)
	private CsvTemplate csvTemplate;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField expFieldDestination;

	public CsvTemplateEnrichment() {
		this.csvTemplateEnrichmentId = null;
		this.csvTemplateEnrichmentColumnNameSource = null;
		this.csvTemplateEnrichmentOperation = null;
		this.csvTemplateEnrichmentValue = null;
		this.csvTemplateEnrichmentType = null;
		this.customListValue = null;
		this.csvTemplateEnrichmentStaticValue = null;
		this.csvTemplate = null;
		this.expFieldDestination = null;
	}
	
	public CsvTemplateEnrichment(Integer csvTemplateEnrichmentId, String csvTemplateEnrichmentColumnNameSource,
			String csvTemplateEnrichmentOperation, String csvTemplateEnrichmentValue, String csvTemplateEnrichmentType,
			CustomListValue customListValue, String csvTemplateEnrichmentStaticValue, CsvTemplate csvTemplate,
			ExperimentField expFieldDestination) {
		this.csvTemplateEnrichmentId = csvTemplateEnrichmentId;
		this.csvTemplateEnrichmentColumnNameSource = csvTemplateEnrichmentColumnNameSource;
		this.csvTemplateEnrichmentOperation = csvTemplateEnrichmentOperation;
		this.csvTemplateEnrichmentValue = csvTemplateEnrichmentValue;
		this.csvTemplateEnrichmentType = csvTemplateEnrichmentType;
		this.customListValue = customListValue;
		this.csvTemplateEnrichmentStaticValue = csvTemplateEnrichmentStaticValue;
		this.csvTemplate = csvTemplate;
		this.expFieldDestination = expFieldDestination;
	}

	public Integer getCsvTemplateEnrichmentId() {
		return csvTemplateEnrichmentId;
	}

	public void setCsvTemplateEnrichmentId(Integer csvTemplateEnrichmentId) {
		this.csvTemplateEnrichmentId = csvTemplateEnrichmentId;
	}

	public String getCsvTemplateEnrichmentColumnNameSource() {
		return csvTemplateEnrichmentColumnNameSource;
	}

	public void setCsvTemplateEnrichmentColumnNameSource(String csvTemplateEnrichmentColumnNameSource) {
		this.csvTemplateEnrichmentColumnNameSource = csvTemplateEnrichmentColumnNameSource;
	}

	public String getCsvTemplateEnrichmentOperation() {
		return csvTemplateEnrichmentOperation;
	}

	public void setCsvTemplateEnrichmentOperation(String csvTemplateEnrichmentOperation) {
		this.csvTemplateEnrichmentOperation = csvTemplateEnrichmentOperation;
	}

	public String getCsvTemplateEnrichmentValue() {
		return csvTemplateEnrichmentValue;
	}

	public void setCsvTemplateEnrichmentValue(String csvTemplateEnrichmentValue) {
		this.csvTemplateEnrichmentValue = csvTemplateEnrichmentValue;
	}

	public String getCsvTemplateEnrichmentType() {
		return csvTemplateEnrichmentType;
	}

	public void setCsvTemplateEnrichmentType(String csvTemplateEnrichmentType) {
		this.csvTemplateEnrichmentType = csvTemplateEnrichmentType;
	}

	public CustomListValue getCustomListValue() {
		return customListValue;
	}

	public void setCustomListValue(CustomListValue customListValue) {
		this.customListValue = customListValue;
	}

	public String getCsvTemplateEnrichmentStaticValue() {
		return csvTemplateEnrichmentStaticValue;
	}

	public void setCsvTemplateEnrichmentStaticValue(String csvTemplateEnrichmentStaticValue) {
		this.csvTemplateEnrichmentStaticValue = csvTemplateEnrichmentStaticValue;
	}

	public CsvTemplate getCsvTemplate() {
		return csvTemplate;
	}

	public void setCsvTemplate(CsvTemplate csvTemplate) {
		this.csvTemplate = csvTemplate;
	}

	public ExperimentField getExpFieldDestination() {
		return expFieldDestination;
	}

	public void setExpFieldDestination(ExperimentField expFieldDestination) {
		this.expFieldDestination = expFieldDestination;
	}	
}
