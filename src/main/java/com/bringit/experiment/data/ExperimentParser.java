package com.bringit.experiment.data;

import com.vaadin.data.Property;
import org.apache.commons.beanutils.ConversionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bringit.experiment.bll.CsvDataLoadExecutionResult;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.CsvTemplateColumns;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.bll.XmlTemplateNode;
import com.bringit.experiment.dao.CsvDataLoadExecutionResultDao;
import com.bringit.experiment.dao.CsvTemplateColumnsDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;
import com.opencsv.CSVReader;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
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
	
	public ResponseObj parseCSV(File csvFile, CsvTemplate template) {
		if (csvFile != null) {
			try {
				String filename = csvFile.getName();
				InputStream is = new FileInputStream(csvFile);
				return parseCSV(is, template, filename);
			} catch (Exception e) {
				System.out.println("Error converting CSV File to InputStream: "+e);
			}

		}
		return null;
	}

	public ResponseObj parseCSV(InputStream csvFile, CsvTemplate template, String filename){
    	ResponseObj respObj = new ResponseObj();
    	respObj.setCode(0);
    	respObj.setDescription("Csv Loaded Successfully");
	    respObj.setCsvInsertColumns(null);
	    respObj.setCsvInsertValues(null);
	    String csvColumns ="";
	    List<String> csvValues = new ArrayList<String>();

        CSVReader reader = null;
        try {
        	
        		if(csvFile != null){

        			//Verify if there is a template and nodes for the experiment.
        			CsvTemplateColumnsDao nodeDao = new CsvTemplateColumnsDao();
        			if (template != null){
        				Experiment exp = template.getExperiment();
        				if(exp != null){
	        				//get the nodes:
	        				List<CsvTemplateColumns> columns = nodeDao.getAllCsvTemplateColumnssByTemplateId(template.getCsvTemplateId());
	        				if(columns != null){

	        					reader = new CSVReader(new InputStreamReader(csvFile));
	        		            // we assume the first line is the header
	        		            String[] header = reader.readNext();

	        		            ArrayList<ArrayList> arr = new ArrayList<ArrayList>();
	        		            
	        		            for (CsvTemplateColumns column : columns) {
	        		            	ArrayList maping = new ArrayList();
	        		            	maping.add( Arrays.asList(header).indexOf(column.getCsvTemplateColumnName()));
	        		            	maping.add( column.getExpField().getExpDbFieldNameId());
	        		            	maping.add(column.getExpField().getExpFieldType());
	        		            	arr.add(maping);
								}
	        		            String[] line;
	        		            for (int i = 0; i < arr.size(); i++)
									csvColumns+=(String) arr.get(i).get(1)+",";

	        		           respObj.setCsvInsertColumns(csvColumns.substring(0, csvColumns.length()-1));
	        		           String fieldType = "";

	        		   			String exceptionRowColumns = "";
	        		   			int rowCount = 1;
	        		            while ((line = reader.readNext()) != null) {
	        		            	String csvValuesLine="";
	            		            for (int i = 0; i < arr.size(); i++) {
	            		            	fieldType= (String) arr.get(i).get(2);
	            		            	String fieldValue = line[(int) arr.get(i).get(0)];

	            		            	if(!validateFieldType(fieldType, fieldValue))
	            		            		exceptionRowColumns += "Invalid Data: " + fieldValue + ". Cast failed to " + fieldType + " Found in Column '" + (String) arr.get(i).get(1) + "' Row #" + rowCount + (fieldType.contains("date") ?  " Allowed Date format: yyyy-MM-dd HH:mm:ss\n": ".\n");
	            		            	
	            		            	csvValuesLine+=fieldType.toLowerCase().startsWith("float")||fieldType.toLowerCase().startsWith("decimal")||fieldType.toLowerCase().startsWith("int") ? line[(int) arr.get(i).get(0)]+"," : "'"+line[(int) arr.get(i).get(0)]+"',";
	            		            }

	            		            rowCount++;
            		            	csvValues.add(csvValuesLine.substring(0,csvValuesLine.length()-1));
	            		            respObj.setCsvInsertValues(csvValues);
	        		            }

	                			reader.close();
	                			System.out.println("Exception Row Columns: " + exceptionRowColumns);
	                			
	                		    if(!exceptionRowColumns.isEmpty())
	                		    {
		                			System.out.println("Returning Ex Details");
	                		    	respObj.setCode(104);
	                	      		respObj.setDescription("Exceptions at casting data. Details: "+exceptionRowColumns);
	                	      		respObj.setDetail("Exceptions at casting data.");

	                	    	    respObj.setCsvInsertColumns(null);
	                	    	    respObj.setCsvInsertValues(null);
	                	    	
		                			return respObj;
	                		    }

			        			return respObj;


	        				}else{
	                    		respObj.setCode(104);
	                    		respObj.setDescription("There are no COLUMNS associated with the CsvTemplate: "+template.getCsvTemplateName());
	                    		respObj.setDetail("NullPointerException");
	                			return respObj;  
	                    	}
        				}else{
        	        		respObj.setCode(101);
        	        		respObj.setDescription("Experiment Object received is null.");
        	        		respObj.setDetail("NullPointerException");
        	    			return respObj;  
        	        	}
        			}else{
                		respObj.setCode(103);
                		respObj.setDescription("There is no Template associated with the Experiment");
                		respObj.setDetail("NullPointerException");
            			return respObj;  
                	}
        		}else{
            		respObj.setCode(102);
            		respObj.setDescription("CSV file received is null.");
            		respObj.setDetail("NullPointerException");
        			return respObj;  
            	}
        	
        } catch (IOException e) {
    		respObj.setCode(100);
    		respObj.setDescription("Unknown error, check for details.");
    		respObj.setDetail(e+e.getMessage());
			return respObj;
        }
	}


	public ResponseObj parseXmlDocument(Document xmlDocument, XmlTemplate xmlTemplate)
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
    	List<String> xRefFieldDBType = new ArrayList<String>();
    	List<String> xRefXmlNodeSlashFormat = new ArrayList<String>();
    	List<Boolean> xRefXmlNodeIsAttribute = new ArrayList<Boolean>();
    	List<String> xRefXmlNodeAttributeName= new ArrayList<String>();
    	List<DBFieldValues> xRefDBFieldValues = new ArrayList<DBFieldValues>();
    	
		ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		List<ExperimentField> experimentFields = experimentFieldDao.getActiveExperimentFields(xmlTemplate.getExperiment());
		
		for(int i=0; experimentFields!= null && i<experimentFields.size(); i++)
		{
			xRefFieldDBId.add(experimentFields.get(i).getExpDbFieldNameId());
			xRefFieldDBType.add(experimentFields.get(i).getExpFieldType());
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
			    		
			 	    	if(xmlNode instanceof Element) 
			 	    	{
			 	    		Element xmlElement = (Element)xmlNode;
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
				    		if(xmlNodesFound.item(j)!=null  && ((Element)xmlNodesFound.item(j)).getChildNodes().getLength() <= 1 )
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
				    		
				 	    	if(xmlNode instanceof Element) 
				 	    	{
				 	    		Element xmlElement = (Element)xmlNode;
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
		
		String exceptionNodes = "";
		int totalRecords = 0;
			
		//3) Load Loop Node Field Values
		
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
	    	 
	    	if(xmlNode instanceof Element) 
	    	{
	    		Element xmlLoopElement = (Element)xmlNode;
	    		
	    		for(int j = 0; j < xRefFieldDBId.size(); j++ )
	    		{
	    			if(xRefXmlNodeSlashFormat.get(j) != null && xRefXmlNodeSlashFormat.get(j).startsWith(loopXmlNodeSlashFormat))
	    			{
	    				Element xmlValueElement = xmlLoopElement;
	    				String nodeNameSlashFormat = xRefXmlNodeSlashFormat.get(j).replace(loopXmlNodeSlashFormat, "");

	    				
	    				while(nodeNameSlashFormat.contains("/"))
	    				{
	    					String[] splitNodes = nodeNameSlashFormat.split("/");
	    					if(xmlValueElement.getElementsByTagName(splitNodes[1]).item(0) != null)
	    						xmlValueElement = (Element)xmlValueElement.getElementsByTagName(splitNodes[1]).item(0);
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
							{
								String fieldType = xRefFieldDBType.get(j);
								String fieldValue = xmlValueElement.getAttribute(attributeName).trim();
								boolean isValidData = validateFieldType(fieldType, fieldValue);
								
								if(isValidData)
									xRefDBFieldValues.get(j).attachFieldValue(xmlValueElement.getAttribute(attributeName).trim());
								else
								{
									xRefDBFieldValues.get(j).attachFieldValue(null);
									exceptionNodes += "Invalid Data: " + fieldValue + ". Cast failed to " + fieldType + " Found in Loop Node '" + xRefXmlNodeSlashFormat.get(j) + "' #" + xRefDBFieldValues.get(j).getFieldValues().size() + (fieldType.contains("date") ?  " Allowed Date format: yyyy-MM-dd HH:mm:ss\n": ".\n");
								}
							}
							else
	    						xRefDBFieldValues.get(j).attachFieldValue(null);
	    				}
	    				else
	    				{
	    					if(xmlValueElement != null && xmlValueElement.getChildNodes().getLength() <= 1 && xmlValueElement.getTextContent() != null)
	    					{
	    						String fieldType = xRefFieldDBType.get(j);
								String fieldValue = xmlValueElement.getTextContent();
								boolean isValidData = validateFieldType(fieldType, fieldValue);
								
								if(isValidData)
									xRefDBFieldValues.get(j).attachFieldValue(xmlValueElement.getTextContent().trim());
								else
								{
									xRefDBFieldValues.get(j).attachFieldValue(null);
									exceptionNodes += "Invalid Data: " + fieldValue + ". Cast failed to " + fieldType + " Found in Loop Node '" + xRefXmlNodeSlashFormat.get(j) + "' #" + xRefDBFieldValues.get(j).getFieldValues().size() + (fieldType.contains("date") ?  " Allowed Date format: yyyy-MM-dd HH:mm:ss\n": ".\n");
								}
	    					}
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
				boolean isValidData = false;
				for(int j=0; j<totalRecords; j++)
				{
					if(j==0)
					{
						String fieldType = xRefFieldDBType.get(j);
						String fieldValue = xmlGlobalValuesMatrix.get(i);
						isValidData = validateFieldType(fieldType, fieldValue);
					
						if(isValidData)
							fieldValues.add(xmlGlobalValuesMatrix.get(i));
						else
						{
							fieldValues.add(null);
							exceptionNodes += "Invalid Data: " + fieldValue + ". Cast failed to " + fieldType + " Found in Global Node '" + xRefXmlNodeSlashFormat.get(j) +"'" + (fieldType.contains("date") ?  " Allowed Date format: yyyy-MM-dd HH:mm:ss\n": ".\n");
						}
					}
				}
				xRefDBFieldValues.get(xRefIndex).setFieldValues(fieldValues);
			}
	     }

	     if(!exceptionNodes.isEmpty())
	     {
	    	respObj.setCode(104);
      		respObj.setDescription("Exceptions at casting data. Details: "+exceptionNodes);
      		respObj.setDetail("Exceptions at casting data.");
     		return respObj;
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

	private boolean validateFieldType(String fieldType, String fieldValue)
	{
		if(fieldType.contains("date"))
		{			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				df.parse(fieldValue);
			} catch (Property.ReadOnlyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Cast Validation failed");
				return false;
		} catch (ConversionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Cast Validation failed");
				return false;
			}
		}
		else if(fieldType.contains("float") || fieldType.contains("decimal"))
		{
			try
			{
				Float.parseFloat(fieldValue);
			}
			catch(Exception e)
			{
				return false;
			}
		}
		else if(fieldType.contains("int"))
		{
			try
			{
				Integer.parseInt(fieldValue);
			}
			catch(Exception e)
			{
				return false;
			}
		}

		return true;

	}

}
