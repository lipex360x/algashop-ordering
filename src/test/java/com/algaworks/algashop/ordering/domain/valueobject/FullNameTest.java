package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FullNameTest {

  @Test
  void shouldThrowExceptionForNullFirstName() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new FullName(null, "Doe"))
      .withMessage(ErrorMessages.VALIDATION_ERROR_FIRST_NAME_IS_NULL);
  }

  @Test
  void shouldThrowExceptionForNullLastName() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new FullName("John", null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_LAST_NAME_IS_NULL);
  }

  @Test
  void shouldThrowExceptionForBlankFirstName() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new FullName("   ", "Doe"))
      .withMessage(ErrorMessages.VALIDATION_ERROR_FIRST_NAME_IS_BLANK);
  }

  @Test
  void shouldThrowExceptionForBlankLastName() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new FullName("John", "   "))
      .withMessage(ErrorMessages.VALIDATION_ERROR_LAST_NAME_IS_BLANK);
  }

  @Test
  void shouldCreateFullName_trimmingValues() {
    FullName fullName = new FullName("  John  ", "  Doe  ");
    assertThat(fullName.firstName()).hasToString("John");
    assertThat(fullName.lastName()).hasToString("Doe");
  }

  @Test
  void shouldReturnToStringCorrectly() {
    FullName fullName = new FullName("John", "Doe");
    assertThat(fullName.toString()).hasToString("John Doe");
  }
}