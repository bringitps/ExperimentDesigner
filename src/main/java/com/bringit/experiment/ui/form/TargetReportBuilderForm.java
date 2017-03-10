package com.bringit.experiment.ui.form;

import com.bringit.experiment.ui.design.TargetReportBuilderDesign;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.Tree.TreeTargetDetails;
import com.vaadin.ui.VerticalLayout;

public class TargetReportBuilderForm extends TargetReportBuilderDesign {

	public TargetReportBuilderForm()
	{	
		
		VerticalLayout infoColGroupLayout = new VerticalLayout();
		GridLayout gridInfoColGroup = new GridLayout();
		gridInfoColGroup.setRows(1);
		gridInfoColGroup.setColumns(1);
		gridInfoColGroup.setSizeFull();
		
		TextField txtInfoColGroupName = new TextField();
		txtInfoColGroupName.setCaption("Field Group Name:");
		txtInfoColGroupName.setRequired(true);
		txtInfoColGroupName.setValue("Information");
		txtInfoColGroupName.setEnabled(false);
		txtInfoColGroupName.setStyleName("tiny");
		txtInfoColGroupName.setWidth(200, Unit.PIXELS);
		
		gridInfoColGroup.addComponent(txtInfoColGroupName, 0, 0);
		
		gridInfoColGroup.setMargin(new MarginInfo(true, true, true, true));
		gridInfoColGroup.setSpacing(true);
		
		infoColGroupLayout.addComponent(gridInfoColGroup);
		tabFieldGroups.addTab(infoColGroupLayout).setCaption("Information");
		
		btnAddFieldGroup.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addFieldGroup(-1);
			}

		});
		
	}
	
	private void addFieldGroup(int fieldGroupId)
	{
		
		if(fieldGroupId == -1)
		{
			
			VerticalLayout newFieldGroupLayout = new VerticalLayout();
			GridLayout gridNewFieldGroup = new GridLayout();
			gridNewFieldGroup.setRows(2);
			gridNewFieldGroup.setColumns(1);
			gridNewFieldGroup.setSizeFull();
			
			TextField newTxtInfoFieldGroupName = new TextField();
			newTxtInfoFieldGroupName.setCaption("Field Group Name:");
			newTxtInfoFieldGroupName.setRequired(true);
			newTxtInfoFieldGroupName.setValue("New Field Group");
			newTxtInfoFieldGroupName.setStyleName("tiny");
			newTxtInfoFieldGroupName.addListener(new TextChangeListener() {
	            
	            public void textChange(TextChangeEvent event) {
	                
	            	VerticalLayout selectedTabLayout = (VerticalLayout)event.getComponent().getParent().getParent();
	            	tabFieldGroups.getTab(selectedTabLayout).setCaption(event.getText());
	            }
	        });
			
			gridNewFieldGroup.addComponent(newTxtInfoFieldGroupName, 0, 0);
			
			gridNewFieldGroup.setMargin(new MarginInfo(true, true, true, true));
			gridNewFieldGroup.setSpacing(true);
			
			newFieldGroupLayout.addComponent(gridNewFieldGroup);
			newFieldGroupLayout.setCaption("New Field Group");
			tabFieldGroups.addTab(newFieldGroupLayout);
			
			//.setCaption("New Field Group");
			//tabFieldGroups.getSelectedTab().set
			/*
			headerGrid.addComponent(imgLogo, 0, 0);
			
			TextField txtField = new TextField();
			txtField.setCaption("Testing TextFields " + this.tabFieldGroups.getComponentCount() );
			VerticalLayout tab1 = new VerticalLayout();
			tab1.setId("Testing TextFields " + this.tabFieldGroups.getComponentCount() );
			tab1.addComponent(txtField);
			
			this.tabFieldGroups.addTab(tab1).setCaption("My Tab " + + this.tabFieldGroups.getComponentCount());
			*/
		}
	}
}
