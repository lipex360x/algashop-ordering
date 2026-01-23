package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

public class ShoppingCartItemInvalidQuantityException extends DomainException {

  public ShoppingCartItemInvalidQuantityException(ShoppingCartItemId id) {
    super(String.format(ErrorMessages.VALIDATION_SHOPPING_CART_ITEM_QUANTITY, id));

  }
}
