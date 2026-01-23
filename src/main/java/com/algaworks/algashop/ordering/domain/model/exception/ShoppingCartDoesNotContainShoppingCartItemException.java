package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import lombok.NonNull;

public class ShoppingCartDoesNotContainShoppingCartItemException extends DomainException {

  private ShoppingCartDoesNotContainShoppingCartItemException(
    @NonNull ShoppingCartId id,
    @NonNull ShoppingCartItemId itemId
  ) {
    super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_NOT_FOUND_BY_ITEM_ID, id, itemId));
  }

  private ShoppingCartDoesNotContainShoppingCartItemException(
    @NonNull ShoppingCartId id,
    @NonNull ProductId productId
  ) {
    super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_NOT_FOUND_BY_PRODUCT_ID, id, productId));
  }

  public static ShoppingCartDoesNotContainShoppingCartItemException noItemByItemId(
    @NonNull ShoppingCartId id,
    @NonNull ShoppingCartItemId itemId
  ) {
    return new ShoppingCartDoesNotContainShoppingCartItemException(id, itemId);
  }

  public static ShoppingCartDoesNotContainShoppingCartItemException noItemByProductId(
    @NonNull ShoppingCartId id,
    @NonNull ProductId productId
  ) {
    return new ShoppingCartDoesNotContainShoppingCartItemException(id, productId);
  }

}
