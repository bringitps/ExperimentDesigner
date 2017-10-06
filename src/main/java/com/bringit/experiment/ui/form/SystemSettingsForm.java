package com.bringit.experiment.ui.form;

import static org.quartz.JobBuilder.newJob;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.SmtpDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.remote.RemoteCsvJob;
import com.bringit.experiment.remote.RemoteFileUtil;
import com.bringit.experiment.ui.design.SystemSettingsDesign;
import com.bringit.experiment.util.DateUtil;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;

public class SystemSettingsForm extends SystemSettingsDesign{
	
	SystemSettings systemSettings;
    private static Scheduler scheduler;

    public SystemSettingsForm() {
    	
    	List<SystemSettings> systemSettingsList = new SystemSettingsDao().getSystemSettingsList();
    	
    	if(systemSettingsList.size() > 0)
    	{
    		systemSettings = systemSettingsList.get(0);
    		this.txtExperimentLabel.setValue(systemSettings.getExperimentLabel());
    		this.txtExperimentPluralLabel.setValue(systemSettings.getExperimentPluralLabel());
    		this.txtExperimentTypeLabel.setValue(systemSettings.getExperimentTypeLabel());
    		this.txtExperimentTypePluralLabel.setValue(systemSettings.getExperimentTypePluralLabel());
    		
    		if(systemSettings.getTargetReportAutoDeleteInterval() != null)
    			this.txtTargetReportAutoDeleteInterval.setValue(systemSettings.getTargetReportAutoDeleteInterval().toString());
    		
    		if(systemSettings.getVisualizationDataRefreshInterval() != null)
    			this.txtRefreshDataInterval.setValue(systemSettings.getVisualizationDataRefreshInterval().toString());
    		
    	}
    	
    	this.btnSave.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveSystemSettings();
            }

        });
    	

		Validator integerValidator = new Validator() {

            public void validate(Object value) throws InvalidValueException {
               try
               {
            	   Integer.parseInt((String) value);
               }
               catch(Exception e)
               {
            	   throw new InvalidValueException("Invalid Number");
               }
            }
        };
        this.txtTargetReportAutoDeleteInterval.addValidator(integerValidator);
        this.txtRefreshDataInterval.addValidator(integerValidator);
    }
    
    private void saveSystemSettings() {

    	if(!validateIntegerFields())
    		return;
    	
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
            systemSettings.setTargetReportAutoDeleteInterval(Integer.parseInt(this.txtTargetReportAutoDeleteInterval.getValue()));
            systemSettings.setVisualizationDataRefreshInterval(Integer.parseInt(this.txtRefreshDataInterval.getValue()));
            
            systemSettings.setLastModifiedBy(sessionUser);
            systemSettings.setModifiedDate(DateUtil.getDate());
            new SystemSettingsDao().addSystemSettings(systemSettings);

            if (systemSettings.getSystemSettingsId() != null) {
                this.getUI().showNotification("Data Updated Successfully.", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                this.getUI().showNotification("Data Saved.", Notification.Type.HUMANIZED_MESSAGE);
            }

            /*ReStart Scheduled Jobs*/
        	
            TriggerKey tkDataRefresh = new TriggerKey("procedureTrigger", "group1");
            TriggerKey tkTargetRptDelete = new TriggerKey("targetReportDeleteTrigger", "groupTargetReportDelete");
                        
            try {
            	
            	if (scheduler == null) 
            	       scheduler = new StdSchedulerFactory().getScheduler();
                
            	if (scheduler.checkExists(tkDataRefresh)) 
				{			
            		 Trigger oldDataRefreshTrigger = scheduler.getTrigger(tkDataRefresh);
					
            		 if(this.txtRefreshDataInterval.getValue() != "0")
            		 {
            			 Trigger trigger = TriggerBuilder
			                    .newTrigger()
			                    .withIdentity("procedureTrigger", "group1")
			                    .withSchedule(
			                    		CronScheduleBuilder.cronSchedule("0 0 0/" + this.txtRefreshDataInterval.getValue() + " * * ?"))
			                            //CronScheduleBuilder.cronSchedule(configuration.getProperty("procedureCronExp")))
			                    .build();
            			 scheduler.rescheduleJob(oldDataRefreshTrigger.getKey(), trigger);
            		 }
            		 else
            			 scheduler.pauseTrigger(oldDataRefreshTrigger.getKey());
            		 
				}
            	
            	if (scheduler.checkExists(tkTargetRptDelete)) 
				{
             		 Trigger oldTargetRptDeleteTrigger = scheduler.getTrigger(tkTargetRptDelete);
 					
             		 if(this.txtRefreshDataInterval.getValue() != "0")
             		 {
             			
             			 Trigger trigger = TriggerBuilder
 			                    .newTrigger()
 			                    .withIdentity("targetReportDeleteTrigger", "groupTargetReportDelete")
 			                    .withSchedule(
 			                    		CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
 	                            //CronScheduleBuilder.cronSchedule("0 0 0 1/" + sysSettings.getTargetReportAutoDeleteInterval() + " * ?"))
 			                    .build();
             			 scheduler.rescheduleJob(oldTargetRptDeleteTrigger.getKey(), trigger);
             		 }
             		 else
             			 scheduler.pauseTrigger(oldTargetRptDeleteTrigger.getKey());            		 
				}
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				System.out.println("Unexpected exception at rescheduling jobs. ");
				e.printStackTrace();
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
    
    private boolean validateIntegerFields()
    {
    	 if (!this.txtTargetReportAutoDeleteInterval.isValid() || !this.txtRefreshDataInterval.isValid()) 
            return false;
         
         return true;
    }
    
}
