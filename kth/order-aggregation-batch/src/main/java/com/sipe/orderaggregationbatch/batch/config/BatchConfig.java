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
  public Job job(JobRepository jobRepository,
                 Step processFileAndWriteToDbStep,
                 Step writeToCsvStep) {
    return new JobBuilder("", jobRepository)
        .start(processFileAndWriteToDbStep)
        .next(writeToCsvStep)
        .build();
  }

  @Bean
  @JobScope
  private Step processFileAndWriteToDbStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("", jobRepository)
        .tasklet((contribution, chunkContext) -> null, transactionManager)
        .build();
  }

  @Bean
  @JobScope
  private Step writeToCsvStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("", jobRepository)
        .tasklet((contribution, chunkContext) -> null, transactionManager)
        .build();
  }
}
