package com.sipe.orderaggregationbatch.batch.config;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderDto;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import com.sipe.orderaggregationbatch.batch.rowmapper.OnlineRetailOrderRowMapper;
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
import org.springframework.batch.item.Chunk;
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
@Slf4j
public class BatchConfig {

  @Bean
  public Job processOnlineRetailOrderFileJob(
      JobRepository jobRepository,
      Step writeToDbStep,
      Step writeToCsvStep
  ) {
    return new JobBuilder("processOnlineRetailOrderFileJob", jobRepository)
        .incrementer(new RunIdIncrementer())
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
        .reader(onlineRetailOrderExcelReaderV2())
        .processor(onlineRetailOrderProcessor())
        .writer(onlineRetailOrderDbWriter())
        .build();
  }

  private ItemReader<? extends OnlineRetailOrderDto> onlineRetailOrderExcelReader() {
    return new FlatFileItemReaderBuilder<OnlineRetailOrderDto>()
        .name("onlineRetailOrderExcelReader")
        .resource(new ClassPathResource("Online_Retail.xlsx"))
        .delimited()
        .names(new String[]{"InvoiceNo", "StockCode", "Description", "Quantity", "InvoiceDate",
                            "UnitPrice", "CustomerID", "Country"})
        .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
          setTargetType(OnlineRetailOrderDto.class);
        }})
        .build();
  }

  @Bean
  @StepScope
  public PoiItemReader<? extends OnlineRetailOrderDto> onlineRetailOrderExcelReaderV2() {
    PoiItemReader<OnlineRetailOrderDto> reader = new PoiItemReader<>();
    reader.setName("onlineRetailOrderExcelReader");
    reader.setLinesToSkip(1);
    reader.setResource(new ClassPathResource("Online_Retail.xlsx"));
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
        return new OnlineRetailOrder(item.getInvoiceNo(),
                                     item.getStockCode(), item.getDescription(), item.getQuantity(),
                                     item.getInvoiceDate(), item.getUnitPrice(),
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

  private ItemWriter<? super OnlineRetailOrder> onlineRetailOrderDbWriter() {
    return new ItemWriter<OnlineRetailOrder>() {
      @Override
      public void write(Chunk<? extends OnlineRetailOrder> chunk) throws Exception {
        log.info(chunk.toString());
      }
    }; // TODO: 구현 예정
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
