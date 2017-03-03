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
@Table(name="XmlDataLoadExecutionResult")
public class XmlDataLoadExecutionResult {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="XmlDataLoadExecId")
	private Integer xmlDataLoadExecId;

	@Column(name="XmlDataLoadExecException")
	private boolean xmlDataLoadExecException;
	
	@Column(name="XmlDataLoadExecExceptionDetails")
	private String xmlDataLoadExecExeptionDetails;
	
	@Column(name="XmlDataLoadExecTime")
	private Date xmlDataLoadExecTime;

	@OneToOne
    @JoinColumn(name="DataFileId", unique=false, updatable=true)
	private DataFile dataFile;
	
	@OneToOne
    @JoinColumn(name="XmlTemplateId", unique=false, updatable=true)
	private XmlTemplate xmlTemplate;

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

	public XmlTemplate getXmlTemplate() {
		return xmlTemplate;
	}

	public void setXmlTemplate(XmlTemplate xmlTemplate) {
		this.xmlTemplate = xmlTemplate;
	}
	
}
