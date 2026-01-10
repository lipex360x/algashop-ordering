package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
  private static final Integer ROUNDING_SCALE = 2;

  public static final Money ZERO = new Money(BigDecimal.ZERO);

  public Money(String value) {
    this(new BigDecimal(value));
  }

  public Money(BigDecimal value) {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    this.value = value.setScale(ROUNDING_SCALE, ROUNDING_MODE);
    if (this.value.signum() == -1)
      throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_VALUE_IS_ZERO_OR_NEGATIVE);
  }

  public Money add(Money money) {
    Objects.requireNonNull(money, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    BigDecimal totalValue = this.value.add(money.value);
    return new Money(totalValue);
  }

  public Money multiply(Quantity quantity) {
    Objects.requireNonNull(quantity);
    BigDecimal multiplier = new BigDecimal(quantity.value());
    if (multiplier.compareTo(BigDecimal.ZERO) <= 0)
      throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_VALUE_IS_ZERO_OR_NEGATIVE);
    BigDecimal multipliedValue = this.value.multiply(new BigDecimal(quantity.value()));
    return new Money(multipliedValue);
  }

  public Money divide(Money divisor) {
    BigDecimal dividedValue = this.value.divide(divisor.value, ROUNDING_SCALE, ROUNDING_MODE);
    return new Money(dividedValue);
  }

  @Override
  public int compareTo(Money money) {
    return this.value.compareTo(money.value);
  }

  @Override
  @NonNull
  public String toString() {
    return value.toString();
  }
}
