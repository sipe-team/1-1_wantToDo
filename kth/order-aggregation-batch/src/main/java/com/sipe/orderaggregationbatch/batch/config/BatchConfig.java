package com.sipe.orderaggregationbatch.batch.config;

import com.sipe.orderaggregationbatch.batch.itemprocessor.OrderItemProcessor;
import com.sipe.orderaggregationbatch.batch.itemreader.OrderItemReader;
import com.sipe.orderaggregationbatch.batch.itemwriter.OrderItemWriter;
import com.sipe.orderaggregationbatch.domain.order.Order;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final OrderItemReader orderItemReader;

  private final OrderItemProcessor orderItemProcessor;

  private final OrderItemWriter orderItemWriter;

  @Bean
  public Job orderAggregationJob(
      JobRepository jobRepository, Step step1
  ) {
    return new JobBuilder("orderAggregationJob", jobRepository).start(step1)
                                                               .build();
  }

  @Bean
  public Step step1(
      JobRepository jobRepository, PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("orderAggregationStep1",
                           jobRepository).<Order, Map<Long, BigDecimal>>chunk(10)
                                         .transactionManager(transactionManager)
                                         .reader(orderItemReader)
                                         .processor(orderItemProcessor)
                                         .writer(orderItemWriter)
                                         .build();

  }

}
