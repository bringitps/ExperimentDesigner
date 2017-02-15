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

	@Column(name="JobExecRepeatLabel")
	private String JobExecRepeatLabel;
	
	@Column(name="JobExecRepeatMilieconds")
	private int jobExecRepeatMilieconds;

	public Integer getJobExecRepeatId() {
		return jobExecRepeatId;
	}

	public void setJobExecRepeatId(Integer jobExecRepeatId) {
		this.jobExecRepeatId = jobExecRepeatId;
	}

	public String getJobExecRepeatLabel() {
		return JobExecRepeatLabel;
	}

	public void setJobExecRepeatLabel(String jobExecRepeatLabel) {
		JobExecRepeatLabel = jobExecRepeatLabel;
	}

	public int getJobExecRepeatMilieconds() {
		return jobExecRepeatMilieconds;
	}

	public void setJobExecRepeatMilieconds(int jobExecRepeatMilieconds) {
		this.jobExecRepeatMilieconds = jobExecRepeatMilieconds;
	}
	
	
}
