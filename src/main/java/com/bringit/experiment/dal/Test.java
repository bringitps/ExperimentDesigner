package com.bringit.experiment.dal;

import java.io.*;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.ExperimentImage;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentImageDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.util.Config;

public class Test {
	 
	 
	public static String buildDbSchemaAndTable(){
		 SysUserDao sysUserDao = new SysUserDao();
		 ExperimentDao experimentDao = new ExperimentDao();
		 ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		 ExecuteQueryDao executeQueryDao = new ExecuteQueryDao();
		 UnitOfMeasureDao unitOfMeasureDao = new UnitOfMeasureDao();
		 
		//New User 
		 SysUser bringITUser = new SysUser();
		 bringITUser.setUserName("bit_seko");
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
		 
		 addNewField("moduleSerialNumber", "varchar(24)", "moduleSerialNumber",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("fpbcSerialNumber", "varchar(24)", "fpbcSerialNumber",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("dieId", "varchar(24)", "dieId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("waferId", "varchar(24)", "waferId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("enId", "varchar(24)", "enId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("workOrder", "varchar(24)", "workOrder",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("date_Time", "datetime", "date_Time",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("memsToMagnetOutlineX", "float", "memsToMagnetOutlineX",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("memsToMagnetOutlineY", "float", "memsToMagnetOutlineY",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("angle", "float", "angle",true,false,unitOfMeasureAngle,firstExperiment,experimentFieldDao);
		 addNewField("testResult", "varchar(24)", "testResult",true,false,noMeasure,firstExperiment,experimentFieldDao);
		
		 /*
		  *Operation already covered by updateDBDataTable function of ExperimentDao Class
		 List<ExperimentField> fields = experimentFieldDao.getActiveExperimentFields(firstExperiment);
		 String createTable = "CREATE TABLE "+firstExperiment.getExpDbTableNameId()+"(";
		 for (ExperimentField experimentField : fields) {
			 createTable += experimentField.getExpFieldName() + " " + experimentField.getExpFieldType() + " " + (experimentField.isExpFieldIsKey() ? "PRIMARY KEY NOT NULL," : ",");
		 }
		 createTable +=");";

		 executeQueryDao.executeQuery(createTable);
		 */
		 
		 //Read a sample XML and load it into Values table;
		 File xmlFile = new File("C:\\Users\\acer\\git\\ExperimentDesigner\\src\\main\\java\\com\\bringit\\experiment\\dal\\xmlSample.xml");
		 ExperimentParser parser = new ExperimentParser();
		 ResponseObj respObj = parser.parseXML(xmlFile, xmlFile.getName());
		 
		 return respObj.getDescription() + respObj.getDetail();
		 
	 }
	public String runTest() {
			 File xmlFile = new File("C:\\Users\\acer\\git\\ExperimentDesigner\\src\\main\\java\\com\\bringit\\experiment\\dal\\xmlSample2.xml");
			 ExperimentParser parser = new ExperimentParser();
			 ResponseObj respObj = parser.parseXML(xmlFile, xmlFile.getName());
			 
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
