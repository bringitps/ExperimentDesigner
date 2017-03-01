package com.bringit.experiment.bll;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CsvDataLoadExecutionResult")
public class CsvDataLoadExecutionResult {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CsvDataLoadExecId")
	private Integer csvDataLoadExecId;

	@Column(name="CsvDataLoadExecException")
	private boolean csvDataLoadExecException;
	
	@Column(name="CsvDataLoadExecExceptionDetails")
	private String csvDataLoadExecExeptionDetails;
	
	@Column(name="CsvDataLoadExecExceptionFile")
	private String csvDataLoadExecExeptionFile;
	
	@Column(name="CsvDataLoadExecTime")
	private Date csvDataLoadExecTime;

	@OneToOne
    @JoinColumn(name="DataFileId", unique=false, updatable=true)
	private DataFile dataFile;
	
	@OneToOne
    @JoinColumn(name="CsvTemplateId", unique=false, updatable=true)
	private CsvTemplate csvTemplate;

	public Integer getCsvDataLoadExecId() {
		return csvDataLoadExecId;
	}

	public void setCsvDataLoadExecId(Integer csvDataLoadExecId) {
		this.csvDataLoadExecId = csvDataLoadExecId;
	}

	public boolean getCsvDataLoadExecException() {
		return csvDataLoadExecException;
	}

	public void setCsvDataLoadExecException(boolean csvDataLoadExecException) {
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

	public DataFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}

	public CsvTemplate getCsvTemplate() {
		return csvTemplate;
	}

	public void setCsvTemplate(CsvTemplate csvTemplate) {
		this.csvTemplate = csvTemplate;
	}

	public String getCsvDataLoadExecExeptionFile() {
		return csvDataLoadExecExeptionFile;
	}

	public void setCsvDataLoadExecExeptionFile(String csvDataLoadExecExeptionFile) {
		this.csvDataLoadExecExeptionFile = csvDataLoadExecExeptionFile;
	}

	
}
