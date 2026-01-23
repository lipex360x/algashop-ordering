package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderDoesNotContainOrderItemException;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertWith;

class OrderRemoveItemTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldRemoveOrderItem() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    Product product1 = ProductDataBuilder.builder()
      .withPrice(() -> new Money("10"))
      .build();

    Product product2 = ProductDataBuilder.builder()
      .withPrice(() -> new Money("10"))
      .build();

    order.addItem(
      product1,
      new Quantity(1)
    );

    order.addItem(
      product2,
      new Quantity(2)
    );

    assertWith(order,
      o -> assertThat(o.totalAmount()).isEqualTo(new Money("30")),
      o -> assertThat(o.totalItems()).isEqualTo(new Quantity(3))
    );

    OrderItem orderItem = order.items().stream()
      .filter(item -> item.productId().equals(product2.id()))
      .findFirst()
      .orElseThrow();

    order.removeItem(orderItem.id());

    assertWith(order,
      o -> assertThat(o.totalAmount()).isEqualTo(new Money("10")),
      o -> assertThat(o.totalItems()).isEqualTo(new Quantity(1))
    );
  }

  @Test
  void shouldThrowExceptionWhenRemoveAnUnexistentOrderItem() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();
    OrderItemId orderItemId = new OrderItemId();

    ThrowableAssert.ThrowingCallable removeItemTask =
      () -> order.removeItem(orderItemId);

    assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class)
      .isThrownBy(removeItemTask)
      .withMessage(String.format(ErrorMessages.ERROR_ORDER_ITEM_NOT_FOUND, order.id(), orderItemId));
  }

  @Test
  void shouldThrowExceptionWhenRemoveOrderItemForANonDraftOrder() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    ThrowableAssert.ThrowingCallable removeItemTask =
      () -> order.removeItem(new OrderItemId());

    assertThatExceptionOfType(OrderCannotBeEditedException.class)
      .isThrownBy(removeItemTask)
      .withMessage(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, order.id(), order.status()));
  }
}








































