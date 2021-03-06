package com.bringit.experiment.job;

import com.bringit.experiment.dao.ExperimentJobDataDao;
import com.bringit.experiment.dao.FinalPassYieldReportJobDataDao;
import com.bringit.experiment.dao.FirstPassYieldReportJobDataDao;
import com.bringit.experiment.dao.FirstTimeYieldReportJobDataDao;
import com.bringit.experiment.dao.TargetReportJobDataDao;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by msilay on 3/8/17.
 */
@DisallowConcurrentExecution
public class ProcedureExecutionJob implements Job {

    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {

        ExperimentJobDataDao experimentJobDataDao = new ExperimentJobDataDao();
        experimentJobDataDao.experimentProcedureJob();

        TargetReportJobDataDao targetReportJobDataDao = new TargetReportJobDataDao();
        targetReportJobDataDao.targetProcedureJob();
        
        FirstPassYieldReportJobDataDao fpyRptJobDataDao = new FirstPassYieldReportJobDataDao();
        fpyRptJobDataDao.fpyProcedureJob();
        
        FirstTimeYieldReportJobDataDao ftyRptJobDataDao = new FirstTimeYieldReportJobDataDao();
        ftyRptJobDataDao.ftyProcedureJob();
        
        FinalPassYieldReportJobDataDao fnyRptJobDataDao = new FinalPassYieldReportJobDataDao();
        fnyRptJobDataDao.fnyProcedureJob();
        
    }


}
