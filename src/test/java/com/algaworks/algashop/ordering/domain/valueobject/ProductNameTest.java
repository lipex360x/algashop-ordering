package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ProductNameTest {

  @Test
  void shouldThrowExceptionWhenValueIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new ProductName(null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

  @Test
  void shouldThrowExceptionWhenValueIsBlank() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new ProductName("  "))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_BLANK);
  }

  @Test
  void shouldReturnProductName() {
    ProductName productName = new ProductName("Macbook Pro  ");
    assertThat(productName.value()).hasToString("Macbook Pro");
  }

  @Test
  void shouldReturnToStringCorrectly() {
    ProductName productName = new ProductName("Macbook Pro  ");
    assertThat(productName.toString()).hasToString("Macbook Pro");
  }
}