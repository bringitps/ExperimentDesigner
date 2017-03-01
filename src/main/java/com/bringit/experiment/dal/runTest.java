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
		 firstExperiment.setExpDbTableNameId("mems_to_magnet_assembx");
		 firstExperiment.setExpName("MEMs_To_Magnet_Assembx");
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
		 
		 ExperimentParser parser = new ExperimentParser();
		 CsvTemplate csvt = new CsvTemplate();
		 CsvTemplateColumns cols = new CsvTemplateColumns();
		 csvt.setCsvTemplateName("CsvPinix");
		 csvt.setExperiment(firstExperiment);
		 csvt.setCsvTemplateComments("Testing");
		 csvt.setCsvTemplatePrefix("MEMS");
		 new CsvTemplateDao().addCsvTemplate(csvt);
		 
		 cols.setCsvTemplate(csvt);
		 cols.setCsvTemplateColumnName("moduleSerialNumber");
		 cols.setExpField(ef);
		 new CsvTemplateColumnsDao().addCsvTemplateColumns(cols);
		 
		 ResponseObj respObj = parser.parseCSV(csvFile, firstExperiment);
		// ResponseObj respObj = parser.parseXML(xmlFile, new ExperimentDao().getExperimentById(1));
		 
		 System.out.println(respObj.getDescription() + respObj.getDetail());
	
	}



}
