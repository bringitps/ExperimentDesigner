package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.util.VaadinControls;

public class ExperimentManagementForm extends ExperimentManagementDesign {
	
	public ExperimentManagementForm() {
		ResultSet experimentViewResults = new DataBaseViewDao().getViewResults("vwExperiment");
		VaadinControls.bindDbViewRsToVaadinTable(tblExperiments, experimentViewResults, 1);
	}
	
}
