package com.bringit.experiment.ui.form;

import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.SmtpDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.SystemSettingsDesign;
import com.bringit.experiment.util.DateUtil;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;

public class SystemSettingsForm extends SystemSettingsDesign{
	
	SystemSettings systemSettings;

    public SystemSettingsForm() {
    	
    	List<SystemSettings> systemSettingsList = new SystemSettingsDao().getSystemSettingsList();
    	
    	if(systemSettingsList.size() > 0)
    	{
    		systemSettings = systemSettingsList.get(0);
    		this.txtExperimentLabel.setValue(systemSettings.getExperimentLabel());
    		this.txtExperimentPluralLabel.setValue(systemSettings.getExperimentPluralLabel());
    		this.txtExperimentTypeLabel.setValue(systemSettings.getExperimentTypeLabel());
    		this.txtExperimentTypePluralLabel.setValue(systemSettings.getExperimentTypePluralLabel());
    	}
    	
    	this.btnSave.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveSystemSettings();
            }

        });
    }
    
    private void saveSystemSettings() {

        if (validateRequiredFields()) {
            SysUser sessionUser = (SysUser) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");

            if (systemSettings == null) {
            	systemSettings = new SystemSettings();
            	systemSettings.setCreatedDate(DateUtil.getDate());
            	systemSettings.setCreatedBy(sessionUser);
            }

            systemSettings.setExperimentLabel(this.txtExperimentLabel.getValue());
            systemSettings.setExperimentPluralLabel(this.txtExperimentPluralLabel.getValue());
            systemSettings.setExperimentTypeLabel(this.txtExperimentTypeLabel.getValue());
            systemSettings.setExperimentTypePluralLabel(this.txtExperimentTypePluralLabel.getValue());
            
            systemSettings.setLastModifiedBy(sessionUser);
            systemSettings.setModifiedDate(DateUtil.getDate());
            new SystemSettingsDao().addSystemSettings(systemSettings);

            System.out.println("systemSettings = " + systemSettings);
            if (systemSettings.getSystemSettingsId() != null) {
                this.getUI().showNotification("Data Updated Successfully.", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                this.getUI().showNotification("Data Saved.", Notification.Type.HUMANIZED_MESSAGE);
            }

			WebApplication webApp = (WebApplication)this.getParent().getParent().getParent().getParent().getParent();
			webApp.reloadMainForm("general settings");			
        }
        else
            this.getUI().showNotification("Please enter mandatory fields", Notification.Type.WARNING_MESSAGE);
    }
    
    private boolean validateRequiredFields() {

        if (this.txtExperimentLabel.getValue().isEmpty()) {
            return false;
        }
        if (this.txtExperimentPluralLabel.getValue().isEmpty()) {
            return false;
        }
        if (this.txtExperimentTypeLabel.getValue().isEmpty()) {
            return false;
        }
        if (this.txtExperimentTypePluralLabel.getValue().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
}
