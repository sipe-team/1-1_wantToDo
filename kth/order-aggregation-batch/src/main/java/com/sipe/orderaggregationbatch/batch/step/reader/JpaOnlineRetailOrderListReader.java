package com.sipe.orderaggregationbatch.batch.step.reader;

import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrderRepository;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JpaOnlineRetailOrderListReader implements ItemReader<List<OnlineRetailOrder>>, StepExecutionListener {

  private final OnlineRetailOrderRepository onlineRetailOrderRepository;
  private List<Long> customerIds;
  Iterator<Long> iterator;
  private boolean isEnd;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    customerIds = onlineRetailOrderRepository.findAllCustomerIds();
    iterator = customerIds.iterator();
  }

  @Override
  public List<OnlineRetailOrder> read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (iterator.hasNext()) {
      return onlineRetailOrderRepository.findAllByCustomerId(iterator.next());
    }
    if (isEnd) {
      log.info("End JpaOnlineRetailOrderListRead..!");
      return null;
    }

    isEnd = true;
    return onlineRetailOrderRepository.findAllByCustomerIdNull();
  }
}
