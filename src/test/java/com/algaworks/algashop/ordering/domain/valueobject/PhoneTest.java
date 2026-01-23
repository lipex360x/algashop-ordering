package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PhoneTest {

  @Test
  void shouldThrowExceptionForNullValue() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Phone(null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_PHONE_IS_NULL);
  }

  @Test
  void shouldThrowExceptionForBlankValue() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Phone(""))
      .withMessage(ErrorMessages.VALIDATION_ERROR_PHONE_IS_BLANK);
  }

  @Test
  void shouldCreatePhone() {
    Phone phone = new Phone("333-333-3333");
    assertThat(phone.value()).hasToString("333-333-3333");
  }

  @Test
  void shouldReturnToStringCorrectly() {
    Phone phone = new Phone("333-333-3333");
    assertThat(phone.toString()).hasToString("333-333-3333");
  }
}