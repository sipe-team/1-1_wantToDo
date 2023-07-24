package com.sipe.orderaggregationbatch.domain.order;

import java.math.BigDecimal;

public class OrderRevenue {

  private Long orderId;
  private Long customerId;
  private BigDecimal orderAmount;
  private RevenueStatus revenueStatus;

  public OrderRevenue(
      Long orderId, Long customerId, BigDecimal orderAmount, RevenueStatus revenueStatus
  ) {
    this.orderId = orderId;
    this.customerId = customerId;
    this.orderAmount = orderAmount;
    this.revenueStatus = revenueStatus;
  }
}
