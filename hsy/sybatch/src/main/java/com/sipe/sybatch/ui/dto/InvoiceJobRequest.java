package com.sipe.sybatch.ui.dto;

import org.springframework.batch.core.JobParameters;

public class InvoiceJobRequest {
	private String name;
	private JobParameters jobParameters;

	public InvoiceJobRequest() {
	}

	public InvoiceJobRequest(String name, JobParameters jobParameters) {
		this.name = name;
		this.jobParameters = jobParameters;
	}

	public String getName() {
		return name;
	}

	public JobParameters getJobParameters() {
		return jobParameters;
	}
}
