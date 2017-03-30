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
@Table(name="XmlTemplate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class XmlTemplate {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="XmlTemplateId")
	private Integer xmlTemplateId;

	@Column(name="XmlTemplateIsActive")
	private Boolean xmlTemplateIsActive;

	@Column(name="XmlTemplateName")
	private String xmlTemplateName;
	
	@Column(name="XmlTemplateComments")
	@Type(type="text")
	private String xmlTemplateComments;
	
	@Column(name="XmlTemplatePrefix")
	private String xmlTemplatePrefix;
	
	@Column(name="XmlTemplateExecStartDate")
	private Date xmlTemplateExecStartDate;
	
	@Column(name="XmlTemplateExecStartHour")
	private Integer xmlTemplateExecStartHour;
	
	@Column(name="XmlTemplateExecEndDate")
	private Date xmlTemplateExecEndDate;
	
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

	public XmlTemplate() {
		this.xmlTemplateId = null;
		this.xmlTemplateIsActive = null;
		this.xmlTemplateName = null;
		this.xmlTemplateComments = null;
		this.xmlTemplatePrefix = null;
		this.xmlTemplateExecStartDate = null;
		this.xmlTemplateExecStartHour = null;
		this.xmlTemplateExecEndDate = null;
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
	

	public XmlTemplate(Integer xmlTemplateId, Boolean xmlTemplateIsActive, String xmlTemplateName,
			String xmlTemplateComments, String xmlTemplatePrefix, Date xmlTemplateExecStartDate,
			Integer xmlTemplateExecStartHour, Date xmlTemplateExecEndDate, Date createdDate, Date modifiedDate,
			ContractManufacturer contractManufacturer, FilesRepository inboundFileRepo,
			FilesRepository processedFileRepo, FilesRepository exceptionFileRepo, JobExecutionRepeat jobExecRepeat,
			Experiment experiment, SysUser createdBy, SysUser lastModifiedBy) {
		this.xmlTemplateId = xmlTemplateId;
		this.xmlTemplateIsActive = xmlTemplateIsActive;
		this.xmlTemplateName = xmlTemplateName;
		this.xmlTemplateComments = xmlTemplateComments;
		this.xmlTemplatePrefix = xmlTemplatePrefix;
		this.xmlTemplateExecStartDate = xmlTemplateExecStartDate;
		this.xmlTemplateExecStartHour = xmlTemplateExecStartHour;
		this.xmlTemplateExecEndDate = xmlTemplateExecEndDate;
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


	public Integer getXmlTemplateId() {
		return xmlTemplateId;
	}

	public void setXmlTemplateId(Integer xmlTemplateId) {
		this.xmlTemplateId = xmlTemplateId;
	}

	public Boolean isXmlTemplateIsActive() {
		return xmlTemplateIsActive;
	}

	public void setXmlTemplateIsActive(Boolean xmlTemplateIsActive) {
		this.xmlTemplateIsActive = xmlTemplateIsActive;
	}

	public String getXmlTemplateName() {
		return xmlTemplateName;
	}

	public void setXmlTemplateName(String xmlTemplateName) {
		this.xmlTemplateName = xmlTemplateName;
	}


	public String getXmlTemplateComments() {
		return xmlTemplateComments;
	}

	public void setXmlTemplateComments(String xmlTemplateComments) {
		this.xmlTemplateComments = xmlTemplateComments;
	}

	public String getXmlTemplatePrefix() {
		return xmlTemplatePrefix;
	}

	public void setXmlTemplatePrefix(String xmlTemplatePrefix) {
		this.xmlTemplatePrefix = xmlTemplatePrefix;
	}

	public Date getXmlTemplateExecStartDate() {
		return xmlTemplateExecStartDate;
	}

	public void setXmlTemplateExecStartDate(Date xmlTemplateExecStartDate) {
		this.xmlTemplateExecStartDate = xmlTemplateExecStartDate;
	}

	public Date getXmlTemplateExecEndDate() {
		return xmlTemplateExecEndDate;
	}

	public void setXmlTemplateExecEndDate(Date xmlTemplateExecEndDate) {
		this.xmlTemplateExecEndDate = xmlTemplateExecEndDate;
	}

	public Integer getXmlTemplateExecStartHour() {
		return xmlTemplateExecStartHour;
	}

	public void setXmlTemplateExecStartHour(Integer xmlTemplateExecStartHour) {
		this.xmlTemplateExecStartHour = xmlTemplateExecStartHour;
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

	public JobExecutionRepeat getJobExecRepeat() {
		return jobExecRepeat;
	}

	public void setJobExecRepeat(JobExecutionRepeat jobExecRepeat) {
		this.jobExecRepeat = jobExecRepeat;
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

	public FilesRepository getExceptionFileRepo() {
		return exceptionFileRepo;
	}

	public void setExceptionFileRepo(FilesRepository exceptionFileRepo) {
		this.exceptionFileRepo = exceptionFileRepo;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}


	
}
