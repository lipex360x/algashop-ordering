package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class QuantityTest {

  @Test
  void shouldThrowExceptionWhenValueIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Quantity(null));
  }

  @Test
  void shouldThrowExceptionWhenValueIsNegative() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Quantity(-1));
  }

  @Test
  void shouldReturnQuantityTotalWhenSummingTwoValues() {
    Quantity quantity1 = new Quantity(2);
    Quantity quantity2 = new Quantity(4);

    Quantity result = quantity1.add(quantity2);
    assertThat(result.value()).isEqualTo(6);
  }

  @Test
  void shouldReturnMinusOneWhenComparingSmallerValueToBiggerValue() {
    Quantity smallerValue = new Quantity(1);
    Quantity biggerValue = new Quantity(2);

    Integer comparison = smallerValue.compareTo(biggerValue);

    assertThat(comparison).isNegative();
  }

  @Test
  void shouldReturnZeroWhenComparingTwoEqualValues() {
    Quantity quantity1 = new Quantity(5);
    Quantity quantity2 = new Quantity(5);

    Integer comparison = quantity1.compareTo(quantity2);

    assertThat(comparison).isZero();
  }

  @Test
  void shouldReturnPlusOneWhenComparingBiggerValueToSmallerValue() {
    Quantity smallerValue = new Quantity(2);
    Quantity biggerValue = new Quantity(3);

    Integer comparison = biggerValue.compareTo(smallerValue);

    assertThat(comparison).isPositive();
  }

  @Test
  void shouldBeEqualAndHaveSameHashCodeWhenValuesAreEqual() {
    Quantity quantity1 = new Quantity(10);
    Quantity quantity2 = new Quantity(10);

    assertThat(quantity1).isEqualTo(quantity2);
    assertThat(quantity1.hashCode()).hasSameHashCodeAs(quantity2.hashCode());
  }

  @Test
  void shouldNotBeEqualAndHaveDifferentHashCodeWhenValuesAreDifferent() {
    Quantity quantity1 = new Quantity(10);
    Quantity quantity2 = new Quantity(11);

    assertThat(quantity1).isNotEqualTo(quantity2);
    assertThat(quantity1.hashCode()).isNotEqualTo(quantity2.hashCode());
  }

  @Test
  void shouldReturnToStringCorrectly() {
    Quantity quantity = new Quantity(7);

    assertThat(quantity.toString()).hasToString("7");
  }

}