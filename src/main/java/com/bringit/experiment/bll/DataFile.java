package com.bringit.experiment.bll;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="DataFile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class DataFile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DataFileId")
	private Integer dataFileId;

	@Column(name="DataFileIsXml")
	private Boolean dataFileIsXml;
	
	@Column(name="DataFileIsCsv")
	private Boolean dataFileIsCsv;
	
	@Column(name="DataFileName")
	private String dataFileName;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FileRepoId", unique=false, updatable=true)
	private FilesRepository fileRepoId;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;

	@Column(name="CreatedDate")
	private Date createdDate;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;

	@Column(name="LastModifiedDate")
	private Date lastModifiedDate;
	
	public DataFile()
	{
		this.dataFileId = null;
		this.dataFileIsXml = null;
		this.dataFileIsCsv = null;
		this.dataFileName = null;
		this.fileRepoId = null;
		this.createdBy = null;
		this.createdDate = null;
		this.lastModifiedBy = null;
		this.lastModifiedDate = null;
	}
	
	public DataFile(Integer dataFileId, boolean dataFileIsXml, boolean dataFileIsCsv, String dataFileName,
			FilesRepository fileRepoId, SysUser createdBy, Date createdDate, SysUser lastModifiedBy, Date lastModifiedDate) {
		super();
		this.dataFileId = dataFileId;
		this.dataFileIsXml = dataFileIsXml;
		this.dataFileIsCsv = dataFileIsCsv;
		this.dataFileName = dataFileName;
		this.fileRepoId = fileRepoId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}
