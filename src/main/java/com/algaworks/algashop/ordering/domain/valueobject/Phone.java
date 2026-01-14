package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;

import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.VALIDATION_ERROR_PHONE_IS_BLANK;

public record Phone(String value) {

  public static final Phone ANONYMOUS = new Phone("000-000-0000");

  public Phone(String value) {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_PHONE_IS_NULL);
    if (value.isBlank()) throw new IllegalArgumentException(VALIDATION_ERROR_PHONE_IS_BLANK);
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
