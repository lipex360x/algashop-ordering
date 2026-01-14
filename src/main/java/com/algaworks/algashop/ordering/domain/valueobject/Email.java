package com.algaworks.algashop.ordering.domain.valueobject;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.VALIDATION_ERROR_EMAIL_IS_INVALID;

public record Email(String value) {

  public static final Email ANONYMOUS = new Email("anonymous@anonymous.com");

  public Email(String value) {
    Objects.requireNonNull(value, VALIDATION_ERROR_EMAIL_IS_INVALID);
    if (value.isBlank()) throw new IllegalArgumentException(VALIDATION_ERROR_EMAIL_IS_INVALID);
    if (!EmailValidator.getInstance().isValid(value)) throw new IllegalArgumentException(VALIDATION_ERROR_EMAIL_IS_INVALID);
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
