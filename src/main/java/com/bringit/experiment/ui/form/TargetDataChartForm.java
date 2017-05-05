package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bringit.experiment.bll.*;
import com.bringit.experiment.dao.*;
import com.bringit.experiment.ui.design.TargetDataChartDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.MarkerSymbolEnum;
import com.vaadin.addon.charts.model.PlotOptionsScatter;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.MSSQLGenerator;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class TargetDataChartForm extends TargetDataChartDesign {

    private TargetReport targetRpt = new TargetReport();
    Experiment experiment = new Experiment();
    List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
    List<ContractManufacturer> contractManufacturers = new ArrayList<ContractManufacturer>();
    SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");

    private SQLContainer vaadinTblContainer;

    public TargetDataChartForm(TargetReport targetRpt, Experiment experiment, List<ExperimentField> experimentFields, List<ContractManufacturer> contractManufacturers, Object selectedDateFieldName, Date fromDate, Date toDate, Object selectedCm, Object selectedExpFieldName, String expFieldValue) {
        this.targetRpt = targetRpt;
        this.experiment = experiment;
        this.experimentFields = experimentFields;
        this.contractManufacturers = contractManufacturers;

        this.tblTargetDataReport.setVisible(false);
        /*
		cbxDateFieldsFilter.setContainerDataSource(null);
		cbxExpFieldFilter.setContainerDataSource(null);

		cbxDateFieldsFilter.addItem("CreatedDate");
		cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

		cbxDateFieldsFilter.addItem("LastModifiedDate");
		cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");

		cbxDateFieldsFilter.setInvalidAllowed(false);
		cbxExpFieldFilter.setInvalidAllowed(false);
		
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
		
		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
			cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
		}
		cbxContractManufacturer.setInvalidAllowed(false);
		*/

        //	List<TargetColumn> targetColumns = new ArrayList<TargetColumn>();
        List<TargetColumnGroup> targetColumnGroups = new TargetColumnGroupDao().getTargetColumnGroupsByReportId(targetRpt.getTargetReportId());

        List<String> dbRptTableCols = new ArrayList<String>();
        List<String> dbRptTableTypes = new ArrayList<String>();

        dbRptTableCols.add("RecordId");
        dbRptTableTypes.add("int");

        for (int i = 0; i < targetColumnGroups.size(); i++) {
            List<TargetColumn> targetRptCols = new TargetColumnDao().getTargetColumnsByColGroupById(targetColumnGroups.get(i).getTargetColumnGroupId());

            for (int j = 0; j < targetRptCols.size(); j++) {
                dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_"));
                dbRptTableTypes.add(targetRptCols.get(j).getExperimentField().getExpFieldType());

                if (!targetRptCols.get(j).getTargetColumnIsInfo()) {
                    dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Result");
                    dbRptTableTypes.add("varchar(20)");
                    
                    String uomAbbreviation = targetRptCols.get(j).getExperimentField().getUnitOfMeasure() != null ? targetRptCols.get(j).getExperimentField().getUnitOfMeasure().getUomAbbreviation() : "";
                    
                    cbxValueX.addItem(targetRptCols.get(j).getTargetColumnId());
                    cbxValueX.setItemCaption(targetRptCols.get(j).getTargetColumnId(), targetRptCols.get(j).getTargetColumnLabel() + ( uomAbbreviation.isEmpty() ? "" : " [" + uomAbbreviation + "]"));
                    
                    cbxValueY.addItem(targetRptCols.get(j).getTargetColumnId());
                    cbxValueY.setItemCaption(targetRptCols.get(j).getTargetColumnId(), targetRptCols.get(j).getTargetColumnLabel() + ( uomAbbreviation.isEmpty() ? "" : " [" + uomAbbreviation + "]"));
                    
                }
            }
        }

        dbRptTableCols.add("Result");
        dbRptTableTypes.add("varchar(20)");

        cbxExpFieldFilter.setContainerDataSource(null);
        cbxDateFieldsFilter.setContainerDataSource(null);

        for (int i = 0; i < dbRptTableCols.size(); i++) {
            if (dbRptTableTypes.get(i).toLowerCase().startsWith("varchar") || dbRptTableTypes.get(i).toLowerCase().startsWith("char")
                    || dbRptTableTypes.get(i).toLowerCase().startsWith("text") || dbRptTableTypes.get(i).toLowerCase().startsWith("nvarchar")
                    || dbRptTableTypes.get(i).toLowerCase().startsWith("nchar") || dbRptTableTypes.get(i).toLowerCase().startsWith("ntext")) {
                cbxExpFieldFilter.addItem(dbRptTableCols.get(i));
                cbxExpFieldFilter.setItemCaption(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " "));
            } else if (dbRptTableTypes.get(i).toLowerCase().contains("date")) {
                cbxDateFieldsFilter.addItem(dbRptTableCols.get(i));
                cbxDateFieldsFilter.setItemCaption(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " "));
            }
        }

        cbxDateFieldsFilter.addItem("CreatedDate");
        cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

        if (cbxDateFieldsFilter.size() == 1)
            cbxDateFieldsFilter.select("CreatedDate");

        cbxDateFieldsFilter.addItem("LastModifiedDate");
        cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");

        if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
            List<CmForSysRole> cmForSysRole =  new CmForSysRoleDao().getListOfCmForSysRoleBysysRoleId(sysRoleSession.getRoleId());
            cmForSysRole.forEach(x-> this.contractManufacturers.add(x.getContractManufacturer()));
        } else {
            contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
        }

        for (int i = 0; contractManufacturers != null && i < contractManufacturers.size(); i++) {
            cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmName());
            cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmName(), contractManufacturers.get(i).getCmName());
        }
        cbxContractManufacturer.setInvalidAllowed(false);
		
		/*
		for(int i=0; targetColumnGroups!=null && i<targetColumnGroups.size(); i++)
		{
			List<TargetColumn> targetGroupCols = new TargetColumnDao().getTargetColumnsByColGroupById(targetColumnGroups.get(i).getTargetColumnGroupId());
			
			for(int j=0; targetGroupCols!=null && j<targetGroupCols.size(); j++)
				targetColumns.add(targetGroupCols.get(j));
		}
		
		for(int i=0; targetColumns!=null && i<targetColumns.size(); i++)
		{
			if(!targetColumns.get(i).getTargetColumnIsInfo())
			{
				cbxValueX.addItem(targetColumns.get(i).getTargetColumnId());
				cbxValueX.setItemCaption(targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel());
			
				cbxValueY.addItem(targetColumns.get(i).getTargetColumnId());
				cbxValueY.setItemCaption(targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel());
			}
		}
		*/

        cbxValueX.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbxValueX.getValue() != null) {
                	TargetColumn selectedTargetColumn = new TargetColumnDao().getTargetColumnById((Integer) cbxValueX.getValue());                    
                    txtOffsetValueX.setValue(selectedTargetColumn.getTargetColumnOffset().toString());
                    txtTargetValueX.setValue(selectedTargetColumn.getTargetColumnGoalValue().toString());
                }

            }
        });

        cbxValueY.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbxValueY.getValue() != null) {
                    TargetColumn selectedTargetColumn = new TargetColumnDao().getTargetColumnById((Integer) cbxValueY.getValue());                    
                    txtOffsetValueY.setValue(selectedTargetColumn.getTargetColumnOffset().toString());
                    txtTargetValueY.setValue(selectedTargetColumn.getTargetColumnGoalValue().toString());
                }

            }
        });


        if (selectedDateFieldName != null) cbxDateFieldsFilter.setValue(selectedDateFieldName);
        if (fromDate != null) dtFilter1.setValue(fromDate);
        if (toDate != null) dtFilter2.setValue(toDate);

        if (selectedExpFieldName != null) cbxExpFieldFilter.setValue(selectedExpFieldName);
        if (expFieldValue != null && !expFieldValue.isEmpty()) txtExpFieldFilter.setValue(expFieldValue);

        if (selectedCm != null) cbxContractManufacturer.setValue(selectedCm);


        btnApplyFilters.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (cbxValueX.getValue() != null && cbxValueY.getValue() != null)
                    buildTargetChart();
            }

        });

    }

    private void buildTargetChart() {
        bindTargetReportRptTable();

        //To do:
        //Include Container Filters to Table according to CM Restrictions
        //1) Get Role of Session
        //SysRole sysRoleSession = (SysRole)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
        //2) Get CmNames String array
        //3) Set static filter to data loaded
        //1 Container Filter by 1 CmName
        //Equal Operator needs to be used vaadinTblContainer.addContainerFilter(new Compare.Equal(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));


        //System.out.println("Filtering Data...");
        vaadinTblContainer.removeAllContainerFilters();

        if (cbxDateFieldsFilter.getValue() != null && dtFilter1.getValue() != null && dtFilter2.getValue() != null) {
            Date dateFilterValue1 = dtFilter1.getValue();
            Date dateFilterValue2 = dtFilter2.getValue();

            dateFilterValue2.setHours(23);
            dateFilterValue2.setMinutes(59);
            dateFilterValue2.setSeconds(59);
            vaadinTblContainer.addContainerFilter(new Between(cbxDateFieldsFilter.getValue(), dateFilterValue1, dateFilterValue2));

        }

        if (cbxExpFieldFilter.getValue() != null)
            vaadinTblContainer.addContainerFilter(new Like(cbxExpFieldFilter.getValue(), "%" + txtExpFieldFilter.getValue().trim() + "%"));

        if (cbxContractManufacturer.getValue() != null)
            vaadinTblContainer.addContainerFilter(new Compare.Equal("CmName", cbxContractManufacturer.getValue()));

        layoutChart.removeAllComponents();

        Chart targetChart = new Chart(ChartType.SCATTER);
        targetChart.setSizeFull();

        Configuration targetChartConf = targetChart.getConfiguration();
        targetChartConf.setTitle(targetRpt.getTargetReportName());
        targetChartConf.getLegend().setEnabled(false);
        targetChartConf.setExporting(true);

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setFormatter("this.point.name");
        targetChartConf.setTooltip(tooltip);

        //--- Start: Building Pass Rectangle --//

        double minValueX = Double.parseDouble(txtTargetValueX.getValue()) - Double.parseDouble(txtOffsetValueX.getValue());
        double minValueY = Double.parseDouble(txtTargetValueY.getValue()) - Double.parseDouble(txtOffsetValueY.getValue());
        double maxValueX = Double.parseDouble(txtTargetValueX.getValue()) + Double.parseDouble(txtOffsetValueX.getValue());
        double maxValueY = Double.parseDouble(txtTargetValueY.getValue()) + Double.parseDouble(txtOffsetValueY.getValue());

        DataSeries passAreaSeries = new DataSeries();
        passAreaSeries.setName("Pass Area");
        passAreaSeries.add(new DataSeriesItem(minValueX, minValueY));
        passAreaSeries.add(new DataSeriesItem(minValueX, maxValueY));
        passAreaSeries.add(new DataSeriesItem(maxValueX, maxValueY));
        passAreaSeries.add(new DataSeriesItem(maxValueX, minValueY));
        passAreaSeries.add(new DataSeriesItem(minValueX, minValueY));

        PlotOptionsSpline plotTargetRectangle = new PlotOptionsSpline();
        plotTargetRectangle.setTurboThreshold(0);
        plotTargetRectangle.setColor(new SolidColor(Color.MAGENTA.getCSS()));
        passAreaSeries.setPlotOptions(plotTargetRectangle);
        targetChartConf.addSeries(passAreaSeries);
        //--- End: Building Pass Rectangle --//


        //--- Start: Building Values Points --//
        DataSeries valuesSeries = new DataSeries();


        DataSeriesItem pointTargetValue = new DataSeriesItem(Double.parseDouble(txtTargetValueX.getValue()), Double.parseDouble(txtTargetValueY.getValue()));
        pointTargetValue.setName("Target Value");
        pointTargetValue.setColor(new SolidColor(Color.GREEN.getCSS()));
        Marker markerTargetValue = new Marker();
        markerTargetValue.setSymbol(MarkerSymbolEnum.DIAMOND);
        markerTargetValue.setRadius(10);
        pointTargetValue.setMarker(markerTargetValue);
        valuesSeries.add(pointTargetValue);

        Marker markerPointValue = new Marker();
        markerPointValue.setSymbol(MarkerSymbolEnum.DIAMOND);
        markerPointValue.setRadius(5);
        //markerPointValue.setFillColor(new SolidColor(Color.MAGENTA.getCSS()));
        DataSeriesItem newValue = new DataSeriesItem(Double.parseDouble(txtTargetValueX.getValue()), Double.parseDouble(txtTargetValueY.getValue()));

        String[] tblColumnHeadersMtx = tblTargetDataReport.getColumnHeaders();
        String[] tblColumnIdsMtx = tblTargetDataReport.getColumnHeaders();


        Collection propertyIds = tblTargetDataReport.getContainerDataSource().getContainerPropertyIds();
        Integer cnt = 0;
        for (Object propertyId : propertyIds) {
            tblColumnIdsMtx[cnt] = propertyId.toString().trim();
            cnt++;
        }

        TargetColumn targetColumnX = new TargetColumnDao().getTargetColumnById((Integer) cbxValueX.getValue());                    
        TargetColumn targetColumnY = new TargetColumnDao().getTargetColumnById((Integer) cbxValueY.getValue());                    
        
        String colXName = targetColumnX.getTargetColumnLabel();
        String colYName = targetColumnY.getTargetColumnLabel();
        
        //String colXName = cbxValueX.getCaption().toString().trim();
        //String colYName = cbxValueY.getCaption().toString().trim();

        String valueX = "";
        String toolTipHeaderX = "";
        String valueY = "";
        String toolTipHeaderY = "";

        String toolTipBody = "";

        Collection itemIds = tblTargetDataReport.getContainerDataSource().getItemIds();
        cnt = 1;
        for (Object itemIdObj : itemIds) {
            toolTipBody = "";

            for (int i = 0; i < tblColumnHeadersMtx.length; i++) {
            	
                if (tblColumnHeadersMtx[i].trim().equals(colXName)) {
                    valueX = tblTargetDataReport.getContainerProperty(itemIdObj, tblColumnIdsMtx[i]).getValue().toString();
                    toolTipHeaderX = "<b>" + colXName + " [X]:</b> " + valueX;
                }
                if (tblColumnHeadersMtx[i].trim().equals(colYName)) {
                    valueY = tblTargetDataReport.getContainerProperty(itemIdObj, tblColumnIdsMtx[i]).getValue().toString();
                    toolTipHeaderY = "<b>" + colYName + " [X]:</b> " + valueY;
                }

                if (!tblColumnHeadersMtx[i].trim().equals(colXName) && !tblColumnHeadersMtx[i].trim().equals(colYName))
                    toolTipBody += tblColumnHeadersMtx[i].trim() + ": " + tblTargetDataReport.getContainerProperty(itemIdObj, tblColumnIdsMtx[i]).getValue() + "<br>";
            }

            if (valueX != null && valueY != null && !valueX.isEmpty() && !valueY.isEmpty()) {
                newValue = new DataSeriesItem(Double.parseDouble(valueX), Double.parseDouble(valueY));
                newValue.setId(cnt.toString());
                newValue.setColor(new SolidColor(Color.BLUE.getCSS()));
                newValue.setName(toolTipHeaderX + "<br>" + toolTipHeaderY + "<br><br>" + toolTipBody);
                valuesSeries.add(newValue);
            }

            cnt++;
        }

        PlotOptionsScatter plotValues = new PlotOptionsScatter();
        plotValues.setTurboThreshold(0);
        valuesSeries.setPlotOptions(plotValues);
        targetChartConf.addSeries(valuesSeries);

        //--- End: Building Values Points --//

        tblTargetDataReport.setVisibleColumns(new Object[]{targetColumnX.getTargetColumnLabel().replaceAll(" ", "_"), targetColumnY.getTargetColumnLabel().replaceAll(" ", "_")});
        tblTargetDataReport.setVisible(true);
        lblChartInstruction.setVisible(false);
        layoutChart.addComponent(targetChart);
		
		
		/*
		 * spTargetReportBuilder 
	 		@TargetReportId NVARCHAR(MAX),
			@ExperimentId NVARCHAR(MAX),
			@DateFieldName NVARCHAR(MAX),
			@FromDate NVARCHAR(MAX),
			@ToDate NVARCHAR(MAX),
			@CmId NVARCHAR(MAX),
			@ExpFieldName NVARCHAR(MAX),
			@ExpFieldValue NVARCHAR(MAX)
		 */
		
		/*	
		String dateFieldName = "";
		String fromDate = "";
		String toDate = "";
		String cmId = "";
		String expFieldName = "";
		String expFieldValue = "";
		
		if(cbxContractManufacturer.getValue() != null && !cbxContractManufacturer.getValue().toString().isEmpty())
			cmId = cbxContractManufacturer.getValue().toString().trim();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		if(cbxDateFieldsFilter.getValue() != null && !cbxDateFieldsFilter.getValue().toString().isEmpty()
				&& dtFilter1.getValue() != null && dtFilter2.getValue() != null)
		{
			dateFieldName = cbxDateFieldsFilter.getValue().toString();
			fromDate = df.format(dtFilter1.getValue());
			Date toDate24hours = dtFilter2.getValue();
			toDate24hours.setHours(23);
			toDate24hours.setMinutes(59);
			toDate24hours.setSeconds(59);
			toDate = df.format(toDate24hours);
		}
		
		if(cbxExpFieldFilter.getValue() != null && !cbxExpFieldFilter.getValue().toString().isEmpty()
				&& txtExpFieldFilter.getValue() != null && !txtExpFieldFilter.getValue().isEmpty())
		{
			expFieldName = cbxExpFieldFilter.getValue().toString();
			expFieldValue = txtExpFieldFilter.getValue();
		}
		
		//Call SP with TargetReportId
		List<String> spParams = new ArrayList<String>();
		spParams.add(targetRpt.getTargetReportId().toString());
		spParams.add(targetRpt.getExperiment().getExpId().toString());
		spParams.add(dateFieldName);
		spParams.add(fromDate);
		spParams.add(toDate);
		spParams.add(cmId);
		spParams.add(expFieldName);
		spParams.add(expFieldValue);
					
		ResultSet spResults = new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spParams);
				
		if(spResults != null)
		{	
			VaadinControls.bindDbViewRsToVaadinTable(tblTargetDataReport, spResults, 1);

			layoutChart.removeAllComponents();
			
			Chart targetChart = new Chart(ChartType.SCATTER);
			targetChart.setSizeFull();
	
			Configuration targetChartConf = targetChart.getConfiguration();
			targetChartConf.setTitle(targetRpt.getTargetReportName());
			targetChartConf.getLegend().setEnabled(false);
			targetChartConf.setExporting(true);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setShared(true);
		    tooltip.setFormatter("this.point.name");
		    targetChartConf.setTooltip(tooltip);
			
			//--- Start: Building Pass Rectangle --//
			
			double minValueX = Double.parseDouble(txtTargetValueX.getValue()) -  Double.parseDouble(txtOffsetValueX.getValue());
			double minValueY = Double.parseDouble(txtTargetValueY.getValue()) -  Double.parseDouble(txtOffsetValueY.getValue());
			double maxValueX = Double.parseDouble(txtTargetValueX.getValue()) +  Double.parseDouble(txtOffsetValueX.getValue());
			double maxValueY = Double.parseDouble(txtTargetValueY.getValue()) +  Double.parseDouble(txtOffsetValueY.getValue());
			
			DataSeries passAreaSeries = new DataSeries();
			passAreaSeries.setName("Pass Area");
			passAreaSeries.add(new DataSeriesItem(minValueX,minValueY));
		    passAreaSeries.add(new DataSeriesItem(minValueX,maxValueY));
		    passAreaSeries.add(new DataSeriesItem(maxValueX,maxValueY));
		    passAreaSeries.add(new DataSeriesItem(maxValueX,minValueY));
		    passAreaSeries.add(new DataSeriesItem(minValueX,minValueY));
	
	        PlotOptionsSpline plotTargetRectangle = new PlotOptionsSpline();
	        plotTargetRectangle.setTurboThreshold(0);
	        plotTargetRectangle.setColor(new SolidColor(Color.MAGENTA.getCSS()));
	        passAreaSeries.setPlotOptions(plotTargetRectangle);
	        targetChartConf.addSeries(passAreaSeries);
			//--- End: Building Pass Rectangle --//
	
	        
	        //--- Start: Building Values Points --//
	        DataSeries valuesSeries = new DataSeries();
				    
	
			DataSeriesItem pointTargetValue = new DataSeriesItem( Double.parseDouble(txtTargetValueX.getValue()), Double.parseDouble(txtTargetValueY.getValue()));
			pointTargetValue.setName("Target Value");
			pointTargetValue.setColor(new SolidColor(Color.GREEN.getCSS()));
			Marker markerTargetValue = new Marker();
			markerTargetValue.setSymbol(MarkerSymbolEnum.DIAMOND);
			markerTargetValue.setRadius(10);
			pointTargetValue.setMarker(markerTargetValue);
			valuesSeries.add(pointTargetValue);
				
		    Marker markerPointValue = new Marker();
		    markerPointValue.setSymbol(MarkerSymbolEnum.DIAMOND);
		    markerPointValue.setRadius(5);
		    //markerPointValue.setFillColor(new SolidColor(Color.MAGENTA.getCSS()));
		    DataSeriesItem newValue = new DataSeriesItem( Double.parseDouble(txtTargetValueX.getValue()), Double.parseDouble(txtTargetValueY.getValue()));
		  
		    String[] tblColumnHeaders = tblTargetDataReport.getColumnHeaders();

			String colXName = cbxValueX.getItemCaption(cbxValueX.getValue());
			String colYName = cbxValueY.getItemCaption(cbxValueY.getValue());

			String valueX = "";
			String toolTipHeaderX = "";
			String valueY = "";
			String toolTipHeaderY = "";
			
			String toolTipBody = "";
			
		    Collection itemIds = tblTargetDataReport.getContainerDataSource().getItemIds();
			Integer cnt=1;
			for (Object itemIdObj : itemIds) 
			{	
				toolTipBody = "";
				
				Item tblRowItem = tblTargetDataReport.getContainerDataSource().getItem(itemIdObj.toString());
				
				for(int i=0; i<tblColumnHeaders.length; i++)
				{
					if(tblColumnHeaders[i].trim().equals(colXName))
					{
						//System.out.println( colXName + " - " + (String)(tblRowItem.getItemProperty(colXName).getValue()));
						
						toolTipHeaderX = "<b>" + colXName + " [X]:</b> " + (String)(tblRowItem.getItemProperty(colXName).getValue());
						valueX = (String)(tblRowItem.getItemProperty(colXName).getValue());
					}
					if(tblColumnHeaders[i].trim().equals(colYName))
					{
						toolTipHeaderY = "<b>" + colYName + " [Y]:</b> " + (String)(tblRowItem.getItemProperty(colYName).getValue());
						valueY = (String)(tblRowItem.getItemProperty(colYName).getValue()); 
					}
					
					//System.out.println("Column Name: "  + tblColumnHeaders[i].trim()  + "Column Value: " + (String)(tblRowItem.getItemProperty(tblColumnHeaders[i]).getValue()));
					if(!tblColumnHeaders[i].trim().equals(colXName) && !tblColumnHeaders[i].trim().equals(colYName))
						toolTipBody +=  tblColumnHeaders[i].trim() + ": " + (String)(tblRowItem.getItemProperty(tblColumnHeaders[i]).getValue()) + "<br>";
				}
				
				if(valueX != null && valueY != null && !valueX.isEmpty() && !valueY.isEmpty())
				{
					newValue = new DataSeriesItem(Double.parseDouble(valueX), Double.parseDouble(valueY));
					newValue.setId(cnt.toString());
					newValue.setColor(new SolidColor(Color.BLUE.getCSS()));
					newValue.setName(toolTipHeaderX+"<br>"+toolTipHeaderY+"<br><br>"+toolTipBody);
					valuesSeries.add(newValue);
				}
				
				//System.out.println("Added Value: " + cnt);
				cnt++;
			}
		    		
			PlotOptionsScatter plotValues = new PlotOptionsScatter();
			plotValues.setTurboThreshold(0);
			valuesSeries.setPlotOptions(plotValues);
		    targetChartConf.addSeries(valuesSeries);
		    
			//--- End: Building Values Points --//
		    
		    tblTargetDataReport.setVisibleColumns(new Object[] {cbxValueX.getItemCaption(cbxValueX.getValue()), cbxValueY.getItemCaption(cbxValueY.getValue())});
			tblTargetDataReport.setVisible(true);
			lblChartInstruction.setVisible(false);		
			layoutChart.addComponent(targetChart);
			
			
		}
		else
			this.getUI().showNotification("No data found for selected criteria.", Type.WARNING_MESSAGE);
		*/
    }


    private void bindTargetReportRptTable() {
        Config configuration = new Config();

        if (configuration.getProperty("dbms").equals("sqlserver")) {
            String dbHost = configuration.getProperty("dbhost");
            String dbPort = configuration.getProperty("dbport");
            String dbDatabase = configuration.getProperty("dbdatabase");
            String dbUsername = configuration.getProperty("dbusername");
            String dbPassword = configuration.getProperty("dbpassword");

            SimpleJDBCConnectionPool connectionPool;

            try {
                connectionPool = new SimpleJDBCConnectionPool("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                        "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName=" + dbDatabase,
                        dbUsername, dbPassword);

                TableQuery tblQuery = new TableQuery(targetRpt.getTargetReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
                tblQuery.setVersionColumn("RecordId");

                vaadinTblContainer = new SQLContainer(tblQuery);

                tblTargetDataReport.setContainerDataSource(vaadinTblContainer);

                List<TargetColumnGroup> targetRptColGroups = new TargetColumnGroupDao().getTargetColumnGroupsByReportId(targetRpt.getTargetReportId());
                List<String> dbRptTableCols = new ArrayList<String>();
                List<String> dbRptTableTypes = new ArrayList<String>();

                dbRptTableCols.add("RecordId");
                dbRptTableTypes.add("int");

                for (int i = 0; i < targetRptColGroups.size(); i++) {
                    List<TargetColumn> targetRptCols = new TargetColumnDao().getTargetColumnsByColGroupById(targetRptColGroups.get(i).getTargetColumnGroupId());

                    for (int j = 0; j < targetRptCols.size(); j++) {
                        dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_"));
                        dbRptTableTypes.add(targetRptCols.get(j).getExperimentField().getExpFieldType());

                        if (!targetRptCols.get(j).getTargetColumnIsInfo()) {
                            dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Result");
                            dbRptTableTypes.add("varchar(20)");
                        }
                    }
                }

                dbRptTableCols.add("Result");
                dbRptTableTypes.add("varchar(20)");


                for (int i = 0; i < dbRptTableCols.size(); i++) {
                    tblTargetDataReport.setColumnHeader(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " "));

                    if (dbRptTableTypes.get(i).toLowerCase().contains("float") ||
                            dbRptTableTypes.get(i).toLowerCase().contains("decimal") ||
                            dbRptTableTypes.get(i).toLowerCase().contains("int")) {
                        tblTargetDataReport.setConverter(dbRptTableCols.get(i), new StringToDoubleConverter() {
                            @Override
                            protected NumberFormat getFormat(Locale locale) {
                                NumberFormat format = NumberFormat.getNumberInstance();
                                format.setGroupingUsed(false);
                                return format;
                            }
                        });

                    }

                    if (dbRptTableTypes.get(i).toLowerCase().contains("date")) {
                        tblTargetDataReport.setConverter(dbRptTableCols.get(i), new StringToDateConverter() {
                            @Override
                            protected DateFormat getFormat(Locale locale) {
                                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            }
                        });
                    }
                }

                tblTargetDataReport.setColumnHeader("RecordId", "Id");

                String[] targetRptCols = new String[dbRptTableCols.size()];
                for (int i = 0; i < dbRptTableCols.size(); i++)
                    targetRptCols[i] = dbRptTableCols.get(i);

                //Visible columns should be determined by selected Values for X and Y
                //tblTargetDataReport.setVisibleColumns(targetRptCols);


            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
