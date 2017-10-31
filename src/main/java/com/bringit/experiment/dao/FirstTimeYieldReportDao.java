package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FirstTimeYieldInfoField;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.FirstTimeYieldReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;

public class FirstTimeYieldReportDao {

	public void addFirstTimeYieldReport(FirstTimeYieldReport ftyReport) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(ftyReport);
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

    public void deleteFirstTimeYieldReport(int ftyReportId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            FirstTimeYieldReport ftyReport = (FirstTimeYieldReport) session.load(FirstTimeYieldReport.class, new Integer(ftyReportId));
            session.delete(ftyReport);
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

    public void updateFirstTimeYieldReport(FirstTimeYieldReport ftyReport) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(ftyReport);
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

    @SuppressWarnings("unused")
    public FirstTimeYieldReport getFirstTimeYieldReportById(int ftyReportId) {
    	FirstTimeYieldReport ftyReport = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from FirstTimeYieldReport where FtyReportId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", ftyReportId);
            ftyReport = (FirstTimeYieldReport) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return ftyReport;
    }
    
    @SuppressWarnings({"unchecked", "unused"})
    public List<FirstTimeYieldReport> getAllFirstTimeYieldReports() {
        List<FirstTimeYieldReport> ftyReports = new ArrayList<FirstTimeYieldReport>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ftyReports = session.createQuery("from FirstTimeYieldReport where FtyReportIsActive = 'true'").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return ftyReports;
    }
    
    public boolean saveDBFtyRptTable(FirstTimeYieldReport ftyRpt, List<FirstTimeYieldInfoField> ftyInfoFields) {
    	deleteDBFtyRptTable(ftyRpt);
    	
    	String query = null;
        boolean successfulExecution = true;

        List<String> dbRptTableCols = new ArrayList<String>();
        List<String> dbRptTableTypes = new ArrayList<String>();

        dbRptTableCols.add("fty_experiment");
        dbRptTableTypes.add("varchar(MAX)");
        
        dbRptTableCols.add("fty_date_time");
        dbRptTableTypes.add("datetime");

        dbRptTableCols.add("fty_serial_number");
        dbRptTableTypes.add("varchar(MAX)");
        
        dbRptTableCols.add("fty_result");
        dbRptTableTypes.add("varchar(MAX)");	        
        
        for (int i = 0; i < ftyInfoFields.size(); i++) 
        {           
        	dbRptTableCols.add(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
            dbRptTableTypes.add(ftyInfoFields.get(i).getExperimentField().getExpFieldType());	             
        }

        Config configuration = new Config();


        if (configuration.getProperty("dbms").equals("sqlserver")) {
            String csvTableCols = "";
            for (int i = 0; i < dbRptTableCols.size(); i++) {
                csvTableCols += dbRptTableCols.get(i) + " " + dbRptTableTypes.get(i);

                if ((i + 1) < dbRptTableCols.size())
                    csvTableCols += ",";
            }

            query = " IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='" + ftyRpt.getFtyReportDbRptTableNameId() + "' AND xtype='U') ";
            query += " CREATE TABLE " + ftyRpt.getFtyReportDbRptTableNameId();
            query += " (FtyRecordId int NOT NULL PRIMARY KEY," + csvTableCols + ");";
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

    public boolean deleteDBFtyRptTable(FirstTimeYieldReport ftyRpt) {
        String query = null;
        boolean successfulExecution = true;

        Config configuration = new Config();
        if (configuration.getProperty("dbms").equals("sqlserver")) {
            query = " IF EXISTS (SELECT * FROM sysobjects WHERE name='" + ftyRpt.getFtyReportDbRptTableNameId() + "' AND xtype='U') ";
            query += " DROP TABLE " + ftyRpt.getFtyReportDbRptTableNameId() + ";";
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
    
    public Map<String,Object> executeFtyRptProcedure(FirstTimeYieldReportJobData ftyRptJobData, FirstTimeYieldReport ftyRpt) {
        Map<String,Object> map= new HashMap<>();
        FirstTimeYieldReportJobDataDao firstTimeYieldReportJobDataDao = new FirstTimeYieldReportJobDataDao();
        String statusMessage = Constants.JOB_FINISHED;

        map.put("statusMessage", statusMessage);
        map.put("status", Constants.SUCCESS);
        try {


            List<String> lstFtyRptBean;
            lstFtyRptBean = new ArrayList<>();
            lstFtyRptBean.add(ftyRpt.getFtyReportId().toString());
            new ExecuteQueryDao().executeUpdateStoredProcedure("spFtyReportBuilder", lstFtyRptBean);

            ftyRpt.setFtyReportDbRptTableLastUpdate(new Date());
            this.updateFirstTimeYieldReport(ftyRpt);

            statusMessage = Constants.JOB_FINISHED;
        } catch (Exception ex) {
            statusMessage = Constants.JOB_EXCEPTION;

            map.put("statusMessage", statusMessage);
            map.put("status", Constants.ERROR);
            ex.printStackTrace();
        } finally {
        	firstTimeYieldReportJobDataDao.updateFtyJobStatus(ftyRptJobData, statusMessage);
        }

        return map;
    }
}
