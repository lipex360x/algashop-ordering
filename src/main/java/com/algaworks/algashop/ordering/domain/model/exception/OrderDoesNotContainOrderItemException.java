package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import lombok.NonNull;

public class OrderDoesNotContainOrderItemException extends DomainException {

  public OrderDoesNotContainOrderItemException(OrderId id, @NonNull OrderItemId orderItemId) {
    super(String.format(ErrorMessages.ERROR_ORDER_ITEM_NOT_FOUND, id, orderItemId));
  }
}
