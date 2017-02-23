package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
	private List<Experiment> experiments = new ExperimentDao().getAllExperiments();
	private int lastNewItemId = 0;
	private Label status = new Label("Please select a file to upload");
    private ProgressIndicator pi = new ProgressIndicator();
    private MyReceiver receiver = new MyReceiver();
    private HorizontalLayout progressLayout = new HorizontalLayout();
    private File tempFile;
    private Document doc;
    private List<ExperimentField> expFields;
	
	public XmlTemplateForm(int xmlId)
	{
		if(xmlId == -1) //New
		{
			uploadFile();
			
			this.xmlt = new XmlTemplate();
			fillCombos();
			this.tblXmlNodes.setContainerDataSource(null);
			this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("ParentNode", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("IsRoot", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("IsField", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Name", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("Experiment Field", ComboBox.class, null);
			/*Object[] itemValues = new Object[5];

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
				*/
		}
		else
		{
			fillCombos();
			xmlt = new XmlTemplateDao().getXmlTemplateById(xmlId);
			this.txtXmlTName.setValue(xmlt.getXmlTemplateName());
			this.comboXmlTExperiment.setValue(xmlt.getExperiment().getExpId());
			//this.comboXmljobScheduler.setValue(xmlt.getJobExecRepeat().getJobExecRepeatId());
			this.comboXmlTinRepo.setValue(xmlt.getInboundFileRepo().getFileRepoId());
			//this.comboXmloutRepo.setValue(xmlt.getProcessedFileRepo().getFileRepoId());
			//this.comboXmlTerrRepo.setValue(xmlt.getExceptionFileRepo().getFileRepoId());
			this.txtXmlTComments.setValue(xmlt.getXmlTemplateComments());
			this.txtXmlTPrefix.setValue(xmlt.getXmlTemplatePrefix());
			
			this.xmlNodes = new XmlTemplateNodeDao().getAllXmlTemplateNodesByTemplateId(xmlt.getXmlTemplateId());
			 
			this.tblXmlNodes.setContainerDataSource(null);
			this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Parent", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("IsRoot", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("IsField", CheckBox.class, null);
			this.tblXmlNodes.addContainerProperty("Name", TextField.class, null);
			this.tblXmlNodes.addContainerProperty("Experiment Field", ComboBox.class, null);
			
			this.expFields = new ExperimentFieldDao().getActiveExperimentFields(xmlt.getExperiment());
			
			Object[] itemValues = new Object[6];
			for(int i=0; i<this.expFields.size(); i++)
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
				
				ComboBox cbxExpFields = new ComboBox("");
				
				for(int j=0; j<expFields.size(); j++)
				{
					cbxExpFields.addItem(expFields.get(j).getExpFieldId());
					cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName());
					cbxExpFields.setWidth(100, Unit.PIXELS);
				}
				//TODO validate that the node needs to be mapped or not
				//Because currently it fails when a node doesnt have an ExpField assigned
				//cbxExpFields.setValue(this.xmlNodes.get(i).getExpField().getExpFieldId());
				cbxExpFields.setNullSelectionAllowed(false);
				cbxExpFields.setImmediate(true);
				cbxExpFields.addStyleName("small");
				
				itemValues[5] = cbxExpFields;
				
				
				this.tblXmlNodes.addItem(itemValues, this.xmlNodes.get(i).getXmlTemplateNodeId());
			}
	
		}
	}


	@SuppressWarnings("deprecation")
	private void uploadFile() {
	    /* Create and configure the upload component */
	    
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
	            /* Let's build a container from the CSV File */
	            FileReader reader = new FileReader(tempFile);
	            SAXBuilder builder = new SAXBuilder(); 
	            doc = builder.build(reader);
	            if (doc != null){ 
	            	fillNodes();
	            }
	            reader.close();
	            tempFile.delete();
	          } catch (IOException | JDOMException e) {
	            e.printStackTrace();
	          }
	        }


	      });
		
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
			
			
			TextField txtAttrName = new TextField();
			txtAttrName.setValue(nodKey);
			txtAttrName.addStyleName("small");
			itemValues[4] = txtAttrName;
			
			ComboBox cbxExpFields = new ComboBox("");
			
			for(int j=0; j<expFields.size(); j++)
			{
				cbxExpFields.addItem(expFields.get(j).getExpFieldId());
				cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName());
				cbxExpFields.setWidth(100, Unit.PIXELS);
			}
			
			cbxExpFields.setNullSelectionAllowed(false);
			cbxExpFields.addStyleName("small");
			
			itemValues[5] = cbxExpFields;
			
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
    public static class MyReceiver implements Receiver {

        private String fileName;
        private String mtype;
        private boolean sleep;
        private int total = 0;

        public OutputStream receiveUpload(String filename, String mimetype) {
            fileName = filename;
            mtype = mimetype;
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    total++;
                    if (sleep && total % 10000 == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
        }

        public String getFileName() {
            return fileName;
        }

        public String getMimeType() {
            return mtype;
        }

        public void setSlow(boolean value) {
            sleep = value;
        }

    }
	
}
