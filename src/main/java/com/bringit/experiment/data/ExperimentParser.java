package com.bringit.experiment.data;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.ExperimentMeasure;
import com.bringit.experiment.bll.ExperimentMeasureFieldValue;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentMeasureDao;
import com.bringit.experiment.dao.ExperimentMeasureFieldValueDao;
import com.bringit.experiment.dao.SysUserDao;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;



public class ExperimentParser {

    public ResponseObj parseXML(File xmlFile, String filename) {
    	ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Xml Loaded Successfully");
        try {
        	
        	ExperimentDao experimentDao = new ExperimentDao();
        	ExperimentFieldDao expFieldDao = new ExperimentFieldDao();
        	ExperimentMeasureDao experimentMeasureDao = new ExperimentMeasureDao();
        	ExperimentMeasureFieldValueDao expMeasureFieldValueDao = new ExperimentMeasureFieldValueDao();
        	SysUserDao sysUserDao = new SysUserDao();
        	SysUser user = sysUserDao.getUserByUserName("bit_seko");
        	
            SAXBuilder builder = new SAXBuilder();         
            Document doc = builder.build(xmlFile);
            if (doc != null){
            	Element root = doc.getRootElement();
            	Element expName = root.getChild("ExperimentName");
            	if(expName != null){  
            		//we get the Experiment Object by searching the name indicated in ExperimentName tag from the xml.
	            	Experiment exp = experimentDao.getExperimentByName(expName.getValue());
	            	if(exp != null){
	            		//If we find an experiment then we look for the active fields of that experiment.
		            	List<ExperimentField> fields = expFieldDao.getActiveExperimentFields(exp);
		            	//TODO add a validation in case there are no fields defined for the experiment in ExperimentField table
		        		//ExperimentResult Nodes from the xml
		        		List<Element> expResultNodes = root.getChildren("ExperimentResults"); 
		        		
		        		if(expResultNodes.size() > 0)
		        		{
		        			// iterate over XML nodes
		        			for(int i=0; i<expResultNodes.size(); i++)
		        			{       
		        				//create a measure for each row or set of results
		        				 ExperimentMeasure experimentMeasure = new ExperimentMeasure();
		        				 experimentMeasure.setExpMeasureComment("Comment for row "+i);
		        				 experimentMeasure.setFileName(filename);
		        				 experimentMeasure.setCreatedBy(user);
		        				 experimentMeasure.setCreatedDate(new Date());
		        				 experimentMeasure.setLastModifiedBy(user);
		        				 experimentMeasure.setModifiedDate(new Date());
		        				 experimentMeasureDao.addExperimentMeasure(experimentMeasure);
		        				 
			        			Element xmlExpResultNode = expResultNodes.get(i);        				
			        			try
			        			{
			        				//This is where we map the fields from the ExperimentField table to the fields on the XML file.
			        				// if we find a match then we save the value on ExperimentMeasureFieldValue table
			        				for (ExperimentField field : fields) {
			        					String fieldValue = xmlExpResultNode.getChild(field.getExpFieldName())!= null ? xmlExpResultNode.getChild(field.getExpFieldName()).getValue() : null;
			        					ExperimentMeasureFieldValue expFieldValue = new ExperimentMeasureFieldValue();
			        					expFieldValue.setExperimentField(field);
			        					expFieldValue.setExperimentMeasure(experimentMeasure);
			        					expFieldValue.setExpMeasureValue(fieldValue);
			        					expMeasureFieldValueDao.addExperimentMeasureFieldValue(expFieldValue);
									}
	
		        				}
		        				catch(Exception e)
		        				{
		    	            		respObj.setCode(104);
		    	            		respObj.setDescription("There was an error mapping one of the fields");
		    	            		respObj.setDetail(e+e.getMessage());
		    	            		return respObj;    
		        				}
		        				
		        			}    		
		        			respObj.setDetail("FileName: "+filename );
		        			return respObj;
		        		}else{
		            		respObj.setCode(103);
		            		respObj.setDescription("There are no nodes for ExperimentResults");
		            		respObj.setDetail("");
		            		return respObj;         		
		            	}
	            	}else{
	            		respObj.setCode(102);
	            		respObj.setDescription("There are Experiments with that name. "+expName.getValue());
	            		respObj.setDetail("");
	            		return respObj;  
	            	}
            	}else{
            		respObj.setCode(101);
            		respObj.setDescription("There are no values for ExperimentName");
            		respObj.setDetail("");
            		return respObj;
            	}
            }

        } catch(Exception e) {
    		respObj.setCode(100);
    		respObj.setDescription("Unknown error, check for details.");
    		respObj.setDetail(e+e.getMessage());
    		return respObj;
        }
        return null;
    }
}
