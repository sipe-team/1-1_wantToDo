package com.sipe.orderaggregationbatch.batch.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OnlineRetailOrderDto {

  private String invoiceNo; // non-null

  private String stockCode; // non-null

  private String description;

  private Integer quantity; // non-null

  private LocalDateTime invoiceDate;

  private Float unitPrice; // non-null

  private Long customerId;

  private String country;

  public OnlineRetailOrderDto(
      String invoiceNo, String stockCode, String description, Integer quantity,
      LocalDateTime invoiceDate, Float unitPrice, Long customerId, String country
  ) {
    this.invoiceNo = invoiceNo;
    this.stockCode = stockCode;
    this.description = description;
    this.quantity = quantity;
    this.invoiceDate = invoiceDate;
    this.unitPrice = unitPrice;
    this.customerId = customerId;
    this.country = country;
  }

  @Override
  public String toString() {
    return "OnlineRetailOrderDto{" +
        "invoiceNo='" + invoiceNo + '\'' +
        ", stockCode='" + stockCode + '\'' +
        ", description='" + description + '\'' +
        ", quantity=" + quantity +
        ", invoiceDate=" + invoiceDate +
        ", unitPrice=" + unitPrice +
        ", customerId=" + customerId +
        ", country='" + country + '\'' +
        '}';
  }
}
