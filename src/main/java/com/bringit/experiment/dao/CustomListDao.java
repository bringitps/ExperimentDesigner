package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.CustomList;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class CustomListDao {

	  private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();

	    public void addCustomList(CustomList customList) {

	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //Session session = HibernateUtil.openSession(dialectXmlFile);

	        try {
	            trns = session.beginTransaction();
	            session.save(customList);
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

	    public void deleteCustomList(int customListId) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        // Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            CustomList customList = (CustomList) session.load(CustomList.class, new Integer(customListId));
	            session.delete(customList);
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

	    public void updateCustomList(CustomList customList) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            session.update(customList);
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
	    public CustomList getCustomListById(int customListId) {
	        CustomList customList = null;
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        // Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            String queryString = "from CustomList where CustomListId = :id";
	            Query query = session.createQuery(queryString);
	            query.setInteger("id", customListId);
	            customList = (CustomList) query.uniqueResult();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return customList;
	    }
	    
	    @SuppressWarnings({"unchecked", "unused"})
	    public List<CustomList> getAllCustomLists() {
	        List<CustomList> customLists = new ArrayList<CustomList>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            customLists = session.createQuery("from CustomList").list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return customLists;
	    }
}
