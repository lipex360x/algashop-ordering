package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import lombok.NonNull;

public class ProductOutOfStockException extends DomainException {

  public ProductOutOfStockException(@NonNull ProductId id) {
    super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
  }

}
