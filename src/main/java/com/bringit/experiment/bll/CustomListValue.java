package com.bringit.experiment.bll;

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
@Table(name="CustomListValue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class CustomListValue {


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CustomListValueId")
	private Integer customListValueId;

	@Column(name="CustomListValueString")
	private String customListValueString;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CustomListId", unique=false, updatable=true)
	private CustomList customList;

	public CustomListValue() {
		this.customListValueId = null;
		this.customListValueString = null;
		this.customList = null;
	}
	
	public CustomListValue(Integer customListValueId, String customListValueString, CustomList customList) {
		this.customListValueId = customListValueId;
		this.customListValueString = customListValueString;
		this.customList = customList;
	}

	public Integer getCustomListValueId() {
		return customListValueId;
	}

	public void setCustomListValueId(Integer customListValueId) {
		this.customListValueId = customListValueId;
	}

	public String getCustomListValueString() {
		return customListValueString;
	}

	public void setCustomListValueString(String customListValueString) {
		this.customListValueString = customListValueString;
	}

	public CustomList getCustomList() {
		return customList;
	}

	public void setCustomList(CustomList customList) {
		this.customList = customList;
	}
		
}
