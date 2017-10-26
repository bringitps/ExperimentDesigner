package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.List;

import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.dao.FirstPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.ui.design.FirstPassYieldReportDataDesign;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.VaadinService;

public class FirstPassYieldReportDataForm extends FirstPassYieldReportDataDesign{

	FirstPassYieldReport fpyReport = new FirstPassYieldReport();
	private SQLContainer vaadinTblContainer;
	SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
	
	private String sqlQuery = "";    
	String firstWhereClause = "";
	List<String> andSqlWhereClause = new ArrayList<String>();
	
	public FirstPassYieldReportDataForm(Integer fpyReportId)
	{		
		this.fpyReport = new FirstPassYieldReportDao().getFirstPassYieldReportById(fpyReportId);
		this.lblFpyRptTitle.setValue(lblFpyRptTitle.getValue() + " - " + this.fpyReport.getFpyReportName()); 

		this.cbxDateFieldsFilter.setContainerDataSource(null);
		this.cbxFpyFieldFilter.setContainerDataSource(null);

		//Set filters 
		List<FirstPassYieldInfoField> fpyInfoFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(fpyReportId);		
		for(int i=0; i<fpyInfoFields.size(); i++)
		{
			if(fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("varchar") || fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("char")
					|| fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("text") ||fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nvarchar") 
					|| fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nchar") || fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("ntext"))
			{
				this.cbxFpyFieldFilter.addItem(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxFpyFieldFilter.setItemCaption(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyInfoFields.get(i).getFpyInfoFieldLabel());
			}
			else if(fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("date"))
			{
				this.cbxDateFieldsFilter.addItem(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxDateFieldsFilter.setItemCaption(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyInfoFields.get(i).getFpyInfoFieldLabel());
			}	
		}
		
		
	}
	
}
