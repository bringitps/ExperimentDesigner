package com.bringit.experiment.bll;

import java.util.Date;

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
import org.hibernate.annotations.Type;

@Entity
@Table(name="CsvDataLoadExecutionResult")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class CsvDataLoadExecutionResult {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CsvDataLoadExecId")
	private Integer csvDataLoadExecId;

	@Column(name="CsvDataLoadExecException")
	private Boolean csvDataLoadExecException;
	
	@Column(name="CsvDataLoadExecExceptionDetails")
	@Type(type="text")
	private String csvDataLoadExecExeptionDetails;
	
	@Column(name="CsvDataLoadExecTime")
	private Date csvDataLoadExecTime;

	@Column(name="CsvDataLoadTotalRecords")
	private Integer csvDataLoadTotalRecords;
	
	@Column(name="CsvDataLoadTotalRecordsProcessed")
	private Integer csvDataLoadTotalRecordsProcessed;
	
	@Column(name="CsvDataLoadTotalRecordsException")
	private Integer csvDataLoadTotalRecordsException;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DataFileId", unique=false, updatable=true)
	private DataFile dataFile;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DataFileProcessedId", unique=false, updatable=true)
	private DataFile dataFileProcessed;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DataFileExceptionId", unique=false, updatable=true)
	private DataFile dataFileException;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CsvTemplateId", unique=false, updatable=true)
	private CsvTemplate csvTemplate;

	public CsvDataLoadExecutionResult() {
		this.csvDataLoadExecId = null;
		this.csvDataLoadExecException = null;
		this.csvDataLoadExecExeptionDetails = null;
		this.csvDataLoadExecTime = null;
		this.csvDataLoadTotalRecords = null;
		this.csvDataLoadTotalRecordsProcessed = null;
		this.csvDataLoadTotalRecordsException = null;
		this.dataFile = null;
		this.dataFileProcessed = null;
		this.dataFileException = null;
		this.csvTemplate = null;
	}

	public CsvDataLoadExecutionResult(Integer csvDataLoadExecId, Boolean csvDataLoadExecException,
			String csvDataLoadExecExeptionDetails, Date csvDataLoadExecTime,
			Integer csvDataLoadTotalRecords, Integer csvDataLoadTotalRecordsProcessed, Integer csvDataLoadTotalRecordsException,
			DataFile dataFile, DataFile dataFileProcessed, 
			DataFile dataFileException, CsvTemplate csvTemplate) {
		this.csvDataLoadExecId = csvDataLoadExecId;
		this.csvDataLoadExecException = csvDataLoadExecException;
		this.csvDataLoadExecExeptionDetails = csvDataLoadExecExeptionDetails;
		this.csvDataLoadExecTime = csvDataLoadExecTime;
		this.csvDataLoadTotalRecords = csvDataLoadTotalRecords;
		this.csvDataLoadTotalRecordsProcessed = csvDataLoadTotalRecordsProcessed;
		this.csvDataLoadTotalRecordsException = csvDataLoadTotalRecordsException;
		this.dataFile = dataFile;
		this.dataFileProcessed = dataFileProcessed;
		this.dataFileException = dataFileException;
		this.csvTemplate = csvTemplate;
	}

	public Integer getCsvDataLoadExecId() {
		return csvDataLoadExecId;
	}

	public void setCsvDataLoadExecId(Integer csvDataLoadExecId) {
		this.csvDataLoadExecId = csvDataLoadExecId;
	}

	public Boolean getCsvDataLoadExecException() {
		return csvDataLoadExecException;
	}

	public void setCsvDataLoadExecException(Boolean csvDataLoadExecException) {
		this.csvDataLoadExecException = csvDataLoadExecException;
	}

	public String getCsvDataLoadExecExeptionDetails() {
		return csvDataLoadExecExeptionDetails;
	}

	public void setCsvDataLoadExecExeptionDetails(String csvDataLoadExecExeptionDetails) {
		this.csvDataLoadExecExeptionDetails = csvDataLoadExecExeptionDetails;
	}

	public Date getCsvDataLoadExecTime() {
		return csvDataLoadExecTime;
	}

	public void setCsvDataLoadExecTime(Date csvDataLoadExecTime) {
		this.csvDataLoadExecTime = csvDataLoadExecTime;
	}

	public Integer getCsvDataLoadTotalRecords() {
		return csvDataLoadTotalRecords;
	}

	public void setCsvDataLoadTotalRecords(Integer csvDataLoadTotalRecords) {
		this.csvDataLoadTotalRecords = csvDataLoadTotalRecords;
	}

	public Integer getCsvDataLoadTotalRecordsProcessed() {
		return csvDataLoadTotalRecordsProcessed;
	}

	public void setCsvDataLoadTotalRecordsProcessed(Integer csvDataLoadTotalRecordsProcessed) {
		this.csvDataLoadTotalRecordsProcessed = csvDataLoadTotalRecordsProcessed;
	}

	public Integer getCsvDataLoadTotalRecordsException() {
		return csvDataLoadTotalRecordsException;
	}

	public void setCsvDataLoadTotalRecordsException(Integer csvDataLoadTotalRecordsException) {
		this.csvDataLoadTotalRecordsException = csvDataLoadTotalRecordsException;
	}
	
	public DataFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}
	
	public DataFile getDataFileProcessed() {
		return dataFileProcessed;
	}

	public void setDataFileProcessed(DataFile dataFileProcessed) {
		this.dataFileProcessed = dataFileProcessed;
	}

	public DataFile getDataFileException() {
		return dataFileException;
	}

	public void setDataFileException(DataFile dataFileException) {
		this.dataFileException = dataFileException;
	}

	public CsvTemplate getCsvTemplate() {
		return csvTemplate;
	}

	public void setCsvTemplate(CsvTemplate csvTemplate) {
		this.csvTemplate = csvTemplate;
	}
	
}
