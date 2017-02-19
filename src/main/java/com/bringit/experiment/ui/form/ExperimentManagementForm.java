package com.bringit.experiment.ui.form;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinService;

public class ExperimentManagementForm extends ExperimentManagementDesign {
	
	@SuppressWarnings("deprecation")
	public ExperimentManagementForm() {
		tblExperiments.removeAllColumns();
	    tblExperiments.setContainerDataSource(new BeanItemContainer(Experiment.class, new ExperimentDao().getAllExperiments()));
	    
		//tblExperiments.setVisible(false);
	}
	
}
