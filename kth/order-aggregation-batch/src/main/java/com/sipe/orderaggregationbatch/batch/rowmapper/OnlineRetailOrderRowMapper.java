package com.sipe.orderaggregationbatch.batch.rowmapper;

import com.sipe.orderaggregationbatch.batch.dto.OnlineRetailOrderDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.assertj.core.util.Strings;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

public class OnlineRetailOrderRowMapper implements RowMapper<OnlineRetailOrderDto> {

  @Override
  public OnlineRetailOrderDto mapRow(RowSet rs) throws Exception {
    OnlineRetailOrderDto order = new OnlineRetailOrderDto();
    String[] currentRow = rs.getCurrentRow();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy H:mm");
    order.setInvoiceNo(Strings.isNullOrEmpty(currentRow[0]) ? null : currentRow[0]);
    order.setStockCode(Strings.isNullOrEmpty(currentRow[1]) ? null : currentRow[1]);
    order.setDescription(Strings.isNullOrEmpty(currentRow[2]) ? null : currentRow[2]);
    order.setQuantity(
        Strings.isNullOrEmpty(currentRow[3]) ? null : Integer.parseInt(currentRow[3]));
    order.setInvoiceDate(
        Strings.isNullOrEmpty(currentRow[4]) ? null
            : LocalDateTime.parse(currentRow[4], formatter));
    order.setUnitPrice(
        Strings.isNullOrEmpty(currentRow[5]) ? null : Float.parseFloat(currentRow[5]));
    order.setCustomerId(
        Strings.isNullOrEmpty(currentRow[6]) ? null : Long.parseLong(currentRow[6]));
    order.setCountry(Strings.isNullOrEmpty(currentRow[7]) ? null : currentRow[7]);
    return order;
  }
}
