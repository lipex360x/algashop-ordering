package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import lombok.NonNull;

public class ShoppingCartInvalidItemQuantityException extends DomainException {

  public ShoppingCartInvalidItemQuantityException(ShoppingCartItemId id) {
    super(String.format(ErrorMessages.VALIDATION_SHOPPING_CART_ITEM_QUANTITY, id));

  }
}
