package com.bringit.experiment.bll;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CsvTemplate")
public class CsvTemplate {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CsvTemplateId")
	private Integer csvTemplateId;

	@Column(name="CsvTemplateIsActive")
	private Boolean csvTemplateIsActive;
	
	@Column(name="CsvTemplateName")
	private String CsvTemplateName;
	
	@Column(name="CsvTemplateComments")
	private String csvTemplateComments;
	
	@Column(name="CsvTemplatePrefix")
	private String csvTemplatePrefix;

	@Column(name="CsvTemplateExecStartDate")
	private Date csvTemplateExecStartDate;
	
	@Column(name="CsvTemplateExecStartHour")
	private Integer csvTemplateExecStartHour;
	
	@Column(name="CsvTemplateExecEndDate")
	private Date csvTemplateExecEndDate;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="InboundFileRepoId", unique=false, updatable=false)
	private FilesRepository inboundFileRepo;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ProcessedFileRepoId", unique=false, updatable=false)
	private FilesRepository processedFileRepo;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExceptionFileRepoId", unique=false, updatable=false)
	private FilesRepository exceptionFileRepo;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="JobExecRepeatId", unique=false, updatable=false)
	private JobExecutionRepeat jobExecRepeat;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExperimentId", unique=false, updatable=false)
	private Experiment experiment;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;

	public CsvTemplate() {
		this.csvTemplateId = null;
		this.csvTemplateIsActive = null;
		this.CsvTemplateName = null;
		this.csvTemplateComments = null;
		this.csvTemplatePrefix = null;
		this.csvTemplateExecStartDate = null;
		this.csvTemplateExecStartHour = null;
		this.csvTemplateExecEndDate = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.inboundFileRepo = null;
		this.processedFileRepo = null;
		this.exceptionFileRepo = null;
		this.jobExecRepeat = null;
		this.experiment = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}
	
	public CsvTemplate(Integer csvTemplateId, boolean csvTemplateIsActive, String csvTemplateName,
			String csvTemplateComments, String csvTemplatePrefix, Date csvTemplateExecStartDate,
			int csvTemplateExecStartHour, Date csvTemplateExecEndDate, Date createdDate, Date modifiedDate,
			FilesRepository inboundFileRepo, FilesRepository processedFileRepo, FilesRepository exceptionFileRepo,
			JobExecutionRepeat jobExecRepeat, Experiment experiment, SysUser createdBy, SysUser lastModifiedBy) {
		this.csvTemplateId = csvTemplateId;
		this.csvTemplateIsActive = csvTemplateIsActive;
		this.CsvTemplateName = csvTemplateName;
		this.csvTemplateComments = csvTemplateComments;
		this.csvTemplatePrefix = csvTemplatePrefix;
		this.csvTemplateExecStartDate = csvTemplateExecStartDate;
		this.csvTemplateExecStartHour = csvTemplateExecStartHour;
		this.csvTemplateExecEndDate = csvTemplateExecEndDate;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.inboundFileRepo = inboundFileRepo;
		this.processedFileRepo = processedFileRepo;
		this.exceptionFileRepo = exceptionFileRepo;
		this.jobExecRepeat = jobExecRepeat;
		this.experiment = experiment;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getCsvTemplateId() {
		return csvTemplateId;
	}

	public void setCsvTemplateId(Integer csvTemplateId) {
		this.csvTemplateId = csvTemplateId;
	}

	public String getCsvTemplateName() {
		return CsvTemplateName;
	}

	public void setCsvTemplateName(String csvTemplateName) {
		CsvTemplateName = csvTemplateName;
	}

	public String getCsvTemplateComments() {
		return csvTemplateComments;
	}

	public void setCsvTemplateComments(String csvTemplateComments) {
		this.csvTemplateComments = csvTemplateComments;
	}

	public String getCsvTemplatePrefix() {
		return csvTemplatePrefix;
	}

	public void setCsvTemplatePrefix(String csvTemplatePrefix) {
		this.csvTemplatePrefix = csvTemplatePrefix;
	}

	public Date getCsvTemplateExecStartDate() {
		return csvTemplateExecStartDate;
	}

	public void setCsvTemplateExecStartDate(Date csvTemplateExecStartDate) {
		this.csvTemplateExecStartDate = csvTemplateExecStartDate;
	}

	public int getCsvTemplateExecStartHour() {
		return csvTemplateExecStartHour;
	}

	public void setCsvTemplateExecStartHour(int csvTemplateExecStartHour) {
		this.csvTemplateExecStartHour = csvTemplateExecStartHour;
	}

	public Date getCsvTemplateExecEndDate() {
		return csvTemplateExecEndDate;
	}

	public void setCsvTemplateExecEndDate(Date csvTemplateExecEndDate) {
		this.csvTemplateExecEndDate = csvTemplateExecEndDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public FilesRepository getInboundFileRepo() {
		return inboundFileRepo;
	}

	public void setInboundFileRepo(FilesRepository inboundFileRepo) {
		this.inboundFileRepo = inboundFileRepo;
	}

	public FilesRepository getProcessedFileRepo() {
		return processedFileRepo;
	}

	public void setProcessedFileRepo(FilesRepository processedFileRepo) {
		this.processedFileRepo = processedFileRepo;
	}

	public FilesRepository getExceptionFileRepo() {
		return exceptionFileRepo;
	}

	public void setExceptionFileRepo(FilesRepository exceptionFileRepo) {
		this.exceptionFileRepo = exceptionFileRepo;
	}

	public JobExecutionRepeat getJobExecRepeat() {
		return jobExecRepeat;
	}

	public void setJobExecRepeat(JobExecutionRepeat jobExecRepeat) {
		this.jobExecRepeat = jobExecRepeat;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public SysUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}

	public SysUser getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(SysUser lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public boolean isCsvTemplateIsActive() {
		return csvTemplateIsActive;
	}

	public void setCsvTemplateIsActive(boolean csvTemplateIsActive) {
		this.csvTemplateIsActive = csvTemplateIsActive;
	}



}
