package com.bringit.experiment.dal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.CsvTemplateColumns;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.CsvTemplateColumnsDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;

public class runTest {

	public static void main(String[] args) throws IOException {
		//File xmlFile = new File("C:\\Users\\acer\\git\\ExperimentDesigner\\src\\main\\java\\com\\bringit\\experiment\\dal\\xmlSample.xml");
		File csvFile = new File("C:\\Users\\acer\\git2\\ExperimentDesigner\\src\\main\\java\\com\\bringit\\experiment\\dal\\csvSample.csv");
		
		 Experiment firstExperiment = new Experiment();
		 ExperimentDao experimentDao =  new ExperimentDao();
		 firstExperiment.setExpDbTableNameId("mems_to_magnet_assemb11");
		 firstExperiment.setExpName("MEMs_To_Magnet_Assemb11");
		 firstExperiment.setExpIsActive(true);
		 firstExperiment.setExpComments("This is the experiment for MEMs to Magnet Assembly for CSV ");
		 firstExperiment.setExpInstructions("The instructions should be here..");
		 firstExperiment.setModifiedDate(new Date());
		 firstExperiment.setCreatedBy(new SysUserDao().getUserById(1));
		 experimentDao.addExperiment(firstExperiment);				//DB Access
		 experimentDao.updateDBDataTable(firstExperiment);
		 
		 ExperimentField ef = new ExperimentField();
		 ef.setExpDbFieldNameId("moduleSerialNumber");
		 ef.setExperiment(firstExperiment);
		 ef.setExpFieldIsActive(true);
		 ef.setExpFieldName("mSN");
		 ef.setExpFieldType("varchar(10)");
		 new ExperimentFieldDao().addExperimentField(ef);
		 new ExperimentFieldDao().updateDBDataTableField(ef);
		 
		 ExperimentField ef1 = new ExperimentField();
		 ef1.setExpDbFieldNameId("fpbcSerialNumber");
		 ef1.setExperiment(firstExperiment);
		 ef1.setExpFieldIsActive(true);
		 ef1.setExpFieldName("fSN");
		 ef1.setExpFieldType("varchar(10)");
		 new ExperimentFieldDao().addExperimentField(ef1);
		 new ExperimentFieldDao().updateDBDataTableField(ef1);
		 
		 ExperimentField ef2 = new ExperimentField();
		 ef2.setExpDbFieldNameId("dieId");
		 ef2.setExperiment(firstExperiment);
		 ef2.setExpFieldIsActive(true);
		 ef2.setExpFieldName("die");
		 ef2.setExpFieldType("varchar(10)");
		 new ExperimentFieldDao().addExperimentField(ef2);
		 new ExperimentFieldDao().updateDBDataTableField(ef2);
		 
		 ExperimentField ef3 = new ExperimentField();
		 ef3.setExpDbFieldNameId("workOrder");
		 ef3.setExperiment(firstExperiment);
		 ef3.setExpFieldIsActive(true);
		 ef3.setExpFieldName("wo");
		 ef3.setExpFieldType("varchar(10)");
		 new ExperimentFieldDao().addExperimentField(ef3);
		 new ExperimentFieldDao().updateDBDataTableField(ef3);
		 
		 ExperimentParser parser = new ExperimentParser();
		 CsvTemplate csvt = new CsvTemplate();
		 CsvTemplateColumnsDao csvDao = new CsvTemplateColumnsDao();
		 csvt.setCsvTemplateName("Csv11");
		 csvt.setExperiment(firstExperiment);
		 csvt.setCsvTemplateComments("Testing");
		 csvt.setCsvTemplatePrefix("MEMS");
		 new CsvTemplateDao().addCsvTemplate(csvt);
		 
		 CsvTemplateColumns cols = new CsvTemplateColumns();
		 cols.setCsvTemplate(csvt);
		 cols.setCsvTemplateColumnName("moduleSerialNumber");
		 cols.setExpField(ef);
		 csvDao.addCsvTemplateColumns(cols); 
		 
		 CsvTemplateColumns cols1 = new CsvTemplateColumns();
		 cols1.setCsvTemplate(csvt);
		 cols1.setCsvTemplateColumnName("fpbcSerialNumber");
		 cols1.setExpField(ef1); 
		 csvDao.addCsvTemplateColumns(cols1);
		 
		 CsvTemplateColumns cols2 = new CsvTemplateColumns();
		 cols2.setCsvTemplate(csvt);
		 cols2.setCsvTemplateColumnName("dieId");
		 cols2.setExpField(ef2);
		 
		 csvDao.addCsvTemplateColumns(cols2);
		 CsvTemplateColumns cols3 = new CsvTemplateColumns();
		 cols3.setCsvTemplate(csvt);
		 cols3.setCsvTemplateColumnName("workOrder");
		 cols3.setExpField(ef3);
		 csvDao.addCsvTemplateColumns(cols3);
		 
		 ResponseObj respObj = parser.parseCSV(csvFile, csvt);
		 //ResponseObj respObj = parser.parseXML(xmlFile, new ExperimentDao().getExperimentById(1));
		 
		 System.out.println(respObj.getDescription() + respObj.getDetail());
	
	}



}
