package com.bringit.experiment.ui.form;

import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.bll.XmlTemplateNode;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;
import com.bringit.experiment.ui.design.XmlTemplateDesign;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class XmlTemplateForm extends XmlTemplateDesign {

	private XmlTemplate xmlt;
	private List<XmlTemplateNode> xmlNodes;
	private List<FilesRepository> repos =  new FilesRepositoryDao().getAllFilesRepositorys();
	private List<JobExecutionRepeat> jobs = new JobExecutionRepeatDao().getAllJobExecutionRepeats();
	private List<Experiment> experiments = new ExperimentDao().getAllExperiments();
	private int lastNewItemId = 0;
	
	public XmlTemplateForm(int xmlId)
	{
		if(xmlId == -1) //New
		{
			this.xmlt = new XmlTemplate();
			fillCombos();
			this.tblXmlNodes.setContainerDataSource(null);
			this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Name", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("IsRoot", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("IsAttribute", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Attribute Name", TextField.class, null);
			Object[] itemValues = new Object[5];

				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;
				
				TextField txtFieldName = new TextField();
				txtFieldName.setEnabled(false);
				txtFieldName.addStyleName("small");
				itemValues[1] = txtFieldName;
				
				CheckBox chxIsRoot = new CheckBox();
				chxIsRoot.addStyleName("small");
				itemValues[2] = chxIsRoot;
				
				CheckBox chxIsAttribute = new CheckBox();
				chxIsAttribute.addStyleName("small");
				itemValues[3] = chxIsAttribute;
				
				
				TextField txtAttrName = new TextField();
				txtAttrName.addStyleName("small");
				itemValues[4] = txtAttrName;

				this.lastNewItemId = this.lastNewItemId - 1;
				this.tblXmlNodes.addItem(itemValues, this.lastNewItemId);
				this.tblXmlNodes.select(this.lastNewItemId);
		}
		else
		{
			fillCombos();
			xmlt = new XmlTemplateDao().getXmlTemplateById(xmlId);
			this.txtXmlTName.setValue(xmlt.getXmlTemplateName());
			this.comboXmlTExperiment.setValue(xmlt.getExperiment());
			this.comboXmljobScheduler.setValue(xmlt.getJobExecRepeat());
			this.comboXmlTinRepo.setValue(xmlt.getInboundFileRepo());
			this.comboXmloutRepo.setValue(xmlt.getProcessedFileRepo());
			this.comboXmlTerrRepo.setValue(xmlt.getExceptionFileRepo());
			this.txtXmlTComments.setValue(xmlt.getXmlTemplateComments());
			this.txtXmlTPrefix.setValue(xmlt.getXmlTemplatePrefix());
			
			this.xmlNodes = new XmlTemplateNodeDao().getAllXmlTemplateNodesByTemplateId(xmlt.getXmlTemplateId());
			 
			this.tblXmlNodes.setContainerDataSource(null);
			this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Name", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("IsRoot", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("IsAttribute", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Attribute Name", TextField.class, null);
			
			Object[] itemValues = new Object[5];
			for(int i=0; i<this.xmlNodes.size(); i++)
			{		
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;
				
				TextField txtFieldName = new TextField();
				txtFieldName.setValue(this.xmlNodes.get(i).getXmlTemplateNodeName());
				txtFieldName.setEnabled(false);
				txtFieldName.addStyleName("small");
				itemValues[1] = txtFieldName;
				
				CheckBox chxIsRoot = new CheckBox();
				chxIsRoot.setValue(this.xmlNodes.get(i).isXmlTemplateNodeIsRoot() ? true : false);
				chxIsRoot.addStyleName("small");
				itemValues[2] = chxIsRoot;
				
				CheckBox chxIsAttribute = new CheckBox();
				chxIsAttribute.setValue(this.xmlNodes.get(i).isXmlTemplateNodeIsAttributeValue() ? true : false);
				chxIsAttribute.addStyleName("small");
				itemValues[3] = chxIsAttribute;
				
				
				TextField txtAttrName = new TextField();
				txtAttrName.setValue(this.xmlNodes.get(i).getXmlTemplateNodeAttributeName());
				txtAttrName.addStyleName("small");
				itemValues[4] = txtAttrName;
				
				this.tblXmlNodes.addItem(itemValues, this.xmlNodes.get(i).getXmlTemplateNodeId());
			}
			
		}
	}


	private void fillCombos() {
		//Experiments
		for(int j=0; j<experiments.size(); j++)
		{
			this.comboXmlTExperiment.addItem(experiments.get(j).getExpId());
			this.comboXmlTExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
			this.comboXmlTExperiment.setWidth(100, Unit.PIXELS);
		}
		
		this.comboXmlTExperiment.setNullSelectionAllowed(false);
		this.comboXmlTExperiment.setImmediate(true);
		this.comboXmlTExperiment.addStyleName("small");
		
		//File Repos
		for(int j=0; j<repos.size(); j++)
		{
			this.comboXmlTinRepo.addItem(repos.get(j).getFileRepoId());
			this.comboXmlTinRepo.setItemCaption(repos.get(j).getFileRepoId(), repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			this.comboXmlTinRepo.setWidth(100, Unit.PIXELS);
			
			this.comboXmloutRepo.addItem(repos.get(j).getFileRepoId());
			this.comboXmloutRepo.setItemCaption(repos.get(j).getFileRepoId(), repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			this.comboXmloutRepo.setWidth(100, Unit.PIXELS);
			
			this.comboXmlTinRepo.addItem(repos.get(j).getFileRepoId());
			this.comboXmlTinRepo.setItemCaption(repos.get(j).getFileRepoId(), repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			this.comboXmlTinRepo.setWidth(100, Unit.PIXELS);
		}
		
		this.comboXmlTinRepo.setNullSelectionAllowed(false);
		this.comboXmlTinRepo.setImmediate(true);
		this.comboXmlTinRepo.addStyleName("small");
		
		this.comboXmloutRepo.setNullSelectionAllowed(false);
		this.comboXmloutRepo.setImmediate(true);
		this.comboXmloutRepo.addStyleName("small");
		
		this.comboXmlTerrRepo.setNullSelectionAllowed(false);
		this.comboXmlTerrRepo.setImmediate(true);
		this.comboXmlTerrRepo.addStyleName("small");
		
		//Jobs
		for(int j=0; j<jobs.size(); j++)
		{
			this.comboXmljobScheduler.addItem(jobs.get(j).getJobExecRepeatId());
			this.comboXmljobScheduler.setItemCaption(jobs.get(j).getJobExecRepeatId(), jobs.get(j).getJobExecRepeatLabel());
			this.comboXmljobScheduler.setWidth(100, Unit.PIXELS);
		}
		
		this.comboXmljobScheduler.setNullSelectionAllowed(false);
		this.comboXmljobScheduler.setImmediate(true);
		this.comboXmljobScheduler.addStyleName("small");
		
	}
	
}
