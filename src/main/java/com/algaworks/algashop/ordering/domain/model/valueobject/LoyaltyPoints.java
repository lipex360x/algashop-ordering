package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;

import java.util.Objects;

public record LoyaltyPoints(Integer value) implements Comparable<LoyaltyPoints> {

  public static final LoyaltyPoints ZERO = new LoyaltyPoints(0);

  public LoyaltyPoints() {
    this(0);
  }

  public LoyaltyPoints(Integer value) {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_IS_NULL);
    if (value < 0) throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_NUMBER_IS_NEGATIVE);
    this.value = value;
  }

  public LoyaltyPoints add(Integer value) {
    return add(new LoyaltyPoints(value));
  }

  public LoyaltyPoints add(LoyaltyPoints loyaltyPoints) {
    Objects.requireNonNull(loyaltyPoints);
    if (loyaltyPoints.value() <= 0) throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_NUMBER_IS_ZERO);
    return new LoyaltyPoints(this.value() + loyaltyPoints.value());
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public int compareTo(LoyaltyPoints o) {
    return this.value().compareTo(o.value());
  }
}