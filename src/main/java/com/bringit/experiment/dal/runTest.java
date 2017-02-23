package com.bringit.experiment.dal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class runTest {

	public static void main(String[] args) {
		try {
			File xmlFile = new File("C:\\Users\\acer\\git\\ExperimentDesigner\\src\\main\\java\\com\\bringit\\experiment\\dal\\xmlSample.xml");
	        SAXBuilder builder = new SAXBuilder(); 	        
			Document doc = builder.build(xmlFile);
			LinkedHashMap<String,String> nodes = new LinkedHashMap<String,String>();
			nodes.put(doc.getRootElement().getName(), "root");
			List<Element> elements = doc.getRootElement().getChildren();
			for (Element element : elements) {
				if(!nodes.containsKey(element.getName())){
					nodes.put(element.getName(), element.getParentElement().getName());
					if(!element.getChildren().isEmpty()){
						List<Element> subElements = element.getChildren();
						for (Element subs : subElements) {
							if(!nodes.containsKey(subs.getName())){
								nodes.put(subs.getName(), subs.getParentElement().getName());
							}
						}
					}
				}
			}
			System.out.println(nodes);

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
