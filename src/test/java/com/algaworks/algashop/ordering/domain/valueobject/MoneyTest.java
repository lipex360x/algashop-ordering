package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MoneyTest {

  @Test
  void shouldThrowExceptionWhenValueIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Money((BigDecimal) null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

  @Test
  void shouldThrowExceptionWhenValueIsNegative() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Money("-19.97"))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_ZERO_OR_NEGATIVE);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Money(BigDecimal.valueOf(-10.0)))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_ZERO_OR_NEGATIVE);
  }

  @Test
  void shouldThrowExceptionWhenMultiplyingMoneyByZeroQuantity() {
    Money money = new Money("10.50");
    Quantity quantity = new Quantity(0);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> money.multiply(quantity))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_ZERO_OR_NEGATIVE);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> money.multiply(Quantity.ZERO))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_ZERO_OR_NEGATIVE);
  }

  @Test
  void shouldThrowExceptionWhenSummingMoneyWithNullQuantity() {
    Money money = new Money("1.97");

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> money.add(null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

  @Test
  void shouldReturnZeroMoney() {
    assertThat(Money.ZERO.value()).isEqualByComparingTo(BigDecimal.ZERO);
  }

  @Test
  void shouldRoundMoneyToTwoDecimalPlaces() {
    Money money = new Money("19.969");
    assertThat(money.value()).isEqualByComparingTo(new BigDecimal("19.97"));
  }

  @Test
  void shouldReturnMoneyTotalWhenSummingTwoValues() {
    Money money1 = new Money("9.97");
    Money money2 = new Money("1.97");
    Money result = money1.add(money2);
    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("11.94"));
  }

  @Test
  void shouldReturnMoneyTotalWhenMultiplyingTwoValues() {
    Money money = new Money("9.97");
    Quantity quantity = new Quantity(2);
    Money result = money.multiply(quantity);
    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("19.94"));
  }

  @Test
  void shouldReturnMoneyTotalWhenDividingTwoValues() {
    Money money1 = new Money("9.97");
    Money money2 = new Money("1.97");
    Money result = money1.divide(money2);
    assertThat(result.value()).isEqualByComparingTo(new BigDecimal("5.06"));
  }

  @Test
  void shouldReturnMinusOneWhenComparingSmallerValueToBiggerValue() {
    Money smallerValue = new Money("1.97");
    Money biggerValue = new Money("2.97");
    Integer comparison = smallerValue.compareTo(biggerValue);
    assertThat(comparison).isNegative();
  }

  @Test
  void shouldReturnZeroWhenComparingTwoEqualValues() {
    Money money1 = new Money("9.97");
    Money money2 = new Money("9.97");
    Integer comparison = money1.compareTo(money2);
    assertThat(comparison).isZero();
  }

  @Test
  void shouldReturnPlusOneWhenComparingBiggerValueToSmallerValue() {
    Money smallerValue = new Money("1.97");
    Money biggerValue = new Money("2.97");
    Integer comparison = biggerValue.compareTo(smallerValue);
    assertThat(comparison).isPositive();
  }

  @Test
  void shouldBeEqualAndHaveSameHashCodeWhenValuesAreEqual() {
    Money money1 = new Money("9.97");
    Money money2 = new Money("9.97");
    assertThat(money1).isEqualTo(money2);
    assertThat(money1.hashCode()).hasSameHashCodeAs(money2.hashCode());
  }

  @Test
  void shouldNotBeEqualAndHaveDifferentHashCodeWhenValuesAreDifferent() {
    Money money1 = new Money("9.97");
    Money money2 = new Money("9.98");
    assertThat(money1).isNotEqualTo(money2);
    assertThat(money1.hashCode()).isNotEqualTo(money2.hashCode());
  }

  @Test
  void shouldReturnToStringCorrectly() {
    Money money = new Money("9.97");
    assertThat(money.toString()).hasToString("9.97");
  }
}