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

  @Query("SELECT o.customerId "
      +  "FROM OnlineRetailOrder o "
      +  "WHERE o.customerId IS NOT null "
      +  "GROUP BY o.customerId")
  List<Long> findAllCustomerIds();

  @Query("SELECT o "
      +  "FROM OnlineRetailOrder o "
      +  "WHERE o.invoiceNo = :invoiceNo")
  List<OnlineRetailOrder> findByInvoiceNo(@Param("invoiceNo") String invoiceNo);

  @Query("SELECT o "
      +  "FROM OnlineRetailOrder o "
      +  "WHERE o.customerId = :customerId")
  List<OnlineRetailOrder> findByCustomerId(@Param("customerId") Long customerId);

  List<OnlineRetailOrder> findAllByCustomerId(Long customerId);

  List<OnlineRetailOrder> findAllByCustomerIdNull();
}
