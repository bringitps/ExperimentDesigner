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
@Table(name="XmlTemplate")
public class XmlTemplate {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="XmlTemplateId")
	private Integer xmlTemplateId;

	@Column(name="XmlTemplateName")
	private String xmlTemplateName;
	
	@Column(name="XmlTemplateComments")
	private String xmlTemplateComments;
	
	@Column(name="XmlTemplatePrefix")
	private String xmlTemplatePrefix;
	
	@Column(name="XmlTemplateExecStartDate")
	private Date xmlTemplateExecStartDate;
	
	@Column(name="XmlTemplateExecEndDate")
	private Date xmlTemplateExecEndDate;
	
	@Column(name="XmlTemplateExecHours")
	private int xmlTemplateExecHours;
	
	@Column(name="XmlTemplateExecMinutes")
	private int xmlTemplateExecMinutes;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@OneToOne
    @JoinColumn(name="InboundFileRepoId", unique=false, updatable=false)
	private FilesRepository inboundFileRepo;
	
	@OneToOne
    @JoinColumn(name="ProcessedFileRepoId", unique=false, updatable=false)
	private FilesRepository processedFileRepo;
	
	@OneToOne
    @JoinColumn(name="JobExecRepeatId", unique=false, updatable=false)
	private JobExecutionRepeat jobExecRepeat;
	
	@OneToOne
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;

	public Integer getXmlTemplateId() {
		return xmlTemplateId;
	}

	public void setXmlTemplateId(Integer xmlTemplateId) {
		this.xmlTemplateId = xmlTemplateId;
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

	public int getXmlTemplateExecHours() {
		return xmlTemplateExecHours;
	}

	public void setXmlTemplateExecHours(int xmlTemplateExecHours) {
		this.xmlTemplateExecHours = xmlTemplateExecHours;
	}

	public int getXmlTemplateExecMinutes() {
		return xmlTemplateExecMinutes;
	}

	public void setXmlTemplateExecMinutes(int xmlTemplateExecMinutes) {
		this.xmlTemplateExecMinutes = xmlTemplateExecMinutes;
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


	
}
