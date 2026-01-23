package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.builder.BillingDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.OrderItemDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ShippingDataBuilder;
import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderDraftTest {

  @Test
  void shouldThrowExceptionWhenAddItemForANonDraftOrder() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    Product product = ProductDataBuilder.builder().build();

    ThrowableAssert.ThrowingCallable addItemTask =
      () -> order.addItem(product, new Quantity(2));

    assertThatExceptionOfType(OrderCannotBeEditedException.class)
      .isThrownBy(addItemTask)
      .withMessage(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, order.id(), order.status()));
  }

  @Test
  void shouldThrowExceptionWhenChangeBillingForANonDraftOrder() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    Billing billing = BillingDataBuilder.builder().build();

    ThrowableAssert.ThrowingCallable changeBillingTask =
      () -> order.changeBilling(billing);

    assertThatExceptionOfType(OrderCannotBeEditedException.class)
      .isThrownBy(changeBillingTask)
      .withMessage(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, order.id(), order.status()));
  }

  @Test
  void shouldThrowExceptionWhenChangeShippingForANonDraftOrder() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    Shipping shipping = ShippingDataBuilder.builder().build();

    ThrowableAssert.ThrowingCallable changeShippingTask =
      () -> order.changeShipping(shipping);

    assertThatExceptionOfType(OrderCannotBeEditedException.class)
      .isThrownBy(changeShippingTask)
      .withMessage(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, order.id(), order.status()));
  }

  @Test
  void shouldThrowExceptionWhenChangeItemQuantityForANonDraftOrder() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    OrderItem orderItem = OrderItemDataBuilder.builder()
      .withOrderId(order::id)
      .build();

    ThrowableAssert.ThrowingCallable changeItemQuantityTask =
      () -> order.changeItemQuantity(orderItem.id(), new Quantity(5));

    assertThatExceptionOfType(OrderCannotBeEditedException.class)
      .isThrownBy(changeItemQuantityTask)
      .withMessage(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, order.id(), order.status()));
  }

  @Test
  void shouldThrowExceptionWhenChangePaymentMethodForANonDraftOrder() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    ThrowableAssert.ThrowingCallable changePaymentMethodTask =
      () -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

    assertThatExceptionOfType(OrderCannotBeEditedException.class)
      .isThrownBy(changePaymentMethodTask)
      .withMessage(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, order.id(), order.status()));
  }
}
