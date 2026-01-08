package com.algaworks.algashop.ordering.domain.validator;

import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.*;

public class FieldValidation {

  private FieldValidation() {}

  public static void requiresValidEmail(String email) {
    requiresValidEmail(email, null);
  }

  public static void requiresValidEmail(String email, String errorMessage) {
    Objects.requireNonNull(email, errorMessage);
    if (email.isBlank()) throw new IllegalArgumentException(errorMessage);
    if (!EmailValidator.getInstance().isValid(email)) throw new IllegalArgumentException(errorMessage);
  }

  public static void requiresValidFullName(FullName fullName) {
    Objects.requireNonNull(fullName, VALIDATION_ERROR_FULLNAME_IS_NULL);
  }

  public static void requiresValidBirthDate(LocalDate birthDate) {
    if (birthDate.isAfter(LocalDate.now())) throw new IllegalArgumentException(VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
  }

  public static void requiresDateInPast(LocalDate date) {
    requiresDateInPast(date, VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
  }

  public static void requiresDateInPast(LocalDate date, String errorMessage) {
    Objects.requireNonNull(date, errorMessage);
    if (date.isAfter(LocalDate.now())) throw new IllegalArgumentException(errorMessage);
  }

}
