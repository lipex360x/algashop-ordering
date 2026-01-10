package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.validator.FieldValidation;
import org.springframework.lang.NonNull;

import java.util.Objects;

public record ProductName(String value) {

  public ProductName(String value) {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    FieldValidation.requireNonBlank(value);
    this.value = value.trim();
  }

  @NonNull
  @Override
  public String toString() {
    return value;
  }
}
