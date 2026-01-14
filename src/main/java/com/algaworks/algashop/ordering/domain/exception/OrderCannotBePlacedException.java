package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBePlacedException extends DomainException {

  public OrderCannotBePlacedException(OrderId orderId, String reason) {
    super(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED, orderId, reason));
  }
}
