package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.validator.annotation.NonBlank;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Builder
public record Product(
  @NonNull ProductId id,
  @NonBlank ProductName name,
  @NonNull Money price,
  @NonNull Boolean inStock
) {

  public void checkOutOfStock() {
    if (isOutOfStock()) throw new ProductOutOfStockException(this.id());
  }

  private boolean isOutOfStock() {
    return !inStock();
  }
}
