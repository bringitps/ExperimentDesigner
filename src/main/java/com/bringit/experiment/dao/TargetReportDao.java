package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.*;
import com.bringit.experiment.util.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class TargetReportDao {

    private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();

    public void addTargetReport(TargetReport targetReport) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);

        try {
            trns = session.beginTransaction();
            session.save(targetReport);
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

    public void deleteTargetReport(int targetReportId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            TargetReport targetReport = (TargetReport) session.load(TargetReport.class, new Integer(targetReportId));
            session.delete(targetReport);
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

    public void updateTargetReport(TargetReport targetReport) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(targetReport);
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
    public List<TargetReport> getAllTargetReports() {
        List<TargetReport> targetReports = new ArrayList<TargetReport>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            targetReports = session.createQuery("from TargetReport").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetReports;
    }

    @SuppressWarnings("unused")
    public TargetReport getTargetReportById(int targetReportId) {
        TargetReport targetReport = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from TargetReport where TargetReportId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", targetReportId);

            //System.out.println("Target Report Id: " + targetReportId);

            targetReport = (TargetReport) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetReport;
    }


    @SuppressWarnings({"unchecked", "unused"})
    public List<TargetReport> getAllActiveTargetReports() {
        List<TargetReport> targetReports = new ArrayList<TargetReport>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            targetReports = session.createQuery("from TargetReport where TargetReportIsActive = 'true'").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetReports;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<TargetReport> getAllActiveTargetReportsByExperimentId(Integer experimentId) {
        List<TargetReport> targetReports = new ArrayList<TargetReport>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            targetReports = session.createQuery("from TargetReport where TargetReportIsActive = 'true' and ExpId = " + experimentId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetReports;
    }


    public boolean deleteDBRptTableByName(String dbRptTableName) {
        String query = null;
        boolean successfulExecution = true;

        Config configuration = new Config();
        if (configuration.getProperty("dbms").equals("sqlserver")) {
            query = " IF EXISTS (SELECT * FROM sysobjects WHERE name='" + dbRptTableName + "' AND xtype='U') ";
            query += " DROP TABLE " + dbRptTableName + ";";
        } else
            return false;

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            successfulExecution = false;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return successfulExecution;
    }

    public boolean deleteDBRptTable(TargetReport targetRpt) {
        String query = null;
        boolean successfulExecution = true;

        Config configuration = new Config();
        if (configuration.getProperty("dbms").equals("sqlserver")) {
            query = " IF EXISTS (SELECT * FROM sysobjects WHERE name='" + targetRpt.getTargetReportDbRptTableNameId() + "' AND xtype='U') ";
            query += " DROP TABLE " + targetRpt.getTargetReportDbRptTableNameId() + ";";
        } else
            return false;

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            successfulExecution = false;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return successfulExecution;
    }

    public boolean updateDBRptTable(TargetReport targetRpt) {
        String query = null;
        boolean successfulExecution = true;

        List<TargetColumnGroup> targetRptColGroups = new TargetColumnGroupDao().getTargetColumnGroupsByReportId(targetRpt.getTargetReportId());
        List<String> dbRptTableCols = new ArrayList<String>();
        List<String> dbRptTableTypes = new ArrayList<String>();

        for (int i = 0; i < targetRptColGroups.size(); i++) {
            List<TargetColumn> targetRptCols = new TargetColumnDao().getTargetColumnsByColGroupById(targetRptColGroups.get(i).getTargetColumnGroupId());

            for (int j = 0; j < targetRptCols.size(); j++) {
                dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_"));
                dbRptTableTypes.add(targetRptCols.get(j).getExperimentField().getExpFieldType());

                if (!targetRptCols.get(j).getTargetColumnIsInfo()) {
                    dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Result");
                    dbRptTableTypes.add("varchar(20)");
                }
            }
        }

        dbRptTableCols.add("Result");
        dbRptTableTypes.add("varchar(20)");

        Config configuration = new Config();


        if (configuration.getProperty("dbms").equals("sqlserver")) {
            String csvTableCols = "";
            for (int i = 0; i < dbRptTableCols.size(); i++) {
                csvTableCols += dbRptTableCols.get(i) + " " + dbRptTableTypes.get(i);

                if ((i + 1) < dbRptTableCols.size())
                    csvTableCols += ",";
            }

            query = " IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='" + targetRpt.getTargetReportDbRptTableNameId() + "' AND xtype='U') ";
            query += " CREATE TABLE " + targetRpt.getTargetReportDbRptTableNameId();
            query += " (RecordId int NOT NULL PRIMARY KEY, Comments text, CmName varchar(255),";
            query += " CreatedBy varchar(255), LastModifiedBy varchar(255), CreatedDate datetime,";
            query += " LastModifiedDate datetime, DataFileName varchar(255)," + csvTableCols + ");";
        } else
            return false;

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            successfulExecution = false;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return successfulExecution;
    }

    public void executeTargetProcedure(TargetReportJobData TargetReportJobData, Integer targetId) {

        TargetReportJobDataDao TargetReportJobDataDao = new TargetReportJobDataDao();
        String status = Constants.JOB_FINISHED;
        try {
            List<TargetReport> lstTarget = new ArrayList<>();
            if (targetId == null || targetId <= 0) {
                lstTarget = this.getAllActiveTargetReports();
            } else {
                lstTarget.add(this.getTargetReportById(targetId));
            }

            List<String> lstTargetBean;
            for (TargetReport target : lstTarget) {
                lstTargetBean = new ArrayList<>();
                lstTargetBean.add(target.getTargetReportId().toString());
                new ExecuteQueryDao().executeUpdateStoredProcedure("spTargetReportBuilder", lstTargetBean);

                target.setTargetReportDbRptTableLastUpdate(new Date());
                this.updateTargetReport(target);
            }
            status = Constants.JOB_FINISHED;
        } catch (Exception ex) {
            status = Constants.JOB_EXCEPTION;
            ex.printStackTrace();
        } finally {
            TargetReportJobDataDao.updateTargetJobStatus(TargetReportJobData, status);
        }
    }
}
