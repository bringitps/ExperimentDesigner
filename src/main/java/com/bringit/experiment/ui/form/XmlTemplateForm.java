package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.bll.XmlTemplateNode;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;
import com.bringit.experiment.ui.design.XmlTemplateDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window;

public class XmlTemplateForm extends XmlTemplateDesign {

	private XmlTemplate xmlt;
	private List<XmlTemplateNode> xmlNodes;
	private List<FilesRepository> repos =  new FilesRepositoryDao().getAllFilesRepositorys();
	private List<JobExecutionRepeat> jobs = new JobExecutionRepeatDao().getAllJobExecutionRepeats();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
	private int lastNewItemId = 0;
    private File tempFile;
    private Document doc;
    private List<ExperimentField> expFields;
	
	public XmlTemplateForm(int xmlId)
	{
		if(xmlId == -1) //New
		{
			uploadFile();
			this.btnDelete.setVisible(false);
			this.xmlt = new XmlTemplate();
			fillCombos();
			//we disable all the components until an experiment is selected
			enableComponents(false); 
			this.tblXmlNodes.setContainerDataSource(null);
			this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("ParentNode", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("IsRoot", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("IsField", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Experiment Field", ComboBox.class, null);
			this.tblXmlNodes.addContainerProperty("Xml Node Name", TextField.class, null);

		}
		else
		{
			fillCombos();
			xmlt = new XmlTemplateDao().getXmlTemplateById(xmlId);
			this.txtXmlTName.setValue(xmlt.getXmlTemplateName());
			this.comboXmlTExperiment.setValue(xmlt.getExperiment().getExpId());
			this.comboXmlTExperiment.setEnabled(false);
			//this.comboXmljobScheduler.setValue(xmlt.getJobExecRepeat().getJobExecRepeatId());
			this.comboXmlTinRepo.setValue(xmlt.getInboundFileRepo().getFileRepoId());
			this.comboXmloutRepo.setValue(xmlt.getProcessedFileRepo().getFileRepoId());
			this.comboXmlTerrRepo.setValue(xmlt.getExceptionFileRepo().getFileRepoId());
			this.txtXmlTComments.setValue(xmlt.getXmlTemplateComments());
			this.txtXmlTPrefix.setValue(xmlt.getXmlTemplatePrefix());
			this.startXmlTstart.setValue(xmlt.getXmlTemplateExecStartDate());
			this.endXmlTstart.setValue(xmlt.getXmlTemplateExecEndDate());
			
			this.xmlNodes = new XmlTemplateNodeDao().getAllXmlTemplateNodesByTemplateId(xmlt.getXmlTemplateId());
			 
			this.tblXmlNodes.setContainerDataSource(null);
			this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("ParentNode", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("IsRoot", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("IsField", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Experiment Field", ComboBox.class, null);
			this.tblXmlNodes.addContainerProperty("Xml Node Name", TextField.class, null);
			
			this.expFields = new ExperimentFieldDao().getActiveExperimentFields(xmlt.getExperiment());
			
			Object[] itemValues = new Object[6];
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
							
				ComboBox cbxExpFields = new ComboBox("");
				if(this.xmlNodes.get(i).isXmlTemplateNodeIsAttributeValue()){
					for(int j=0; j<expFields.size(); j++)
					{
						cbxExpFields.addItem(expFields.get(j).getExpFieldId());
						cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName());
					}
					if(this.xmlNodes.get(i).getExpField()!=null){
						cbxExpFields.setValue(this.xmlNodes.get(i).getExpField().getExpFieldId());
					}
					cbxExpFields.setNullSelectionAllowed(false);
					cbxExpFields.setImmediate(true);
					cbxExpFields.addStyleName("small");
				}else{
					if(!this.xmlNodes.get(i).getXmlTemplateNodeName().equals("ExperimentResults"))
						cbxExpFields.setEnabled(false);
					else
						for(int j=0; j<expFields.size(); j++)
						{
							cbxExpFields.addItem(expFields.get(j).getExpFieldId());
							cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName());
						}
						if(this.xmlNodes.get(i).getExpField()!=null){
							cbxExpFields.setValue(this.xmlNodes.get(i).getExpField().getExpFieldId());
						}
						cbxExpFields.setNullSelectionAllowed(false);
						cbxExpFields.setImmediate(true);
						cbxExpFields.addStyleName("small");
				}
				
				itemValues[4] = cbxExpFields;
				
				TextField txtAttrName = new TextField();
				txtAttrName.setValue(this.xmlNodes.get(i).getXmlTemplateNodeAttributeName());
				txtAttrName.addStyleName("small");
				txtAttrName.setEnabled(false);
				itemValues[5] = txtAttrName;

				
				
				this.tblXmlNodes.addItem(itemValues, this.xmlNodes.get(i).getXmlTemplateNodeId());
			}
	
		}
		comboXmlTExperiment.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(comboXmlTExperiment.getValue()!=null){
					enableComponents(true);
				}
				
			}   
	    });
		btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}

		});
		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				closeModalWindow();
			}


		});
		btnDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onDelete();
			}

		});
		
	}
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	@SuppressWarnings("deprecation")
	protected void onSave() {
		Collection itemIds = this.tblXmlNodes.getContainerDataSource().getItemIds();
		boolean validateReqFieldsResult = validateRequiredFields();
		
		
		//---Validate Required Fields---//
		if(itemIds.size() > 0 && validateReqFieldsResult )
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			
			//Save template
			this.xmlt.setExperiment(new ExperimentDao().getExperimentById((int) this.comboXmlTExperiment.getValue()));
			this.xmlt.setXmlTemplateName(this.txtXmlTName.getValue());
			this.xmlt.setXmlTemplatePrefix(this.txtXmlTPrefix.getValue());
			this.xmlt.setXmlTemplateComments(this.txtXmlTComments.getValue());
			this.xmlt.setExceptionFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboXmlTerrRepo.getValue()));
			this.xmlt.setProcessedFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboXmloutRepo.getValue()));
			this.xmlt.setInboundFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboXmlTinRepo.getValue()));
			//this.xmlt.setJobExecRepeat(new JobExecutionRepeatDao().getJobExecutionRepeatById((int) this.comboXmljobScheduler.getValue()));
			this.xmlt.setLastModifiedBy(sessionUser);
			this.xmlt.setModifiedDate(new Date());
			this.xmlt.setXmlTemplateExecStartDate(this.startXmlTstart.getValue());
			this.xmlt.setXmlTemplateExecEndDate(this.endXmlTstart.getValue());
			if(this.xmlt.getXmlTemplateId() != null )
				new XmlTemplateDao().updateXmlTemplate(xmlt);
			else
			{
				this.xmlt.setCreatedBy(sessionUser);
				this.xmlt.setCreatedDate(this.xmlt.getModifiedDate());
				new XmlTemplateDao().addXmlTemplate(xmlt);
			}
			
			
			//Save XmlTemplateNodes
			XmlTemplateNodeDao xmlTempNodeDao = new XmlTemplateNodeDao();
			
			for (Object itemIdObj : itemIds) 
			{
				
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblXmlNodes.getContainerDataSource().getItem(itemId);
				
				XmlTemplateNode xmlNode = new XmlTemplateNode();
				xmlNode.setXmlTemplateNodeName(((TextField)(tblRowItem.getItemProperty("ParentNode").getValue())).getValue());
				xmlNode.setXmlTemplateNodeIsRoot(((CheckBox)(tblRowItem.getItemProperty("IsRoot").getValue())).getValue());
				xmlNode.setXmlTemplateNodeIsAttributeValue(((CheckBox)(tblRowItem.getItemProperty("IsField").getValue())).getValue());				
				xmlNode.setXmlTemplateNodeAttributeName((((TextField)(tblRowItem.getItemProperty("Xml Node Name").getValue())).getValue()));
				xmlNode.setXmlTemplate(xmlt);
				
				if(xmlNode.isXmlTemplateNodeIsAttributeValue()){
					ExperimentField selectedExpField = new ExperimentField();
					selectedExpField.setExpFieldId((int)((ComboBox)(tblRowItem.getItemProperty("Experiment Field").getValue())).getValue());
					xmlNode.setExpField(selectedExpField);
				}
				if(itemId > 0)
				{
					xmlNode.setXmlTemplateNodeId(itemId);
					xmlTempNodeDao.updateXmlTemplateNode(xmlNode);;
				}
				else
					xmlTempNodeDao.addXmlTemplateNode(xmlNode);
				
			}
		
			closeModalWindow();
		}
		else
		{
			if(itemIds.size() <= 0)
				this.getUI().showNotification("There are no xml nodes mapped on your XmlTemplate", Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
		}
	}
	private boolean validateRequiredFields()
	{
		if(!this.txtXmlTName.isValid()) return false;
		if(!this.txtXmlTPrefix.isValid()) return false;
		if(!((ComboBox)(this.comboXmloutRepo)).isValid()) return false;
		if(!((ComboBox)(this.comboXmlTerrRepo)).isValid()) return false;
		if(!((ComboBox)(this.comboXmlTinRepo)).isValid()) return false;
		if(!((ComboBox)(this.comboXmlTExperiment)).isValid()) return false;
		
		Collection itemIds = this.tblXmlNodes.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblXmlNodes.getContainerDataSource().getItem(itemId);
			if(!((ComboBox)(tblRowItem.getItemProperty("Experiment Field").getValue())).isValid()) return false;
			
		}
		
		return true;
	}
	@SuppressWarnings("deprecation")
	private void uploadFile() {

        class MyReceiver implements Receiver {
            private static final long serialVersionUID = -1276759102490466761L;

            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Output stream to write to
                try {
                    // Open the file for writing.
                	tempFile = File.createTempFile("temp", ".xml");
                    fos = new FileOutputStream(tempFile);
                }  catch (IOException e) {
      	          e.printStackTrace();
    	          return null;
                }
                return fos; // Return the output stream to write to
            }
        };
        final MyReceiver uploader = new MyReceiver(); 
        upXml.setReceiver(uploader);
	    upXml.addFinishedListener(new Upload.FinishedListener() {
	        @Override
	        public void uploadFinished(Upload.FinishedEvent finishedEvent) {
	          try {
	            FileReader reader = new FileReader(tempFile);
	            SAXBuilder builder = new SAXBuilder(); 
	            doc = builder.build(reader);
	            if (doc != null){ 
	            	fillNodes();
	            	comboXmlTExperiment.setEnabled(false);
	            }
	            reader.close();
	            tempFile.delete();
	          } catch (IOException | JDOMException e) {
	            e.printStackTrace();
	          }
	        }
	      });
		
	}
	private void enableComponents(boolean b) {
		this.txtXmlTName.setEnabled(b);
		this.txtXmlTComments.setEnabled(b);
		this.txtXmlTPrefix.setEnabled(b);
		this.comboXmloutRepo.setEnabled(b);
		this.comboXmlTerrRepo.setEnabled(b);
		this.comboXmlTinRepo.setEnabled(b);
		this.tblXmlNodes.setEnabled(b);
		this.upXml.setEnabled(b);
		this.comboXmljobScheduler.setEnabled(b);
	}
	private void fillNodes() {
		ExperimentDao expdao = new ExperimentDao();
		Experiment expNew = expdao.getExperimentById((int)(this.comboXmlTExperiment.getValue()));
		expFields = new ExperimentFieldDao().getActiveExperimentFields(expNew);
		//we loop thru the xml doc and get unique nodes in a hashmap including parent elements
		// currently supporting 2 levels deep, we can create a recursively function if needed.
		LinkedHashMap<String,String> nodes = new LinkedHashMap<String,String>();
		nodes.put(doc.getRootElement().getName(), "root");
		List<Element> elements = doc.getRootElement().getChildren();
		for (Element element : elements) {
			if(!nodes.containsKey(element.getName())){
				nodes.put(element.getName(), element.getParentElement().getName());
				if(!element.getChildren().isEmpty()){
					List<Element> subElements = element.getChildren();
					for (Element subs : subElements) {
						if(!nodes.containsKey(subs.getName())){
							nodes.put(subs.getName(), subs.getParentElement().getName());
						}
					}
				}
			}
		}
		Object[] itemValues = new Object[6];
		for(String nodKey : nodes.keySet())
		{		
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;
			
			TextField txtFieldName = new TextField();
			txtFieldName.setValue(nodes.get(nodKey));
			txtFieldName.setEnabled(false);
			txtFieldName.addStyleName("small");
			itemValues[1] = txtFieldName;
			
			CheckBox chxIsRoot = new CheckBox();
			chxIsRoot.setValue(nodes.get(nodKey).equals("root") ? true : false);
			chxIsRoot.addStyleName("small");
			itemValues[2] = chxIsRoot;
			
			CheckBox chxIsAttribute = new CheckBox();
			chxIsAttribute.setValue(nodes.get(nodKey).equals("ExperimentResults") ? true : false);
			chxIsAttribute.addStyleName("small");
			itemValues[3] = chxIsAttribute;
			
			ComboBox cbxExpFields = new ComboBox("");
			if(nodes.get(nodKey).equals("ExperimentResults")){
				for(int j=0; j<expFields.size(); j++)
				{
					cbxExpFields.addItem(expFields.get(j).getExpFieldId());
					cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName());
					//cbxExpFields.setWidth(100, Unit.PIXELS);
				}
				
				cbxExpFields.setNullSelectionAllowed(false);
				cbxExpFields.addStyleName("small");
			}else{
				cbxExpFields.setEnabled(false);
			}
			itemValues[4] = cbxExpFields;
			
			TextField txtAttrName = new TextField();
			txtAttrName.setValue(nodKey);
			txtAttrName.addStyleName("small");
			txtAttrName.setEnabled(false);
			itemValues[5] = txtAttrName;
			

			
			this.lastNewItemId = this.lastNewItemId - 1;
			this.tblXmlNodes.addItem(itemValues, this.lastNewItemId);

		}
		
	}

	private void fillCombos() {
		//Experiments
		for(int j=0; j<experiments.size(); j++)
		{
			this.comboXmlTExperiment.addItem(experiments.get(j).getExpId());
			this.comboXmlTExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
			//this.comboXmlTExperiment.setWidth(100, Unit.PIXELS);
		}
		
		this.comboXmlTExperiment.setNullSelectionAllowed(false);
		this.comboXmlTExperiment.setImmediate(true);
		this.comboXmlTExperiment.addStyleName("small");
		
		//File Repos
		for(int j=0; j<repos.size(); j++)
		{
			this.comboXmlTinRepo.addItem(repos.get(j).getFileRepoId());
			this.comboXmlTinRepo.setItemCaption(repos.get(j).getFileRepoId(), repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboXmlTinRepo.setWidth(100, Unit.PIXELS);
			
			this.comboXmloutRepo.addItem(repos.get(j).getFileRepoId());
			this.comboXmloutRepo.setItemCaption(repos.get(j).getFileRepoId(), repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboXmloutRepo.setWidth(100, Unit.PIXELS);
			
			this.comboXmlTerrRepo.addItem(repos.get(j).getFileRepoId());
			this.comboXmlTerrRepo.setItemCaption(repos.get(j).getFileRepoId(), repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboXmlTerrRepo.setWidth(100, Unit.PIXELS);
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
			this.comboXmljobScheduler.setItemCaption(jobs.get(j).getJobExecRepeatId(), jobs.get(j).getJobExecRepeatName());
		}
		
		this.comboXmljobScheduler.setNullSelectionAllowed(false);
		this.comboXmljobScheduler.setImmediate(true);
		this.comboXmljobScheduler.addStyleName("small");
		
	}
	private void onDelete()
	{	 
		int xmlTemplateId = this.xmlt.getXmlTemplateId();	
		XmlTemplateNodeDao xmlNodeDao = new XmlTemplateNodeDao();
		this.xmlNodes = xmlNodeDao.getAllXmlTemplateNodesByTemplateId(xmlTemplateId);
		for (XmlTemplateNode node : xmlNodes) {
			xmlNodeDao.deleteXmlTemplateNode(node.getXmlTemplateNodeId());
		}
		XmlTemplateDao xmlDao = new XmlTemplateDao();
		xmlDao.deleteXmlTemplate(xmlTemplateId);
		closeModalWindow();
    }
	
}
