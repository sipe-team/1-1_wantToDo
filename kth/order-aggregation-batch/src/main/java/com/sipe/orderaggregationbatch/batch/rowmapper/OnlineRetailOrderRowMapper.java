package com.sipe.orderaggregationbatch.batch.rowmapper;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderDto;
import java.sql.Timestamp;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

public class OnlineRetailOrderRowMapper implements RowMapper<OnlineRetailOrderDto> {

  @Override
  public OnlineRetailOrderDto mapRow(RowSet rs) throws Exception {
    OnlineRetailOrderDto order = new OnlineRetailOrderDto();
    String[] currentRow = rs.getCurrentRow();
    order.setInvoiceNo(currentRow[0]);
    order.setStockCode(currentRow[1]);
    order.setDescription(currentRow[2]);
    order.setQuantity(Integer.parseInt(currentRow[3]));
    order.setInvoiceDate(currentRow[4] == null ? null : Timestamp.valueOf(currentRow[4]).toLocalDateTime());
    order.setUnitPrice(Float.parseFloat(currentRow[5]));
    order.setCustomerId(Long.parseLong(currentRow[6]));
    order.setCountry(currentRow[7]);
    return order;
  }
}
