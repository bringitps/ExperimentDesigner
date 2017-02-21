package com.bringit.experiment.ui.form;

import java.util.List;

import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.XmlTemplateDesign;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class XmlTemplateForm extends XmlTemplateDesign {

	private XmlTemplate xmlt;

	
	public XmlTemplateForm(int xmlId)
	{
		if(xmlId == -1) //New
		{
			xmlt = new XmlTemplate();
		}
		else
		{
			//Loading Header Info
			xmlt = new XmlTemplateDao().getXmlTemplateById(xmlId);
			this.txtXmlTName.setValue(xmlt.getXmlTemplateName());
			//TODO add more fields maybe xml drag and drop mapping?
			
		}
	}
	
}
