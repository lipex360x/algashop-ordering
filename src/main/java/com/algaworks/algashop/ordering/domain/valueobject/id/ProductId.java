package com.algaworks.algashop.ordering.domain.valueobject.id;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

  public ProductId {
    Objects.requireNonNull(value);
  }

  public ProductId() {
    this(IdGenerator.generateUUID());
  }

  @Override
  @NonNull
  public String toString() {
    return value.toString();
  }
}
