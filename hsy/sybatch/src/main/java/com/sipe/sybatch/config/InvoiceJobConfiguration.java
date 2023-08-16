package com.sipe.sybatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class InvoiceJobConfiguration {

	public static final String JOB_NAME = "INVOICE_JOB";

	// TODO jobRepository bean 스캔 필요
	@Bean
	public Job invoiceJob(JobRepository jobRepository, Step invoiceStep) {
		return new JobBuilder(JOB_NAME, jobRepository)
			.start(invoiceStep)
			.build();
	}

	@Bean
	public Tasklet invoiceTasklet() {
		return new InvoiceTasklet();
	}

	@Bean
	public Step invoiceStep(
		JobRepository jobRepository,
		Tasklet invoiceTasklet,
		PlatformTransactionManager transactionManager) {
		return new StepBuilder("myStep", jobRepository)
			.tasklet(invoiceTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
			.build();
	}

}
