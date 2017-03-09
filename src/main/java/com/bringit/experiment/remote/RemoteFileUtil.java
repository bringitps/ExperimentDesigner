package com.bringit.experiment.remote;

import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dao.XmlTemplateDao;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.List;

import static org.quartz.JobBuilder.newJob;

/**
 * Created by msilay on 2/16/17.
 */
public class RemoteFileUtil {

    private static RemoteFileUtil instance = null;
    private static Scheduler scheduler;
    public static final String TRIGGER_GROUP = "experimentTriggers";
    public static final String JOB_GROUP = "experimentJobs";
    private XmlTemplateDao xmlTemplateDao;

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

    public void updateJob(String jobName, XmlTemplate jobData) {
        try {

            if (jobName == null) throw new RuntimeException("Cannot restart a job with a null name");

            //RemoteXmlJob ssd = new RemoteXmlJob();
            TriggerKey tk = new TriggerKey(jobName, JOB_GROUP);
            JobDetail job = newJob(RemoteXmlJob.class)
                    .withIdentity(jobName, JOB_GROUP)
                    .build();

            Trigger trigger = getTrigger(jobData);

            if (scheduler.checkExists(tk)) {
                System.out.println("UPDATING job: "+jobName+ " rescheduled the job.");
                scheduler.rescheduleJob(tk, trigger);
            } else {
                System.out.println("Scheduling a new job: "+jobName+ " NOT AN UPDATE.");
                scheduler.scheduleJob(job, trigger);
            }


        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void cancelJob(String jobName) {
        try {
            TriggerKey tk = new TriggerKey(jobName, JOB_GROUP);
            if (scheduler.checkExists(tk)) {
                System.out.println("UNSCHEDULED: "+jobName);
                scheduler.unscheduleJob(tk);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

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

            List<XmlTemplate> jobsData = xmlTemplateDao.getAllXmlTemplates();
            System.out.println("Jobs: "+jobsData.size());
            System.out.println("Retriving Jobs from Database and Scheduling One by One | Total Number of Jobs: " + jobsData.size());
            try{

                if (scheduler == null) {
                    scheduler = new StdSchedulerFactory().getScheduler();
                }
                scheduler.start();

                for(XmlTemplate jobData: jobsData){
                    JobDetail job = newJob(RemoteXmlJob.class)
                            .withIdentity(jobData.getXmlTemplateId().toString(), JOB_GROUP)
                            .build();
                    JobDataMap jobDataMap = job.getJobDataMap();
                    jobDataMap.put("jobData", jobData);

                    if(scheduler.checkExists(new JobKey(jobData.getXmlTemplateId().toString(), JOB_GROUP))){
                        System.out.println("Rescheduling the Job");
                        Trigger oldTrigger = scheduler.getTrigger(new TriggerKey(jobData.getXmlTemplateId().toString(), TRIGGER_GROUP));

                        Trigger trigger = getTrigger(jobData);

                        scheduler.rescheduleJob(oldTrigger.getKey(), trigger);
                    }else{
                        System.out.println("Scheduling the Job");
                        scheduler.scheduleJob(job,getTrigger(jobData));
                    }
                }
            }catch (SchedulerException e) {
                System.out.println("Scheduler Exception : "+e.getMessage());
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private Trigger getTrigger(XmlTemplate jobData){

        JobExecutionRepeat jobExecutionRepeat = jobData.getJobExecRepeat();
        // Simple trigger uses seconds for the repeat interval.  We will need to convert our long interval
        // value to an int for setting the trigger.
        Long iRepeat = new Long(jobExecutionRepeat.getJobExecRepeatMilliseconds()/1000);

        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobData.getXmlTemplateId().toString(), TRIGGER_GROUP)
                .startAt(new Date(jobData.getXmlTemplateExecStartDate().getTime())/*new Date(java.util.Calendar.getInstance().getTimeInMillis() + 60000)*/)
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

}
