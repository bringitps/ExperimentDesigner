package com.bringit.experiment.dao;

import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.bll.TargetReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;
import com.bringit.experiment.util.HibernateXmlConfigSupport;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

public class TargetReportJobDataDao {

    private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();

    public TargetReportJobData addTargetJob(TargetReportJobData exp) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(exp);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return exp;
    }

    public void deleteTargetJob(int expId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            TargetReportJobData exp = (TargetReportJobData) session.load(TargetReportJobData.class, new Integer(expId));
            session.delete(exp);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    public void updateTargetJob(TargetReportJobData exp) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(exp);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<TargetReportJobData> getAllTargetJobs() {
        List<TargetReportJobData> targetReportJobDatas = new ArrayList<TargetReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            targetReportJobDatas = session.createQuery("from TargetReportJobData").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetReportJobDatas;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<TargetReportJobData> getActiveTargetJobs(Integer targetId) {
        List<TargetReportJobData> targetReportJobDatas = new ArrayList<TargetReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            targetReportJobDatas = session.createQuery("from TargetReportJobData where IsCompleted=0 and targetId="+targetId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetReportJobDatas;
    }

    public TargetReportJobData createJob(String type, SysUser sysUser, Integer targetId) {
        TargetReportJobData targetReportJobData = new TargetReportJobData();
        TargetReportDao targetReportDao= new TargetReportDao();
        targetReportJobData.setCreatedDate(new Date());
        targetReportJobData.setStatus(Constants.JOB_RUNNING);
        if (sysUser != null) {
            targetReportJobData.setCreatedBy(sysUser);
        }
        if(targetId !=null) {
            targetReportJobData.setTargetId(targetReportDao.getTargetReportById(targetId));
        }else {
            targetReportJobData.setTargetId(null);
        }

        targetReportJobData.setIsCompleted(false);
        if ((Constants.Auto).equalsIgnoreCase(type)) {
            targetReportJobData.setIsAutoScheduler(true);
        } else {
            targetReportJobData.setIsAutoScheduler(false);
        }
        return this.addTargetJob(targetReportJobData);
    }

    public void updateTargetJobStatus(TargetReportJobData targetReportJobData, String status) {
        targetReportJobData.setIsCompleted(true);
        targetReportJobData.setLastModifiedDate(new Date());
        targetReportJobData.setStatus(status);
        this.updateTargetJob(targetReportJobData);
    }

    public void targetProcedureJob() {
        targetProcedureJob(null);
    }


    public Map<String,Object> targetProcedureJob(Integer targetId) {

        Map<String,Object> map= new HashMap<>();
        TargetReportDao targetDao = new TargetReportDao();
        TargetReportJobData targetReportJobData = new TargetReportJobData();

        try {
        	System.out.println("Start of Refreshing Target Report data: " + new Date());
            List<TargetReport> lstTarget = new ArrayList<>();
            if (targetId == null || targetId <= 0) {
                lstTarget = targetDao.getAllActiveTargetReports();
            } else {
                lstTarget.add(targetDao.getTargetReportById(targetId));
            }

            for (TargetReport targetReport : lstTarget) {
                if (this.getActiveTargetJobs(targetReport.getTargetReportId()).size() <= 0) {
                	System.out.println("Refreshing Target Report data: " + targetReport.getTargetReportName());
                    targetReportJobData = this.createJob(Constants.Auto, null, targetReport.getTargetReportId());
                    map=targetDao.executeTargetProcedure(targetReportJobData, targetReport);
                } else {
                    map.put("statusMessage",Constants.JOB_NOT_EXECUTED );
                    map.put("status",Constants.ERROR);
                    targetReportJobData = this.createJob(Constants.Auto, null, targetReport.getTargetReportId());
                    this.updateTargetJobStatus(targetReportJobData, Constants.JOB_NOT_EXECUTED);
                }
            }
        	System.out.println("End of Refreshing Target Report data: " + new Date());
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return map;
    }

}
