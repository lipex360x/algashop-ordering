package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.VALIDATION_ERROR_EMAIL_IS_INVALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EmailTest {

  @Test
  void shouldThrowExceptionForNullValue() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Email(null))
      .withMessage(VALIDATION_ERROR_EMAIL_IS_INVALID);
  }

  @Test
  void shouldThrowExceptionForBlankValue() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Email(""))
      .withMessage(VALIDATION_ERROR_EMAIL_IS_INVALID);
  }

  @Test
  void shouldThrowExceptionForInvalidEmailFormat() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Email("invalid-email-format"))
      .withMessage(VALIDATION_ERROR_EMAIL_IS_INVALID);
  }

  @Test
  void shouldCreateEmail() {
    Email email = new Email("john@mail.com");
    assertThat(email.value()).hasToString("john@mail.com");
  }

  @Test
  void shouldReturnToStringCorrectly() {
    Email email = new Email("john@mail.com");
    assertThat(email.toString()).hasToString("john@mail.com");
  }

}