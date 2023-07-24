package com.sipe.orderaggregationbatch.batch.itemwriter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemWriter implements ItemWriter<Map<Long, BigDecimal>> {

  @Override
  public void write(Chunk<? extends Map<Long, BigDecimal>> chunk) throws Exception {
    String result = chunk.getItems()
                          .stream()
                          .flatMap(revenueByOrderId -> revenueByOrderId.entrySet()
                                                                       .stream())
                          .map(e -> "Customer ID: " + e.getKey() + ", Total Order Amount: "
                              + e.getValue())
                          .collect(Collectors.joining("\n"));
    System.out.println(result);
  }
}
