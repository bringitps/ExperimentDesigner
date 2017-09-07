package com.bringit.experiment.data;

import java.util.ArrayList;
import java.util.List;

public class ResponseObj {

	private int code;
	private String description;
	private String detail;
	private String csvSQLInsertColumns;
	private List<String> csvSQLInsertValues;
	private List<Integer> csvRowException;
	private List<String> csvRowExceptionDetails;
	
	
	
	public ResponseObj() {
		this.code = -1;
		this.description = null;
		this.detail = null;
		this.csvSQLInsertColumns = null;
		this.csvSQLInsertValues = new ArrayList<String>();
		this.csvRowException = new ArrayList<Integer>();
		this.csvRowExceptionDetails = new ArrayList<String>();
	}
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
	public List<Integer> getCsvRowException() {
		return csvRowException;
	}
	public void setCsvRowException(List<Integer> csvRowException) {
		this.csvRowException = csvRowException;
	}
	public List<String> getCsvRowExceptionDetails() {
		return csvRowExceptionDetails;
	}
	public void setCsvRowExceptionDetails(List<String> csvRowExceptionDetails) {
		this.csvRowExceptionDetails = csvRowExceptionDetails;
	}
	
	
}
