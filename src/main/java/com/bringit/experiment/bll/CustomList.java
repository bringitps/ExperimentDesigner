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
@Table(name="CustomList")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class CustomList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CustomListId")
	private Integer customListId;
	
	@Column(name="CustomListName")
	private String customListName;

	@Column(name="CustomListDescription")
	private String customListDescription;
	
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

	public CustomList() {
		this.customListId = null;
		this.customListName = null;
		this.customListDescription = null;
		this.createdDate = null;
		this.modifiedDate = null;
		this.createdBy = null;
		this.lastModifiedBy = null;
	}
		
	public CustomList(Integer customListId, String customListName, String customListDescription, 
			Date createdDate, Date modifiedDate, SysUser createdBy, SysUser lastModifiedBy) {
		this.customListId = customListId;
		this.customListName = customListName;
		this.customListDescription = customListDescription;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getCustomListId() {
		return customListId;
	}

	public void setCustomListId(Integer customListId) {
		this.customListId = customListId;
	}

	public String getCustomListName() {
		return customListName;
	}

	public void setCustomListName(String customListName) {
		this.customListName = customListName;
	}

	public String getCustomListDescription() {
		return customListDescription;
	}

	public void setCustomListDescription(String customListDescription) {
		this.customListDescription = customListDescription;
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
