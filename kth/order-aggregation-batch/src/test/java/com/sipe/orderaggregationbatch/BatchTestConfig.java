package com.sipe.orderaggregationbatch;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.format.DateTimeFormatter;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@ComponentScan(basePackages = "com.sipe.orderaggregationbatch")
public class BatchTestConfig {

  public static final DateTimeFormatter FORMATTER = ofPattern("yyyyMMdd");

}
