package com.sipe.orderaggregationbatch.batch.step;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderDto;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ItemFailureLoggerListener extends ItemListenerSupport<OnlineRetailOrderDto, OnlineRetailOrder> implements
    StepExecutionListener {

  @Override
  public void onProcessError(OnlineRetailOrderDto item, Exception e) {
    log.error("Encountered error on read: {}", item, e);
  }

  @Override
  public void onReadError(Exception e) {
    log.error("Encountered error on read", e);  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void onWriteError(Exception e, Chunk<? extends OnlineRetailOrder> items) {
    log.error("Encountered error on write", e);
  }
}
