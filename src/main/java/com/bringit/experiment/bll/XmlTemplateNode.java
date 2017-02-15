package com.bringit.experiment.bll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="XmlTemplateNode")
public class XmlTemplateNode {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="XmlTemplateNodeId")
	private Integer xmlTemplateNodeId;

	@Column(name="XmlTemplateNodeName")
	private String xmlTemplateNodeName;
	
	@Column(name="XmlTemplateNodeIsAttributeValue")
	private boolean xmlTemplateNodeIsAttributeValue;
	
	@Column(name="XmlTemplateNodeAttributeName")
	private String xmlTemplateNodeAttributeName;
	
	@OneToOne
    @JoinColumn(name="XmlTemplateId", unique=false, updatable=false)
	private XmlTemplate xmlTemplate;
	
	@OneToOne
    @JoinColumn(name="ExpFieldId", unique=false, updatable=false)
	private ExperimentField expField;

	public Integer getXmlTemplateNodeId() {
		return xmlTemplateNodeId;
	}

	public void setXmlTemplateNodeId(Integer xmlTemplateNodeId) {
		this.xmlTemplateNodeId = xmlTemplateNodeId;
	}

	public String getXmlTemplateNodeName() {
		return xmlTemplateNodeName;
	}

	public void setXmlTemplateNodeName(String xmlTemplateNodeName) {
		this.xmlTemplateNodeName = xmlTemplateNodeName;
	}

	public boolean isXmlTemplateNodeIsAttributeValue() {
		return xmlTemplateNodeIsAttributeValue;
	}

	public void setXmlTemplateNodeIsAttributeValue(boolean xmlTemplateNodeIsAttributeValue) {
		this.xmlTemplateNodeIsAttributeValue = xmlTemplateNodeIsAttributeValue;
	}

	public String getXmlTemplateNodeAttributeName() {
		return xmlTemplateNodeAttributeName;
	}

	public void setXmlTemplateNodeAttributeName(String xmlTemplateNodeAttributeName) {
		this.xmlTemplateNodeAttributeName = xmlTemplateNodeAttributeName;
	}

	public XmlTemplate getXmlTemplate() {
		return xmlTemplate;
	}

	public void setXmlTemplate(XmlTemplate xmlTemplate) {
		this.xmlTemplate = xmlTemplate;
	}

	public ExperimentField getExpField() {
		return expField;
	}

	public void setExpField(ExperimentField expField) {
		this.expField = expField;
	}
	
	
	
}
