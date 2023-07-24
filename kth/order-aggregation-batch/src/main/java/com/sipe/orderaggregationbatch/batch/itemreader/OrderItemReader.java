package com.sipe.orderaggregationbatch.batch.itemreader;

import com.sipe.orderaggregationbatch.domain.order.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemReader implements ItemReader<Order> {
  private static final String SELECT_ORDERS_SQL = "SELECT orderId, customerId, orderAmount FROM order";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private int currentRow = 0;

  @Override
  public Order read() {
    List<Order> orders = jdbcTemplate.query(SELECT_ORDERS_SQL, new BeanPropertyRowMapper<>(Order.class));
    return currentRow < orders.size() ? orders.get(currentRow++) : null;
  }
}
