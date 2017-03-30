package com.bringit.experiment.remote;

import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;
import java.util.Calendar;

import static org.quartz.JobBuilder.newJob;

/**
 * Created by msilay on 2/16/17.
 */
public class RemoteFileUtil {

    private static RemoteFileUtil instance = null;
    private static Scheduler scheduler;
    public static final String XML_TRIGGER_GROUP = "xmlExperimentTriggers";
    public static final String XML_JOB_GROUP = "xmlExperimentJobs";
    public static final String CSV_TRIGGER_GROUP = "csvExperimentTriggers";
    public static final String CSV_JOB_GROUP = "csvExperimentJobs";
    private XmlTemplateDao xmlTemplateDao;
    private CsvTemplateDao csvTemplateDao;

    protected RemoteFileUtil() {
    }

    // Lazy Initialization (If required then only)
    public static RemoteFileUtil getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (RemoteFileUtil.class) {
                if (instance == null) {
                    instance = new RemoteFileUtil();
                }
            }
        }
        return instance;
    }

    public void updateJob(CsvTemplate jobData) {
        try {

            if (jobData == null) throw new RuntimeException("Cannot restart a job with a null name");

            //RemoteXmlJob ssd = new RemoteXmlJob();
            TriggerKey tk = new TriggerKey(jobData.getCsvTemplateId().toString(), CSV_JOB_GROUP);
            JobDetail job = newJob(RemoteCsvJob.class)
                    .withIdentity(jobData.getCsvTemplateId().toString(), CSV_JOB_GROUP)
                    .build();

            Trigger trigger = getTrigger(jobData);

            if (scheduler.checkExists(tk)) {
                System.out.println("UPDATING job: "+jobData.getCsvTemplateId().toString()+ " rescheduled the job.");
                scheduler.rescheduleJob(tk, trigger);
            } else {
                System.out.println("Scheduling a new job: "+jobData.getCsvTemplateId().toString()+ " NOT AN UPDATE.");
                scheduler.scheduleJob(job, trigger);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void updateJob(XmlTemplate jobData) {
        try {
        	System.out.println("Trying to update a job\n");
            if (jobData == null) throw new RuntimeException("Cannot restart a job with a null name");

            //RemoteXmlJob ssd = new RemoteXmlJob();
            TriggerKey tk = new TriggerKey(jobData.getXmlTemplateId().toString(), XML_JOB_GROUP);
            JobDetail job = newJob(RemoteXmlJob.class)
                    .withIdentity(jobData.getXmlTemplateId().toString(), XML_JOB_GROUP)
                    .build();

            Trigger trigger = getTrigger(jobData);

            if (scheduler.checkExists(tk)) {
            		System.out.println("UPDATING job: "+jobData.getXmlTemplateId().toString()+ " rescheduled the job.");
            		scheduler.rescheduleJob(tk, trigger);            	
            } else {
                System.out.println("Scheduling a new job: "+jobData.getXmlTemplateId().toString()+ " NOT AN UPDATE.");
                
                JobDataMap jobDataMap = job.getJobDataMap();
                jobDataMap.put("jobData", jobData);
                scheduler.scheduleJob(job, getTrigger(jobData));
            }


        } catch (Exception ex) {

        	System.out.println("Failed to update a job\n");
            System.out.println(ex);
        }
    }

    public void cancelJob(CsvTemplate jobData) {
        try {
        	cancelJobByIdAndGroup(jobData.getCsvTemplateId().toString(), CSV_JOB_GROUP);
        	
        	/*
            TriggerKey tk = new TriggerKey(jobData.getCsvTemplateId().toString(), CSV_JOB_GROUP);
            if (scheduler.checkExists(tk)) {
                System.out.println("UNSCHEDULED: "+jobData.getCsvTemplateId().toString());
                scheduler.unscheduleJob(tk);
            }
			*/
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public void cancelJob(XmlTemplate jobData) {
        try {
        	
        	cancelJobByIdAndGroup(jobData.getXmlTemplateId().toString(), XML_JOB_GROUP);
        	/*
        	TriggerKey tk = new TriggerKey(jobData.getXmlTemplateId().toString(), XML_JOB_GROUP);
            if (scheduler.checkExists(tk)) {
                System.out.println("UNSCHEDULED: "+jobData.getXmlTemplateId().toString());
                scheduler.unscheduleJob(tk);
            }
            */

        } catch (Exception ex) {
            System.out.println(ex);
        }
     
        System.out.println("Scheduled Jobs:" + getScheduledJobs());
    }

    public void stopAll() {
        try {

            scheduler.pauseAll();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void restartAll() {
        try {

            scheduler.resumeAll();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (Exception ex) {
            System.out.println("COULD NOT SHUTDOWN Scheduler: "+ex);
        }

    }

    public void startAll() {
        try {
            System.out.println("--- Service Looking for jobs ---");

            if (xmlTemplateDao == null) xmlTemplateDao = new XmlTemplateDao();

            List<XmlTemplate> jobsData = xmlTemplateDao.getAllScheduledXmlTemplates();
            if (jobsData != null) {
                System.out.println("Jobs: "+jobsData.size());
                System.out.println("Retrieving XML Jobs from Database and Scheduling One by One | Total Number of Jobs: " + jobsData.size());
            } else {
                System.out.println("NO XML Jobs to schedule...");
            }

            if (csvTemplateDao == null) csvTemplateDao = new CsvTemplateDao();
            List<CsvTemplate> csvJobsData = csvTemplateDao.getAllScheduledCsvTemplates();
            if (csvJobsData != null) {
                System.out.println("Jobs: "+csvJobsData.size());
                System.out.println("Retrieving CSV Jobs from Database and Scheduling One by One | Total Number of Jobs: " + csvJobsData.size());
            } else {
                System.out.println("NO CSV Jobs to schedule...");
            }

            try{

                if (scheduler == null) {
                    scheduler = new StdSchedulerFactory().getScheduler();
                }
                scheduler.start();

                for(XmlTemplate jobData: jobsData){
                    JobDetail job = newJob(RemoteXmlJob.class)
                            .withIdentity(jobData.getXmlTemplateId().toString(), XML_JOB_GROUP)
                            .build();
                    JobDataMap jobDataMap = job.getJobDataMap();
                    jobDataMap.put("jobData", jobData);

                    if(scheduler.checkExists(new JobKey(jobData.getXmlTemplateId().toString(), XML_JOB_GROUP))){
                        System.out.println("Rescheduling the Job");
                        Trigger oldTrigger = scheduler.getTrigger(new TriggerKey(jobData.getXmlTemplateId().toString(), XML_TRIGGER_GROUP));

                        Trigger trigger = getTrigger(jobData);

                        scheduler.rescheduleJob(oldTrigger.getKey(), trigger);
                    }else{
                        System.out.println("Scheduling the Job");
                        scheduler.scheduleJob(job,getTrigger(jobData));
                    }
                }

                for(CsvTemplate csvData: csvJobsData){
                    JobDetail job = newJob(RemoteCsvJob.class)
                            .withIdentity(csvData.getCsvTemplateId().toString(), CSV_JOB_GROUP)
                            .build();
                    JobDataMap jobDataMap = job.getJobDataMap();
                    jobDataMap.put("jobData", csvData);

                    if(scheduler.checkExists(new JobKey(csvData.getCsvTemplateId().toString(), CSV_JOB_GROUP))){
                        System.out.println("Rescheduling the Job");
                        Trigger oldTrigger = scheduler.getTrigger(new TriggerKey(csvData.getCsvTemplateId().toString(), CSV_TRIGGER_GROUP));

                        Trigger trigger = getTrigger(csvData);

                        scheduler.rescheduleJob(oldTrigger.getKey(), trigger);
                    }else{
                        System.out.println("Scheduling the Job");
                        scheduler.scheduleJob(job,getTrigger(csvData));
                    }
                }


            }catch (SchedulerException e) {
                System.out.println("Scheduler Exception : "+e.getMessage());
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public List<String> getScheduledJobs() {
        List<String> allJobs = new ArrayList<String>();
        try {

            for (String groupName : scheduler.getJobGroupNames()) {

                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();

                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    Date nextFireTime = triggers.get(0).getNextFireTime();
                    
                    String strJobDetails = "[jobName] : " + jobName + " [groupName] : "
                            + jobGroup + " - " + nextFireTime;
                    allJobs.add(strJobDetails);
                    System.out.println("[jobName] : " + jobName + " [groupName] : "
                            + jobGroup + " - " + nextFireTime);


                }
            }

        } catch (Exception ex) {
            System.out.println("Error listing schedules: "+ex);
        }
        return allJobs;
    }

    private Trigger getTrigger(CsvTemplate jobData){

        JobExecutionRepeat jobExecutionRepeat = jobData.getJobExecRepeat();
        // Simple trigger uses seconds for the repeat interval.  We will need to convert our long interval
        // value to an int for setting the trigger.
        Long iRepeat = new Long(jobExecutionRepeat.getJobExecRepeatMilliseconds()/1000);

        /*
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobData.getCsvTemplateId().toString(), CSV_TRIGGER_GROUP)
                .startAt(new Date(jobData.getCsvTemplateExecStartDate().getTime()))
                .endAt(new Date(jobData.getCsvTemplateExecEndDate().getTime()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(iRepeat.intValue())
                        .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).build();
        return trigger;*/
        

        int startHour = jobData.getCsvTemplateExecStartHour();
        System.out.println("Setting trigger with the following hour: "+startHour);
        java.util.Calendar startCal = java.util.Calendar.getInstance();
        startCal.setTimeInMillis(jobData.getCsvTemplateExecStartDate().getTime());
        startCal.set(java.util.Calendar.HOUR_OF_DAY, startHour);
        startCal.set(java.util.Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobData.getCsvTemplateId().toString(), CSV_TRIGGER_GROUP)
                .startAt(new Date(startCal.getTimeInMillis()))
                .endAt(new Date(jobData.getCsvTemplateExecEndDate().getTime()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(iRepeat.intValue())
                        .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).build();
        return trigger;
    }

    private Trigger getTrigger(XmlTemplate jobData){

        JobExecutionRepeat jobExecutionRepeat = jobData.getJobExecRepeat();
        // Simple trigger uses seconds for the repeat interval.  We will need to convert our long interval
        // value to an int for setting the trigger.
        Long iRepeat = new Long(jobExecutionRepeat.getJobExecRepeatMilliseconds()/1000);

        int startHour = jobData.getXmlTemplateExecStartHour();
        System.out.println("Setting trigger with the following hour: "+startHour);
        java.util.Calendar startCal = java.util.Calendar.getInstance();
        startCal.setTimeInMillis(jobData.getXmlTemplateExecStartDate().getTime());
        startCal.set(java.util.Calendar.HOUR_OF_DAY, startHour);
        startCal.set(java.util.Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobData.getXmlTemplateId().toString(), XML_TRIGGER_GROUP)
                .startAt(new Date(startCal.getTimeInMillis()))
                .endAt(new Date(jobData.getXmlTemplateExecEndDate().getTime()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(iRepeat.intValue())
                        .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)).build();
        return trigger;
    }

    /*private List<JobData> populateJobData() {
        List<JobData> jobs = new ArrayList<JobData>();

        try {

            JobData jd = new JobData();
            jd.setPort(22);
            jd.setDirectory("/EDI_backup/CMs");
            jd.setHost("ftp.aliph.com");
            jd.setPassword("m1cr0SFTP!!");
            jd.setUsername("ingrammicro");
            jd.setType("SFTP");
            jd.setSettings("0 0/10 * * * ?");
            jd.setJobId("000001");
            jd.setStartDate(Calendar.getInstance().getTimeInMillis() + 60000);
            jd.setEndDate(Calendar.getInstance().getTimeInMillis() + (60000 * 60 * 24));
            jd.setRepeatInterval(60000 * 5);
            jobs.add(jd);

        } catch (Exception ex) {
            System.out.println("Error populating job data");
        }

        return jobs;
    }*/

    
    
    private boolean cancelJobByIdAndGroup(String jobId, String jobGroup)
    {
    	try {

            for (String groupName : scheduler.getJobGroupNames()) {

                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    String jobKeyName = jobKey.getName();
                    String jobKeyGroup = jobKey.getGroup();

                    if(jobId.equals(jobKeyName) && jobGroup.equals(jobKeyGroup))
                    {
                    	List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                        scheduler.unscheduleJob(triggers.get(0).getKey());
                        System.out.println("Cancelled [jobName] : " + jobKeyName + " [groupName] : "
                                + jobKeyGroup );
                    	return true;
                    }                    
                }
            }

        } catch (Exception ex) {
            System.out.println("Error cancelling job: "+ex);
            return false;
        }
    	return false;
    }
    
}
