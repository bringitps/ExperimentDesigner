package com.bringit.experiment.data;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.SysUserDao;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;



public class ExperimentParser2 {

    public ResponseObj parseXML(InputStream xmlFile, String filename) {
    	ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Xml Loaded Successfully");
    	
        try {
        	
        	ExperimentDao experimentDao = new ExperimentDao();
        	ExperimentFieldDao expFieldDao = new ExperimentFieldDao();
        	//ExperimentMeasureDao experimentMeasureDao = new ExperimentMeasureDao();
        	//ExperimentMeasureFieldValueDao expMeasureFieldValueDao = new ExperimentMeasureFieldValueDao();
        	
        	//The user that will be inserting the data for the experiment. This will be probably a system user.
        	SysUserDao sysUserDao = new SysUserDao();
        	SysUser user = sysUserDao.getUserByUserName("bit_seko");
        	
            SAXBuilder builder = new SAXBuilder(); 
            //here we could compare the xml against a XSD to quicky identify any errors on the xml.
            //TODO xml vs xsd validation
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
		        		
        				String query = "INSERT INTO "+exp.getExpDbTableNameId()+" (";
        				String qryValues = " VALUES (";
        				
		        		if(expResultNodes.size() > 0)
		        		{
		        			// iterate over XML nodes
		        			for(int i=0; i<expResultNodes.size(); i++)
		        			{           				 
			        			Element xmlExpResultNode = expResultNodes.get(i);        				
			        			try
			        			{

			        				//Here we map the fields from ExperimentField/ExperimentDataTable vs Xml Fields
			        				for (ExperimentField field : fields) {
				        				if (i==0){
				        					query +=field.getExpFieldName()+",";
				        				}
			        					String fieldValue = xmlExpResultNode.getChild(field.getExpFieldName())!= null ? xmlExpResultNode.getChild(field.getExpFieldName()).getValue() : null;
			        					String fieldType = field.getExpFieldType();
			        					qryValues += fieldType.toLowerCase().startsWith("float")||fieldType.toLowerCase().startsWith("decimal")||fieldType.toLowerCase().startsWith("int") ? fieldValue+"," : "'"+fieldValue+"',";
									}

			        				qryValues = qryValues.substring(0,qryValues.length()-1)+"),(";

		        				}
		        				catch(Exception e)
		        				{
		    	            		respObj.setCode(104);
		    	            		respObj.setDescription("There was an error mapping one of the fields");
		    	            		respObj.setDetail(e+e.getMessage());
		    	            		return respObj;    
		        				}
		        				
		        			} 
		        			//removing commas
        					query = query.substring(0,query.length()-1)+")";
		        			qryValues = qryValues.substring(0,qryValues.length()-2);
	        				ExecuteQueryDao executeQueryDao = new ExecuteQueryDao();
	        				System.err.println(query+qryValues);
	        				executeQueryDao.executeQuery(query+qryValues);
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
        return respObj;
    }
}
