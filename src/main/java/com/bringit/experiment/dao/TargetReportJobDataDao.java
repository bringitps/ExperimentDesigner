package com.bringit.experiment.dao;

import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.TargetReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;
import com.bringit.experiment.util.HibernateXmlConfigSupport;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public List<TargetReportJobData> getActiveTargetJobs() {
        List<TargetReportJobData> targetReportJobDatas = new ArrayList<TargetReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            targetReportJobDatas = session.createQuery("from TargetReportJobData where IsCompleted=0").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetReportJobDatas;
    }

    public TargetReportJobData createJob(String type, SysUser sysUser) {
        TargetReportJobData targetReportJobData = new TargetReportJobData();
        targetReportJobData.setCreatedDate(new Date());
        targetReportJobData.setStatus(Constants.JOB_RUNNING);
        if (sysUser != null) {
            targetReportJobData.setCreatedBy(sysUser);
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
    public void targetProcedureJob(Integer targetId) {
        TargetReportJobData targetReportJobData = new TargetReportJobData();
        TargetReportDao targetDao = new TargetReportDao();
        try {
            if (this.getActiveTargetJobs().size() <= 0) {
                targetReportJobData = this.createJob(Constants.Auto, null);
                targetDao.executeTargetProcedure(targetReportJobData, targetId);
            } else {
                targetReportJobData = this.createJob(Constants.Auto, null);
                this.updateTargetJobStatus(targetReportJobData, Constants.JOB_NOT_EXECUTED);
            }
        } catch (Exception ex) {
            this.updateTargetJobStatus(targetReportJobData, Constants.JOB_EXCEPTION);
            ex.printStackTrace();
        }
    }

}
