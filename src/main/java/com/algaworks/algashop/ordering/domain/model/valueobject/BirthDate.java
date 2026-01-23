package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.validator.FieldValidation.requiresDateInPast;

public record BirthDate(LocalDate value) {

  public BirthDate(LocalDate value) {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_BIRTHDATE_IS_NULL);
    requiresDateInPast(value);
    this.value = value;
  }

  public Integer age() {
    return Period.between(value, LocalDate.now()).getYears();
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
