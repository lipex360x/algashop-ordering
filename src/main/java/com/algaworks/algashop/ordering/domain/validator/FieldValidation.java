package com.algaworks.algashop.ordering.domain.validator;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.*;

public class FieldValidation {

  private FieldValidation() {}

  public static void requiresDateInPast(LocalDate date) {
    requiresDateInPast(date, VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
  }

  public static void requiresDateInPast(LocalDate date, String errorMessage) {
    Objects.requireNonNull(date, errorMessage);
    if (date.isAfter(LocalDate.now())) throw new IllegalArgumentException(errorMessage);
  }

}
