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
@Table(name="FilesRepository")
public class FilesRepository {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FileRepoId")
	private Integer fileRepoId;

	@Column(name="FileRepoIsLocal")
	private boolean fileRepoIsLocal;
	
	@Column(name="FileRepoIsFtp")
	private boolean fileRepoIsFtp;
	
	@Column(name="FileRepoIsSftp")
	private boolean fileRepoIsSftp;
	
	@Column(name="FileRepoPath")
	private String fileRepoPath;
	
	@Column(name="FileRepoHost")
	private String fileRepoHost;
	
	@Column(name="FileRepoPort")
	private String fileRepoPort;
	
	@Column(name="FileRepoUser")
	private String fileRepoUser;
	
	@Column(name="FileRepoPass")
	private String fileRepoPass;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@OneToOne
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;

	public Integer getFileRepoId() {
		return fileRepoId;
	}

	public void setFileRepoId(Integer fileRepoId) {
		this.fileRepoId = fileRepoId;
	}

	public boolean isFileRepoIsLocal() {
		return fileRepoIsLocal;
	}

	public void setFileRepoIsLocal(boolean fileRepoIsLocal) {
		this.fileRepoIsLocal = fileRepoIsLocal;
	}

	public boolean isFileRepoIsFtp() {
		return fileRepoIsFtp;
	}

	public void setFileRepoIsFtp(boolean fileRepoIsFtp) {
		this.fileRepoIsFtp = fileRepoIsFtp;
	}

	public boolean isFileRepoIsSftp() {
		return fileRepoIsSftp;
	}

	public void setFileRepoIsSftp(boolean fileRepoIsSftp) {
		this.fileRepoIsSftp = fileRepoIsSftp;
	}

	public String getFileRepoPath() {
		return fileRepoPath;
	}

	public void setFileRepoPath(String fileRepoPath) {
		this.fileRepoPath = fileRepoPath;
	}

	public String getFileRepoHost() {
		return fileRepoHost;
	}

	public void setFileRepoHost(String fileRepoHost) {
		this.fileRepoHost = fileRepoHost;
	}

	public String getFileRepoPort() {
		return fileRepoPort;
	}

	public void setFileRepoPort(String fileRepoPort) {
		this.fileRepoPort = fileRepoPort;
	}

	public String getFileRepoUser() {
		return fileRepoUser;
	}

	public void setFileRepoUser(String fileRepoUser) {
		this.fileRepoUser = fileRepoUser;
	}

	public String getFileRepoPass() {
		return fileRepoPass;
	}

	public void setFileRepoPass(String fileRepoPass) {
		this.fileRepoPass = fileRepoPass;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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
