package com.bringit.experiment.data;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.XmlDataLoadExecutionResultDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class ExperimentParser {
	
	public ResponseObj parseCSV(File csvFile,CsvTemplate template){
    	ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Csv Loaded Successfully");
    	CsvDataLoadExecutionResult csvResult = new CsvDataLoadExecutionResult();
    	CsvDataLoadExecutionResultDao csvResultDao = new CsvDataLoadExecutionResultDao();
        CSVReader reader = null;
        try {
        	
        		if(csvFile != null){
        			String filename = csvFile.getName();

        			//Verify if there is a template and nodes for the experiment.
        			CsvTemplateColumnsDao nodeDao = new CsvTemplateColumnsDao();
        			if (template != null){
        				Experiment exp = template.getExperiment();
        				if(exp != null){
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
	                			reader.close();
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
        	        		respObj.setCode(101);
        	        		respObj.setDescription("Experiment Object recieved is null.");
        	        		respObj.setDetail("NullPointerException");
        	    			csvResult.setCsvDataLoadExecException(true);
        	    			csvResult.setCsvDataLoadExecExeptionDetails(respObj.getDescription());
        	    			csvResult.setCsvDataLoadExecTime(new Date());
        	    			csvResultDao.addCsvDataLoadExecutionResult(csvResult);
        	        		return respObj;  
        	        	}
        			}else{
                		respObj.setCode(103);
                		respObj.setDescription("There is no Template associated with the Experiment");
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


	public ResponseObj parseXmlDocument(String fileName, org.w3c.dom.Document xmlDocument, XmlTemplate xmlTemplate)
	{
		
		class DBFieldValues {
            
			private List<String> fieldValues;
			
			public DBFieldValues()
			{
				this.fieldValues = new ArrayList<String>();
			}
			
			public List<String> getFieldValues() {
				return fieldValues;
			}
			
			public void setFieldValues(List<String> fieldValues) {
				this.fieldValues = fieldValues;
			}
			
			public void attachFieldValue(String fieldValue)
			{
				this.fieldValues.add(fieldValue);
			}
        };
		
		ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Xml Loaded Successfully");
    	respObj.setCsvInsertColumns(null);
    	respObj.setCsvInsertValues(null);
		
		
    	//XRef Table between ExpFieldDBId, XmlNode, IsAttribute and NodeValues
    	List<String> xRefFieldDBId = new ArrayList<String>();
    	List<String> xRefXmlNodeSlashFormat = new ArrayList<String>();
    	List<Boolean> xRefXmlNodeIsAttribute = new ArrayList<Boolean>();
    	List<String> xRefXmlNodeAttributeName= new ArrayList<String>();
    	List<DBFieldValues> xRefDBFieldValues = new ArrayList<DBFieldValues>();
    	
		ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		List<ExperimentField> experimentFields = experimentFieldDao.getActiveExperimentFields(xmlTemplate.getExperiment());
		
		for(int i=0; experimentFields!= null && i<experimentFields.size(); i++)
		{
			xRefFieldDBId.add(experimentFields.get(i).getExpDbFieldNameId());
			xRefXmlNodeSlashFormat.add(null);
			xRefXmlNodeIsAttribute.add(null);
			xRefXmlNodeAttributeName.add(null);
			xRefDBFieldValues.add(new DBFieldValues());
		}
		
		//Run Validations and Fill xRef table
    	//1) All Xml nodes mapped to Experiment Field must exist in Xml Document to Load
    	XmlTemplateNodeDao xmlTemplateNodeDao = new XmlTemplateNodeDao();
    	List<XmlTemplateNode> xmlTemplateNodes = xmlTemplateNodeDao.getMappedXmlTemplateNodesByTemplateId(xmlTemplate.getXmlTemplateId());
		
		for(int i=0; i<xmlTemplateNodes.size(); i++)
		{
			if(xmlTemplateNodes.get(i).getExpField() != null || xmlTemplateNodes.get(i).isXmlTemplateNodeIsLoop())
			{
				String xmlNodeSlashFormat = "/" + xmlTemplateNodes.get(i).getXmlTemplateNodeName().replaceAll("<", "/").replaceAll(">", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
				String attributeName = "";
				
				if(xmlTemplateNodes.get(i).isXmlTemplateNodeIsAttribute())
				{
					attributeName = (xmlTemplateNodes.get(i).getXmlTemplateNodeName().substring(xmlTemplateNodes.get(i).getXmlTemplateNodeName().lastIndexOf(" ") + 1)).replaceAll(">", "");
					
					//Remove Attribute name to find node
					xmlNodeSlashFormat = xmlTemplateNodes.get(i).getXmlTemplateNodeName().substring(0, xmlTemplateNodes.get(i).getXmlTemplateNodeName().lastIndexOf(" "));
					xmlNodeSlashFormat = "/" + xmlNodeSlashFormat.replaceAll("<", "/").replaceAll(">", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
				
				}
				
				if(!xmlTemplateNodes.get(i).isXmlTemplateNodeIsLoop())
				{
					int xRefIndex = xRefFieldDBId.indexOf(xmlTemplateNodes.get(i).getExpField().getExpDbFieldNameId());
					if(xRefIndex >= 0)
					{
						xRefXmlNodeSlashFormat.set(xRefIndex, xmlNodeSlashFormat);
						xRefXmlNodeIsAttribute.set(xRefIndex, xmlTemplateNodes.get(i).isXmlTemplateNodeIsAttribute());
						
						if(xmlTemplateNodes.get(i).isXmlTemplateNodeIsAttribute())
							xRefXmlNodeAttributeName.set(xRefIndex, attributeName);
					}
				}
					
			     //Pending to consider when value is passed as Attribute
				 XPath xPath = XPathFactory.newInstance().newXPath();
			     XPathExpression xPathExpression = null;
			     try 
			     {
			    	 xPathExpression = xPath.compile(xmlNodeSlashFormat);
			     } catch (XPathExpressionException e) 
			     {
					// TODO Auto-generated catch block
					e.printStackTrace();
			     }
			     
			     Object resultObj = null;
				
			     try 
			     {
					resultObj = xPathExpression.evaluate(xmlDocument, XPathConstants.NODESET);
			     } catch (XPathExpressionException e) 
			     {
						// TODO Auto-generated catch block
						e.printStackTrace();
			     }

			     NodeList xmlNodesFound = (NodeList)resultObj;
			     
			     if(xmlNodesFound.getLength() <= 0)
			     {
			    	respObj.setCode(104);
             		respObj.setDescription("Mapped XML Node was not found in XML File. Node: "+xmlTemplateNodes.get(i).getXmlTemplateNodeName());
             		respObj.setDetail("Mapped XML Node Not Found");
	        		return respObj;
			     }
			     else if(xmlTemplateNodes.get(i).isXmlTemplateNodeIsAttribute())
			     {
			    	 boolean isAttributeFound = false;
			    	 for(int j=0; j<xmlNodesFound.getLength(); j++)
			    	 {
			    		Node xmlNode = xmlNodesFound.item(i);
			    		
			 	    	if(xmlNode instanceof org.w3c.dom.Element) 
			 	    	{
			 	    		org.w3c.dom.Element xmlElement = (org.w3c.dom.Element)xmlNode;
			 	    		if(xmlElement.getAttribute(attributeName) != null)
			 	    		{
			 	    			isAttributeFound = true;
			 	    			break;
			 	    		}
			 	    	}			 	    	
			    	 }
			    	 
			    	 if(!isAttributeFound)
			    	 {
			    		respObj.setCode(104);
	             		respObj.setDescription("Mapped XML Node Attribute was not found in XML File. Node Attribute: "+xmlTemplateNodes.get(i).getXmlTemplateNodeName());
	             		respObj.setDetail("Mapped XML Node Attribute Not Found");
		        		return respObj;
			    	}
			     }
 			}
		}
		
    	//2) Load Global Values (Not included by Loop)
		List<String> expDBFieldIdMatrix = new ArrayList<String>();
		List<String> xmlGlobalValuesMatrix = new ArrayList<String>();
		
		String loopXmlNodeFullName = xmlTemplateNodeDao.getLoopXmlTemplateNodeByTemplateId(xmlTemplate.getXmlTemplateId()).getXmlTemplateNodeName();
		String loopXmlNodeSlashFormat = "/" + loopXmlNodeFullName.replaceAll("<", "/").replaceAll(">", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
		
		for(int i=0; i<xmlTemplateNodes.size(); i++)
		{
			if(xmlTemplateNodes.get(i).getExpField() != null)
			{
				String xmlNodeSlashFormat = "/" + xmlTemplateNodes.get(i).getXmlTemplateNodeName().replaceAll("<", "/").replaceAll(">", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
			
				String attributeName = "";
				
				if(xmlTemplateNodes.get(i).isXmlTemplateNodeIsAttribute())
				{
					attributeName = (xmlTemplateNodes.get(i).getXmlTemplateNodeName().substring(xmlTemplateNodes.get(i).getXmlTemplateNodeName().lastIndexOf(" ") + 1)).replaceAll(">", "");
					
					//Remove Attribute name to find node
					xmlNodeSlashFormat = xmlTemplateNodes.get(i).getXmlTemplateNodeName().substring(0, xmlTemplateNodes.get(i).getXmlTemplateNodeName().lastIndexOf(" "));
					xmlNodeSlashFormat = "/" + xmlNodeSlashFormat.replaceAll("<", "/").replaceAll(">", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
				}
				
				if(!xmlNodeSlashFormat.startsWith(loopXmlNodeSlashFormat))
				{
					expDBFieldIdMatrix.add(xmlTemplateNodes.get(i).getExpField().getExpDbFieldNameId());
					
					 XPath xPath = XPathFactory.newInstance().newXPath();
				     XPathExpression xPathExpression = null;
				     try 
				     {
				    	 xPathExpression = xPath.compile(xmlNodeSlashFormat);
				     } catch (XPathExpressionException e) 
				     {
						// TODO Auto-generated catch block
						e.printStackTrace();
				     }
				     
				     Object resultObj = null;
					
				     try 
				     {
						resultObj = xPathExpression.evaluate(xmlDocument, XPathConstants.NODESET);
				     } catch (XPathExpressionException e) 
				     {
							// TODO Auto-generated catch block
							e.printStackTrace();
				     }

				     NodeList xmlNodesFound = (NodeList)resultObj;
					
				     if(!xmlTemplateNodes.get(i).isXmlTemplateNodeIsAttribute())
				     {
				    	 for(int j=0; j<xmlNodesFound.getLength(); j++)
				    	 {
				    		if(xmlNodesFound.item(j)!=null  && ((org.w3c.dom.Element)xmlNodesFound.item(j)).getChildNodes().getLength() <= 1 )
				    		{
				    			xmlGlobalValuesMatrix.add(xmlNodesFound.item(j).getTextContent().trim());
				    			break;
				    		}
				    	 }
				     }
				     else
				     {
				    	 //Value Passed as Attribute
				    	 for(int j=0; j<xmlNodesFound.getLength(); j++)
				    	 {
				    		Node xmlNode = xmlNodesFound.item(i);
				    		
				 	    	if(xmlNode instanceof org.w3c.dom.Element) 
				 	    	{
				 	    		org.w3c.dom.Element xmlElement = (org.w3c.dom.Element)xmlNode;
				 	    		if(xmlElement.getAttribute(attributeName) != null && xmlElement.getAttribute(attributeName).trim().length() > 0)
				 	    		{
				 	    			xmlGlobalValuesMatrix.add(xmlElement.getAttribute(attributeName).trim());
					    			break;
				 	    		}
				 	    	}			 	    	
				    	 }
				     }			    	 
				}
			}
    	
		}
		
		int totalRecords = 0;
			
		//4) Load Loop Node Field Values
		
		 XPath xPath = XPathFactory.newInstance().newXPath();
	     XPathExpression xPathExpression = null;
	     try 
	     {
	    	 xPathExpression = xPath.compile(loopXmlNodeSlashFormat);
	     } catch (XPathExpressionException e) 
	     {
			// TODO Auto-generated catch block
			e.printStackTrace();
	     }
	     
	     Object resultObj = null;
		
	     try 
	     {
			resultObj = xPathExpression.evaluate(xmlDocument, XPathConstants.NODESET);
	     } catch (XPathExpressionException e) 
	     {
				// TODO Auto-generated catch block
				e.printStackTrace();
	     }

	     NodeList xmlNodesFound = (NodeList)resultObj;
	     
	     if(xmlNodesFound != null)
	    	 totalRecords = xmlNodesFound.getLength();
	     
	     for (int i = 0; i < xmlNodesFound.getLength(); i++) 
	     { 
	    	Node xmlNode = xmlNodesFound.item(i);
	    	 
	    	if(xmlNode instanceof org.w3c.dom.Element) 
	    	{
	    		org.w3c.dom.Element xmlLoopElement = (org.w3c.dom.Element)xmlNode;
	    		
	    		for(int j = 0; j < xRefFieldDBId.size(); j++ )
	    		{
	    			if(xRefXmlNodeSlashFormat.get(j) != null && xRefXmlNodeSlashFormat.get(j).startsWith(loopXmlNodeSlashFormat))
	    			{
	    				org.w3c.dom.Element xmlValueElement = xmlLoopElement;
	    				String nodeNameSlashFormat = xRefXmlNodeSlashFormat.get(j).replace(loopXmlNodeSlashFormat, "");

	    				
	    				while(nodeNameSlashFormat.contains("/"))
	    				{
	    					String[] splitNodes = nodeNameSlashFormat.split("/");
	    					if(xmlValueElement.getElementsByTagName(splitNodes[1]).item(0) != null)
	    						xmlValueElement = (org.w3c.dom.Element)xmlValueElement.getElementsByTagName(splitNodes[1]).item(0);
	    					else
	    					{
	    						xmlValueElement = null;
	    						break;
	    					}
	    					nodeNameSlashFormat = nodeNameSlashFormat.substring(splitNodes[0].length()+1);
	    				}
	    				
	    				if(xRefXmlNodeIsAttribute.get(j) != null && xRefXmlNodeIsAttribute.get(j))
	    				{
							String attributeName = xRefXmlNodeAttributeName.get(j);
							if(xmlValueElement.getAttribute(attributeName) != null && xmlValueElement.getAttribute(attributeName).trim().length() > 0)
								xRefDBFieldValues.get(j).attachFieldValue(xmlValueElement.getAttribute(attributeName).trim());
							else
	    						xRefDBFieldValues.get(j).attachFieldValue(null);
	    				}
	    				else
	    				{
	    					if(xmlValueElement != null && xmlValueElement.getChildNodes().getLength() <= 1 && xmlValueElement.getTextContent() != null)
	    						xRefDBFieldValues.get(j).attachFieldValue(xmlValueElement.getTextContent().trim());
	    					else
	    						xRefDBFieldValues.get(j).attachFieldValue(null);
	    				}
	    			}
	    		}
	    	 }
	     }
	     
	     
	     //Load Global Values into XRef Table
	     for(int i=0; i<expDBFieldIdMatrix.size(); i++)
	     {
	    	int xRefIndex = xRefFieldDBId.indexOf(expDBFieldIdMatrix.get(i));
	    	
			if(xRefIndex >= 0)
			{
				List<String> fieldValues = new ArrayList<String>();
				for(int j=0; j<totalRecords; j++)
					fieldValues.add(xmlGlobalValuesMatrix.get(i));
				xRefDBFieldValues.get(xRefIndex).setFieldValues(fieldValues);
			}
	     }

	     //Build CSV Insert Columns
	     String csvInsertColumns = ""; 
	     
	     for(int i=0; i<xRefFieldDBId.size(); i++)
	     {
	    	 csvInsertColumns += xRefFieldDBId.get(i).toString();
	    	 if(xRefFieldDBId.size() > (i+1))
	    		 csvInsertColumns += ",";
	     }

	     //Build CSV Insert Values
	     List<String> csvInsertValues = new ArrayList<String>();
	     for(int i=0; i<totalRecords; i++)
	     {
	    	 String csvInsertColumnValues = "";
	    	 for(int j=0; j<xRefDBFieldValues.size(); j++)
		     {
	    		 if(xRefDBFieldValues.get(j).getFieldValues() != null && xRefDBFieldValues.get(j).getFieldValues().size() > 0 && xRefDBFieldValues.get(j).getFieldValues().get(i) != null)
	    			csvInsertColumnValues += "'" + xRefDBFieldValues.get(j).getFieldValues().get(i).toString() + "'";
	    		 else
	    			csvInsertColumnValues += "null";
		    	
	    		 if(xRefFieldDBId.size() > (j+1))
	    			 csvInsertColumnValues += ",";
	         }
	    	 
	    	 csvInsertValues.add(csvInsertColumnValues);
	     }
	    respObj.setCsvInsertColumns(csvInsertColumns);
	    respObj.setCsvInsertValues(csvInsertValues);
		return respObj;
	}

	
	public ResponseObj parseXML(File xmlFile, XmlTemplate template) {
    	ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Xml Loaded Successfully");
		XmlDataLoadExecutionResult xmlResult = new XmlDataLoadExecutionResult();
		XmlDataLoadExecutionResultDao xmlResultDao = new XmlDataLoadExecutionResultDao();
    	//Experiment exp = template.getExperiment();
        try {
        //	if(exp != null){
        		if(xmlFile != null){
        			String filename = xmlFile.getName();

        			//Verify if there is a template and nodes for the experiment.
        			XmlTemplateDao templateDao = new XmlTemplateDao();
        			XmlTemplateNodeDao nodeDao = new XmlTemplateNodeDao();
        			//XmlTemplate template = templateDao.getXmlTemplateByExperimentId(expId);
        			if (template != null){
        				Experiment exp = template.getExperiment();
        				if(exp != null){
	            			int expId = exp.getExpId();
	            			String expName = exp.getExpName();
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
					        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
					        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
			        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
            		}else{
                		respObj.setCode(103);
                		respObj.setDescription("The template is null");
                		respObj.setDetail("NullPointerException");
	        			xmlResult.setXmlDataLoadExecException(true);
	        			xmlResult.setXmlDataLoadExecExeptionDetails(respObj.getDescription());
	        			xmlResult.setXmlDataLoadExecTime(new Date());
	        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
					        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
					        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
				        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
		        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
	        			//xmlResult.setXmlDataLoadExecExeptionFile(filename);
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
