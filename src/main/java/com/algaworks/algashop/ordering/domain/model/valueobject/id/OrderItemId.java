package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import org.springframework.lang.NonNull;

import java.util.Objects;

public record OrderItemId(TSID value) {

  public OrderItemId {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

  public OrderItemId() {
    this(IdGenerator.generateTSID());
  }

  public OrderItemId(Long value) {
    this(TSID.from(value));
  }

  public OrderItemId(String value) {
    this(TSID.from(value));
  }

  @Override
  @NonNull
  public String toString() {
    return value.toString();
  }
}
