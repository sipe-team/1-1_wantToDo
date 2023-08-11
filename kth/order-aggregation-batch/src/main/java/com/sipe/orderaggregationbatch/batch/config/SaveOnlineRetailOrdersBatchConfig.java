package com.sipe.orderaggregationbatch.batch.config;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderDto;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrderRepository;
import com.sipe.orderaggregationbatch.batch.rowmapper.OnlineRetailOrderRowMapper;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SaveOnlineRetailOrdersBatchConfig {

  private final EntityManagerFactory entityManagerFactory;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final OnlineRetailOrderRepository onlineRetailOrderRepository;

  @Bean
  public Job processOnlineRetailOrderFileJob() {
    return new JobBuilder("saveOnlineRetailOrdersJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(writeToDbStep())
        .next(writeToCsvStep())
        .build();
  }

  @Bean
  @JobScope
  public Step writeToDbStep() {
    return new StepBuilder("writeToDbStep", jobRepository)
        .<OnlineRetailOrderDto, OnlineRetailOrder>chunk(10, transactionManager)
        .reader(onlineRetailOrderExcelReaderV2(null))
        .processor(onlineRetailOrderProcessor())
        .writer(onlineRetailOrderDbWriter())
        .build();
  }

//  private ItemReader<? extends OnlineRetailOrderDto> onlineRetailOrderExcelReader() {
//    return new FlatFileItemReaderBuilder<OnlineRetailOrderDto>()
//        .name("onlineRetailOrderExcelReader")
//        .resource(new ClassPathResource("online_retail_20230810.xlsx"))
//        .delimited()
//        .names(new String[]{"InvoiceNo", "StockCode", "Description", "Quantity", "InvoiceDate",
//                            "UnitPrice", "CustomerID", "Country"})
//        .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//          setTargetType(OnlineRetailOrderDto.class);
//        }})
//        .build();
//  }

  @Bean
  @StepScope
  public PoiItemReader<? extends OnlineRetailOrderDto> onlineRetailOrderExcelReaderV2(
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

  private ItemProcessor<? super OnlineRetailOrderDto, ? extends OnlineRetailOrder> onlineRetailOrderProcessor() {
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
                                        item.getCountry());
      }
    };
  }

  private boolean isRequiredFieldNull(OnlineRetailOrderDto order) {
    return Strings.isNullOrEmpty(order.getInvoiceNo()) ||
        Strings.isNullOrEmpty(order.getStockCode()) ||
        order.getQuantity() == null ||
        order.getUnitPrice() == null;
  }

  private JpaItemWriter<? super OnlineRetailOrder> onlineRetailOrderDbWriter() {
    JpaItemWriter<OnlineRetailOrder> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
  }

  @Bean
  @JobScope
  public Step writeToCsvStep() {
    return new StepBuilder("writeToCsvStep", jobRepository)
        .tasklet((contribution, chunkContext) -> null, transactionManager)
        .build();
  }
}
