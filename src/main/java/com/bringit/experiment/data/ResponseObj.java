package com.bringit.experiment.data;

import java.util.List;

public class ResponseObj {

	private int code;
	private String description;
	private String detail;
	private String csvSQLInsertColumns;
	private List<String> csvSQLInsertValues;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getCsvInsertColumns() {
		return csvSQLInsertColumns;
	}
	public void setCsvInsertColumns(String csvInsertColumns) {
		this.csvSQLInsertColumns = csvInsertColumns;
	}
	public List<String> getCsvInsertValues() {
		return csvSQLInsertValues;
	}
	public void setCsvInsertValues(List<String> csvInsertValues) {
		this.csvSQLInsertValues = csvInsertValues;
	}
	

}
