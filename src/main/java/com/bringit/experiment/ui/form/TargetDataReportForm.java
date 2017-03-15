package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.TargetDataReportDesign;
import com.bringit.experiment.util.ExperimentUtil;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class TargetDataReportForm extends TargetDataReportDesign{

	private TargetReport targetRpt = new TargetReport();
	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
	List<ContractManufacturer> contractManufacturers = new ArrayList<ContractManufacturer>();
	
	public TargetDataReportForm(Integer targetDataReportId)
	{
		targetRpt = new TargetReportDao().getTargetReportById(targetDataReportId);
		lblTargetRptTitle.setValue(lblTargetRptTitle.getValue() + " - " + targetRpt.getTargetReportName());
		
		experimentFields = new ExperimentFieldDao().getActiveExperimentFields(targetRpt.getExperiment());
		
		cbxDateFieldsFilter.setContainerDataSource(null);
		cbxExpFieldFilter.setContainerDataSource(null);

		cbxDateFieldsFilter.addItem("CreatedDate");
		cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

		cbxDateFieldsFilter.addItem("LastModifiedDate");
		cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");

		for(int i=0; experimentFields != null &&i<experimentFields.size(); i++)
		{
			if(experimentFields.get(i).getExpFieldType().trim().contains("date"))
			{
				cbxDateFieldsFilter.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxDateFieldsFilter.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
			}
			else 	if(experimentFields.get(i).getExpFieldType().trim().startsWith("varchar") || experimentFields.get(i).getExpFieldType().trim().startsWith("char")
					|| experimentFields.get(i).getExpFieldType().trim().startsWith("text") ||experimentFields.get(i).getExpFieldType().trim().startsWith("nvarchar") 
					|| experimentFields.get(i).getExpFieldType().trim().startsWith("nchar") || experimentFields.get(i).getExpFieldType().trim().startsWith("ntext"))
		
			{
				cbxExpFieldFilter.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxExpFieldFilter.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
			}
		}
		
		contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
			cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
		}
		
		//Call SP with TargetReportId
		List<String> spParams = new ArrayList<String>();
		spParams.add(this.targetRpt.getTargetReportId().toString());
		spParams.add(this.targetRpt.getExperiment().getExpId().toString());
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		
		/*
		 * @TargetReportId NVARCHAR(MAX),
			@ExpId NVARCHAR(MAX),
			@Date NVARCHAR(MAX),
			@FDate NVARCHAR(MAX),
			@TDate NVARCHAR(MAX),
			@CmV NVARCHAR(MAX),
			@ExpFieldName NVARCHAR(MAX),
			@ExpFieldValue NVARCHAR(MAX),
			@result VARCHAR(MAX) OUTPUT,
			@Tname NVARCHAR(MAX) OUTPUT,
			@Query NVARCHAR(MAX) OUTPUT,
			@result0 VARCHAR(MAX) OUTPUT,
			@Info VARCHAR(MAX) OUTPUT
		 */
		
		ResultSet spResults = new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spParams);
		
		if(spResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(this.tblTargetDataReport, spResults, 1);
		/*
		//executeStoredProcedure
		//spTargetReportBuilder
		this.btnApplyFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				List<String> spParams3 = new ArrayList<String>(); 
				spParams3.add(txtExpFieldFilter.getValue());
				ResultSet spResults = new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spParams3);
				
				if(spResults != null)
					VaadinControls.bindDbViewRsToVaadinTable(tblTargetDataReport, spResults, 1);
				
			}

		});
		*/
	}
}
