package com.bringit.experiment.bll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JobExecutionRepeat")
public class JobExecutionRepeat {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="JobExecRepeatId")
	private Integer jobExecRepeatId;

	@Column(name="JobExecRepeatName")
	private String JobExecRepeatName;
	
	@Column(name="JobExecRepeatMilliseconds")
	private int jobExecRepeatMilliseconds;

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
