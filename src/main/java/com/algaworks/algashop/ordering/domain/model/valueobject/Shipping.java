package com.algaworks.algashop.ordering.domain.model.valueobject;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record Shipping(
  @NonNull Recipient recipient,
  @NonNull Address address,
  @NonNull Money cost,
  @NonNull LocalDate expectedDate
) {

}
