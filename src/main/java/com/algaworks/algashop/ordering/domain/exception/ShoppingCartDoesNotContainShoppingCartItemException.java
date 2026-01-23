package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import lombok.NonNull;

public class ShoppingCartDoesNotContainShoppingCartItemException extends DomainException {

  public ShoppingCartDoesNotContainShoppingCartItemException(
    @NonNull ShoppingCartId id,
    @NonNull ShoppingCartItemId itemId
  ) {
    super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_NOT_FOUND, id, itemId));
  }
}
