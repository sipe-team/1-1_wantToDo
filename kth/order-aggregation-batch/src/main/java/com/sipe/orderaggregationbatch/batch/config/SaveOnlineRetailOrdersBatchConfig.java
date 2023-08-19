package com.sipe.orderaggregationbatch.batch.config;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderAggregationByCustomer;
import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderDto;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import com.sipe.orderaggregationbatch.batch.rowmapper.OnlineRetailOrderRowMapper;
import com.sipe.orderaggregationbatch.batch.step.ItemFailureLoggerListener;
import com.sipe.orderaggregationbatch.batch.step.processor.OnlineRetailOrderAggregateProcessor;
import com.sipe.orderaggregationbatch.batch.step.reader.JpaOnlineRetailOrderListReader;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
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
public class SaveOnlineRetailOrdersBatchConfig {

  private final EntityManagerFactory entityManagerFactory;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final ItemFailureLoggerListener itemFailureLoggerListener;
  private final JpaOnlineRetailOrderListReader jpaOnlineRetailOrderListReader;

  @Bean
  public Job processOnlineRetailOrderFileJob() {
    return new JobBuilder("saveOnlineRetailOrdersJob", jobRepository)
        .incrementer(new RunIdIncrementer()) // TODO: Incrementer 수정
        .start(writeToDbStep())
        .next(aggregateOnlineRetailOrderByCustomerIds())
        .build();
  }

  @Bean
  @JobScope
  public Step writeToDbStep() {
    return new StepBuilder("writeToDbStep", jobRepository)
        .<OnlineRetailOrderDto, OnlineRetailOrder>chunk(100, transactionManager)
        .reader(onlineRetailOrderExcelReader(null))
        .listener((ItemReadListener<? super OnlineRetailOrderDto>) itemFailureLoggerListener)
        .processor(onlineRetailOrderProcessor(null))
        .listener(
            (ItemProcessListener<? super OnlineRetailOrderDto, ? super OnlineRetailOrder>) itemFailureLoggerListener)
        .writer(onlineRetailOrderDbWriter())
        .listener((ItemWriteListener<? super OnlineRetailOrder>) itemFailureLoggerListener)
        .build();
  }

  @Bean
  @StepScope
  public PoiItemReader<? extends OnlineRetailOrderDto> onlineRetailOrderExcelReader(
      @Value("#{jobParameters[requestDate]}") String requestDate
  ) {
    PoiItemReader<OnlineRetailOrderDto> reader = new PoiItemReader<>();
    reader.setName("onlineRetailOrderExcelReader");
    reader.setLinesToSkip(1);
    reader.setResource(new ClassPathResource("online_retail_" + requestDate + ".xlsx"));
    reader.setRowMapper(onlineRetailOrderRowMapper());
    return reader;
  }

  @Bean
  public RowMapper<OnlineRetailOrderDto> onlineRetailOrderRowMapper() {
    return new OnlineRetailOrderRowMapper();
  }

  @Bean
  @StepScope
  public ItemProcessor<? super OnlineRetailOrderDto, ? extends OnlineRetailOrder> onlineRetailOrderProcessor(
      @Value("#{jobParameters[requestDate]}") String requestDate
  ) {
    return new ItemProcessor<OnlineRetailOrderDto, OnlineRetailOrder>() {
      @Override
      public OnlineRetailOrder process(OnlineRetailOrderDto item) throws Exception {
        if (isRequiredFieldNull(item)) {
          log.info("Skipped item: {}", item);
          return null;
        }
        return OnlineRetailOrder.create(item.getInvoiceNo(),
                                        item.getStockCode(),
                                        item.getDescription(),
                                        item.getQuantity(),
                                        item.getInvoiceDate(),
                                        item.getUnitPrice(),
                                        item.getCustomerId(),
                                        item.getCountry(),
                                        requestDate);
      }
    };
  }

  private boolean isRequiredFieldNull(OnlineRetailOrderDto order) {
    return Strings.isNullOrEmpty(order.getInvoiceNo()) ||
        Strings.isNullOrEmpty(order.getStockCode()) ||
        order.getQuantity() == null ||
        order.getUnitPrice() == null;
  }

  private JpaItemWriter<? super OnlineRetailOrder> onlineRetailOrderDbWriter() { // TODO: Batch insert
    JpaItemWriter<OnlineRetailOrder> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    jpaItemWriter.setUsePersist(true);
    return jpaItemWriter;
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
