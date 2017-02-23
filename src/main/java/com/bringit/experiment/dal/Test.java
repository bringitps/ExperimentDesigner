package com.bringit.experiment.dal;

import java.io.*;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.*;
import com.bringit.experiment.dao.*;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.util.Config;

public class Test {
	 
	 
	public static String buildDbSchemaAndTable(){
		 SysUserDao sysUserDao = new SysUserDao();
		 ExperimentDao experimentDao = new ExperimentDao();
		 ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		 UnitOfMeasureDao unitOfMeasureDao = new UnitOfMeasureDao();
		 ExperimentImageDao experimentImageDao = new ExperimentImageDao();
		 JobExecutionRepeatDao jobDao = new JobExecutionRepeatDao();
		 FilesRepositoryDao fileRepoDao = new FilesRepositoryDao();
		 XmlTemplateDao xmlDao = new XmlTemplateDao();
		 XmlTemplateNodeDao xmlNodeDao = new XmlTemplateNodeDao();
		 
		//New User 
		 SysUser bringITUser = new SysUser();
		 bringITUser.setUserName("bit_seko");
		 bringITUser.setUserPass("123456");
		 sysUserDao.addSysUser(bringITUser); 						//DB Access
		 
		 //New Experiment
		 Experiment firstExperiment = new Experiment();
		 firstExperiment.setExpDbTableNameId("mems_to_magnet_assembly");
		 firstExperiment.setExpName("MEMs_To_Magnet_Assembly");
		 firstExperiment.setExpIsActive(true);
		 firstExperiment.setExpComments("This is the experiment for MEMs to Magnet Assembly");
		 firstExperiment.setExpInstructions("The instructions should be here..");
		 firstExperiment.setModifiedDate(new Date());
		 firstExperiment.setCreatedBy(bringITUser);
		 firstExperiment.setLastModifiedBy(bringITUser);
		 experimentDao.addExperiment(firstExperiment);				//DB Access
		 experimentDao.updateDBDataTable(firstExperiment);
		 
		 
		 //New Unit Of Measure
		 UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		 unitOfMeasure.setUomName("uMs");
		 unitOfMeasure.setUomAbbreviation("um");
		 unitOfMeasureDao.addUnitOfMeasure(unitOfMeasure);			//DB Access
		 //New Unit Of Measure
		 UnitOfMeasure unitOfMeasureAngle = new UnitOfMeasure();
		 unitOfMeasureAngle.setUomName("o");
		 unitOfMeasureAngle.setUomAbbreviation("grades");
		 unitOfMeasureDao.addUnitOfMeasure(unitOfMeasureAngle);			//DB Access
		 //Not a measure
		 UnitOfMeasure noMeasure = new UnitOfMeasure();
		 noMeasure.setUomName("NA");
		 noMeasure.setUomAbbreviation("NA");
		 unitOfMeasureDao.addUnitOfMeasure(noMeasure);			//DB Access
		 
		 addNewField("moduleSerialNumber", "varchar(24)", "moduleSerialNumber",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("fpbcSerialNumber", "varchar(24)", "fpbcSerialNumber",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("dieId", "varchar(24)", "dieId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("waferId", "varchar(24)", "waferId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("enId", "varchar(24)", "enId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("workOrder", "varchar(24)", "workOrder",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("date_Time", "datetime", "dateTime",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("memsToMagnetOutlineX", "float", "memsToMagnetOutlineX",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("memsToMagnetOutlineY", "float", "memsToMagnetOutlineY",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("angle", "float", "angle",true,false,unitOfMeasureAngle,firstExperiment,experimentFieldDao);
		 addNewField("testResult", "varchar(24)", "testResult",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 //New Experiment Image
		 ExperimentImage experimentImage = new ExperimentImage();
		 experimentImage.setExpImagePath("image.png");
		 experimentImage.setExperiment(firstExperiment);
		 experimentImageDao.addExperimentImage(experimentImage);

		 JobExecutionRepeat job = new JobExecutionRepeat();
		 job.setJobExecRepeatName("Pull for MEMs to Magnet Experiment");
		 job.setJobExecRepeatMilliseconds(1800000);//30 min
		 jobDao.addJobExecutionRepeat(job);
		 
		 FilesRepository inboundRepo = new FilesRepository();
		 inboundRepo.setFileRepoIsLocal(false);
		 inboundRepo.setFileRepoIsSftp(true);
		 inboundRepo.setFileRepoPath("/Intel/sepa/la/bola");
		 inboundRepo.setFileRepoHost("fpt.intel.com");
		 inboundRepo.setFileRepoUser("bringit1");
		 inboundRepo.setFileRepoPass("br1n61t");
		 inboundRepo.setCreatedDate(new Date());
		 inboundRepo.setCreatedBy(bringITUser);
		 fileRepoDao.addFilesRepository(inboundRepo);
		 
		 FilesRepository outbound = new FilesRepository();
		 outbound.setFileRepoIsLocal(true);
		 outbound.setFileRepoPath("/Intel/sepa/la/bola");
		 outbound.setFileRepoHost("localhost");
		 outbound.setCreatedDate(new Date());
		 outbound.setCreatedBy(bringITUser);
		 fileRepoDao.addFilesRepository(outbound);
		 
		 FilesRepository exceptionRepo = new FilesRepository();
		 exceptionRepo.setFileRepoIsLocal(true);
		 exceptionRepo.setFileRepoPath("/Intel/exception/");
		 exceptionRepo.setFileRepoHost("localhost");
		 exceptionRepo.setCreatedDate(new Date());
		 exceptionRepo.setCreatedBy(bringITUser);
		 fileRepoDao.addFilesRepository(exceptionRepo);
		 
		 XmlTemplate xml = new XmlTemplate();
		 xml.setXmlTemplateName("MEMs_To_Magnet_Assembly");
		 xml.setXmlTemplatePrefix("MTM_");
		 xml.setExceptionFileRepo(exceptionRepo);
		 xml.setInboundFileRepo(inboundRepo);
		 xml.setProcessedFileRepo(outbound);
		 xml.setXmlTemplateComments("Loading sample template for mems to magnet experiments");
		 xml.setCreatedDate(new Date());
		 xml.setCreatedBy(bringITUser);
		 xml.setExperiment(firstExperiment);
		 
		 xmlDao.addXmlTemplate(xml);
		 
		 //1. map the root element of the xml (assuming all of them will have <Experiment> as the root.
		 addNewNode("root",true,false,"Experiment",null,xml,xmlNodeDao);
		 
		 //2. map the experiment name element (this is not part of the fields but will be used to obtain
		 // the experiment id and then map the fields from the data tables
		 addNewNode("Experiment",false,true,"ExperimentName",null,xml,xmlNodeDao);
		 
		 //3. Map the experimentResults tag which will contain the set of fields with results
		 addNewNode("Experiment",false,false,"ExperimentResults",null,xml,xmlNodeDao);
		 
		//get active fileds for the experiment
		 List<ExperimentField> fields = experimentFieldDao.getActiveExperimentFields(firstExperiment); 
		 for (ExperimentField experimentField : fields) {
			 switch (experimentField.getExpFieldName()) {
			case "moduleSerialNumber":
				addNewNode("ExperimentResults",false,true,"moduleSerialNumber",experimentField,xml,xmlNodeDao);
				break;
			case "fpbcSerialNumber":
				addNewNode("ExperimentResults",false,true,"fpbcSerialNumber",experimentField,xml,xmlNodeDao);
				break;
			case "dieId":
				addNewNode("ExperimentResults",false,true,"dieId",experimentField,xml,xmlNodeDao);
				break;
			case "waferId":
				addNewNode("ExperimentResults",false,true,"waferId",experimentField,xml,xmlNodeDao);
				break;
			case "enId":
				addNewNode("ExperimentResults",false,true,"enId",experimentField,xml,xmlNodeDao);
				break;
			case "workOrder":
				addNewNode("ExperimentResults",false,true,"workOrder",experimentField,xml,xmlNodeDao);
				break;
			case "dateTime":
				addNewNode("ExperimentResults",false,true,"dateTime",experimentField,xml,xmlNodeDao);
				break;
			case "memsToMagnetOutlineX":
				addNewNode("ExperimentResults",false,true,"memsToMagnetOutlineX",experimentField,xml,xmlNodeDao);
				break;
			case "memsToMagnetOutlineY":
				addNewNode("ExperimentResults",false,true,"memsToMagnetOutlineY",experimentField,xml,xmlNodeDao);
				break;
			case "angle":
				addNewNode("ExperimentResults",false,true,"angle",experimentField,xml,xmlNodeDao);
				break;
			case "testResult":
				addNewNode("ExperimentResults",false,true,"testResult",experimentField,xml,xmlNodeDao);
				break;
			default:
				break;
			}
			 
		 }
		 
		 
		 
		 //Read a sample XML and load it into Values table;
		 File xmlFile = new File("C:\\Users\\acer\\git\\ExperimentDesigner\\src\\main\\java\\com\\bringit\\experiment\\dal\\xmlSample.xml");
		 ExperimentParser parser = new ExperimentParser();
		 ResponseObj respObj = parser.parseXML(xmlFile, firstExperiment);
		 
		 return respObj.getDescription() + respObj.getDetail();
	 }
	private static void addNewNode(String parent, boolean isRoot, boolean isAttr, String tagname, ExperimentField expFieldId,
			XmlTemplate xml, XmlTemplateNodeDao xmlNodeDao) {
		 XmlTemplateNode xmlNode = new XmlTemplateNode();
		 xmlNode.setXmlTemplateNodeName(parent);
		 xmlNode.setXmlTemplateNodeIsRoot(isRoot);
		 xmlNode.setXmlTemplateNodeIsAttributeValue(isAttr);
		 xmlNode.setXmlTemplateNodeAttributeName(tagname);
		 xmlNode.setExpField(expFieldId);
		 xmlNode.setXmlTemplate(xml);
		 xmlNodeDao.addXmlTemplateNode(xmlNode);
		
	}
	public String runTest() {
			 File xmlFile = new File("C:\\Users\\acer\\git\\ExperimentDesigner\\src\\main\\java\\com\\bringit\\experiment\\dal\\xmlSample.xml");
			 ExperimentDao dao = new ExperimentDao();
			 ExperimentParser parser = new ExperimentParser();
			 ResponseObj respObj = parser.parseXML(xmlFile, dao.getExperimentById(1));
			 
			 return respObj.getDescription() + respObj.getDetail();
		}
		

	public static boolean buildDbSchema()
	 {
		 SysUserDao sysUserDao = new SysUserDao();
		 ExperimentDao experimentDao = new ExperimentDao();
		 ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		 ExperimentImageDao experimentImageDao = new ExperimentImageDao();
		 UnitOfMeasureDao unitOfMeasureDao = new UnitOfMeasureDao();
		 
		 //New User 
		 SysUser bringITUser = new SysUser();
		 bringITUser.setUserName("New BringIT User");
		 bringITUser.setUserPass("123456");
		 sysUserDao.addSysUser(bringITUser); 						//DB Access
		 
		 //New Experiment
		 Experiment firstExperiment = new Experiment();
		 firstExperiment.setExpDbTableNameId("mems_to_magnet_assembly");
		 firstExperiment.setExpName("MEMS_TO_MAGNET_ASSEMBLY");
		 firstExperiment.setExpIsActive(true);
		 firstExperiment.setExpComments("This is the experiment for MEMs to Magnet Assembly");
		 firstExperiment.setExpInstructions("The instructions should be here..");
		 firstExperiment.setModifiedDate(new Date());
		 firstExperiment.setCreatedBy(bringITUser);
		 firstExperiment.setLastModifiedBy(bringITUser);
		 experimentDao.addExperiment(firstExperiment);				//DB Access
		 experimentDao.updateDBDataTable(firstExperiment);
		 
		 //New Unit Of Measure
		 UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		 unitOfMeasure.setUomName("uMs");
		 unitOfMeasure.setUomAbbreviation("um");
		 unitOfMeasureDao.addUnitOfMeasure(unitOfMeasure);			//DB Access
		 //New Unit Of Measure
		 UnitOfMeasure unitOfMeasureAngle = new UnitOfMeasure();
		 unitOfMeasureAngle.setUomName("o");
		 unitOfMeasureAngle.setUomAbbreviation("grades");
		 unitOfMeasureDao.addUnitOfMeasure(unitOfMeasureAngle);			//DB Access
		 //Not a measure
		 UnitOfMeasure noMeasure = new UnitOfMeasure();
		 noMeasure.setUomName("NA");
		 noMeasure.setUomAbbreviation("NA");
		 unitOfMeasureDao.addUnitOfMeasure(noMeasure);			//DB Access
		 
		 addNewField("moduleSerialNumber", "varchar(24)", "moduleSerialNumber",true,true,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("fpbcSerialNumber", "varchar(24)", "fpbcSerialNumber",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("dieId", "varchar(24)", "dieId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("waferId", "varchar(24)", "waferId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("enId", "varchar(24)", "enId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("workOrder", "varchar(24)", "workOrder",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("dateTime", "datetime", "dateTime",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("memsToMagnetOutlineX", "float", "memsToMagnetOutlineX",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("memsToMagnetOutlineY", "float", "memsToMagnetOutlineY",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("angle", "float", "angle",true,false,unitOfMeasureAngle,firstExperiment,experimentFieldDao);
		 addNewField("testResult", "varchar(24)", "testResult",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 
		 

		 //New Experiment Image
		 ExperimentImage experimentImage = new ExperimentImage();
		 experimentImage.setExpImagePath("image.png");
		 experimentImage.setExperiment(firstExperiment);
		 experimentImageDao.addExperimentImage(experimentImage);
		 
		 return true;
		
	 }

	 private static void addNewField(String dbDataTableFieldNameId, String type, String name, boolean active, boolean isKey, UnitOfMeasure uom, Experiment exp, ExperimentFieldDao dao) {
		 ExperimentField field = new ExperimentField();
		 field.setExpDbFieldNameId(dbDataTableFieldNameId);
		 field.setExpFieldType(type);
		 field.setExpFieldName(name);
		 field.setExpFieldIsActive(active);
		 field.setExpFieldIsKey(isKey);
		 field.setUnitOfMeasure(uom);
		 field.setExperiment(exp);
		 dao.addExperimentField(field);	
		 dao.updateDBDataTableField(field);
	}

	
}
