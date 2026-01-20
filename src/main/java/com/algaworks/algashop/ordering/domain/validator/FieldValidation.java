package com.algaworks.algashop.ordering.domain.validator;

import java.time.LocalDate;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.*;

public class FieldValidation {

  private FieldValidation() {}

  public static void requireNonBlank(String value) {
    requireNonBlank(value, VALIDATION_ERROR_VALUE_IS_BLANK);
  }

  public static void requireNonBlank(String value, String errorMessage) {
    Objects.requireNonNull(value, VALIDATION_ERROR_VALUE_IS_NULL);
    if (value.isBlank()) throw new IllegalArgumentException(errorMessage);
  }

  public static void requiresDateInPast(LocalDate date) {
    requiresDateInPast(date, VALIDATION_ERROR_BIRTHDATE_IS_IN_THE_FUTURE);
  }

  public static void requiresDateInPast(LocalDate date, String errorMessage) {
    Objects.requireNonNull(date, VALIDATION_ERROR_VALUE_IS_NULL);
    if (date.isAfter(LocalDate.now())) throw new IllegalArgumentException(errorMessage);
  }
}
