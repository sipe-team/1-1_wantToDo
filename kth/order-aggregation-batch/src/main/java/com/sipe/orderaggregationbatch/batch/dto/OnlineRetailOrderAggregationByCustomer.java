package com.sipe.orderaggregationbatch.batch.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

@Getter
public class OnlineRetailOrderAggregationByCustomer {

  private Long customerId;

  private Integer totalOrderCount;

  private BigDecimal totalOrderPrice;

  private Countries atCountries;

  public OnlineRetailOrderAggregationByCustomer(
      Long customerId, Integer totalOrderCount, BigDecimal totalOrderPrice, List<String> atCountries
  ) {
    this.customerId = customerId;
    this.totalOrderCount = totalOrderCount;
    this.totalOrderPrice = totalOrderPrice;
    this.atCountries = new Countries(atCountries);
  }
}
