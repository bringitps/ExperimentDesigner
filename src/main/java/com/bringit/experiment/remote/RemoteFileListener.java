package com.bringit.experiment.remote;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by msilay on 2/27/17.
 */
public class RemoteFileListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        RemoteFileUtil rfu = RemoteFileUtil.getInstance();
        rfu.shutdown();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        try {

            RemoteFileUtil rfu = RemoteFileUtil.getInstance();
            rfu.startAll();

            List<String> allJobs = rfu.getScheduledJobs();
            System.out.println("Jobs Scheduled: " + allJobs );
            
        } catch (Exception ex) {
            System.out.println("Error stating Scheduler: "+ex);
        }
    }
}
