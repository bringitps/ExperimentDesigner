package com.bringit.experiment.remote;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * Created by msilay on 2/27/17.
 */
public class RemoteFileListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        RemoteFileUtil rfu = RemoteFileUtil.getInstance();
        rfu.shutdown();

        ProcedureExecutionScheduler procedureExecutionScheduler= new ProcedureExecutionScheduler();
        procedureExecutionScheduler.shutdown();
        
        TargetReportDeleteScheduler targetReportDeleteScheduler = new TargetReportDeleteScheduler();
        targetReportDeleteScheduler.shutdown();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        try {

            RemoteFileUtil rfu = RemoteFileUtil.getInstance();
            rfu.startAll();

            List<String> allJobs = rfu.getScheduledJobs();
            System.out.println("Jobs Scheduled: " + allJobs );

            ProcedureExecutionScheduler procedureExecutionScheduler= new ProcedureExecutionScheduler();
            procedureExecutionScheduler.start();
            
            TargetReportDeleteScheduler targetReportDeleteScheduler = new TargetReportDeleteScheduler();
            targetReportDeleteScheduler.start();

        } catch (Exception ex) {
            System.out.println("Error stating Scheduler: "+ex);
        }
    }
}
