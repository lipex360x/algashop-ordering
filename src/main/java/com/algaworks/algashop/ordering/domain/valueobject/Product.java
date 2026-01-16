package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Product(
  @NonNull ProductId id,
  @NonNull ProductName name,
  @NonNull Money price,
  @NonNull Boolean inStock
) {

}
