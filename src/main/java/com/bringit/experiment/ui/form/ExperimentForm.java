package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.callback.ConfirmationCallback;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.ExperimentImage;
import com.bringit.experiment.bll.ExperimentType;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentImageDao;
import com.bringit.experiment.dao.ExperimentTypeDao;
import com.bringit.experiment.dao.SystemSettingsDao;
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
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
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
	List <ExperimentType> experimentTypes = new ExperimentTypeDao().getAllExperimentTypes();
	String[] dbfieldTypes;
	private int lastNewItemId = 0;
	private File tempImageFile;
	private File tempCsvFile;    
	private boolean isNewExperiment = false;
	List<Integer> imagesToDelete = new ArrayList<Integer>();
	
	private TextField selectedImage = new TextField();
	  
	private List<StreamResource> imagesStreamResourceList;
	  
	private SystemSettings systemSettings;

	public ExperimentForm(int experimentId)
	{	
		//Rename entities
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		this.cbxExperimentType.setCaption(this.systemSettings.getExperimentTypeLabel());
		this.expElements.getTab(0).setCaption(this.systemSettings.getExperimentLabel() + " Images");
		this.expElements.getTab(1).setCaption(this.systemSettings.getExperimentLabel() + " Fields");
		this.btnDeleteImage.setCaption("Delete " + this.systemSettings.getExperimentLabel() + " Image");
		this.btnAddField.setCaption("Add " + this.systemSettings.getExperimentLabel() + " Field");
		this.btnDeleteField.setCaption("Delete " + this.systemSettings.getExperimentLabel() + " Field");
		this.btnDelete.setCaption("Delete " + this.systemSettings.getExperimentLabel());
		
		expElements.setSelectedTab(1);

		//Load Experiment Type Combo Box
		for(int i=0; i<experimentTypes.size(); i++)
		{
			cbxExperimentType.addItem(experimentTypes.get(i).getExpTypeId());
			cbxExperimentType.setItemCaption(experimentTypes.get(i).getExpTypeId(), experimentTypes.get(i).getExpTypeName());
		}
		
		upCsvExpFields.setButtonCaption("Upload by CSV");
	    replaceCsvUploadComponent(upCsvExpFields);
		
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
			this.chxActive.setEnabled(false);
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
			if(this.experiment.getExperimentType() != null)
				this.cbxExperimentType.setValue(this.experiment.getExperimentType().getExpTypeId());
			
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
			this.tblExperimentFields.setPageLength(0);
			
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
	
	private void addField(String fieldName, String fieldDbId, String fieldDbType, Integer uomId){
		
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
		txtFieldName.setValue(fieldName);
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
		
		if(fieldDbId!=null)
			txtExpDbFieldNameId.setValue(fieldDbId);
		
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
		
		if(fieldDbType != null) 
		{
			//Select DB DataType
			if(fieldDbType.trim().toLowerCase().startsWith("varchar") && selectedCsvVarcharValidation(fieldDbType))
			{				
				if(fieldDbType.trim().toLowerCase().equals("varchar") || fieldDbType.trim().toLowerCase().equals("text"))
					fieldDbType = "varchar(max)";
			
				if(!cbxFieldType.containsId(fieldDbType.trim().toLowerCase())) 
				{
					cbxFieldType.addItem(fieldDbType.trim().toLowerCase());
					cbxFieldType.setItemCaption(fieldDbType.trim().toLowerCase(), fieldDbType.trim().toLowerCase());
				}
				
				cbxFieldType.select(fieldDbType.trim().toLowerCase());
				
			}
			else
			{
				if(cbxFieldType.containsId(fieldDbType.trim().toLowerCase())) 
					cbxFieldType.select(fieldDbType.trim().toLowerCase());
			}
			
			cbxFieldType.setValue(fieldDbType);
		}
		
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
		
		if(uomId != -1)
			cbxUnitOfMeasure.setValue(uomId);
		
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

			if(this.cbxExperimentType.getValue() != null)
				this.experiment.setExperimentType(new ExperimentTypeDao().getExperimentTypeById((int)this.cbxExperimentType.getValue()));
			else
				this.experiment.setExperimentType(null);
			
			//New line added to store RPT Table name into Experiment entity
			this.experiment.setExpDbRptTableNameId("rpt#" + this.experiment.getExpDbTableNameId());
			
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
			expDao.deleteDBRptTable(experiment);
			expDao.updateDBRptTable(experiment);
			
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
				expfieldDao.updateDBRptTableField(experimentField);
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
				this.getUI().showNotification(this.systemSettings.getExperimentLabel() +  " must contain at least 1 " + this.systemSettings.getExperimentTypeLabel(), Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
			else if(!validateDbNameFieldsResult)
				this.getUI().showNotification("Only AlphaNumeric and Underscores are allowed for DB Element Names", Type.WARNING_MESSAGE);
			else if(!validateDuplicatedDbNameFieldsResult)
				this.getUI().showNotification(this.systemSettings.getExperimentLabel() +  " Field DB Ids must be unique.", Type.WARNING_MESSAGE);
			else if(!validateDuplicateDbTableNameResult)
			{
				this.getUI().showNotification("DB Table Name already exists.", Type.WARNING_MESSAGE);
				this.txtExpDbTableNameId.selectAll();
			}
			else if(!validateRestrictedDbTableFieldsResult)
				this.getUI().showNotification("'RecordId', 'Comments', 'CreatedBy', 'LastModifiedBy', 'DataFileId', 'CreatedDate', 'LastModifiedDate' are Non-Eligible for " + this.systemSettings.getExperimentLabel() + " Field DB Ids.", Type.WARNING_MESSAGE);
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
	
	private void replaceCsvUploadComponent(Upload upToReplace)
	{
		//Replace Uploader Component by a new one due to issues detected at loading file with same name
	    Upload newUpload = new Upload();
	    newUpload.setImmediate(true);
	    newUpload.setButtonCaption(upToReplace.getButtonCaption());
	    newUpload.setStyleName(upToReplace.getStyleName());
	    
	    newUpload.setReceiver(new Receiver(){

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) 
			{
			      FileOutputStream fos = null; 
			      try 
			      {
			    	  if(!filename.trim().toLowerCase().endsWith("csv"))
					    return null;
					  
			          // Open the file for writing.
			    	  tempCsvFile = File.createTempFile("tempCsv", ".csv");
			          fos = new FileOutputStream(tempCsvFile);
			      } 
			      catch (Exception e) {
			          // Error while opening the file. Not reported here.
			          e.printStackTrace();
			          return null;
			      }
			
			      return fos; // Return the output stream to write to
			}
	    });
	    
	    newUpload.addSucceededListener(new SucceededListener() {
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				// TODO Auto-generated method stub
			    processCsvUpload(tempCsvFile);
			    replaceCsvUploadComponent((Upload)event.getComponent());
			    tempCsvFile.delete();
			}
		});
	    
	    newUpload.addFailedListener(new FailedListener() {
			
			@Override
			public void uploadFailed(FailedEvent event) {
				// TODO Auto-generated method stub
				getUI().showNotification("Invalid CSV file. Try again.", Type.WARNING_MESSAGE);
			}
		});
	    pnlCsvUpload.setContent(null);
	    pnlCsvUpload.setContent(newUpload);
	}
	
	private void processCsvUpload(File csvFile)
	{
	    System.out.println("________________ PROCESSING FILE");
	    CSVReader reader = null;
	    
	    //Matrix with CSV Data
	    List<String> fieldNameCsvMtx = new ArrayList<String>();
	    List<String> fieldNameLowerCaseCsvMtx = new ArrayList<String>();
	    List<String> fieldDbIdCsvMtx = new ArrayList<String>();
	    List<String> fieldDbTypeCsvMtx = new ArrayList<String>();
	    List<String> fieldUoMCsvMtx = new ArrayList<String>();
	    List<Boolean> fieldAddedCsvMtx = new ArrayList<Boolean>();
	    
	    List<String> fieldNameCsvMtxRp = new ArrayList<String>();
	    List<String> fieldNameLowerCaseCsvMtxRp = new ArrayList<String>();
	    List<String> fieldDbIdCsvMtxRp = new ArrayList<String>();
	    List<String> fieldDbTypeCsvMtxRp = new ArrayList<String>();
	    List<String> fieldUoMCsvMtxRp = new ArrayList<String>();
	    List<Boolean> fieldAddedCsvMtxRp = new ArrayList<Boolean>();
	    List<Integer> fieldPositionCsvMtxRp = new ArrayList<Integer>();
	    
		try {
		
			Integer rowCnt = 0;
			//reader = new CSVReader(new FileReader(tempCsvFile));
			//reader = new CSVReader(new FileReader(tempCsvFile));
			reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));
		
			if(reader != null)
			{	
				String[] csvRow = reader.readNext();
				if(csvRow.length < 4)
				{
					getUI().showNotification("Invalid CSV file. File should contain 4 columns: 'FieldName', 'DatabaseId', 'DatabaseType', 'UoM'", Type.WARNING_MESSAGE);
			    	
					return;
				}
				

	            //Same column name scenario
	    		List<String> addedCsvColumnNameMtx = new ArrayList<String>();
	    		List<Integer> addedCsvColumnTimesMtx = new ArrayList<Integer>();	    		
				
				while(csvRow != null)
				{
					if(rowCnt == 0)
					{
						//System.out.println("First column : " + csvRow[0].trim().toLowerCase());
						//Validate Headers: FieldName, DatabaseId, DatabaseType, UoM
						if(csvRow[0] == null || (csvRow[0] != null && !csvRow[0].trim().toLowerCase().equals("fieldname")))
					    {
					    	getUI().showNotification("Invalid CSV file. FieldName column not found.", Type.WARNING_MESSAGE);
					    	break;
					    }
					    if(csvRow[1] == null || (csvRow[1] != null && !csvRow[1].trim().toLowerCase().equals("databaseid")))
					    {
					    	getUI().showNotification("Invalid CSV file. DatabaseId column not found.", Type.WARNING_MESSAGE);
					    	break;
					    }
					    if(csvRow[2] == null || (csvRow[2] != null && !csvRow[2].trim().toLowerCase().equals("databasetype")))
	    					{
					    	getUI().showNotification("Invalid CSV file. DatabaseType column not found.", Type.WARNING_MESSAGE);
					        break;
					    }
					    if(csvRow[3] == null || (csvRow[3] != null && !csvRow[3].trim().toLowerCase().equals("uom")))
					    {
							getUI().showNotification("Invalid CSV file. UoM column not found.", Type.WARNING_MESSAGE);
				            break;
					    }
					}
					else
					{						
						if(csvRow[0].trim().isEmpty() || csvRow[1].trim().isEmpty()
								&& (!csvRow[2].trim().isEmpty() || !csvRow[3].trim().isEmpty()))
							getUI().showNotification("Invalid CSV file. FieldName and DatabaseId are mandatory. CSV Line: " + (rowCnt+1), Type.WARNING_MESSAGE);
				        else
						{
				        	if(!csvRow[0].trim().isEmpty() && !csvRow[1].trim().isEmpty())
				        	{
				        		if(fieldNameCsvMtx.indexOf(csvRow[0].trim()) == -1)
				        		{				        			
				        			fieldNameCsvMtx.add(csvRow[0].trim());
				        			fieldNameLowerCaseCsvMtx.add(csvRow[0].trim().toLowerCase());
				        			fieldDbIdCsvMtx.add(csvRow[1].trim());
				        			fieldDbTypeCsvMtx.add(csvRow[2].trim());
				        			fieldUoMCsvMtx.add(csvRow[3].trim());
				        			fieldAddedCsvMtx.add(false);		        			

        		    				addedCsvColumnNameMtx.add(csvRow[0].trim());
        		    				addedCsvColumnTimesMtx.add(0);
				        		}
				        		else				        			
			        			{//+ "_" +  rowCnt
				        			
				        			String csvColumnName = csvRow[0].trim();
	        		    			Integer csvColumnAddedTimes = 0;
	        		    			if(addedCsvColumnNameMtx.indexOf(csvRow[0].trim()) != -1)
	        		    			{
	        		    				csvColumnAddedTimes = addedCsvColumnTimesMtx.get(addedCsvColumnNameMtx.indexOf(csvColumnName));
	        		    				csvColumnAddedTimes = csvColumnAddedTimes + 1;
	        		    				addedCsvColumnTimesMtx.set(addedCsvColumnNameMtx.indexOf(csvRow[0].trim()), csvColumnAddedTimes);
	        		    				csvRow[0] = csvRow[0].trim()  + "_" + csvColumnAddedTimes;
	        		    				csvRow[1] = csvRow[1].trim()  + "_" + csvColumnAddedTimes;
	        		    			}
	        		    			
				        			fieldNameCsvMtx.add(csvRow[0].trim());
				        			fieldNameLowerCaseCsvMtx.add(csvRow[0].trim().toLowerCase());
				        			fieldDbIdCsvMtx.add(csvRow[1].trim());
				        			fieldDbTypeCsvMtx.add(csvRow[2].trim());
				        			fieldUoMCsvMtx.add(csvRow[3].trim());
				        			fieldAddedCsvMtx.add(false);
				        			
				        			/*
				        			String newColumnName = csvRow[0].trim() + "_1";		
				        			String newColumnDbId = csvRow[1].trim() + "_1";
				        			boolean columnRepeatedFound = true;
				        			for(int i=1; columnRepeatedFound; i++)
				        			{
				        				if(fieldNameCsvMtxRp.indexOf(csvRow[0].trim() + "_" + i) == -1)
				        				{
				        					newColumnName = csvRow[0].trim() + "_" + i;	
				        					newColumnDbId = csvRow[1].trim() + "_" + i;	
				        					columnRepeatedFound = false;
				        					break;
				        				}
				        			}
				        			
				        			fieldNameCsvMtxRp.add(newColumnName);
				        			fieldNameLowerCaseCsvMtxRp.add(newColumnName.toLowerCase());
				        			fieldDbIdCsvMtxRp.add(newColumnDbId);
				        			fieldDbTypeCsvMtxRp.add(csvRow[2].trim());
				        			fieldUoMCsvMtxRp.add(csvRow[3].trim());
				        			fieldAddedCsvMtxRp.add(false);	
				        			fieldPositionCsvMtxRp.add(rowCnt);
				        			*/
			        			}
				        	}
						}
						
					}
					rowCnt++;
					csvRow = reader.readNext();
				}
				
	            reader.close();
	            /*
	            for (int i = 0; i< fieldNameCsvMtxRp.size(); i++) 
	            {
        			fieldNameCsvMtx.add(fieldPositionCsvMtxRp.get(i),fieldNameCsvMtxRp.get(i));
        			fieldNameLowerCaseCsvMtx.add(fieldPositionCsvMtxRp.get(i),fieldNameLowerCaseCsvMtxRp.get(i));
        			fieldDbIdCsvMtx.add(fieldPositionCsvMtxRp.get(i),fieldDbIdCsvMtxRp.get(i));
        			fieldDbTypeCsvMtx.add(fieldPositionCsvMtxRp.get(i),fieldDbTypeCsvMtxRp.get(i));
        			fieldUoMCsvMtx.add(fieldPositionCsvMtxRp.get(i),fieldUoMCsvMtxRp.get(i));
        			fieldAddedCsvMtx.add(fieldPositionCsvMtxRp.get(i),fieldAddedCsvMtxRp.get(i));
	            }
	            */
	            //Load Experiment Fields into TBL
	            List<UnitOfMeasure> unitOfMeasures = new UnitOfMeasureDao().getAllUnitOfMeasures();
	            List<String> uomNameMtx = new ArrayList<String>();
	            List<String> uomAbbreviationMtx = new ArrayList<String>();
	            List<Integer> uomIdMtx = new ArrayList<Integer>();
	            
	            for(int i=0; unitOfMeasures != null && i<unitOfMeasures.size(); i++)
	            {
	            	uomNameMtx.add(unitOfMeasures.get(i).getUomName());
	            	uomAbbreviationMtx.add(unitOfMeasures.get(i).getUomAbbreviation());
	            	uomIdMtx.add(unitOfMeasures.get(i).getUomId());
	            }
	            
	            
	            //Update Exp Fields already Added
	            Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
	    		
	    		for (Object itemIdObj : itemIds) 
	    		{	
	    			int itemId = (int)itemIdObj;
	    			
    				Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
    					
    				String fieldName = ((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue();
    				
    				if(fieldNameLowerCaseCsvMtx.indexOf(fieldName.trim().toLowerCase()) >= 0)
    				{
    					Integer fieldCsvMtxIndex = fieldNameLowerCaseCsvMtx.indexOf(fieldName.trim().toLowerCase());
    					fieldAddedCsvMtx.set(fieldCsvMtxIndex, true);
    					
    					String csvFieldDataType = fieldDbTypeCsvMtx.get(fieldCsvMtxIndex);
	        			
	        			Integer csvFieldUomId = -1;
	        			String csvFieldUom = fieldUoMCsvMtx.get(fieldCsvMtxIndex);
	        			
	        			if(uomNameMtx.indexOf(csvFieldUom) >= 0)
	        				csvFieldUomId = uomIdMtx.get(uomNameMtx.indexOf(csvFieldUom));
	        			else if(uomAbbreviationMtx.indexOf(csvFieldUom) >= 0)
	        				csvFieldUomId = uomIdMtx.get(uomAbbreviationMtx.indexOf(csvFieldUom));
	        			
	        			//Select DB DataType
	        			if(csvFieldDataType.trim().toLowerCase().startsWith("varchar") && selectedCsvVarcharValidation(csvFieldDataType))
	        			{
	        				if(csvFieldDataType.trim().toLowerCase().equals("varchar") || csvFieldDataType.trim().toLowerCase().equals("text"))
	        					csvFieldDataType = "varchar(max)";
	        				

	        				if(!((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).containsId(csvFieldDataType.trim().toLowerCase())) 
	        				{
	        					((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).addItem(csvFieldDataType.trim().toLowerCase());
	        					((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).setItemCaption(csvFieldDataType.trim().toLowerCase(), csvFieldDataType.trim().toLowerCase());
	        				}
	        				
	        				((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).select(csvFieldDataType.trim().toLowerCase());
        					
	        			}
	        			else
	        			{
	        				if(((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).containsId(csvFieldDataType.trim().toLowerCase())) 
	        					((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).select(csvFieldDataType.trim().toLowerCase());
	        			}
	        			
	        			//Select UoM
    					if(csvFieldUomId != -1)
    						((ComboBox)(tblRowItem.getItemProperty("UoM").getValue())).select(csvFieldUomId);			        		
    				}
	    			
	    		}
	    		

	            //Add new Exp Fields
	    		for(int i = 0; i<fieldNameCsvMtx.size(); i++)
	    		{
	    			System.out.println(fieldNameCsvMtx.size());
		            if(!fieldAddedCsvMtx.get(i))
		            {
		    			String csvFieldName = fieldNameCsvMtx.get(i);
	        			String csvFieldDbId = fieldDbIdCsvMtx.get(i);
	        			String csvFieldDataType = fieldDbTypeCsvMtx.get(i);
	        			
	        			Integer csvFieldUomId = -1;
	        			String csvFieldUom = fieldUoMCsvMtx.get(i);
	        			
	        			if(uomNameMtx.indexOf(csvFieldUom) >= 0)
	        				csvFieldUomId = uomIdMtx.get(uomNameMtx.indexOf(csvFieldUom));
	        			else if(uomAbbreviationMtx.indexOf(csvFieldUom) >= 0)
	        				csvFieldUomId = uomIdMtx.get(uomAbbreviationMtx.indexOf(csvFieldUom));
	        		
	        			//Set DB Id
	        			if(!StringUtils.isEmpty(csvFieldDbId.trim().toLowerCase()) && StringUtils.isAlphanumeric(csvFieldDbId.trim().toLowerCase().replaceAll("_", ""))
	        					&& !csvFieldDbId.trim().toLowerCase().equals("recordid") && !csvFieldDbId.trim().toLowerCase().equals("comments") 
	        					&& !csvFieldDbId.trim().toLowerCase().equals("createdby") && !csvFieldDbId.trim().toLowerCase().equals("lastmodifiedby") 
	        					&& !csvFieldDbId.trim().toLowerCase().equals("datafileid") && !csvFieldDbId.trim().toLowerCase().equals("createddate") 
	        					&& !csvFieldDbId.trim().toLowerCase().equals("lastmodifieddate")) 
	        				csvFieldDbId = csvFieldDbId.trim().toLowerCase();
	        			else
	        				csvFieldDbId = null;
	        			
	        			
		    			addField(csvFieldName, csvFieldDbId, csvFieldDataType, csvFieldUomId);	    	
		            }
	    		}
			}
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean selectedCsvVarcharValidation(String varcharDataType)
	{
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			if(varcharDataType.trim().toLowerCase().equals("varchar") || varcharDataType.trim().toLowerCase().equals("varchar(max)") || varcharDataType.trim().toLowerCase().equals("text"))
				return true;
			else
			{
				String varcharLengthStr = varcharDataType.trim().replace(" ", "").replace("varchar(", "").replace(")", "");
				return StringUtils.isNumeric(varcharLengthStr);
			}
		}
		
		return true;
	}
}
