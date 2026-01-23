package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.validator.FieldValidation;
import org.springframework.lang.NonNull;

public record ProductName(@NonNull String value) {

  public ProductName(String value) {
    FieldValidation.requireNonBlank(value);
    this.value = value.trim();
  }

  @NonNull
  @Override
  public String toString() {
    return value;
  }
}
