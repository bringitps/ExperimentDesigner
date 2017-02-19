package com.bringit.experiment.data;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.XmlDataLoadExecutionResult;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.bll.XmlTemplateNode;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.XmlDataLoadExecutionResultDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class ExperimentParser {

    public ResponseObj parseXML(File xmlFile, Experiment exp) {
    	ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Xml Loaded Successfully");
		XmlDataLoadExecutionResult xmlResult = new XmlDataLoadExecutionResult();
		XmlDataLoadExecutionResultDao xmlResultDao = new XmlDataLoadExecutionResultDao();
    	
        try {
        	if(exp != null){
        		if(xmlFile != null){
        			String filename = xmlFile.getName();
        			int expId = exp.getExpId();
        			String expName = exp.getExpName();
        			//Verify if there is a template and nodes for the experiment.
        			XmlTemplateDao templateDao = new XmlTemplateDao();
        			XmlTemplateNodeDao nodeDao = new XmlTemplateNodeDao();
        			XmlTemplate template = templateDao.getXmlTemplateByExperimentId(expId);
        			if (template != null){
        				//get the nodes:
        				List<XmlTemplateNode> nodes = nodeDao.getAllXmlTemplateNodesByTemplateId(template.getXmlTemplateId());
        				if(nodes != null){
				        	
				        	ExperimentFieldDao expFieldDao = new ExperimentFieldDao();
		
				        	//The user that will be inserting the data for the experiment. This will be probably a system user.
				        	SysUserDao sysUserDao = new SysUserDao();
				        	SysUser user = sysUserDao.getUserByUserName("bit_seko");
				        	
				            SAXBuilder builder = new SAXBuilder(); 
				            //here we could compare the xml against a XSD to quickly identify any errors on the xml.
				            //TODO xml vs xsd validation
				            Document doc = builder.build(xmlFile);
				            if (doc != null){
				            	Element root = doc.getRootElement();
				            	Element expName_element = root.getChild("ExperimentName");				            
				            	if(expName_element != null && expName.trim().equals(expName_element.getValue().trim())){  
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
						        				for(XmlTemplateNode nod : nodes){
						        					if(nod.getXmlTemplateNodeName().equals("ExperimentResults")){
						        		 				if (i==0){
								        					query +=nod.getExpField().getExpDbFieldNameId()+",";
								        				}
							        					String fieldValue = xmlExpResultNode.getChild(nod.getXmlTemplateNodeAttributeName())!= null ? xmlExpResultNode.getChild(nod.getXmlTemplateNodeAttributeName()).getValue() : null;
							        					String fieldType = nod.getExpField().getExpFieldType();
							        					qryValues += fieldType.toLowerCase().startsWith("float")||fieldType.toLowerCase().startsWith("decimal")||fieldType.toLowerCase().startsWith("int") ? fieldValue+"," : "'"+fieldValue+"',";
						        					}
							       
						        				}
						        				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						        				qryValues = qryValues+"'"+user.getUserId()+"','"+df.format(new Date())+"','"+filename+"'),(";
						        				
					        				}
					        				catch(Exception e)
					        				{
					    	            		respObj.setCode(107);
					    	            		respObj.setDescription("There was an error mapping one of the fields");
					    	            		respObj.setDetail(e+e.getMessage());
					    	            		return respObj;    
					        				}
					        				
					        			} 
					        			//removing commas
			        					query = query+"CreatedBy,CreatedDate,FileName)";
					        			qryValues = qryValues.substring(0,qryValues.length()-2);
				        				ExecuteQueryDao executeQueryDao = new ExecuteQueryDao();
				        				System.out.println(query+qryValues);
				        				executeQueryDao.executeQuery(query+qryValues);
					        			respObj.setDetail("FileName: "+filename );
					        			
					        			//uploading trace and log tables
					        			xmlResult.setXmlDataLoadExecException(false);
					        			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
					        			xmlResult.setXmlDataLoadExecTime(new Date());
					        			xmlResult.setXmlTemplate(template);
					        			xmlResult.setXmlDataLoadExecExeptionFile(filename);
					        			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
					        			return respObj;
					        		}else{
					            		respObj.setCode(106);
					            		respObj.setDescription("There are no nodes for ExperimentResults");
					            		respObj.setDetail("");

					        			xmlResult.setXmlDataLoadExecException(true);
					        			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
					        			xmlResult.setXmlDataLoadExecTime(new Date());
					        			xmlResult.setXmlTemplate(template);
					        			xmlResult.setXmlDataLoadExecExeptionFile(filename);
					        			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
					            		return respObj;         		
					            	}
				            	}else{
				            		respObj.setCode(105);
				            		respObj.setDescription("The ExperimentName value doesnt match the XmlTemplate");
				            		respObj.setDetail("");
				        			xmlResult.setXmlDataLoadExecException(true);
				        			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
				        			xmlResult.setXmlDataLoadExecTime(new Date());
				        			xmlResult.setXmlTemplate(template);
				        			xmlResult.setXmlDataLoadExecExeptionFile(filename);
				        			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
				            		return respObj;
				            	}
				            }
        				}else{
                    		respObj.setCode(104);
                    		respObj.setDescription("There are no xmlTemplate nodes associated with the XmlTemplate: "+template.getXmlTemplateName());
                    		respObj.setDetail("NullPointerException");
		        			xmlResult.setXmlDataLoadExecException(true);
		        			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
		        			xmlResult.setXmlDataLoadExecTime(new Date());
		        			xmlResult.setXmlDataLoadExecExeptionFile(filename);
		        			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
                    		return respObj;  
                    	}
            		}else{
                		respObj.setCode(103);
                		respObj.setDescription("There is no Template associated with the Experiment: "+expName);
                		respObj.setDetail("NullPointerException");
	        			xmlResult.setXmlDataLoadExecException(true);
	        			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
	        			xmlResult.setXmlDataLoadExecTime(new Date());
	        			xmlResult.setXmlDataLoadExecExeptionFile(filename);
	        			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
                		return respObj;  
                	}
        		}else{
            		respObj.setCode(102);
            		respObj.setDescription("Xml file received is null.");
            		respObj.setDetail("NullPointerException");
        			xmlResult.setXmlDataLoadExecException(true);
        			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
        			xmlResult.setXmlDataLoadExecTime(new Date());
        			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
            		return respObj;  
            	}
        	}else{
        		respObj.setCode(101);
        		respObj.setDescription("Experiment Object recieved is null.");
        		respObj.setDetail("NullPointerException");
    			xmlResult.setXmlDataLoadExecException(true);
    			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
    			xmlResult.setXmlDataLoadExecTime(new Date());
    			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
        		return respObj;  
        	}
        } catch(Exception e) {
    		respObj.setCode(100);
    		respObj.setDescription("Unknown error, check for details.");
    		respObj.setDetail(e+e.getMessage());
			xmlResult.setXmlDataLoadExecException(true);
			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
			xmlResult.setXmlDataLoadExecTime(new Date());
			xmlResultDao.addXmlDataLoadExecutionResult(xmlResult);
    		return respObj;
        }
        return respObj;
    }
}
