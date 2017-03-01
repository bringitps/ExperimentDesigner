package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.XmlTemplateNode;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class XmlTemplateNodeDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addXmlTemplateNode(XmlTemplateNode xmlTemplateNode) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(xmlTemplateNode);
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

    public void deleteXmlTemplateNode(int xmlTemplateNodeId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            XmlTemplateNode xmlTemplateNode = (XmlTemplateNode)session.load(XmlTemplateNode.class, new Integer(xmlTemplateNodeId));
            session.delete(xmlTemplateNode);
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

    public void updateXmlTemplateNode(XmlTemplateNode xmlTemplateNode) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(xmlTemplateNode);
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
	public List<XmlTemplateNode> getAllXmlTemplateNodes() {
        List<XmlTemplateNode> xmlTemplateNodes = new ArrayList<XmlTemplateNode>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            xmlTemplateNodes = session.createQuery("from XmlTemplateNode").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlTemplateNodes;
    }

    @SuppressWarnings("unused")
	public XmlTemplateNode getXmlTemplateNodeById(int xmlTemplateNodeId) {
    	XmlTemplateNode xmlTemplateNode = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from XmlTemplateNode where XmlTemplateNodeId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", xmlTemplateNodeId);
            xmlTemplateNode = (XmlTemplateNode) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlTemplateNode;
    }

	public List<XmlTemplateNode> getAllXmlTemplateNodesByTemplateId(int xmlTemplateId) {
		List<XmlTemplateNode> nodes = new ArrayList<XmlTemplateNode>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            nodes = session.createQuery("from XmlTemplateNode where XmlTemplateId ="+xmlTemplateId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return nodes;
	}

	public XmlTemplateNode getLoopXmlTemplateNodeByTemplateId(Integer xmlTemplateId) {
		List<XmlTemplateNode> nodes = new ArrayList<XmlTemplateNode>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            nodes = session.createQuery("from XmlTemplateNode where XmlTemplateId ="+xmlTemplateId+ " and XmlTemplateNodeIsLoop = 'true'").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return nodes.get(0);
	}

	public List<XmlTemplateNode> getMappedXmlTemplateNodesByTemplateId(Integer xmlTemplateId) {
		List<XmlTemplateNode> nodes = new ArrayList<XmlTemplateNode>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            nodes = session.createQuery("from XmlTemplateNode where XmlTemplateId ="+xmlTemplateId+ " and ExpFieldId is not null").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return nodes;
	}
	
	
}
