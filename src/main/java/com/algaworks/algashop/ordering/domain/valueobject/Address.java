package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.FieldValidation;
import lombok.Builder;

import java.util.Objects;

public record Address(
  String street,
  String number,
  String complement,
  String neighborhood,
  String city,
  String state,
  ZipCode zipCode
) {

  public static final String ANONYMOUS_NUMBER = "Anon";

  @Builder(toBuilder = true)
  public Address {
    FieldValidation.requireNonBlank(street);
    FieldValidation.requireNonBlank(number);
    FieldValidation.requireNonBlank(neighborhood);
    FieldValidation.requireNonBlank(city);
    FieldValidation.requireNonBlank(state);
    Objects.requireNonNull(zipCode);
  }

  public Address anonymized() {
    return this.toBuilder()
      .number(ANONYMOUS_NUMBER)
      .complement(null)
      .build();
  }
}
