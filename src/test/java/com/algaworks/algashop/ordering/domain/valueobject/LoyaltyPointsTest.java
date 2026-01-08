package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LoyaltyPointsTest {

  @Test
  void shouldThrowExceptionForInvalidValue() {
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> loyaltyPoints.add(0));
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> loyaltyPoints.add(-5));
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