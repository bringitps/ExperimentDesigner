package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.CustomList;
import com.bringit.experiment.bll.CustomListValue;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class CustomListValueDao {

	 private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();

	    public void addCustomListValue(CustomListValue customListValue) {

	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //Session session = HibernateUtil.openSession(dialectXmlFile);

	        try {
	            trns = session.beginTransaction();
	            session.save(customListValue);
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

	    public void deleteCustomListValue(int customListValueId) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        // Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            CustomListValue customListValue = (CustomListValue) session.load(CustomListValue.class, new Integer(customListValueId));
	            session.delete(customListValue);
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

	    public void updateCustomListValue(CustomListValue customListValue) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            session.update(customListValue);
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
	    public List<CustomListValue> getAllCustomListValuesByCustomList(CustomList customList) {
	        List<CustomListValue> customListValues = new ArrayList<CustomListValue>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            customListValues = session.createQuery("from CustomListValue where CustomListId="+customList.getCustomListId()).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return customListValues;
	    }
	    
	    @SuppressWarnings("unused")
	    public CustomListValue getCustomListValueById(int customListValueId) {
	        CustomListValue customListValue = null;
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        // Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            String queryString = "from CustomListValue where CustomListValueId = :id";
	            Query query = session.createQuery(queryString);
	            query.setInteger("id", customListValueId);
	            customListValue = (CustomListValue) query.uniqueResult();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return customListValue;
	    }
	    
	    
}
