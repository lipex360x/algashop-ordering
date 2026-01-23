package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.builder.BillingDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.OrderItemDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ShippingDataBuilder;
import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.ERROR_ORDER_STATUS_CANNOT_BE_CHANGED;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertWith;

class OrderTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldGenerateDraftOrder() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = Order.draft(customerId);

    String[] orderInDraftNullProperties = new String[]{
      "id",
      "customerId",
      "totalAmount",
      "totalItems",
      "status",
      "items"
    };

    assertWith(order,
      o -> assertThat(o).hasAllNullFieldsOrPropertiesExcept(orderInDraftNullProperties),
      o -> assertThat(o.isDraft()).isTrue(),
      o -> assertThat(o.customerId()).isEqualTo(customerId),
      o -> assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
      o -> assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
      o -> assertThat(o.shipping()).isNull(),
      o -> assertThat(o.items()).isEmpty()
    );
  }

  @Test
  void shouldThrowExceptionWhenPlaceOrderWithNoShippingInfo() {
    CustomerId customerId = customFaker.valueObject().customerId();

    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withBillingInfo(() -> BillingDataBuilder.builder().build())
      .withPaymentMethod(() -> customFaker.options().option(PaymentMethod.class))
      .withItems(() -> OrderItemDataBuilder.builder()
        .buildList(customFaker.number().numberBetween(1, 5)))
      .build();

    assertThatExceptionOfType(OrderCannotBePlacedException.class)
      .isThrownBy(order::place)
      .withMessage(String.format(ErrorMessages.VALIDATION_ORDER_NO_SHIPPING_INFO, order.id()));
  }

  @Test
  void shouldThrowExceptionWhenPlaceOrderWithNoBillingInfo() {
    CustomerId customerId = customFaker.valueObject().customerId();

    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withShipping(() -> ShippingDataBuilder.builder().build())
      .withPaymentMethod(() -> customFaker.options().option(PaymentMethod.class))
      .withItems(() -> OrderItemDataBuilder.builder()
        .buildList(customFaker.number().numberBetween(1, 5)))
      .build();

    assertThatExceptionOfType(OrderCannotBePlacedException.class)
      .isThrownBy(order::place)
      .withMessage(String.format(ErrorMessages.VALIDATION_ORDER_NO_BILLING_INFO, order.id()));
  }

  @Test
  void shouldThrowExceptionWhenPlaceOrderWithInvalidExpectedDate() {
    CustomerId customerId = customFaker.valueObject().customerId();

    LocalDate expectedDate = LocalDate.ofInstant(customFaker.timeAndDate().past(), UTC);
    System.out.println(expectedDate);

    Shipping shippingInThePast = ShippingDataBuilder.builder()
      .withExpectedDate(() -> expectedDate)
      .build();

    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withShipping(() -> shippingInThePast)
      .withBillingInfo(() -> BillingDataBuilder.builder().build())
      .withPaymentMethod(() -> customFaker.options().option(PaymentMethod.class))
      .withItems(() -> OrderItemDataBuilder.builder()
        .buildList(customFaker.number().numberBetween(1, 5)))
      .build();

    assertThatExceptionOfType(OrderCannotBePlacedException.class)
      .isThrownBy(order::place)
      .withMessage(String.format(ErrorMessages.VALIDATION_ORDER_INVALID_EXPECTED_DATE, order.id()));
  }

  @Test
  void shouldGenerateExceptionWhenTryToChangeItemSet() {
    Order order = OrderDataBuilder.builder().build();
    Set<OrderItem> item = order.items();

    assertThatExceptionOfType(UnsupportedOperationException.class)
      .isThrownBy(item::clear);
  }

  @Test
  void shouldCalculateTotalsWhenOrderHasItems() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Shipping shipping = ShippingDataBuilder.builder().build();

    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withShipping(() -> shipping)
      .build();

    Product product1 = customFaker.valueObject().product(new Money("10"));
    Product product2 = customFaker.valueObject().product(new Money("20"));

    OrderItem orderItem1 = OrderItemDataBuilder.builder()
      .withQuantity(() -> new Quantity(2))
      .withTotalAmount(() -> new Money("20"))
      .build();

    order.addItem(
      product1,
      orderItem1.quantity()
    );

    OrderItem orderItem2 = OrderItemDataBuilder.builder()
      .withQuantity(() -> new Quantity(1))
      .withTotalAmount(() -> new Money("20"))
      .build();

    order.addItem(
      product2,
      orderItem2.quantity()
    );

    Quantity expectedTotalQuantity = orderItem1.quantity().add(orderItem2.quantity());
    Money shippingCost = Optional.of(order.shipping().cost()).orElse(Money.ZERO);
    Money expectedTotalAmount = orderItem1.totalAmount()
      .add(orderItem2.totalAmount())
      .add(shippingCost);

    assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
    assertThat(order.totalItems()).isEqualTo(expectedTotalQuantity);
  }

  @Test
  void shouldPlaceAnOrder() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withBillingInfo(() -> BillingDataBuilder.builder().build())
      .withShipping(() -> ShippingDataBuilder.builder().build())
      .withPaymentMethod(() -> customFaker.options().option(PaymentMethod.class))
      .withItems(() -> OrderItemDataBuilder.builder()
        .buildList(customFaker.number().numberBetween(1, 5)))
      .build();

    order.place();

    assertThat(order.isPlaced()).isTrue();
    assertThat(order.placedAt()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenChangeStatusOrderFromPlacedToPlaced() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
      .isThrownBy(order::place)
      .withMessage(String.format(ERROR_ORDER_STATUS_CANNOT_BE_CHANGED, order.id(), OrderStatus.PLACED, OrderStatus.PLACED));
  }

  @Test
  void shouldChangePaymentMethod() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    assertThat(order.paymentMethod()).isNull();

    order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

    assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
  }

  @Test
  void shouldChangeBilling() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order orderDraft = Order.draft(customerId);

    Order order = OrderDataBuilder.builder(orderDraft)
      .withShipping(() -> ShippingDataBuilder.builder().build())
      .build();

    assertThat(order.billing()).isNull();

    Billing billing = BillingDataBuilder.builder().build();
    order.changeBilling(billing);

    assertThat(order.billing()).isEqualTo(billing);
  }

  @Test
  void shouldChangeShipping() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    assertThat(order.shipping()).isNull();

    Money shippingCost = customFaker.valueObject().money();
    LocalDate expectedDeliveryDate = LocalDate.ofInstant(customFaker.timeAndDate().future(), UTC);

    Shipping shipping = ShippingDataBuilder.builder()
      .withExpectedDate(() -> expectedDeliveryDate)
      .withCost(() -> shippingCost)
      .build();

    order.changeShipping(shipping);

    assertWith(order,
      o -> assertThat(o.shipping()).isEqualTo(shipping),
      o -> assertThat(o.shipping().cost()).isEqualTo(shippingCost),
      o -> assertThat(o.shipping().expectedDate()).isEqualTo(expectedDeliveryDate)
    );
  }

  @Test
  void shouldTrowExceptionWhenChangeShippingWithPastDate() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    assertThat(order.shipping()).isNull();

    LocalDate expectedDeliveryDate = LocalDate.ofInstant(customFaker.timeAndDate().past(), UTC);

    Shipping shipping = ShippingDataBuilder.builder()
      .withExpectedDate(() -> expectedDeliveryDate)
      .build();

    assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
      .isThrownBy(() -> order.changeShipping(shipping))
      .withMessage(String.format("Order %s expected date cannot be in the past", order.id()));
  }

  @Test
  void shouldChangeAnOrderToPaid() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.DRAFT)
      .withPaidAt(() -> null)
      .build();

    order.place();
    assertThat(order.isPlaced()).isTrue();
    assertThat(order.isPaid()).isFalse();

    order.markAsPaid();
    assertThat(order.isPaid()).isTrue();
    assertThat(order.paidAt()).isNotNull();
  }

  @Test
  void shouldChangeAnOrderToReady() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.DRAFT)
      .withPaidAt(() -> null)
      .build();

    order.place();
    order.markAsPaid();
    order.markAsReady();

    assertWith(order,
      o -> assertThat(o.placedAt()).isNotNull(),
      o -> assertThat(o.isPaid()).isTrue(),
      o -> assertThat(o.isReady()).isTrue(),
      o -> assertThat(o.readyAt()).isNotNull()
    );
  }

  @Test
  void shouldChangeAnOrderToCancelled() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.DRAFT)
      .build();

    order.cancel();

    assertWith(order,
      o -> assertThat(o.cancelledAt()).isNotNull(),
      o -> assertThat(o.isCanceled()).isTrue()
    );
  }

  @Test
  void shouldChangeItemQuantity() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    Product product = ProductDataBuilder.builder()
      .withPrice(() -> new Money("10"))
      .build();

    order.addItem(
      product,
      new Quantity(3)
    );

    OrderItem orderItem = order.items().stream().iterator().next();

    assertWith(order,
      o -> assertThat(o.totalAmount()).isEqualTo(new Money("30")),
      o -> assertThat(o.totalItems()).isEqualTo(new Quantity(3))
    );

    order.changeItemQuantity(orderItem.id(), new Quantity(5));

    assertWith(order,
      o -> assertThat(o.totalAmount()).isEqualTo(new Money("50")),
      o -> assertThat(o.totalItems()).isEqualTo(new Quantity(5))
    );
  }
}








































