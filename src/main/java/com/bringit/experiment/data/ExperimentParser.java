package com.bringit.experiment.data;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bringit.experiment.bll.CsvDataLoadExecutionResult;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.CsvTemplateColumns;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.XmlDataLoadExecutionResult;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.bll.XmlTemplateNode;
import com.bringit.experiment.dao.CsvDataLoadExecutionResultDao;
import com.bringit.experiment.dao.CsvTemplateColumnsDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.XmlDataLoadExecutionResultDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.opencsv.CSVReader;


public class ExperimentParser {

	public ResponseObj parseCSV(File csvFile,Experiment exp){
    	ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Csv Loaded Successfully");
    	CsvDataLoadExecutionResult csvResult = new CsvDataLoadExecutionResult();
    	CsvDataLoadExecutionResultDao csvResultDao = new CsvDataLoadExecutionResultDao();
        CSVReader reader = null;
        try {
        	if(exp != null){
        		if(csvFile != null){
        			String filename = csvFile.getName();
        			int expId = exp.getExpId();
        			String expName = exp.getExpName();
        			//Verify if there is a template and nodes for the experiment.
        			CsvTemplateDao templateDao = new CsvTemplateDao();
        			CsvTemplateColumnsDao nodeDao = new CsvTemplateColumnsDao();
        			CsvTemplate template = templateDao.getCsvTemplateByExperimentId(expId);
        			if (template != null){
        				//get the nodes:
        				List<CsvTemplateColumns> columns = nodeDao.getAllCsvTemplateColumnssByTemplateId(template.getCsvTemplateId());
        				if(columns != null){
				        	SysUserDao sysUserDao = new SysUserDao();
				        	SysUser user = sysUserDao.getUserByUserName("bit_seko");
	        				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		            reader = new CSVReader(new FileReader(csvFile));
        		            // we assume the first line is the header
        		            String[] header = reader.readNext();
        		            
        		            String colName = "";
        		            ArrayList<ArrayList> arr = new ArrayList<ArrayList>();
        		            
        		            for (CsvTemplateColumns column : columns) {
        		            	ArrayList maping = new ArrayList();
        		            	maping.add( Arrays.asList(header).indexOf(column.getCsvTemplateColumnName()));
        		            	maping.add( column.getExpField().getExpDbFieldNameId());
        		            	maping.add(column.getExpField().getExpFieldType());
        		            	arr.add(maping);
							}
        		            String[] line;
        		            String insertQry = "INSERT INTO "+exp.getExpDbTableNameId()+" (";
        		            String valuesQry = " VALUES (";
        		            for (int i = 0; i < arr.size(); i++) {
								insertQry+=(String) arr.get(i).get(1)+",";
							}
        		           
        		            String fieldType = "";
        		            while ((line = reader.readNext()) != null) {
            		            for (int i = 0; i < arr.size(); i++) {
            		            	fieldType= (String) arr.get(i).get(2);
    								//valuesQry+= line[(int) arr.get(i).get(0)]+",";
            		            	valuesQry+=fieldType.toLowerCase().startsWith("float")||fieldType.toLowerCase().startsWith("decimal")||fieldType.toLowerCase().startsWith("int") ? line[(int) arr.get(i).get(0)]+"," : "'"+line[(int) arr.get(i).get(0)]+"',";
    							}
            		            valuesQry+="'"+user.getUserId()+"','"+df.format(new Date())+"','"+filename+"'),(";
        		            }
		        			//removing commas
        		            insertQry+="CreatedBy,CreatedDate,FileName)";
        		            valuesQry = valuesQry.substring(0,valuesQry.length()-2);
	        				ExecuteQueryDao executeQueryDao = new ExecuteQueryDao();
	        				System.out.println(insertQry+valuesQry);
	        				executeQueryDao.executeQuery(insertQry+valuesQry);
		        			respObj.setDetail("FileName: "+filename );
		        			
		        			//uploading trace and log tables
                			csvResult.setCsvDataLoadExecException(false);
                			csvResult.setCsvDataLoadExecExeptionDetails(respObj.getDescription());
                			csvResult.setCsvDataLoadExecTime(new Date());
                			csvResult.setCsvTemplate(template);
                			csvResult.setCsvDataLoadExecExeptionFile(filename);
                			csvResultDao.addCsvDataLoadExecutionResult(csvResult);
                			
		        			return respObj;
        				}else{
                    		respObj.setCode(104);
                    		respObj.setDescription("There are no COLUMNS associated with the CsvTemplate: "+template.getCsvTemplateName());
                    		respObj.setDetail("NullPointerException");
                			csvResult.setCsvDataLoadExecException(true);
                			csvResult.setCsvDataLoadExecExeptionDetails(respObj.getDescription());
                			csvResult.setCsvDataLoadExecTime(new Date());
                			csvResult.setCsvDataLoadExecExeptionFile(filename);
                			csvResultDao.addCsvDataLoadExecutionResult(csvResult);
                    		return respObj;  
                    	}
        			}else{
                		respObj.setCode(103);
                		respObj.setDescription("There is no Template associated with the Experiment: "+expName);
                		respObj.setDetail("NullPointerException");
            			csvResult.setCsvDataLoadExecException(true);
            			csvResult.setCsvDataLoadExecExeptionDetails(respObj.getDescription());
            			csvResult.setCsvDataLoadExecTime(new Date());
            			csvResult.setCsvDataLoadExecExeptionFile(filename);
            			csvResultDao.addCsvDataLoadExecutionResult(csvResult);
                		return respObj;  
                	}
        		}else{
            		respObj.setCode(102);
            		respObj.setDescription("CSV file received is null.");
            		respObj.setDetail("NullPointerException");
        			csvResult.setCsvDataLoadExecException(true);
        			csvResult.setCsvDataLoadExecExeptionDetails(respObj.getDescription());
        			csvResult.setCsvDataLoadExecTime(new Date());
        			csvResultDao.addCsvDataLoadExecutionResult(csvResult);
            		return respObj;  
            	}
        	}else{
        		respObj.setCode(101);
        		respObj.setDescription("Experiment Object recieved is null.");
        		respObj.setDetail("NullPointerException");
    			csvResult.setCsvDataLoadExecException(true);
    			csvResult.setCsvDataLoadExecExeptionDetails(respObj.getDescription());
    			csvResult.setCsvDataLoadExecTime(new Date());
    			csvResultDao.addCsvDataLoadExecutionResult(csvResult);
        		return respObj;  
        	}
        } catch (IOException e) {
    		respObj.setCode(100);
    		respObj.setDescription("Unknown error, check for details.");
    		respObj.setDetail(e+e.getMessage());
			csvResult.setCsvDataLoadExecException(true);
			csvResult.setCsvDataLoadExecExeptionDetails(respObj.getDescription());
			csvResult.setCsvDataLoadExecTime(new Date());
			csvResultDao.addCsvDataLoadExecutionResult(csvResult);
    		return respObj;
        }
	}

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
        				//get loop and nodes:
        				String loopNode = nodeDao.getLoopXmlTemplateNodeByTemplateId(template.getXmlTemplateId()).getXmlTemplateNodeName();
        				String loopNodeName = loopNode.substring(loopNode.lastIndexOf("<")+1);//remove the rest of the tree
        				loopNodeName = loopNodeName.substring(0,loopNodeName.length()-1); //to remove the closing tag
        				List<XmlTemplateNode> nodes = nodeDao.getMappedXmlTemplateNodesByTemplateId(template.getXmlTemplateId());
        				if(nodes != null){			
				        	//The user that will be inserting the data for the experiment. This will be probably a system user.
				        	SysUserDao sysUserDao = new SysUserDao();
				        	SysUser user = sysUserDao.getUserByUserName("bit_seko");
				        	
				            SAXBuilder builder = new SAXBuilder(); 
				            //here we could compare the xml against a XSD to quickly identify any errors on the xml.
				            //TODO xml vs xsd validation
				            Document doc = builder.build(xmlFile);
				            if (doc != null){
				            	Element root = doc.getRootElement();			            
				        		List<Element> expResultNodes = root.getChildren(loopNodeName); 	
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
					        					//if(nod.getXmlTemplateNodeName().equals("ExperimentResults")){
					        					if(nod.getExpField() != null){
					        		 				if (i==0){
							        					query +=nod.getExpField().getExpDbFieldNameId()+",";
							        				}
					        		 				String nodName = nod.getXmlTemplateNodeName();
					        		 				nodName = nodName.substring(nodName.lastIndexOf("<")+1);
					        		 				nodName = nodName.substring(0,nodName.length()-1);
						        					String fieldValue = xmlExpResultNode.getChild(nodName)!= null ? xmlExpResultNode.getChild(nodName).getValue() : null;
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
    
    
    public ResponseObj parseXMLOLD(File xmlFile, Experiment exp) {
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
							        					String fieldValue = "";// xmlExpResultNode.getChild(nod.getXmlTemplateNodeAttributeName())!= null ? xmlExpResultNode.getChild(nod.getXmlTemplateNodeAttributeName()).getValue() : null;
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
