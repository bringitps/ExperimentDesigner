package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinService;

public class ExperimentManagementForm extends ExperimentManagementDesign {
	
	@SuppressWarnings("deprecation")
	public ExperimentManagementForm() {
		tblExperiments.removeAllColumns();
		ResultSet experimentViewResults = new DataBaseViewDao().getViewResults("vwExperiment");
		String test = "";
		try {
			
		    while (experimentViewResults.next()) {
		      test +=experimentViewResults.getString(1) + ", " + experimentViewResults.getString(2) + ", "
		          + experimentViewResults.getString(3);
		    }
		    System.out.println(test);
			tblExperiments.setCaption("Testing: " + test);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    //tblExperiments.setContainerDataSource(new BeanItemContainer(Experiment.class, new ExperimentDao().getAllExperiments()));
	    
	}
	
}
