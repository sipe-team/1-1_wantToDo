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
  public void beforeProcess(OnlineRetailOrderDto item) {
    log.info("Start Processing..");
  }

  @Override
  public void afterProcess(OnlineRetailOrderDto item, OnlineRetailOrder result) {
    log.info("End Processing..");
  }

  @Override
  public void onProcessError(OnlineRetailOrderDto item, Exception e) {
    log.error("Encountered error on read: {}", item, e);
  }

  @Override
  public void beforeRead() {
    log.info("Start Reading..");
  }

  @Override
  public void afterRead(OnlineRetailOrderDto item) {
    log.info("End Reading..");
  }

  @Override
  public void onReadError(Exception e) {
    log.error("Encountered error on read", e);  }

  @Override
  public void beforeWrite(Chunk<? extends OnlineRetailOrder> items) {
    log.info("Start Writing..");
  }

  @Override
  public void afterWrite(Chunk<? extends OnlineRetailOrder> items) {
    log.info("End Writing: { size={} }", items.size());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void onWriteError(Exception e, Chunk<? extends OnlineRetailOrder> items) {
    log.error("Encountered error on write", e);
  }
}
