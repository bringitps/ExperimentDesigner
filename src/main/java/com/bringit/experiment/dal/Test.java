package com.bringit.experiment.dal;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.ExperimentImage;
import com.bringit.experiment.bll.ExperimentMeasure;
import com.bringit.experiment.bll.ExperimentMeasureFieldValue;
import com.bringit.experiment.bll.ExperimentMeasureFieldValueUpdateLog;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentImageDao;
import com.bringit.experiment.dao.ExperimentMeasureDao;
import com.bringit.experiment.dao.ExperimentMeasureFieldValueDao;
import com.bringit.experiment.dao.ExperimentMeasureFieldValueUpdateLogDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;

public class Test {
	 
	 public static int createSysUserTest()
	 {
	     SysUserDao sysUserDao = new SysUserDao();
		 
	     SysUser sysUser = new SysUser();
		 sysUser.setUserName("BringIT");
		 sysUser.setUserPass("123456");
		 
		 sysUserDao.addSysUser(sysUser);
		 
		 return sysUser.getUserId();
	 }
	 
	 public static int createExperimentTest()
	 {
		 SysUserDao sysUserDao = new SysUserDao();
		 
	     SysUser sysUser = new SysUser();
		 sysUser.setUserName("Edgar Beltrán");
		 sysUser.setUserPass("123456");
		 
		 sysUserDao.addSysUser(sysUser);
 
	     SysUser sysUserErik = new SysUser();
	     sysUserErik.setUserName("Erik Tete Samario");
	     sysUserErik.setUserPass("123456");
		 
		 sysUserDao.addSysUser(sysUserErik);
		 
		 
		 ExperimentDao experimentDao = new ExperimentDao();
		 
		 Experiment experiment = new Experiment();
		 experiment.setExpComments("Test Experiment Comments");
		 experiment.setExpInstructions("Test Experiment Instructions");
		 experiment.setExpIsActive(true);
		 experiment.setExpName("Test Experiment Name");
		 experiment.setModifiedDate(new Date());
		 experiment.setCreatedBy(sysUser);
		 experiment.setLastModifiedBy(sysUserErik);
		 
		 experimentDao.addExperiment(experiment);
		 return experiment.getExpId();
		 
	 }


	 public static String buildDbSchemaAndTable(){
		 SysUserDao sysUserDao = new SysUserDao();
		 ExperimentDao experimentDao = new ExperimentDao();
		 ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		 //ExecuteQueryDao executeQueryDao = new ExecuteQueryDao();
		 UnitOfMeasureDao unitOfMeasureDao = new UnitOfMeasureDao();
		 
		//New User 
		 SysUser bringITUser = new SysUser();
		 bringITUser.setUserName("bit_seko");
		 bringITUser.setUserPass("123456");
		 sysUserDao.addSysUser(bringITUser); 						//DB Access
		 
		 //New Experiment
		 Experiment firstExperiment = new Experiment();
		 firstExperiment.setExpName("MEMS_TO_MAGNET_ASSEMBLY");
		 firstExperiment.setExpIsActive(true);
		 firstExperiment.setExpComments("This is the experiment for MEMs to Magnet Assembly");
		 firstExperiment.setExpInstructions("The instructions should be here..");
		 firstExperiment.setModifiedDate(new Date());
		 firstExperiment.setCreatedBy(bringITUser);
		 firstExperiment.setLastModifiedBy(bringITUser);
		 experimentDao.addExperiment(firstExperiment);				//DB Access

		 
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
		 
		 addNewField("varchar(24)", "moduleSerialNumber",true,true,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("varchar(24)", "fpbcSerialNumber",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("varchar(24)", "dieId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("varchar(24)", "waferId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("varchar(24)", "enId",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("varchar(24)", "workOrder",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("datetime", "dateTime",true,false,noMeasure,firstExperiment,experimentFieldDao);
		 addNewField("float", "memsToMagnetOutlineX",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("float", "memsToMagnetOutlineY",true,false,unitOfMeasure,firstExperiment,experimentFieldDao);
		 addNewField("float", "angle",true,false,unitOfMeasureAngle,firstExperiment,experimentFieldDao);
		 addNewField("varchar(24)", "testResult",true,false,noMeasure,firstExperiment,experimentFieldDao);

		 /*
		 List<ExperimentField> fields = experimentFieldDao.getActiveExperimentFields(firstExperiment);
		 String createTable = "CREATE TABLE "+firstExperiment.getExpName()+"(";
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
		
	 private static void addNewField(String type, String name, boolean active, boolean isKey, UnitOfMeasure uom, Experiment exp, ExperimentFieldDao dao) {
		 ExperimentField field = new ExperimentField();
		 field.setExpFieldType(type);
		 field.setExpFieldName(name);
		 field.setExpFieldIsActive(active);
		 field.setExpFieldIsKey(isKey);
		 field.setUnitOfMeasure(uom);
		 field.setExperiment(exp);
		 dao.addExperimentField(field);	
	}

	public static boolean buildDbSchema()
	 {
		 SysUserDao sysUserDao = new SysUserDao();
		 ExperimentDao experimentDao = new ExperimentDao();
		 ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		 ExperimentImageDao experimentImageDao = new ExperimentImageDao();
		 UnitOfMeasureDao unitOfMeasureDao = new UnitOfMeasureDao();
		 ExperimentMeasureDao experimentMeasureDao = new ExperimentMeasureDao();
		 ExperimentMeasureFieldValueDao experimentMeasureFieldValueDao = new ExperimentMeasureFieldValueDao();
		 ExperimentMeasureFieldValueUpdateLogDao experimentMeasureFieldValueUpdateLogDao = new ExperimentMeasureFieldValueUpdateLogDao();
		 
		 //New User 
		 SysUser bringITUser = new SysUser();
		 bringITUser.setUserName("New BringIT User");
		 bringITUser.setUserPass("123456");
		 sysUserDao.addSysUser(bringITUser); 						//DB Access
		 
		 //New Experiment
		 Experiment firstExperiment = new Experiment();
		 firstExperiment.setExpName("First Experiment Name");
		 firstExperiment.setExpIsActive(true);
		 firstExperiment.setExpComments("First Experiment Comments");
		 firstExperiment.setExpInstructions("First Experiment Instructions");
		 firstExperiment.setModifiedDate(new Date());
		 firstExperiment.setCreatedBy(bringITUser);
		 firstExperiment.setLastModifiedBy(bringITUser);
		 experimentDao.addExperiment(firstExperiment);				//DB Access

		 
		 //New Unit Of Measure
		 UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		 unitOfMeasure.setUomName("Volts");
		 unitOfMeasure.setUomAbbreviation("V");
		 unitOfMeasureDao.addUnitOfMeasure(unitOfMeasure);			//DB Access
		 
		 //New Experiment Fields
		 ExperimentField serialNumberField = new ExperimentField();
		 serialNumberField.setExpFieldType("String");
		 serialNumberField.setExpFieldName("Serial Number");
		 serialNumberField.setExpFieldIsActive(true);
		 serialNumberField.setExpFieldIsKey(true);
		 serialNumberField.setExperiment(firstExperiment);
		 experimentFieldDao.addExperimentField(serialNumberField);	//DB Access
		 
		 ExperimentField voltageField = new ExperimentField();
		 voltageField.setExpFieldType("String");
		 voltageField.setExpFieldName("Device Voltage");
		 voltageField.setExpFieldIsActive(true);
		 voltageField.setExpFieldIsKey(false);
		 voltageField.setUnitOfMeasure(unitOfMeasure);
		 voltageField.setExperiment(firstExperiment);
		 experimentFieldDao.addExperimentField(voltageField);	//DB Access


		 //New Experiment Image
		 ExperimentImage experimentImage = new ExperimentImage();
		 experimentImage.setExpImagePath("image.png");
		 experimentImage.setExperiment(firstExperiment);
		 experimentImageDao.addExperimentImage(experimentImage);
		 
		 //New Experiment Measure
		 ExperimentMeasure experimentMeasure = new ExperimentMeasure();
		 experimentMeasure.setExpMeasureComment("This is a comment for the first Experiment Measure");
		 experimentMeasure.setCreatedBy(bringITUser);
		 experimentMeasure.setCreatedDate(new Date());
		 experimentMeasure.setLastModifiedBy(bringITUser);
		 experimentMeasure.setModifiedDate(new Date());
		 experimentMeasureDao.addExperimentMeasure(experimentMeasure);
		 
		 //Experiment Measure Field Value for Serial Number
		 ExperimentMeasureFieldValue serialNumberFieldValue = new ExperimentMeasureFieldValue();
		 serialNumberFieldValue.setExperimentMeasure(experimentMeasure);
		 serialNumberFieldValue.setExperimentField(serialNumberField);
		 serialNumberFieldValue.setExpMeasureValue("SN123456");
		 experimentMeasureFieldValueDao.addExperimentMeasureFieldValue(serialNumberFieldValue);
		 
		 //Experiment Measure Field Value for Voltaje
		 ExperimentMeasureFieldValue voltajeFieldValue = new ExperimentMeasureFieldValue();
		 voltajeFieldValue.setExperimentMeasure(experimentMeasure);
		 voltajeFieldValue.setExperimentField(serialNumberField);
		 voltajeFieldValue.setExpMeasureValue("5.5");
		 experimentMeasureFieldValueDao.addExperimentMeasureFieldValue(voltajeFieldValue);
		 
		 //Experiment Measure Field Value Update for Voltaje
		 ExperimentMeasureFieldValueUpdateLog voltajeFieldValueUpdateLog = new ExperimentMeasureFieldValueUpdateLog();
		 voltajeFieldValueUpdateLog.setExperimentMeasureFieldValue(voltajeFieldValue);
		 voltajeFieldValueUpdateLog.setExpMeasureOldValue(voltajeFieldValue.getExpMeasureValue());
		 voltajeFieldValueUpdateLog.setExpMeasureOldCreatedDate(experimentMeasure.getModifiedDate());
		 voltajeFieldValueUpdateLog.setExpMeasureNewValue("6.5");
		 voltajeFieldValueUpdateLog.setExpMeasureNewCreatedDate(new Date());
		 voltajeFieldValueUpdateLog.setCreatedBy(bringITUser);
		 experimentMeasureFieldValueUpdateLogDao.addExperimentMeasureFieldValueUpdateLog(voltajeFieldValueUpdateLog);
		 
		 //Update Parent Entities
		 experimentMeasure.setLastModifiedBy(voltajeFieldValueUpdateLog.getCreatedBy());
		 experimentMeasure.setModifiedDate(voltajeFieldValueUpdateLog.getExpMeasureNewCreatedDate());
		 experimentMeasureDao.updateExperimentMeasure(experimentMeasure);
		 
		 voltajeFieldValue.setExpMeasureValue(voltajeFieldValueUpdateLog.getExpMeasureNewValue());
		 experimentMeasureFieldValueDao.updateExperimentMeasureFieldValue(voltajeFieldValue);
		 
		 return true;
	 }


}
