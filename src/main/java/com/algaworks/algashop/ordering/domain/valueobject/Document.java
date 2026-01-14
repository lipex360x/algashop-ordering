package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;

import java.util.Objects;

public record Document(String value) {

  public static final Document ANONYMOUS = new Document("000-00-0000");

  public Document(String value) {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_DOCUMENT_IS_NULL);
    if (value.isBlank()) throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_DOCUMENT_IS_BLANK);
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
