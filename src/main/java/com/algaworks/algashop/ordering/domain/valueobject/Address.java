package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.FieldValidation;
import lombok.Builder;

import java.util.Objects;

public record Address(
  String number,
  String street,
  String complement,
  String neighborhood,
  String city,
  String state,
  ZipCode zipCode
) {

  @Builder(toBuilder = true)
  public Address {
    FieldValidation.requireNonBlank(number);
    FieldValidation.requireNonBlank(street);
    FieldValidation.requireNonBlank(neighborhood);
    FieldValidation.requireNonBlank(city);
    FieldValidation.requireNonBlank(state);
    Objects.requireNonNull(zipCode);
  }
}
