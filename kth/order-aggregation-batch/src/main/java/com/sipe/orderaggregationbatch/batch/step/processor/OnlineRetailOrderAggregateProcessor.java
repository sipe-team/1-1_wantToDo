package com.sipe.orderaggregationbatch.batch.step.processor;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderAggregationByCustomer;
import com.sipe.orderaggregationbatch.batch.entity.OnlineRetailOrder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.item.ItemProcessor;

public class OnlineRetailOrderAggregateProcessor implements ItemProcessor<List<OnlineRetailOrder>, OnlineRetailOrderAggregationByCustomer> {

  @Override
  public OnlineRetailOrderAggregationByCustomer process(List<OnlineRetailOrder> items) throws Exception {
    if (items.isEmpty()) {
      return null;
    }
    Long customerId = items.get(0)
                           .getCustomerId();
    int totalOrderCount = items.size();
    BigDecimal totalOrderPrice = items.stream()
                                      .map(OnlineRetailOrder::getTotalPrice)
                                      .reduce((b1, b2) -> b1.add(b2))
                                      .orElse(BigDecimal.ZERO)
                                      .setScale(1, RoundingMode.CEILING);
    List<String> atCountries = items.stream()
                                    .map(OnlineRetailOrder::getCountry)
                                    .distinct()
                                    .collect(Collectors.toList());
    return new OnlineRetailOrderAggregationByCustomer(customerId,
                                                      totalOrderCount,
                                                      totalOrderPrice,
                                                      atCountries);
  }
}
