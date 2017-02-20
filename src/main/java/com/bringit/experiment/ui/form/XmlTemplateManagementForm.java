package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.ui.design.XmlTemplateManagementDesign;
import com.bringit.experiment.util.VaadinControls;

public class XmlTemplateManagementForm extends XmlTemplateManagementDesign  {
	
	public XmlTemplateManagementForm() {
		ResultSet vwXmlTemplateResults = new DataBaseViewDao().getViewResults("vwXmlTemplate");
		VaadinControls.bindDbViewRsToVaadinTable(tblXmlTemplate, vwXmlTemplateResults, 1);
	}
	
}
