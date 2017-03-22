package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.ui.design.ExperimentRecordChangesHistoryDesign;
import com.bringit.experiment.util.ExperimentUtil;
import com.bringit.experiment.util.VaadinControls;

public class ExperimentRecordChangesHistoryForm extends ExperimentRecordChangesHistoryDesign{

	public ExperimentRecordChangesHistoryForm(Experiment experiment, Integer experimentDbRecordId)
	{
		//this.lblExperimentRecordTitle.setValue(" -" + experiment.getExpName());
		
		String sqlSelectQuery = ExperimentUtil.buildSqlSelectQueryExperimentChangesLog(experiment.getExpId().toString(), experimentDbRecordId.toString());
		ResultSet experimentRecordChangesViewResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
		
		if(experimentRecordChangesViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblExperimentRecordHistoricData, experimentRecordChangesViewResults, 1);
	}
}
