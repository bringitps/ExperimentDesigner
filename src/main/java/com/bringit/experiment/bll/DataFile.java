package com.bringit.experiment.bll;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="DataFile")
public class DataFile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DataFileId")
	private Integer dataFileId;

	@Column(name="DataFileIsXml")
	private boolean dataFileIsXml;
	
	@Column(name="DataFileIsCsv")
	private boolean dataFileIsCsv;
	
	@Column(name="DataFileName")
	private String dataFileName;
	
	@OneToOne
    @JoinColumn(name="FileRepoId", unique=false, updatable=true)
	private FilesRepository fileRepoId;

	@OneToOne
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;

	public Integer getDataFileId() {
		return dataFileId;
	}

	public void setDataFileId(Integer dataFileId) {
		this.dataFileId = dataFileId;
	}

	public boolean isDataFileIsXml() {
		return dataFileIsXml;
	}

	public void setDataFileIsXml(boolean dataFileIsXml) {
		this.dataFileIsXml = dataFileIsXml;
	}

	public boolean isDataFileIsCsv() {
		return dataFileIsCsv;
	}

	public void setDataFileIsCsv(boolean dataFileIsCsv) {
		this.dataFileIsCsv = dataFileIsCsv;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public FilesRepository getFileRepoId() {
		return fileRepoId;
	}

	public void setFileRepoId(FilesRepository fileRepoId) {
		this.fileRepoId = fileRepoId;
	}

	public SysUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SysUser createdBy) {
		this.createdBy = createdBy;
	}

	public SysUser getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(SysUser lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
