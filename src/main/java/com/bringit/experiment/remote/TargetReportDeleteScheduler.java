package com.bringit.experiment.remote;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.job.TargetReportDeleteJob;
import com.bringit.experiment.util.Config;

public class TargetReportDeleteScheduler {
    private static Scheduler scheduler;
    Config configuration = new Config();

    SystemSettings sysSettings = new SystemSettingsDao().getCurrentSystemSettings();
    
    public void start() {
        try {
            JobDetail job = JobBuilder.newJob(TargetReportDeleteJob.class)
                    .withIdentity("targetReportDeleteJob", "groupTargetReportDelete").build();

            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("targetReportDeleteTrigger", "groupTargetReportDelete")
                    .withSchedule(
                    		CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
                            //CronScheduleBuilder.cronSchedule("0 0 0 1/" + sysSettings.getTargetReportAutoDeleteInterval() + " * ?"))
                            //CronScheduleBuilder.cronSchedule(configuration.getProperty("procedureCronExp")))
                    .build();

            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            
            if(sysSettings.getTargetReportAutoDeleteInterval() != 0) 
            	scheduler.scheduleJob(job, trigger);

        } catch (SchedulerException ex) {

        }
    }

    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (Exception ex) {
            System.out.println("COULD NOT SHUTDOWN Scheduler: " + ex);
        }

    }
}
