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

@Entity
@Table(name="XmlDataLoadExecutionResult")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class XmlDataLoadExecutionResult {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="XmlDataLoadExecId")
	private Integer xmlDataLoadExecId;

	@Column(name="XmlDataLoadExecException")
	private Boolean xmlDataLoadExecException;
	
	@Column(name="XmlDataLoadExecExceptionDetails")
	private String xmlDataLoadExecExeptionDetails;
	
	@Column(name="XmlDataLoadExecTime")
	private Date xmlDataLoadExecTime;

	@Column(name="XmlDataLoadTotalRecords")
	private Integer xmlDataLoadTotalRecords;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DataFileId", unique=false, updatable=true)
	private DataFile dataFile;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="XmlTemplateId", unique=false, updatable=true)
	private XmlTemplate xmlTemplate;

	
	public XmlDataLoadExecutionResult() {
		this.xmlDataLoadExecId = null;
		this.xmlDataLoadExecException = null;
		this.xmlDataLoadExecExeptionDetails = null;
		this.xmlDataLoadExecTime = null;
		this.xmlDataLoadTotalRecords = null;
		this.dataFile = null;
		this.xmlTemplate = null;
	}
	
	public XmlDataLoadExecutionResult(Integer xmlDataLoadExecId, boolean xmlDataLoadExecException,
			String xmlDataLoadExecExeptionDetails, Date xmlDataLoadExecTime, 
			Integer xmlDataLoadTotalRecords, DataFile dataFile,	XmlTemplate xmlTemplate) {
		this.xmlDataLoadExecId = xmlDataLoadExecId;
		this.xmlDataLoadExecException = xmlDataLoadExecException;
		this.xmlDataLoadExecExeptionDetails = xmlDataLoadExecExeptionDetails;
		this.xmlDataLoadExecTime = xmlDataLoadExecTime;
		this.xmlDataLoadTotalRecords = xmlDataLoadTotalRecords;
		this.dataFile = dataFile;
		this.xmlTemplate = xmlTemplate;
	}

	public Integer getXmlDataLoadExecId() {
		return xmlDataLoadExecId;
	}

	public void setXmlDataLoadExecId(Integer xmlDataLoadExecId) {
		this.xmlDataLoadExecId = xmlDataLoadExecId;
	}

	public boolean getXmlDataLoadExecException() {
		return xmlDataLoadExecException;
	}

	public void setXmlDataLoadExecException(boolean xmlDataLoadExecException) {
		this.xmlDataLoadExecException = xmlDataLoadExecException;
	}

	public String getXmlDataLoadExecExeptionDetails() {
		return xmlDataLoadExecExeptionDetails;
	}

	public void setXmlDataLoadExecExeptionDetails(String xmlDataLoadExecExeptionDetails) {
		this.xmlDataLoadExecExeptionDetails = xmlDataLoadExecExeptionDetails;
	}

	public DataFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}

	public Date getXmlDataLoadExecTime() {
		return xmlDataLoadExecTime;
	}

	public void setXmlDataLoadExecTime(Date xmlDataLoadExecTime) {
		this.xmlDataLoadExecTime = xmlDataLoadExecTime;
	}

	public Integer getXmlDataLoadTotalRecords() {
		return xmlDataLoadTotalRecords;
	}

	public void setXmlDataLoadTotalRecords(Integer xmlDataLoadTotalRecords) {
		this.xmlDataLoadTotalRecords = xmlDataLoadTotalRecords;
	}

	public void setXmlDataLoadExecException(Boolean xmlDataLoadExecException) {
		this.xmlDataLoadExecException = xmlDataLoadExecException;
	}

	public XmlTemplate getXmlTemplate() {
		return xmlTemplate;
	}

	public void setXmlTemplate(XmlTemplate xmlTemplate) {
		this.xmlTemplate = xmlTemplate;
	}
	
}
