package com.sipe.orderaggregationbatch.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OnlineRetailOrder {

  @Id
  private String invoiceNo;

  private String stockCode;

  private String description;

  private Integer quantity;

  private LocalDateTime invoiceDate;

  private Float unitPrice;

  private Long customerId;

  private String country;

  public OnlineRetailOrder(
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
}
