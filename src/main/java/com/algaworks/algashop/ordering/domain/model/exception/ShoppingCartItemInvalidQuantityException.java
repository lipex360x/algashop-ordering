package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;

public class ShoppingCartItemInvalidQuantityException extends DomainException {

  public ShoppingCartItemInvalidQuantityException(ShoppingCartItemId id) {
    super(String.format(ErrorMessages.VALIDATION_SHOPPING_CART_ITEM_QUANTITY, id));

  }
}
