package com.algaworks.algashop.ordering.domain.valueobject.id;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import org.springframework.lang.NonNull;

import java.util.Objects;

public record OrderId(TSID value) {

  public OrderId {
    Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

  public OrderId() {
    this(IdGenerator.generateTSID());
  }

  public OrderId(Long value) {
    this(TSID.from(value));
  }

  public OrderId(String value) {
    this(TSID.from(value));
  }

  @Override
  @NonNull
  public String toString() {
    return value.toString();
  }
}
