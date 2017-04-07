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
@Table(name="CsvTemplate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
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
	@Type(type="text")
	private String csvTemplateComments;
	
	@Column(name="CsvTemplatePrefix")
	private String csvTemplatePrefix;

	@Column(name="CsvTemplateNotScheduled")
	private Boolean csvTemplateNotScheduled;
	
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
    @JoinColumn(name="CmId", unique=false, updatable=true)
	private ContractManufacturer contractManufacturer;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="InboundFileRepoId", unique=false, updatable=true)
	private FilesRepository inboundFileRepo;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ProcessedFileRepoId", unique=false, updatable=true)
	private FilesRepository processedFileRepo;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExceptionFileRepoId", unique=false, updatable=true)
	private FilesRepository exceptionFileRepo;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="JobExecRepeatId", unique=false, updatable=true)
	private JobExecutionRepeat jobExecRepeat;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExperimentId", unique=false, updatable=true)
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
		this.csvTemplateNotScheduled = null;
		this.csvTemplateExecStartDate = null;
		this.csvTemplateExecStartHour = null;
		this.csvTemplateExecEndDate = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.contractManufacturer = null;
		this.inboundFileRepo = null;
		this.processedFileRepo = null;
		this.exceptionFileRepo = null;
		this.jobExecRepeat = null;
		this.experiment = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}
	
	
	public CsvTemplate(Integer csvTemplateId, Boolean csvTemplateIsActive, String csvTemplateName,
			String csvTemplateComments, String csvTemplatePrefix, Boolean csvTemplateNotScheduled, Date csvTemplateExecStartDate,
			Integer csvTemplateExecStartHour, Date csvTemplateExecEndDate, Date createdDate, Date modifiedDate,
			ContractManufacturer contractManufacturer, FilesRepository inboundFileRepo,
			FilesRepository processedFileRepo, FilesRepository exceptionFileRepo, JobExecutionRepeat jobExecRepeat,
			Experiment experiment, SysUser createdBy, SysUser lastModifiedBy) {
		this.csvTemplateId = csvTemplateId;
		this.csvTemplateIsActive = csvTemplateIsActive;
		this.CsvTemplateName = csvTemplateName;
		this.csvTemplateComments = csvTemplateComments;
		this.csvTemplatePrefix = csvTemplatePrefix;
		this.csvTemplateNotScheduled = csvTemplateNotScheduled;
		this.csvTemplateExecStartDate = csvTemplateExecStartDate;
		this.csvTemplateExecStartHour = csvTemplateExecStartHour;
		this.csvTemplateExecEndDate = csvTemplateExecEndDate;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.contractManufacturer = contractManufacturer;
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
		this.CsvTemplateName = csvTemplateName;
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

	public Boolean getCsvTemplateNotScheduled() {
		return csvTemplateNotScheduled;
	}


	public void setCsvTemplateNotScheduled(Boolean csvTemplateNotScheduled) {
		this.csvTemplateNotScheduled = csvTemplateNotScheduled;
	}


	public Boolean getCsvTemplateIsActive() {
		return csvTemplateIsActive;
	}


	public Date getCsvTemplateExecStartDate() {
		return csvTemplateExecStartDate;
	}

	public void setCsvTemplateExecStartDate(Date csvTemplateExecStartDate) {
		this.csvTemplateExecStartDate = csvTemplateExecStartDate;
	}

	public Integer getCsvTemplateExecStartHour() {
		return csvTemplateExecStartHour;
	}

	public void setCsvTemplateExecStartHour(Integer csvTemplateExecStartHour) {
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

	public ContractManufacturer getContractManufacturer() {
		return contractManufacturer;
	}


	public void setContractManufacturer(ContractManufacturer contractManufacturer) {
		this.contractManufacturer = contractManufacturer;
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

	public Boolean isCsvTemplateIsActive() {
		return csvTemplateIsActive;
	}

	public void setCsvTemplateIsActive(Boolean csvTemplateIsActive) {
		this.csvTemplateIsActive = csvTemplateIsActive;
	}



}
