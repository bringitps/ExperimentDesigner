package com.bringit.experiment.dao;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ContractManufacturerDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addContractManufacturer(ContractManufacturer contractManufacturer) {

        Transaction trns = null;
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        Session session = HibernateUtil.getSessionFactory().openSession();
		//
        try {
            trns = session.beginTransaction();
            session.save(contractManufacturer);
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

    public void deleteContractManufacturer(int contractManufacturerId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ContractManufacturer contractManufacturer = (ContractManufacturer)session.load(ContractManufacturer.class, new Integer(contractManufacturerId));
            session.delete(contractManufacturer);
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

    public void updateContractManufacturer(ContractManufacturer contractManufacturer) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(contractManufacturer);
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

    @SuppressWarnings({ "unchecked", "unused" })
	public List<ContractManufacturer> getAllContractManufacturers() {
        List<ContractManufacturer> contractManufacturers = new ArrayList<ContractManufacturer>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            contractManufacturers = session.createQuery("from ContractManufacturer").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return contractManufacturers;
    }

    @SuppressWarnings("unused")
	public ContractManufacturer getContractManufacturerById(int contractManufacturerId) {
    	ContractManufacturer contractManufacturer = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ContractManufacturer where CmId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", contractManufacturerId);
            contractManufacturer = (ContractManufacturer) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return contractManufacturer;
    }
	
}
