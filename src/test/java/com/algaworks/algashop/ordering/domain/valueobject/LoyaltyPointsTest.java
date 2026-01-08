package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LoyaltyPointsTest {

  @Test
  void shouldThrowExceptionForNullValue() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new LoyaltyPoints(null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_IS_NULL);
  }

  @Test
  void shouldThrowExceptionForInvalidValue() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> loyaltyPoints.add(0))
      .withMessage(ErrorMessages.VALIDATION_ERROR_NUMBER_IS_ZERO);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> loyaltyPoints.add(-5))
      .withMessage(ErrorMessages.VALIDATION_ERROR_NUMBER_IS_NEGATIVE);
  }

  @Test
  void shouldCreateLoyaltyPoints() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
    assertThat(loyaltyPoints.value()).isEqualTo(10);
  }

  @Test
  void shouldAddValue() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
    LoyaltyPoints newLoyaltyPoint = loyaltyPoints.add(5);
    assertThat(newLoyaltyPoint.value()).isEqualTo(15);
  }

}