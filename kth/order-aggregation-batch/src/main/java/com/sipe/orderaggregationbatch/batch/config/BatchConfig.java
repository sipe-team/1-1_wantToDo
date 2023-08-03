package com.sipe.orderaggregationbatch.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

  @Bean
  public Job processOnlineRetailOrderFileJob(JobRepository jobRepository,
                 Step writeToDbStep,
                 Step writeToCsvStep) {
    return new JobBuilder("processOnlineRetailOrderFileJob", jobRepository)
        .start(writeToDbStep)
        .next(writeToCsvStep)
        .build();
  }

  @Bean
  @JobScope
  private Step writeToDbStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("writeToDbStep", jobRepository)
        .tasklet((contribution, chunkContext) -> null, transactionManager)
        .build();
  }

  @Bean
  @JobScope
  private Step writeToCsvStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("writeToCsvStep", jobRepository)
        .tasklet((contribution, chunkContext) -> null, transactionManager)
        .build();
  }
}
