package com.bringit.experiment.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.TargetReportDao;

public class TargetReportDeleteJob implements Job {

    SystemSettings sysSettings = new SystemSettingsDao().getCurrentSystemSettings();
    
	public void execute(JobExecutionContext arg0)
            throws JobExecutionException {

		List<TargetReport> listTargetReport = new TargetReportDao().getAllActiveTargetReports();
	    
		if(listTargetReport != null && listTargetReport.size() > 0)
		{
			for(int i=0; i<listTargetReport.size(); i++)
			{
				System.out.println(new Date() + "Start Deleting Target Report : " + listTargetReport.get(i).getTargetReportName());
				
				Date targetRptCreatedDate = listTargetReport.get(i).getCreatedDate();
			     
				Calendar c = Calendar.getInstance();
			    c.setTime(targetRptCreatedDate);
			    c.add(Calendar.DATE, sysSettings.getTargetReportAutoDeleteInterval());
			    c.set(Calendar.HOUR, 0); 
			    c.set(Calendar.MINUTE, 0);
			    c.set(Calendar.SECOND, 0);
			    
			    Date expirationDate = c.getTime();

		    	System.out.println("Current Date: " + new Date());
		    	System.out.println("Expiration Date: " + expirationDate);
			    if(new Date().getTime() >= expirationDate.getTime())
			    {	
			    	new TargetReportDao().deleteDBRptTable(listTargetReport.get(i));
					listTargetReport.get(i).setTargetReportIsActive(false);
			    	new TargetReportDao().updateTargetReport(listTargetReport.get(i));
			    }
			    else
			    	System.out.println(new Date() + "Target Report is not expired yet.: " + listTargetReport.get(i).getTargetReportName());
			    
				System.out.println(new Date() + "End Deleting Target Report : " + listTargetReport.get(i).getTargetReportName());				
			}
			
		}
    }
}
