package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import org.springframework.lang.NonNull;

import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {

  public static final Quantity ZERO = new Quantity(0);

  public Quantity {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    if (value < 0) throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NEGATIVE);
  }

  public Quantity add(Quantity quantity) {
    Objects.requireNonNull(quantity, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    return new Quantity(this.value + quantity.value());
  }

  @Override
  public int compareTo(Quantity o) {
    return this.value.compareTo(o.value);
  }

  @NonNull
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
