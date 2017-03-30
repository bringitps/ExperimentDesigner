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

import com.bringit.experiment.util.M5Encryption;

@Entity
@Table(name="FilesRepository")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class FilesRepository {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FileRepoId")
	private Integer fileRepoId;

	@Column(name="FileRepoName")
	private String fileRepoName;
	
	@Column(name="FileRepoIsLocal")
	private Boolean fileRepoIsLocal;
	
	@Column(name="FileRepoIsFtp")
	private Boolean fileRepoIsFtp;
	
	@Column(name="FileRepoIsSftp")
	private Boolean fileRepoIsSftp;
	
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
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
	private SysUser createdBy;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
	private SysUser lastModifiedBy;

	public FilesRepository() {
		this.fileRepoId = null;
		this.fileRepoName = null;
		this.fileRepoIsLocal = null;
		this.fileRepoIsFtp = null;
		this.fileRepoIsSftp = null;
		this.fileRepoPath = null;
		this.fileRepoHost = null;
		this.fileRepoPort = null;
		this.fileRepoUser = null;
		this.fileRepoPass = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}

	
	
	public FilesRepository(Integer fileRepoId, String fileRepoName, boolean fileRepoIsLocal, boolean fileRepoIsFtp,
			boolean fileRepoIsSftp, String fileRepoPath, String fileRepoHost, String fileRepoPort, String fileRepoUser,
			String fileRepoPass, Date createdDate, Date modifiedDate, SysUser createdBy, SysUser lastModifiedBy) {
		this.fileRepoId = fileRepoId;
		this.fileRepoName = fileRepoName;
		this.fileRepoIsLocal = fileRepoIsLocal;
		this.fileRepoIsFtp = fileRepoIsFtp;
		this.fileRepoIsSftp = fileRepoIsSftp;
		this.fileRepoPath = fileRepoPath;
		this.fileRepoHost = fileRepoHost;
		this.fileRepoPort = fileRepoPort;
		this.fileRepoUser = fileRepoUser;
		this.fileRepoPass = fileRepoPass;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getFileRepoId() {
		return fileRepoId;
	}

	public void setFileRepoId(Integer fileRepoId) {
		this.fileRepoId = fileRepoId;
	}

	public String getFileRepoName() {
		return fileRepoName;
	}
	
	public void setFileRepoName(String fileRepoName) {
		this.fileRepoName = fileRepoName;
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

	public String getFileRepoPass() 
	{
	   	return M5Encryption.decrypt(fileRepoPass);
	}

	public void setFileRepoPass(String fileRepoPass) {
	    this.fileRepoPass = M5Encryption.encrypt(fileRepoPass);
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
