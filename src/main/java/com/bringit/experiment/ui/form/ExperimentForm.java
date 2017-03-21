package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.ExperimentImage;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentImageDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.ui.design.ExperimentDesign;
import com.bringit.experiment.util.Config;
import com.opencsv.CSVReader;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinService;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Panel;

public class ExperimentForm extends ExperimentDesign {

	private Experiment experiment;
	private List<ExperimentField> experimentFields;
	private List<ExperimentImage> experimentImages;
	List <UnitOfMeasure> unitOfMeasures= new UnitOfMeasureDao().getAllUnitOfMeasures();
	String[] dbfieldTypes;
	private int lastNewItemId = 0;
	private File tempImageFile;
	private boolean isNewExperiment = false;
	List<Integer> imagesToDelete = new ArrayList<Integer>();
	
	private TextField selectedImage = new TextField();
	  
	private List<StreamResource> imagesStreamResourceList;
	  
	public ExperimentForm(int experimentId)
	{		
		expElements.getTab(0).setCaption("Experiment Images");
		expElements.getTab(0).setIcon(FontAwesome.FILE_IMAGE_O);
		
		expElements.getTab(1).setCaption("Experiment Fields");
		expElements.getTab(1).setIcon(FontAwesome.FILE_TEXT_O);
		expElements.setSelectedTab(1);
		
		this.vlViewer.setSizeFull();
		//this.vlViewer.setMargin(true);
		//this.vlViewer.setSpacing(true);
		this.tsImages.setSizeFull();
		
		upImage.setButtonCaption("Add Experiment Image");
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
			dbfieldTypes = configuration.getProperty("sqlserverdatatypes").split(",");
		
		this.tblExperimentFields.setContainerDataSource(null);
		this.tblExperimentFields.addContainerProperty("*", CheckBox.class, null);
		this.tblExperimentFields.addContainerProperty("Name", TextField.class, null);
		this.tblExperimentFields.addContainerProperty("Active", CheckBox.class, null);
		this.tblExperimentFields.addContainerProperty("DB Id", TextField.class, null);
		this.tblExperimentFields.addContainerProperty("DB DataType", ComboBox.class, null);
		this.tblExperimentFields.addContainerProperty("UoM", ComboBox.class, null);
		
		uploadImage();
		this.txtExpDbTableNameId.addValidator(new Validator() {

            public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isAlphanumeric(((String) value).replaceAll("_", "")))
                    throw new InvalidValueException("Only AlphaNumeric and Underscores are allowed for DB Names");
            }
            
        });
		
		if(experimentId == -1) //New Experiment
		{
			isNewExperiment = true;
			this.chxActive.setValue(true);
			this.btnDelete.setEnabled(false);
			this.experiment = new Experiment();
			this.tsImages.setCaption("No images found.");
		}
		else
		{
			//Loading Header Info
			this.experiment = new ExperimentDao().getExperimentById(experimentId);
			this.txtExpName.setValue(this.experiment.getExpName());
			this.txtExpDbTableNameId.setValue(this.experiment.getExpDbTableNameId());
			this.txtExpDbTableNameId.setEnabled(false);
			this.chxActive.setValue(this.experiment.isExpIsActive());
			this.txtExpInstructions.setValue(this.experiment.getExpInstructions());
			this.txtExpComments.setValue(this.experiment.getExpComments());
			
			loadImageTabs();

			this.experimentFields = new ExperimentFieldDao().getAllExperimentFieldsByExperiment(this.experiment);

			Object[] itemValues = new Object[6];

			//Loading Child Fields
			
			for(int i=0; i<this.experimentFields.size(); i++)
			{	
				//tblExperimentFields.select(itemId);
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				chxSelect.setWidth(50, Unit.PIXELS);
				itemValues[0] = chxSelect;
				
				TextField txtFieldName = new TextField();
				txtFieldName.setValue(this.experimentFields.get(i).getExpFieldName());
				txtFieldName.setEnabled(false);
				txtFieldName.addStyleName("tiny");
				txtFieldName.setWidth(200, Unit.PIXELS);
				itemValues[1] = txtFieldName;
				
				CheckBox chxActive = new CheckBox();
				chxActive.setValue(this.experimentFields.get(i).isExpFieldIsActive() ? true : false);
				chxActive.addStyleName("tiny");
				itemValues[2] = chxActive;
				
				TextField txtExpDbFieldNameId = new TextField();
				txtExpDbFieldNameId.setValue(this.experimentFields.get(i).getExpDbFieldNameId());
				txtExpDbFieldNameId.setEnabled(false);
				txtExpDbFieldNameId.addStyleName("tiny");
				txtExpDbFieldNameId.setWidth(200, Unit.PIXELS);
				itemValues[3] = txtExpDbFieldNameId;
				
				ComboBox cbxFieldType = new ComboBox("");
				
				for(int j=0; j<dbfieldTypes.length; j++)
				{
					cbxFieldType.addItem(dbfieldTypes[j]);
					cbxFieldType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
				}
				
				cbxFieldType.setValue(this.experimentFields.get(i).getExpFieldType());
				cbxFieldType.setEnabled(false);
				cbxFieldType.setStyleName("tiny");
				cbxFieldType.setWidth(150, Unit.PIXELS);
				itemValues[4] = cbxFieldType;
				
				ComboBox cbxUnitOfMeasure = new ComboBox("");
				
				for(int j=0; j<unitOfMeasures.size(); j++)
				{
					cbxUnitOfMeasure.addItem(unitOfMeasures.get(j).getUomId());
					cbxUnitOfMeasure.setItemCaption(unitOfMeasures.get(j).getUomId(), unitOfMeasures.get(j).getUomAbbreviation());
				}
				
				if(this.experimentFields.get(i).getUnitOfMeasure() != null)
				{
					cbxUnitOfMeasure.setValue(this.experimentFields.get(i).getUnitOfMeasure().getUomId());
					cbxUnitOfMeasure.setImmediate(true);
					cbxUnitOfMeasure.addStyleName("tiny");
				}
				cbxUnitOfMeasure.setStyleName("tiny");
				cbxUnitOfMeasure.setWidth(100, Unit.PIXELS);
				itemValues[5] = cbxUnitOfMeasure;
				
				this.tblExperimentFields.addItem(itemValues, this.experimentFields.get(i).getExpFieldId());
		    }
			
			this.tblExperimentFields.setEditable(true);
			this.tblExperimentFields.setPageLength(100);
			
		}
		btnAddField.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addField();
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
		btnDeleteField.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				removeExperimentFieldItem();
			}
		});
		btnDeleteImage.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				removeExperimentImage();
			}

		});

	}

	private void loadImageTabs() {
		this.experimentImages = new ExperimentImageDao().getAllExperimentImagesByExperiment(this.experiment);
	
		if(this.experimentImages == null || this.experimentImages.size() <= 0)
			this.tsImages.setCaption("No images found.");
		else
			this.tsImages.setCaption("");
			
		for (ExperimentImage expImg : experimentImages) {
			final byte[] img = expImg.getExpImage();
			String imgName = "ExpImage"+expImg.getExpImageId();
			 
			try {
				if (img != null){
					StreamResource.StreamSource imageSource = new StreamResource.StreamSource() {
						@Override
							public InputStream getStream() {
								return new java.io.ByteArrayInputStream(img);
							}
					};
						StreamResource imageResource = new StreamResource(imageSource, imgName+".png");
						imageResource.setCacheTime(0);
						Image tabImg = new Image(null,imageResource);
						System.out.println("Width:" + tabImg.getWidth() + "\n");
						System.out.println("Height:" + tabImg.getHeight() + "\n");
						//tabImg.setWidth("600px");
						//tabImg.setHeight("400px");
						tsImages.addTab(tabImg, imgName);	
						tsImages.setCaption("");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
	private void removeExperimentImage() {
		//this.experiment.setExpImage(new byte[0]);
		if(tempImageFile != null){
			tempImageFile.delete();
		}
		Component c = tsImages.getSelectedTab();
		if (tsImages.getTab(c).getCaption().substring(0,3).equals("new")){
			tsImages.removeComponent(c);
		}else{
			int imgId = Integer.parseInt(tsImages.getTab(c).getCaption().substring(8));
			tsImages.removeComponent(c);
			imagesToDelete.add(imgId);
		}
	}
	
	private void uploadImage() {
	       class MyReceiver implements Receiver {
	            //private static final long serialVersionUID = -1276759102490466761L;

	            public OutputStream receiveUpload(String filename, String mimeType) {
	                // Create upload stream
	                FileOutputStream fos = null; // Output stream to write to
	                try {
	                    // Open the file for writing.
	                	tempImageFile = File.createTempFile("new", ".png");
	                    fos = new FileOutputStream(tempImageFile);
	                }  catch (IOException e) {
	      	          e.printStackTrace();
	    	          return null;
	                }
	                return fos; // Return the output stream to write to
	            }
	        };
	        final MyReceiver uploader = new MyReceiver(); 
	       
	        upImage.setReceiver(uploader);
	        upImage.addFinishedListener(new Upload.FinishedListener() {
		        @Override
		        public void uploadFinished(Upload.FinishedEvent finishedEvent) {
					try {
					      // Display the uploaded file in the image panel.
					      final FileResource imageResource = new FileResource(tempImageFile);
							String imgName = tempImageFile.getName();
							 
							try {
										Image tabImg = new Image(null,imageResource);
										System.out.println("Width:" + tabImg.getWidth() + "\n");
										System.out.println("Height:" + tabImg.getHeight() + "\n");
										//tabImg.setWidth("600px");
										//tabImg.setHeight("400px");
										tsImages.addTab(tabImg, imgName);	
										tsImages.setCaption("");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    		
		    			//tempImageFile.delete();
					} catch ( Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		      });
		
	}

	private void addField(){
		
		Object[] itemValues = new Object[6];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		chxSelect.setWidth(50, Unit.PIXELS);
		itemValues[0] = chxSelect;
		
		TextField txtFieldName = new TextField();
		txtFieldName.setImmediate(true);
		txtFieldName.focus();
		txtFieldName.addStyleName("tiny");
		txtFieldName.setRequired(true);
		txtFieldName.setRequiredError("This field is mandatory");
		txtFieldName.setWidth(200, Unit.PIXELS);
		itemValues[1] = txtFieldName;
		
		CheckBox chxActive = new CheckBox();
		chxActive.addStyleName("tiny");
		chxActive.setValue(true);
		itemValues[2] = chxActive;
		
		
		TextField txtExpDbFieldNameId = new TextField();
		txtExpDbFieldNameId.setRequired(true);
		txtExpDbFieldNameId.setRequiredError("This field is mandatory");
		txtExpDbFieldNameId.addStyleName("tiny");
		txtExpDbFieldNameId.setWidth(200, Unit.PIXELS);
		txtExpDbFieldNameId.addValidator(new Validator() {

            public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isAlphanumeric(((String) value).replaceAll("_", "")))
                    throw new InvalidValueException("Only AlphaNumeric and Underscores are allowed for DB Names");
                else
                {
                	String dbFieldIdName = ((String) value);
                	if(dbFieldIdName.toLowerCase().equals("recordid") || dbFieldIdName.toLowerCase().equals("comments") || dbFieldIdName.toLowerCase().equals("createdby")
						 || dbFieldIdName.toLowerCase().equals("lastmodifiedby") || dbFieldIdName.toLowerCase().equals("datafileid")
						 || dbFieldIdName.toLowerCase().equals("createddate") || dbFieldIdName.toLowerCase().equals("lastmodifieddate")) 
                    throw new InvalidValueException("'RecordId', 'Comments', 'CreatedBy', 'LastModifiedBy', 'DataFileId', 'CreatedDate', 'LastModifiedDate' are Non-Eligible for Experiment Field DB Ids.");
                }
            }
        });
		
		itemValues[3] = txtExpDbFieldNameId;
		
		ComboBox cbxFieldType = new ComboBox("");
		
		for(int j=0; j<dbfieldTypes.length; j++)
		{
			cbxFieldType.addItem(dbfieldTypes[j]);
			cbxFieldType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
		}

		cbxFieldType.setNullSelectionAllowed(false);
		cbxFieldType.setImmediate(true);
		cbxFieldType.setRequired(true);
		cbxFieldType.setRequiredError("This field is mandatory");
		cbxFieldType.addStyleName("tiny");
		cbxFieldType.setWidth(150, Unit.PIXELS);
		itemValues[4] = cbxFieldType;
		
		ComboBox cbxUnitOfMeasure = new ComboBox("");
		
		for(int j=0; j<unitOfMeasures.size(); j++)
		{
			cbxUnitOfMeasure.addItem(unitOfMeasures.get(j).getUomId());
			cbxUnitOfMeasure.setItemCaption(unitOfMeasures.get(j).getUomId(), unitOfMeasures.get(j).getUomAbbreviation());
		}
		
		cbxUnitOfMeasure.setImmediate(true);
		cbxUnitOfMeasure.addStyleName("tiny");
		cbxUnitOfMeasure.setWidth(100, Unit.PIXELS);
		itemValues[5] = cbxUnitOfMeasure;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		this.tblExperimentFields.addItem(itemValues, this.lastNewItemId);
		this.tblExperimentFields.select(this.lastNewItemId);
		
	}
	
	private void removeExperimentFieldItem()
	{
		if(this.tblExperimentFields.getValue() != null && (int)this.tblExperimentFields.getValue() < 0)
			this.tblExperimentFields.removeItem((int)this.tblExperimentFields.getValue());
	}
	
	private void onSave()
	{
		boolean isNewRecord = false;
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		boolean validateReqFieldsResult = validateRequiredFields();
		boolean validateDbNameFieldsResult = validateDbNameFields();
		boolean validateDuplicateDbTableNameResult = validateDuplicateDbTableName();
		boolean validateDuplicatedDbNameFieldsResult = validateDuplicatedDbNameFields();
		boolean validateRestrictedDbTableFieldsResult = validateRestrictedDbTableFields();
		
		//---Validate Required Fields---//
		if(itemIds.size() > 0 && validateReqFieldsResult && validateDbNameFieldsResult && validateDuplicateDbTableNameResult && validateDuplicatedDbNameFieldsResult && validateRestrictedDbTableFieldsResult )
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			
			//Save experiment
			this.experiment.setExpName(this.txtExpName.getValue());
			this.experiment.setExpDbTableNameId(this.txtExpDbTableNameId.getValue());
			this.experiment.setExpIsActive(this.chxActive.getValue());
			this.experiment.setExpInstructions(this.txtExpInstructions.getValue());
			this.experiment.setExpComments(this.txtExpComments.getValue());
			this.experiment.setLastModifiedBy(sessionUser);
			this.experiment.setModifiedDate(new Date());

			ExperimentDao expDao = new ExperimentDao();
			
			if(this.experiment.getExpId() != null )
				expDao.updateExperiment(experiment);
			else
			{
				this.experiment.setCreatedBy(sessionUser);
				this.experiment.setCreatedDate(this.experiment.getModifiedDate());
				expDao.addExperiment(experiment);
				isNewRecord = true;
			}
			
			expDao.updateDBDataTable(experiment);
			
			//Save ExperimentFields
			ExperimentFieldDao expfieldDao = new ExperimentFieldDao();
			
			for (Object itemIdObj : itemIds) 
			{
				
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
				
				ExperimentField experimentField = new ExperimentField();
				experimentField.setExpFieldName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				experimentField.setExpFieldIsActive(((CheckBox)(tblRowItem.getItemProperty("Active").getValue())).getValue());
				experimentField.setExpDbFieldNameId(((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue());
				experimentField.setExpFieldType((String)((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).getValue());
				

				if(((ComboBox)(tblRowItem.getItemProperty("UoM").getValue())).getValue() != null && !((ComboBox)(tblRowItem.getItemProperty("UoM").getValue())).getValue().toString().isEmpty())
				{
					UnitOfMeasure selectedUnitOfMeasure = new UnitOfMeasure();
					selectedUnitOfMeasure.setUomId((int)((ComboBox)(tblRowItem.getItemProperty("UoM").getValue())).getValue());
					experimentField.setUnitOfMeasure(selectedUnitOfMeasure);
				}
				
				experimentField.setExperiment(experiment);
				
				if(itemId > 0)
				{
					experimentField.setExpFieldId(itemId);
					expfieldDao.updateExperimentField(experimentField);
				}
				else
					expfieldDao.addExperimentField(experimentField);
				
				expfieldDao.updateDBDataTableField(experimentField);
			}
			saveImages();
			if(tempImageFile!=null){
				tempImageFile.delete();
			}
			
			if(isNewRecord)
			{
				WebApplication webApp = (WebApplication)this.getParent().getParent();
				webApp.reloadMainForm("manage experiments");
			}
			closeModalWindow();
		}
		else
		{
			if(itemIds.size() <= 0)
				this.getUI().showNotification("Experiment must contain at least 1 Experiment Field", Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
			else if(!validateDbNameFieldsResult)
				this.getUI().showNotification("Only AlphaNumeric and Underscores are allowed for DB Element Names", Type.WARNING_MESSAGE);
			else if(!validateDuplicatedDbNameFieldsResult)
				this.getUI().showNotification("Experiment Field DB Ids must be unique.", Type.WARNING_MESSAGE);
			else if(!validateDuplicateDbTableNameResult)
			{
				this.getUI().showNotification("DB Table Name already exists.", Type.WARNING_MESSAGE);
				this.txtExpDbTableNameId.selectAll();
			}
			else if(!validateRestrictedDbTableFieldsResult)
				this.getUI().showNotification("'RecordId', 'Comments', 'CreatedBy', 'LastModifiedBy', 'DataFileId', 'CreatedDate', 'LastModifiedDate' are Non-Eligible for Experiment Field DB Ids.", Type.WARNING_MESSAGE);
		}
    }
	
	private void saveImages() {
		Iterator<Component> i = tsImages.getComponentIterator();
		while (i.hasNext()) {
		    Component c = (Component) i.next();
		    Tab tab = tsImages.getTab(c);
		    if (tab.getCaption().substring(0,3).equals("new")) {
		         Image im = (Image) tab.getComponent();
		         FileResource imResource = (FileResource) im.getSource();
		         
		         try {
					byte[] bytes = IOUtils.toByteArray(imResource.getStream().getStream());
					ExperimentImage expImg = new ExperimentImage();
					expImg.setExperiment(this.experiment);
					expImg.setExpImage(bytes);
					new ExperimentImageDao().addExperimentImage(expImg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		//removingImages 
		if(imagesToDelete.size()>0){
			for (int j = 0; j < imagesToDelete.size(); j++) {
				new ExperimentImageDao().deleteExperimentImage(imagesToDelete.get(j));
			}
		}
	}

	private void onDelete()
	{
		this.experiment.setExpIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.experiment.setLastModifiedBy(sessionUser);
		this.experiment.setModifiedDate(new Date());
		ExperimentDao expDao = new ExperimentDao();
		expDao.updateExperiment(experiment);
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("manage experiments");
		closeModalWindow();
    }
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	private boolean validateRequiredFields()
	{
		if(!this.txtExpName.isValid()) return false;
		
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
			
			if(!((TextField)(tblRowItem.getItemProperty("Name").getValue())).isValid()) return false;
			if(!((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).isValid()) return false;			
		}
		
		return true;
	}
	
	private boolean validateDuplicateDbTableName()
	{
		List<Experiment> savedExperiments = new ExperimentDao().getAllExperiments();
		for(int i=0; i<savedExperiments.size(); i++)
		{
			if(this.txtExpDbTableNameId.getValue().equals(savedExperiments.get(i).getExpDbTableNameId()) && 
					this.experiment.getExpId() != savedExperiments.get(i).getExpId())
			return false;
		}
		return true;
	}
	
	private boolean validateDuplicatedDbNameFields()
	{
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		List<String> dbFieldNames = new ArrayList<String>();
	
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
			String dbFieldIdName = ((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue();
			
			if(itemId < 0 && dbFieldNames != null)
			{
				if(dbFieldNames.indexOf(dbFieldIdName) > -1)
					return false;
			}
			
			dbFieldNames.add(dbFieldIdName);			
		}
		
		return true;
	}

	private boolean validateDbNameFields()
	{
		if(StringUtils.isEmpty(this.txtExpDbTableNameId.getValue())) 
			return false;
		
		if((!StringUtils.isAlphanumeric(txtExpDbTableNameId.getValue().replaceAll("_", "")))) 
			return false;
		
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			if(itemId < 0)
			{
				Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
					
				String dbFieldIdName = ((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue();

				if(StringUtils.isEmpty(dbFieldIdName)) 
					return false;
					
				if(!StringUtils.isAlphanumeric(dbFieldIdName.replaceAll("_", "")))
					return false;
			}
		}
		
		return true;
	}
	
	private boolean validateRestrictedDbTableFields()
	{
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			if(itemId < 0)
			{
				Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
					
				String dbFieldIdName = ((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue();

				if(dbFieldIdName.toLowerCase().equals("recordid") || dbFieldIdName.toLowerCase().equals("comments") || dbFieldIdName.toLowerCase().equals("createdby")
					 || dbFieldIdName.toLowerCase().equals("lastmodifiedby") || dbFieldIdName.toLowerCase().equals("datafileid")
					 || dbFieldIdName.toLowerCase().equals("createddate") || dbFieldIdName.toLowerCase().equals("lastmodifieddate")) 
                	return false;				
			}
		}
		
		return true;
	}
	
}
