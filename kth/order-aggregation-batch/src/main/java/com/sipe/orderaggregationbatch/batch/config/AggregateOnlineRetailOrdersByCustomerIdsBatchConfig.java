package com.sipe.orderaggregationbatch.batch.config;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderAggregationByCustomer;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import com.sipe.orderaggregationbatch.batch.step.processor.OnlineRetailOrderAggregateProcessor;
import com.sipe.orderaggregationbatch.batch.step.reader.JpaOnlineRetailOrderListReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AggregateOnlineRetailOrdersByCustomerIdsBatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final JpaOnlineRetailOrderListReader jpaOnlineRetailOrderListReader;

  @Bean
  public Job aggregateOnlineRetailOrdersByCustomerIdsJob() {
    return new JobBuilder("aggregateOnlineRetailOrdersByCustomerIdsJob", jobRepository)
        .incrementer(new RunIdIncrementer()) // TODO: Incrementer 수정
        .start(aggregateOnlineRetailOrderByCustomerIds())
        .build();
  }

  @Bean
  @JobScope
  public Step aggregateOnlineRetailOrderByCustomerIds() {
    return new StepBuilder("aggregateOnlineRetailOrderByCustomerIds", jobRepository)
        .<List<OnlineRetailOrder>, OnlineRetailOrderAggregationByCustomer>chunk(100,
                                                                                transactionManager)
        .reader(jpaOnlineRetailOrderListReader)
        .listener(jpaOnlineRetailOrderListReader)
        .processor(onlineRetailOrderAggregateProcessor())
        .writer(flatFileOnlineRetailOrderAggregationWriter(null))
        .build();
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<OnlineRetailOrderAggregationByCustomer> flatFileOnlineRetailOrderAggregationWriter(
      @Value("#{jobParameters[requestDate]}") String requestDate
  ) {
    FlatFileHeaderCallback headerCallback = writer -> writer.write(
        "customerId,totalOrderCount,totalOrderPrice,atCountries");

    return new FlatFileItemWriterBuilder<OnlineRetailOrderAggregationByCustomer>()
        .name("flatFileOnlineRetailOrderAggregationWriter")
        .resource(new FileSystemResource(
            new ClassPathResource("online_retail_aggregation_" + requestDate + ".csv")
                .getPath()))
        .headerCallback(headerCallback)
        .delimited()
        .delimiter(",")
        .names(new String[]{"customerId", "totalOrderCount", "totalOrderPrice", "atCountries"})
        .build();
  }

  @Bean
  @StepScope
  public ItemProcessor<List<OnlineRetailOrder>, OnlineRetailOrderAggregationByCustomer> onlineRetailOrderAggregateProcessor() {
    return new OnlineRetailOrderAggregateProcessor();
  }
}
