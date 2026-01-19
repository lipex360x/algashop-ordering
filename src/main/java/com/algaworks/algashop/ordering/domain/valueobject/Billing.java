package com.algaworks.algashop.ordering.domain.valueobject;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record Billing(
  @NonNull Recipient recipient,
  @NonNull Address address,
  @NonNull Email email
) {
  public Billing {}
}
