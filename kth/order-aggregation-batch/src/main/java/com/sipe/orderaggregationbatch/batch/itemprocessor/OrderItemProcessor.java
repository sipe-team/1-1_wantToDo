package com.sipe.orderaggregationbatch.batch.itemprocessor;

import com.sipe.orderaggregationbatch.domain.order.Order;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrderItemProcessor implements ItemProcessor<Order, Map<Long, BigDecimal>> {

  @Override
  public Map<Long, BigDecimal> process(Order order) throws Exception {
    Map<Long, BigDecimal> customerOrderTotals = new HashMap<>();

    if (order != null) {
      Long customerId = order.getCustomerId();
      BigDecimal orderAmount = order.getOrderAmount();

      updateOrAdd(customerOrderTotals, customerId, orderAmount);
    }

    return customerOrderTotals;
  }

  private static void updateOrAdd(
      Map<Long, BigDecimal> customerOrderTotals, Long customerId, BigDecimal orderAmount
  ) {
    if (customerOrderTotals.containsKey(customerId)) {
      BigDecimal totalAmount = customerOrderTotals.get(customerId)
                                                  .add(orderAmount);
      customerOrderTotals.put(customerId, totalAmount);
    }
    else {
      customerOrderTotals.put(customerId, orderAmount);
    }
  }
}
