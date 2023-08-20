package com.sipe.orderaggregationbatch.batch.config;

import com.sipe.orderaggregationbatch.BatchTestConfig;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrderRepository;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {SaveOnlineRetailOrdersFromFlatFileBatchConfig.class, BatchTestConfig.class})
public class SaveOnlineRetailOrdersFromFlatFileBatchConfigTest {

  @Autowired
  private OnlineRetailOrderRepository onlineRetailOrderRepository;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  private LocalDate requestDate = LocalDate.of(2022, 9, 16);

  @AfterEach
  void tearDown() {
    onlineRetailOrderRepository.deleteAllInBatch();
  }

  @Test
  void 날짜_별_파일을_validation_후_저장한다() throws Exception {
    // given
    JobParameters jobParameters = new JobParametersBuilder(
        jobLauncherTestUtils.getUniqueJobParameters())
        .addString("requestDate", requestDate.format(BatchTestConfig.FORMATTER))
        .toJobParameters();

    //when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // then
    Assertions.assertThat(jobExecution.getStatus())
              .isEqualTo(BatchStatus.COMPLETED);
    List<OnlineRetailOrder> onlineRetailOrders = onlineRetailOrderRepository.findAll();
    Assertions.assertThat(onlineRetailOrders)
              .hasSize(2);
  }
}
