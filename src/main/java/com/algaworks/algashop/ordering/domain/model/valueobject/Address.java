package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.validator.FieldValidation;
import lombok.Builder;
import lombok.NonNull;

import java.util.Objects;

public record Address(
  @NonNull String street,
  @NonNull String number,
  String complement,
  @NonNull String neighborhood,
  @NonNull String city,
  @NonNull String state,
  @NonNull ZipCode zipCode
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
