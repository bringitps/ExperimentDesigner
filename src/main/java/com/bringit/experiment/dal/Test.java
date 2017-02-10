package com.bringit.experiment.dal;

import java.util.Date;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.ExperimentImage;
import com.bringit.experiment.bll.ExperimentMeasure;
import com.bringit.experiment.bll.ExperimentMeasureFieldValue;
import com.bringit.experiment.bll.ExperimentMeasureFieldValuePK;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentImageDao;
import com.bringit.experiment.dao.ExperimentMeasureDao;
import com.bringit.experiment.dao.ExperimentMeasureFieldValueDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;

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

	 public static boolean buildDbSchema()
	 {
		 SysUserDao sysUserDao = new SysUserDao();
		 ExperimentDao experimentDao = new ExperimentDao();
		 ExperimentFieldDao experimentFieldDao = new ExperimentFieldDao();
		 ExperimentImageDao experimentImageDao = new ExperimentImageDao();
		 UnitOfMeasureDao unitOfMeasureDao = new UnitOfMeasureDao();
		 ExperimentMeasureDao experimentMeasureDao = new ExperimentMeasureDao();
		 ExperimentMeasureFieldValueDao experimentMeasureFieldValueDao = new ExperimentMeasureFieldValueDao();
		 
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
		 serialNumberField.setUnitOfMeasure(unitOfMeasure);
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
		 experimentMeasure.setExperimentField(serialNumberField);
		 experimentMeasure.setCreatedDate(new Date());
		 experimentMeasure.setModifiedDate(new Date());
		 experimentMeasure.setCreatedBy(bringITUser);
		 experimentMeasure.setLastModifiedBy(bringITUser);
		 experimentMeasureDao.addExperimentMeasure(experimentMeasure);
		 
		 //New Experiment Measure value for serialNumberField
		 ExperimentMeasureFieldValuePK experimentMeasureFieldValuePK = new ExperimentMeasureFieldValuePK(experimentMeasure,serialNumberField);
		 ExperimentMeasureFieldValue expMeasureFieldValueSN = new ExperimentMeasureFieldValue();
		 expMeasureFieldValueSN.setExperimentMeasureFieldValuePK(experimentMeasureFieldValuePK);
		 expMeasureFieldValueSN.setExpMeasureComment("no comments");
		 expMeasureFieldValueSN.setExpMeasureValue("SN10001");
		 experimentMeasureFieldValueDao.addExperimentMeasureFieldValue(expMeasureFieldValueSN);
		 
		 
		 return true;
	 }
}
