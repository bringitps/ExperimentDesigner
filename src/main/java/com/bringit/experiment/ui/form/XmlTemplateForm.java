package com.bringit.experiment.ui.form;

import com.bringit.experiment.bll.CmForSysRole;
import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.bll.XmlTemplateNode;
import com.bringit.experiment.dao.CmForSysRoleDao;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;
import com.bringit.experiment.remote.RemoteFileUtil;
import com.bringit.experiment.ui.design.XmlTemplateDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Window;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class XmlTemplateForm extends XmlTemplateDesign {

    private XmlTemplate xmlt;
    private List<XmlTemplateNode> xmlNodes;
    private List<FilesRepository> repos = new FilesRepositoryDao().getAllFilesRepositorys();
    private List<JobExecutionRepeat> jobs = new JobExecutionRepeatDao().getAllJobExecutionRepeats();
    private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
    SysRole sysRoleSession = (SysRole)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");

    private List<ContractManufacturer> contractManufacturers ;

    private int lastNewItemId = 0;
    private File tempFile;
    private List<ExperimentField> expFields;
    private Document xmlDocument;
    private List<Integer> dbIdOfXmlTemplateNodeItemsToDelete = new ArrayList<Integer>();

    private SystemSettings systemSettings;

    private void loadSpecificContractManufacturer() {
        contractManufacturers = new ArrayList<>();

        if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
            List<CmForSysRole> cmForSysRole =  new CmForSysRoleDao().getListOfCmForSysRoleBysysRoleId(sysRoleSession.getRoleId());
            cmForSysRole.forEach(x-> contractManufacturers.add(x.getContractManufacturer()));
        } else {
            contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
        }


    }

    public XmlTemplateForm(int xmlId) {
        
    	this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		this.comboXmlTExperiment.setCaption(this.systemSettings.getExperimentLabel());
    	loadSpecificContractManufacturer();

        this.tblXmlNodes.setContainerDataSource(null);
        this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
        this.tblXmlNodes.addContainerProperty("Xml Node Name", TextArea.class, null);
        this.tblXmlNodes.addContainerProperty("Is Loop Node", CheckBox.class, null);
        this.tblXmlNodes.addContainerProperty("Is Attribute", CheckBox.class, null);
        this.tblXmlNodes.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);
        this.tblXmlNodes.setPageLength(0);
        fillCombos();
        uploadFile();

        if (xmlId == -1) //New
        {
            this.btnDelete.setEnabled(false);
            this.xmlt = new XmlTemplate();
            this.chxActive.setValue(true);
            this.chxActive.setEnabled(false);
            this.btnDelete.setEnabled(false);
            //we disable all the components until an experiment is selected
            enableComponents(false);
        } else {
            xmlt = new XmlTemplateDao().getXmlTemplateById(xmlId);
            this.txtXmlTName.setValue(xmlt.getXmlTemplateName());
            this.comboXmlTExperiment.setValue(xmlt.getExperiment().getExpId());
            this.comboXmlTExperiment.setEnabled(false);
            this.chxActive.setValue(xmlt.isXmlTemplateIsActive());
            this.chxNotScheduled.setValue(xmlt.getXmlTemplateNotScheduled());

            if (xmlt.getContractManufacturer() != null)
                this.cbxContractManufacturer.setValue(xmlt.getContractManufacturer().getCmId());
            if (xmlt.getJobExecRepeat() != null)
                this.comboXmljobScheduler.setValue(xmlt.getJobExecRepeat().getJobExecRepeatId());
            if (xmlt.getInboundFileRepo() != null)
                this.comboXmlTinRepo.setValue(xmlt.getInboundFileRepo().getFileRepoId());
            if (xmlt.getProcessedFileRepo() != null)
                this.comboXmloutRepo.setValue(xmlt.getProcessedFileRepo().getFileRepoId());
            if (xmlt.getExceptionFileRepo() != null)
                this.comboXmlTerrRepo.setValue(xmlt.getExceptionFileRepo().getFileRepoId());
            this.txtXmlTComments.setValue(xmlt.getXmlTemplateComments());
            
            String opFilterSavedCriteria = "Prefix"; 
			
			if(xmlt.getXmlTemplatePrefix() != null)
			{
				opFilterSavedCriteria = "Prefix"; 
				this.txtXmlTFilterCriteria.setValue(xmlt.getXmlTemplatePrefix());				
			}
			
			if(xmlt.getXmlTemplateSuffix() != null)
			{
				opFilterSavedCriteria = "Suffix"; 
				this.txtXmlTFilterCriteria.setValue(xmlt.getXmlTemplateSuffix());				
			}
			
			if(xmlt.getXmlTemplateRegex() != null)
			{
				opFilterSavedCriteria = "Regular"; 
				this.txtXmlTFilterCriteria.setValue(xmlt.getXmlTemplateRegex());				
			}
			
			Collection itemIds = this.opFilterCriteriaType.getItemIds();
			for (Object itemIdObj : itemIds) 
			{
				if(((String)itemIdObj).startsWith(opFilterSavedCriteria))
					this.opFilterCriteriaType.select(itemIdObj);				
			}
			
            //this.txtXmlTFilterCriteria.setValue(xmlt.getXmlTemplatePrefix());
            this.startXmlTstart.setValue(xmlt.getXmlTemplateExecStartDate());
            this.endXmlTstart.setValue(xmlt.getXmlTemplateExecEndDate());

            this.xmlNodes = new XmlTemplateNodeDao().getAllXmlTemplateNodesByTemplateId(xmlt.getXmlTemplateId());
            this.expFields = new ExperimentFieldDao().getActiveExperimentFields(xmlt.getExperiment());
            this.cbxStartHour.setValue(xmlt.getXmlTemplateExecStartHour());

            if (xmlt.getXmlTemplateNotScheduled() != null) {
                cbxStartHour.setEnabled(!xmlt.getXmlTemplateNotScheduled());
                comboXmljobScheduler.setEnabled(!xmlt.getXmlTemplateNotScheduled());
                startXmlTstart.setEnabled(!xmlt.getXmlTemplateNotScheduled());
                endXmlTstart.setEnabled(!xmlt.getXmlTemplateNotScheduled());
            }

            Object[] itemValues = new Object[5];
            for (int i = 0; i < this.xmlNodes.size(); i++) {

                CheckBox chxSelect = new CheckBox();
                chxSelect.setVisible(false);
                itemValues[0] = chxSelect;

                TextArea txtAreaXmlNodeName = new TextArea();
                txtAreaXmlNodeName.setValue(this.xmlNodes.get(i).getXmlTemplateNodeName());
                txtAreaXmlNodeName.setReadOnly(true);
                txtAreaXmlNodeName.addStyleName("tiny");
                txtAreaXmlNodeName.setWidth(100, Unit.PERCENTAGE);
                txtAreaXmlNodeName.setRows(3);
                itemValues[1] = txtAreaXmlNodeName;

                CheckBox chxIsLoop = new CheckBox();
                chxIsLoop.setValue(this.xmlNodes.get(i).isXmlTemplateNodeIsLoop());
                chxIsLoop.setEnabled(!this.xmlNodes.get(i).isXmlTemplateNodeIsAttribute());
                chxIsLoop.addStyleName("tiny");
                itemValues[2] = chxIsLoop;

                CheckBox chxIsAttribute = new CheckBox();
                chxIsAttribute.setValue(this.xmlNodes.get(i).isXmlTemplateNodeIsAttribute());
                chxIsAttribute.setEnabled(false);
                chxIsAttribute.addStyleName("tiny");
                itemValues[3] = chxIsAttribute;

                ComboBox cbxExpFields = new ComboBox("");
                for (int j = 0; j < expFields.size(); j++) {
                    cbxExpFields.addItem(expFields.get(j).getExpFieldId());
                    cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName() + " [ " + expFields.get(j).getExpFieldType() + " ]");
                    cbxExpFields.setWidth(100, Unit.PERCENTAGE);
                }

                //cbxExpFields.setNullSelectionAllowed(false);
                cbxExpFields.addStyleName("tiny");
                if (this.xmlNodes.get(i).getExpField() != null)
                    cbxExpFields.setValue(this.xmlNodes.get(i).getExpField().getExpFieldId());
                itemValues[4] = cbxExpFields;

                this.tblXmlNodes.addItem(itemValues, this.xmlNodes.get(i).getXmlTemplateNodeId());
            }

        }
        comboXmlTExperiment.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (comboXmlTExperiment.getValue() != null) {
                    enableComponents(true);
                }

            }
        });

        chxNotScheduled.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cbxStartHour.setEnabled(!chxNotScheduled.getValue());
                comboXmljobScheduler.setEnabled(!chxNotScheduled.getValue());
                startXmlTstart.setEnabled(!chxNotScheduled.getValue());
                endXmlTstart.setEnabled(!chxNotScheduled.getValue());
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

    private void closeModalWindow() {
        //Close Parent Modal Window
        ((Window) this.getParent()).close();
    }

    @SuppressWarnings("deprecation")
    protected void onSave() {
        Collection itemIds = this.tblXmlNodes.getContainerDataSource().getItemIds();
        boolean validateXmlTemplateNameResult = validateXmlTemplateName();
        boolean validateReqFieldsResult = validateRequiredFields();
        boolean validateUniqueLoopNodeResult = validateUniqueLoopNode();
        boolean validateNonRepeatedExperimentFieldsResult = validateNonRepeatedExperimentFields();

        //---Validate Required Fields---//
        if (itemIds.size() > 0 && validateReqFieldsResult && validateUniqueLoopNodeResult
                && validateNonRepeatedExperimentFieldsResult && validateXmlTemplateNameResult) {

            SysUser sessionUser = (SysUser) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");

            //Save template
            this.xmlt.setExperiment(new ExperimentDao().getExperimentById((int) this.comboXmlTExperiment.getValue()));
            this.xmlt.setXmlTemplateName(this.txtXmlTName.getValue());
            this.xmlt.setXmlTemplateIsActive(this.chxActive.getValue());
           
            String opFilterSavedCriteria = this.opFilterCriteriaType.getValue().toString().trim(); 

			if(opFilterSavedCriteria.startsWith("Prefix"))
			{
				this.xmlt.setXmlTemplatePrefix(this.txtXmlTFilterCriteria.getValue());
				this.xmlt.setXmlTemplateSuffix(null);
				this.xmlt.setXmlTemplateRegex(null);				
			}

			if(opFilterSavedCriteria.startsWith("Suffix"))
			{
				this.xmlt.setXmlTemplatePrefix(null);
				this.xmlt.setXmlTemplateSuffix(this.txtXmlTFilterCriteria.getValue());
				this.xmlt.setXmlTemplateRegex(null);				
			}

			if(opFilterSavedCriteria.startsWith("Regular"))
			{
				this.xmlt.setXmlTemplatePrefix(null);
				this.xmlt.setXmlTemplateSuffix(null);
				this.xmlt.setXmlTemplateRegex(this.txtXmlTFilterCriteria.getValue());				
			}
			
            //this.xmlt.setXmlTemplatePrefix(this.txtXmlTFilterCriteria.getValue());
            this.xmlt.setXmlTemplateComments(this.txtXmlTComments.getValue());
            this.xmlt.setExceptionFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboXmlTerrRepo.getValue()));
            this.xmlt.setProcessedFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboXmloutRepo.getValue()));
            this.xmlt.setInboundFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboXmlTinRepo.getValue()));

            if (this.cbxContractManufacturer.getValue() != null)
                this.xmlt.setContractManufacturer(new ContractManufacturerDao().getContractManufacturerById((int) this.cbxContractManufacturer.getValue()));
            else
                this.xmlt.setContractManufacturer(null);

            if (this.comboXmljobScheduler.getValue() != null)
                this.xmlt.setJobExecRepeat(new JobExecutionRepeatDao().getJobExecutionRepeatById((int) this.comboXmljobScheduler.getValue()));
            else
                this.xmlt.setJobExecRepeat(null);

            this.xmlt.setLastModifiedBy(sessionUser);
            this.xmlt.setModifiedDate(new Date());


            this.xmlt.setXmlTemplateNotScheduled(chxNotScheduled.getValue());

            if (chxNotScheduled.getValue()) {
                this.xmlt.setJobExecRepeat(null);
                this.xmlt.setXmlTemplateExecStartDate(null);
                this.xmlt.setXmlTemplateExecEndDate(null);
                this.xmlt.setXmlTemplateExecStartHour(null);
            } else {
                this.xmlt.setXmlTemplateExecStartDate(this.startXmlTstart.getValue());
                this.xmlt.setXmlTemplateExecEndDate(this.endXmlTstart.getValue());
                this.xmlt.setXmlTemplateExecStartHour((int) this.cbxStartHour.getValue());
            }


            if (this.xmlt.getXmlTemplateId() != null) {
                new XmlTemplateDao().updateXmlTemplate(xmlt);
                XmlTemplate xmltWithId = new XmlTemplateDao().getActiveXmlTemplateByName(this.txtXmlTName.getValue());
                RemoteFileUtil remoteFileUtil = RemoteFileUtil.getInstance();

                if (xmltWithId.isXmlTemplateIsActive() && (xmltWithId.getXmlTemplateNotScheduled() == null || !xmltWithId.getXmlTemplateNotScheduled()))
                {
                    remoteFileUtil.cancelJob(xmlt);
                	remoteFileUtil.updateJob(xmltWithId);
                }
                else
                    remoteFileUtil.cancelJob(xmlt);
            } else {
                this.xmlt.setCreatedBy(sessionUser);
                this.xmlt.setCreatedDate(this.xmlt.getModifiedDate());
                new XmlTemplateDao().addXmlTemplate(xmlt);
                XmlTemplate xmltWithId = new XmlTemplateDao().getActiveXmlTemplateByName(this.txtXmlTName.getValue());
                RemoteFileUtil remoteFileUtil = RemoteFileUtil.getInstance();

                if (xmlt.getXmlTemplateNotScheduled() == null || !xmlt.getXmlTemplateNotScheduled())
                    remoteFileUtil.updateJob(xmltWithId);
            }

            //Save XmlTemplateNodes

            XmlTemplateNodeDao xmlTempNodeDao = new XmlTemplateNodeDao();

            //Remove deprecated XmlTemplateNodes
            if (this.xmlt.getXmlTemplateId() != null && dbIdOfXmlTemplateNodeItemsToDelete.size() > 0) {
                for (int i = 0; i < dbIdOfXmlTemplateNodeItemsToDelete.size(); i++)
                    xmlTempNodeDao.deleteXmlTemplateNode(dbIdOfXmlTemplateNodeItemsToDelete.get(i));
            }

            for (Object itemIdObj : itemIds) {

                int itemId = (int) itemIdObj;
                Item tblRowItem = this.tblXmlNodes.getContainerDataSource().getItem(itemId);

                XmlTemplateNode xmlNode = new XmlTemplateNode();
                xmlNode.setXmlTemplateNodeName(((TextArea) (tblRowItem.getItemProperty("Xml Node Name").getValue())).getValue());
                xmlNode.setXmlTemplateNodeIsLoop(((CheckBox) (tblRowItem.getItemProperty("Is Loop Node").getValue())).getValue());
                xmlNode.setXmlTemplateNodeIsAttribute(((CheckBox) (tblRowItem.getItemProperty("Is Attribute").getValue())).getValue());
                xmlNode.setXmlTemplate(xmlt);

                if (((ComboBox) (tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue() != null) {

                    ExperimentField selectedExpField = new ExperimentField();
                    selectedExpField.setExpFieldId((int) ((ComboBox) (tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue());
                    xmlNode.setExpField(selectedExpField);
                }
                if (itemId > 0) {
                    xmlNode.setXmlTemplateNodeId(itemId);

                    xmlTempNodeDao.updateXmlTemplateNode(xmlNode);
                } else
                    xmlTempNodeDao.addXmlTemplateNode(xmlNode);

            }


            closeModalWindow();
        } else {
            if (itemIds.size() <= 0)
                this.getUI().showNotification("There are no xml nodes mapped on your Xml Template", Type.WARNING_MESSAGE);
            else if (!validateReqFieldsResult)
                this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
            else if (!validateUniqueLoopNodeResult)
                this.getUI().showNotification("Only 1 Loop Node is allowed", Type.WARNING_MESSAGE);
            else if (!validateNonRepeatedExperimentFieldsResult)
                this.getUI().showNotification("You can not map 1 " + this.systemSettings.getExperimentLabel() + " Field to 2 or more Xml Node Value/Attribute.", Type.WARNING_MESSAGE);
            else if (!validateXmlTemplateNameResult)
                this.getUI().showNotification("Name is already selected for another Xml Template. Please rename XmlTemplate", Type.WARNING_MESSAGE);
        }
    }

    private boolean validateNonRepeatedExperimentFields() {
        List<Integer> selectedExpFields = new ArrayList<Integer>();

        Collection itemIds = this.tblXmlNodes.getContainerDataSource().getItemIds();

        for (Object itemIdObj : itemIds) {
            int itemId = (int) itemIdObj;
            Item tblRowItem = this.tblXmlNodes.getContainerDataSource().getItem(itemId);

            if (((ComboBox) (tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue() != null) {
                if (selectedExpFields.indexOf(((int) ((ComboBox) (tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue())) > -1)
                    return false;
                else
                    selectedExpFields.add(((int) ((ComboBox) (tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue()));
            }
        }
        return true;
    }

    private boolean validateRequiredFields() {
        if (!this.txtXmlTName.isValid()) return false;
        if (!this.txtXmlTFilterCriteria.isValid()) return false;
        if (!this.comboXmloutRepo.isValid()) return false;
        if (!this.comboXmlTerrRepo.isValid()) return false;
        if (!this.comboXmlTinRepo.isValid()) return false;
        if (!this.comboXmlTExperiment.isValid()) return false;


        if (!this.chxNotScheduled.getValue()) {
            if (!this.cbxStartHour.isValid()) return false;
            if (!this.comboXmljobScheduler.isValid()) return false;
            if (!this.startXmlTstart.isValid()) return false;
            if (!this.endXmlTstart.isValid()) return false;
        }

        return true;
    }

    private boolean validateUniqueLoopNode() {
        int loopNodeCnt = 0;
        Collection itemIds = this.tblXmlNodes.getContainerDataSource().getItemIds();

        for (Object itemIdObj : itemIds) {
            int itemId = (int) itemIdObj;
            Item tblRowItem = this.tblXmlNodes.getContainerDataSource().getItem(itemId);
            if (((CheckBox) (tblRowItem.getItemProperty("Is Loop Node").getValue())).getValue())
                loopNodeCnt++;

            if (loopNodeCnt > 1)
                return false;
        }

        return true;

    }

    private boolean validateXmlTemplateName() {
        if (this.xmlt.getXmlTemplateId() == null && new XmlTemplateDao().getActiveXmlTemplateByName(this.txtXmlTName.getValue()) != null)
            return false;
        return true;
    }

    @SuppressWarnings("deprecation")
    private void uploadFile() {

        class MyReceiver implements Receiver {
            //private static final long serialVersionUID = -1276759102490466761L;

            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Output stream to write to
                try {
                    // Open the file for writing.
                    tempFile = File.createTempFile("temp", ".xml");
                    fos = new FileOutputStream(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return fos; // Return the output stream to write to
            }
        }
        ;
        final MyReceiver uploader = new MyReceiver();
        upXml.setReceiver(uploader);
        upXml.addFinishedListener(new Upload.FinishedListener() {
            @Override
            public void uploadFinished(Upload.FinishedEvent finishedEvent) {
                FileReader reader;
                try {

                    reader = new FileReader(tempFile);


                    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                    xmlDocument = docBuilder.parse(tempFile);
                    if (xmlDocument != null) {
                        fillNodes();
                        comboXmlTExperiment.setEnabled(false);
                        reader.close();
                    }

                    tempFile.delete();
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }

    private void enableComponents(boolean b) {
        this.txtXmlTName.setEnabled(b);
        this.txtXmlTComments.setEnabled(b);
        this.opFilterCriteriaType.setEnabled(b);
        this.txtXmlTFilterCriteria.setEnabled(b);
        this.cbxContractManufacturer.setEnabled(b);
        this.comboXmloutRepo.setEnabled(b);
        this.comboXmlTerrRepo.setEnabled(b);
        this.comboXmlTinRepo.setEnabled(b);
        this.tblXmlNodes.setEnabled(b);
        this.upXml.setEnabled(b);
        this.comboXmljobScheduler.setEnabled(b);
        this.cbxStartHour.setEnabled(b);
    }

    private void fillNodes() {

        if (this.xmlt.getXmlTemplateId() != null && this.xmlt.getXmlTemplateId() > 0) {
            // Remove deprecated Nodes from UI and store Ids to delete from DB at saving
            int loopNodeCnt = 0;
            Collection itemIds = this.tblXmlNodes.getContainerDataSource().getItemIds();

            for (Object itemIdObj : itemIds) {
                int itemId = (int) itemIdObj;
                if (itemId > 0)
                    dbIdOfXmlTemplateNodeItemsToDelete.add(itemId);
            }

            this.tblXmlNodes.setContainerDataSource(null);
            this.tblXmlNodes.addContainerProperty("*", CheckBox.class, null);
            this.tblXmlNodes.addContainerProperty("Xml Node Name", TextArea.class, null);
            this.tblXmlNodes.addContainerProperty("Is Loop Node", CheckBox.class, null);
            this.tblXmlNodes.addContainerProperty("Is Attribute", CheckBox.class, null);
            this.tblXmlNodes.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);
            this.tblXmlNodes.setPageLength(0);

        }

        ExperimentDao expdao = new ExperimentDao();
        Experiment expNew = expdao.getExperimentById((int) (this.comboXmlTExperiment.getValue()));
        expFields = new ExperimentFieldDao().getActiveExperimentFields(expNew);

        List<String> xmlStringTreeMtx = new ArrayList<String>();
        List<Boolean> xmlIsAttributeTreeMtx = new ArrayList<Boolean>();

        try {

            Element root = xmlDocument.getDocumentElement();
            Object rootItem = root.getNodeName();
            xmlStringTreeMtx.add("<" + rootItem.toString() + ">");
            xmlIsAttributeTreeMtx.add(false);
            addChildrenToStringTree(xmlStringTreeMtx, xmlIsAttributeTreeMtx, root.getChildNodes(), rootItem);

        } catch (Exception e) {
        }

        Object[] itemValues = new Object[5];
        for (int i = 0; i < xmlStringTreeMtx.size(); i++) {
            CheckBox chxSelect = new CheckBox();
            chxSelect.setVisible(false);
            itemValues[0] = chxSelect;

            TextArea txtAreaXmlNodeName = new TextArea();
            txtAreaXmlNodeName.setValue(xmlStringTreeMtx.get(i));
            txtAreaXmlNodeName.setReadOnly(true);
            txtAreaXmlNodeName.addStyleName("tiny");
            txtAreaXmlNodeName.setWidth(100, Unit.PERCENTAGE);
            txtAreaXmlNodeName.setRows(3);
            itemValues[1] = txtAreaXmlNodeName;

            CheckBox chxIsLoop = new CheckBox();
            chxIsLoop.setValue(false);
            chxIsLoop.setEnabled(!xmlIsAttributeTreeMtx.get(i));
            chxIsLoop.addStyleName("tiny");
            itemValues[2] = chxIsLoop;

            CheckBox chxIsAttribute = new CheckBox();
            chxIsAttribute.setValue(xmlIsAttributeTreeMtx.get(i));
            chxIsAttribute.setEnabled(false);
            chxIsAttribute.addStyleName("tiny");
            itemValues[3] = chxIsAttribute;

            ComboBox cbxExpFields = new ComboBox("");
            for (int j = 0; j < expFields.size(); j++) {
                cbxExpFields.addItem(expFields.get(j).getExpFieldId());
                cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName() + " [ " + expFields.get(j).getExpFieldType() + " ]");
                cbxExpFields.setWidth(100, Unit.PERCENTAGE);
            }

            //cbxExpFields.setNullSelectionAllowed(false);
            cbxExpFields.addStyleName("tiny");
            itemValues[4] = cbxExpFields;

            this.lastNewItemId = this.lastNewItemId - 1;
            this.tblXmlNodes.addItem(itemValues, this.lastNewItemId);

        }

    }

    private void fillCombos() {
        //Experiments
        for (int j = 0; j < experiments.size(); j++) {
            this.comboXmlTExperiment.addItem(experiments.get(j).getExpId());
            this.comboXmlTExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
            //this.comboXmlTExperiment.setWidth(100, Unit.PIXELS);
        }

        this.comboXmlTExperiment.setNullSelectionAllowed(false);
        this.comboXmlTExperiment.setImmediate(true);

        //Contract Manufacturer

        this.cbxContractManufacturer.setNullSelectionAllowed(true);
        this.cbxContractManufacturer.setImmediate(true);

        for (int i = 0; contractManufacturers != null && i < contractManufacturers.size(); i++) {
            this.cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
            this.cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
        }


        //File Repos
        for (int j = 0; j < repos.size(); j++) {
            this.comboXmlTinRepo.addItem(repos.get(j).getFileRepoId());
            this.comboXmlTinRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost() + ":" + repos.get(j).getFileRepoPath());
            //this.comboXmlTinRepo.setWidth(100, Unit.PIXELS);

            this.comboXmloutRepo.addItem(repos.get(j).getFileRepoId());
            this.comboXmloutRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost() + ":" + repos.get(j).getFileRepoPath());
            //this.comboXmloutRepo.setWidth(100, Unit.PIXELS);

            this.comboXmlTerrRepo.addItem(repos.get(j).getFileRepoId());
            this.comboXmlTerrRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost() + ":" + repos.get(j).getFileRepoPath());
            //this.comboXmlTerrRepo.setWidth(100, Unit.PIXELS);
        }

        this.comboXmlTinRepo.setNullSelectionAllowed(true);
        this.comboXmlTinRepo.setImmediate(true);

        this.comboXmloutRepo.setNullSelectionAllowed(true);
        this.comboXmloutRepo.setImmediate(true);

        this.comboXmlTerrRepo.setNullSelectionAllowed(true);
        this.comboXmlTerrRepo.setImmediate(true);

        //Jobs
        for (int j = 0; j < jobs.size(); j++) {
            this.comboXmljobScheduler.addItem(jobs.get(j).getJobExecRepeatId());
            this.comboXmljobScheduler.setItemCaption(jobs.get(j).getJobExecRepeatId(), jobs.get(j).getJobExecRepeatName());
        }

        this.comboXmljobScheduler.setNullSelectionAllowed(true);
        this.comboXmljobScheduler.setImmediate(true);

        //Hour Execution Start
        this.cbxStartHour.setNullSelectionAllowed(false);
        for (int i = 0; i < 24; i++) {
            this.cbxStartHour.addItem(i);
            this.cbxStartHour.setItemCaption(i, (i < 10 ? "0" : "") + i + ":00");
        }

    }

    private void onDelete() {
        this.xmlt.setXmlTemplateIsActive(false);
        SysUser sessionUser = (SysUser) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
        this.xmlt.setLastModifiedBy(sessionUser);
        this.xmlt.setModifiedDate(new Date());
        this.xmlt.setContractManufacturer(null);
        XmlTemplateDao xmlDao = new XmlTemplateDao();
        xmlDao.updateXmlTemplate(xmlt);

        RemoteFileUtil remoteFileUtil = RemoteFileUtil.getInstance();
        remoteFileUtil.cancelJob(xmlt);

        closeModalWindow();

		/*
		XmlTemplateNodeDao xmlNodeDao = new XmlTemplateNodeDao();
		this.xmlNodes = xmlNodeDao.getAllXmlTemplateNodesByTemplateId(xmlTemplateId);
		for (XmlTemplateNode node : xmlNodes) {
			xmlNodeDao.deleteXmlTemplateNode(node.getXmlTemplateNodeId());
		}
		XmlTemplateDao xmlDao = new XmlTemplateDao();
		xmlDao.deleteXmlTemplate(xmlTemplateId);
		*/
    }

    private void addChildrenToStringTree(List<String> StringTree, List<Boolean> IsAttributeTree, NodeList children, Object parent) {
        if (children.getLength() > 0) {
            for (int i = 0; i < children.getLength(); i++) {

                //Getting Full Node Name
                Node tmpNode = children.item(i);
                if (!tmpNode.getNodeName().toString().startsWith("#")) {
                    String fullNodeName = "<" + tmpNode.getNodeName().toString() + ">";
                    while (tmpNode.getParentNode() != null && !tmpNode.getParentNode().getNodeName().toString().startsWith("#")) {
                        fullNodeName = "<" + tmpNode.getParentNode().getNodeName().toString() + "> \n\t" + fullNodeName;
                        tmpNode = tmpNode.getParentNode();
                    }

                    if (StringTree.indexOf(fullNodeName) < 0) {
                        StringTree.add(fullNodeName);
                        IsAttributeTree.add(false);
                    }

                    Node node = children.item(i);
                    Object child = node.getNodeName();

                    for (int j = 0; node.getAttributes() != null && j < node.getAttributes().getLength(); j++) {
                        Object childAttribute = fullNodeName.substring(0, fullNodeName.length() - 1) + " " + node.getAttributes().item(j).getNodeName() + ">";

                        if (StringTree.indexOf(childAttribute) < 0) {
                            StringTree.add(childAttribute.toString());
                            IsAttributeTree.add(true);
                        }
                    }

                    addChildrenToStringTree(StringTree, IsAttributeTree, node.getChildNodes(), child);
                }
            }
        }
    }

}
