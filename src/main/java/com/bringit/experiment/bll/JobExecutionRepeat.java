package com.bringit.experiment.bll;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="JobExecutionRepeat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class JobExecutionRepeat {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="JobExecRepeatId")
	private Integer jobExecRepeatId;

	@Column(name="JobExecRepeatName")
	private String JobExecRepeatName;
	
	@Column(name="JobExecRepeatMilliseconds")
	private Integer jobExecRepeatMilliseconds;

	public JobExecutionRepeat() {
		this.jobExecRepeatId = null;
		this.JobExecRepeatName = null;
		this.jobExecRepeatMilliseconds = null;
	}
	
	public JobExecutionRepeat(Integer jobExecRepeatId, String jobExecRepeatName, int jobExecRepeatMilliseconds) {
		this.jobExecRepeatId = jobExecRepeatId;
		this.JobExecRepeatName = jobExecRepeatName;
		this.jobExecRepeatMilliseconds = jobExecRepeatMilliseconds;
	}

	public Integer getJobExecRepeatId() {
		return jobExecRepeatId;
	}

	public void setJobExecRepeatId(Integer jobExecRepeatId) {
		this.jobExecRepeatId = jobExecRepeatId;
	}

	public String getJobExecRepeatName() {
		return JobExecRepeatName;
	}

	public void setJobExecRepeatName(String jobExecRepeatName) {
		JobExecRepeatName = jobExecRepeatName;
	}

	public int getJobExecRepeatMilliseconds() {
		return jobExecRepeatMilliseconds;
	}

	public void setJobExecRepeatMilliseconds(int jobExecRepeatMilliseconds) {
		this.jobExecRepeatMilliseconds = jobExecRepeatMilliseconds;
	}
	
	
}
