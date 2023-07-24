package com.sipe.orderaggregationbatch.domain.order;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

  private Long orderId;
  private Long customerId;
  private BigDecimal orderAmount;

  public Order(Long orderId, Long customerId, BigDecimal orderAmount) {
    this.orderId = orderId;
    this.customerId = customerId;
    this.orderAmount = orderAmount;
  }
}
