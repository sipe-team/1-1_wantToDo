package com.sipe.orderaggregationbatch.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnlineRetailOrder {

  @Id
  @Column(name = "invoice_no")
  private String invoiceNo;

  @Column(name = "stock_code")
  private String stockCode;

  @Column(name = "description")
  private String description;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "invoice_date")
  private LocalDateTime invoiceDate;

  @Column(name = "unit_price")
  private Float unitPrice;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "country")
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
