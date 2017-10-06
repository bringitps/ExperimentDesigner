package com.bringit.experiment.remote;

import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.job.ProcedureExecutionJob;
import com.bringit.experiment.util.Config;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class ProcedureExecutionScheduler {

    private static Scheduler scheduler;
    Config configuration = new Config();

    SystemSettings sysSettings = new SystemSettingsDao().getCurrentSystemSettings();
    
    public void start() {
        try {
            Config configuration = new Config();
//            int min = 01;
//            int hours = 0;
            JobDetail job = JobBuilder.newJob(ProcedureExecutionJob.class)
                    .withIdentity("procedureExecutionJob", "group1").build();

            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("procedureTrigger", "group1")
                    .withSchedule(
                    		CronScheduleBuilder.cronSchedule("0 0 0/" + sysSettings.getVisualizationDataRefreshInterval() + " * * ?"))
                            //CronScheduleBuilder.cronSchedule(configuration.getProperty("procedureCronExp")))
                    .build();

            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
           
            if(sysSettings.getVisualizationDataRefreshInterval() != 0) 
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
