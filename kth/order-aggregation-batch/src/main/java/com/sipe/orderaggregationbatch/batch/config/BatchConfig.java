package com.sipe.orderaggregationbatch.batch.config;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderDto;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig extends DefaultBatchConfiguration {

  private final ItemProcessor itemProcessor; // TODO: 제거 예정

  private final ItemWriter itemWriter; // TODO: 제거 예정

  @Bean
  public Job processOnlineRetailOrderFileJob(
      JobRepository jobRepository,
      Step writeToDbStep,
      Step writeToCsvStep
  ) {
    return new JobBuilder("processOnlineRetailOrderFileJob", jobRepository)
        .start(writeToDbStep)
        .next(writeToCsvStep)
        .build();
  }

  @Bean
  @JobScope
  public Step writeToDbStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("writeToDbStep", jobRepository)
        .<OnlineRetailOrderDto, OnlineRetailOrder>chunk(10, transactionManager)
        .reader(onlineRetailOrderExcelReader())
        .processor(onlineRetailOrderProcessor())
        .writer(onlineRetailOrderDbWriter())
        .build();
  }

  private ItemReader<? extends OnlineRetailOrderDto> onlineRetailOrderExcelReader() {
    return new FlatFileItemReaderBuilder<OnlineRetailOrderDto>()
        .name("onlineRetailOrderExcelReader")
        .resource(new ClassPathResource("Online Retail.xlsx"))
        .delimited()
        .names(new String[]{"InvoiceNo", "StockCode", "Description", "Quantity", "InvoiceDate",
                            "UnitPrice", "CustomerID", "Country"})
        .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
          setTargetType(OnlineRetailOrderDto.class);
        }})
        .build();
  }

  private ItemProcessor<? super OnlineRetailOrderDto, ? extends OnlineRetailOrder> onlineRetailOrderProcessor() {
    return itemProcessor; // TODO: 구현 예정
  }

  private ItemWriter<? super OnlineRetailOrder> onlineRetailOrderDbWriter() {
    return itemWriter; // TODO: 구현 예정
  }

  @Bean
  @JobScope
  public Step writeToCsvStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager
  ) {
    return new StepBuilder("writeToCsvStep", jobRepository)
        .tasklet((contribution, chunkContext) -> null, transactionManager)
        .build();
  }
}
