package com.sipe.orderaggregationbatch.batch.entity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OnlineRetailOrderRepository extends JpaRepository<OnlineRetailOrder, Long> {

  @Query("SELECT o.invoiceNo "
      +  "FROM OnlineRetailOrder o "
      +  "GROUP BY o.invoiceNo")
  List<String> findAllInvoiceNo();

  @Query("SELECT o "
      +  "FROM OnlineRetailOrder o "
      +  "WHERE o.invoiceNo = :invoiceNo")
  List<OnlineRetailOrder> findByInvoiceNumbers(@Param("invoiceNo") String invoiceNo);
}
