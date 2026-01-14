package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;

import java.util.Objects;

public record FullName(String firstName, String lastName) {

  public static final FullName ANONYMOUS = new FullName("Anonymous", "Anonymous");

  public FullName(String firstName, String lastName) {
    Objects.requireNonNull(firstName, ErrorMessages.VALIDATION_ERROR_FIRST_NAME_IS_NULL);
    Objects.requireNonNull(lastName, ErrorMessages.VALIDATION_ERROR_LAST_NAME_IS_NULL);
    if (firstName.isBlank()) throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_FIRST_NAME_IS_BLANK);
    if (lastName.isBlank()) throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_LAST_NAME_IS_BLANK);
    this.firstName = firstName.trim();
    this.lastName = lastName.trim();
  }

  @Override
  public String toString() {
    return  firstName + ' ' + lastName;
  }
}
