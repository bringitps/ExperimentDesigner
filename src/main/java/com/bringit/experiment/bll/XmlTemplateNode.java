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
@Table(name="XmlTemplateNode")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class XmlTemplateNode {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="XmlTemplateNodeId")
	private Integer xmlTemplateNodeId;
	
	@Column(name="XmlTemplateNodeName")
	private String xmlTemplateNodeName;

	@Column(name="XmlTemplateNodeIsLoop")
	private Boolean xmlTemplateNodeIsLoop;

	@Column(name="XmlTemplateNodeIsAttribute")
	private Boolean xmlTemplateNodeIsAttribute;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="XmlTemplateId", unique=false, updatable=true)
	private XmlTemplate xmlTemplate;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ExpFieldId", unique=false, updatable=true)
	private ExperimentField expField;

	public XmlTemplateNode() {
		this.xmlTemplateNodeId = null;
		this.xmlTemplateNodeName = null;
		this.xmlTemplateNodeIsLoop = null;
		this.xmlTemplateNodeIsAttribute = null;
		this.xmlTemplate = null;
		this.expField = null;
	}
	
	public XmlTemplateNode(Integer xmlTemplateNodeId, String xmlTemplateNodeName, boolean xmlTemplateNodeIsLoop,
			boolean xmlTemplateNodeIsAttribute, XmlTemplate xmlTemplate, ExperimentField expField) {
		this.xmlTemplateNodeId = xmlTemplateNodeId;
		this.xmlTemplateNodeName = xmlTemplateNodeName;
		this.xmlTemplateNodeIsLoop = xmlTemplateNodeIsLoop;
		this.xmlTemplateNodeIsAttribute = xmlTemplateNodeIsAttribute;
		this.xmlTemplate = xmlTemplate;
		this.expField = expField;
	}

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

	public boolean isXmlTemplateNodeIsLoop() {
		return xmlTemplateNodeIsLoop;
	}

	public void setXmlTemplateNodeIsLoop(boolean xmlTemplateNodeIsLoop) {
		this.xmlTemplateNodeIsLoop = xmlTemplateNodeIsLoop;
	}

	public boolean isXmlTemplateNodeIsAttribute() {
		return xmlTemplateNodeIsAttribute;
	}

	public void setXmlTemplateNodeIsAttribute(boolean xmlTemplateNodeIsAttribute) {
		this.xmlTemplateNodeIsAttribute = xmlTemplateNodeIsAttribute;
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
