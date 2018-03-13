package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FinalPassYieldInfoField;
import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.FinalPassYieldReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;

public class FinalPassYieldReportDao {


	public void addFinalPassYieldReport(FinalPassYieldReport fnyReport) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(fnyReport);
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

    public void deleteFinalPassYieldReport(int fnyReportId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            FinalPassYieldReport fnyReport = (FinalPassYieldReport) session.load(FinalPassYieldReport.class, new Integer(fnyReportId));
            session.delete(fnyReport);
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

    public void updateFinalPassYieldReport(FinalPassYieldReport fnyReport) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(fnyReport);
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
    public FinalPassYieldReport getFinalPassYieldReportById(int fnyReportId) {
    	FinalPassYieldReport fnyReport = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from FinalPassYieldReport where FnyReportId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", fnyReportId);
            fnyReport = (FinalPassYieldReport) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fnyReport;
    }
    
    @SuppressWarnings({"unchecked", "unused"})
    public List<FinalPassYieldReport> getAllFinalPassYieldReports() {
        List<FinalPassYieldReport> fnyReports = new ArrayList<FinalPassYieldReport>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            fnyReports = session.createQuery("from FinalPassYieldReport where FnyReportIsActive = 'true'").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fnyReports;
    }
    
    public boolean saveDBFnyRptTable(FinalPassYieldReport fnyRpt, List<FinalPassYieldInfoField> fnyInfoFields) {
    	deleteDBFnyRptTable(fnyRpt);
    	
    	String query = null;
        boolean successfulExecution = true;

        List<String> dbRptTableCols = new ArrayList<String>();
        List<String> dbRptTableTypes = new ArrayList<String>();

        dbRptTableCols.add("fny_experiment");
        dbRptTableTypes.add("varchar(MAX)");
        
        dbRptTableCols.add("fny_date_time");
        dbRptTableTypes.add("datetime");

        dbRptTableCols.add("fny_serial_number");
        dbRptTableTypes.add("varchar(MAX)");
        
        dbRptTableCols.add("fny_result");
        dbRptTableTypes.add("varchar(MAX)");	        
        
        for (int i = 0; i < fnyInfoFields.size(); i++) 
        {           
        	dbRptTableCols.add(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
            dbRptTableTypes.add(fnyInfoFields.get(i).getExperimentField().getExpFieldType());	             
        }

        Config configuration = new Config();


        if (configuration.getProperty("dbms").equals("sqlserver")) {
            String csvTableCols = "";
            for (int i = 0; i < dbRptTableCols.size(); i++) {
                csvTableCols += dbRptTableCols.get(i) + " " + dbRptTableTypes.get(i);

                if ((i + 1) < dbRptTableCols.size())
                    csvTableCols += ",";
            }

            query = " IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='" + fnyRpt.getFnyReportDbRptTableNameId() + "' AND xtype='U') ";
            query += " CREATE TABLE " + fnyRpt.getFnyReportDbRptTableNameId();
            query += " (FnyRecordId int NOT NULL PRIMARY KEY," + csvTableCols + ");";
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

    public boolean deleteDBFnyRptTable(FinalPassYieldReport fnyRpt) {
        String query = null;
        boolean successfulExecution = true;

        Config configuration = new Config();
        if (configuration.getProperty("dbms").equals("sqlserver")) {
            query = " IF EXISTS (SELECT * FROM sysobjects WHERE name='" + fnyRpt.getFnyReportDbRptTableNameId() + "' AND xtype='U') ";
            query += " DROP TABLE " + fnyRpt.getFnyReportDbRptTableNameId() + ";";
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
    
    public Map<String,Object> executeFnyRptProcedure(FinalPassYieldReportJobData fnyRptJobData, FinalPassYieldReport fnyRpt) {
        Map<String,Object> map= new HashMap<>();
        FinalPassYieldReportJobDataDao finalPassYieldReportJobDataDao = new FinalPassYieldReportJobDataDao();
        String statusMessage = Constants.JOB_FINISHED;

        map.put("statusMessage", statusMessage);
        map.put("status", Constants.SUCCESS);
        try {


            List<String> lstFnyRptBean;
            lstFnyRptBean = new ArrayList<>();
            lstFnyRptBean.add(fnyRpt.getFnyReportId().toString());
            new ExecuteQueryDao().executeUpdateStoredProcedure("spFnyReportBuilder", lstFnyRptBean);

            fnyRpt.setFnyReportDbRptTableLastUpdate(new Date());
            this.updateFinalPassYieldReport(fnyRpt);

            statusMessage = Constants.JOB_FINISHED;
        } catch (Exception ex) {
            statusMessage = Constants.JOB_EXCEPTION;

            map.put("statusMessage", statusMessage);
            map.put("status", Constants.ERROR);
            ex.printStackTrace();
        } finally {
        	finalPassYieldReportJobDataDao.updateFnyJobStatus(fnyRptJobData, statusMessage);
        }

        return map;
    }
    

	
}
