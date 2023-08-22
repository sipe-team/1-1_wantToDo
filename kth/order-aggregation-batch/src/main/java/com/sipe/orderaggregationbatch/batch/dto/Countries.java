package com.sipe.orderaggregationbatch.batch.dto;

import java.util.List;
import java.util.stream.Collectors;

public class Countries {
  private List<String> values;

  public Countries(List<String> values) {
    this.values = values;
  }

  @Override
  public String toString() {
    return values.stream()
                 .collect(Collectors.joining(" | "));
  }
}
