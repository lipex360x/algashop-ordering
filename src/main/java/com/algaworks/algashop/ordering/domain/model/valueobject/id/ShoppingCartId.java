package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import org.springframework.lang.NonNull;

import java.util.Objects;

public record ShoppingCartId(TSID value) {

  public ShoppingCartId {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

  public ShoppingCartId() {
    this(IdGenerator.generateTSID());
  }

  public ShoppingCartId(Long value) {
    this(TSID.from(value));
  }

  public ShoppingCartId(String value) {
    this(TSID.from(value));
  }

  @Override
  @NonNull
  public String toString() {
    return value.toString();
  }
}
