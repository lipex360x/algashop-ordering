package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.builder.BillingDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.OrderItemDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ShippingDataBuilder;
import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertWith;

class OrderTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldCreateDraft() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    String[] nullProperties = new String[]{
      "id",
      "customerId",
      "totalAmount",
      "totalItems",
      "status",
      "items"
    };

    assertWith(order,
      o -> assertThat(o.isDraft()).isTrue(),
      o -> assertThat(o.customerId()).isEqualTo(customerId),
      o -> assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
      o -> assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
      o -> assertThat(o.shipping()).isNull(),
      o -> assertThat(o.items()).isEmpty(),
      o -> assertThat(o).hasAllNullFieldsOrPropertiesExcept(nullProperties)
    );
  }

  @Test
  void shouldThrowExceptionWhenPlaceOrderWithNoShippingInfo() {
    CustomerId customerId = customFaker.valueObject().customerId();

    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withBillingInfo(() -> BillingDataBuilder.builder().build())
      .withPaymentMethod(() -> customFaker.options().option(PaymentMethod.class))
      .withItems(() -> OrderItemDataBuilder.builder()
        .buildExistingList(customFaker.number().numberBetween(1, 5)))
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
        .buildExistingList(customFaker.number().numberBetween(1, 5)))
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
        .buildExistingList(customFaker.number().numberBetween(1, 5)))
      .build();

    assertThatExceptionOfType(OrderCannotBePlacedException.class)
      .isThrownBy(order::place)
      .withMessage(String.format(ErrorMessages.VALIDATION_ORDER_INVALID_EXPECTED_DATE, order.id()));
  }

  @Test
  void shouldAddItem() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    Product product1 = customFaker.valueObject().product();
    Product product2 = customFaker.valueObject().product();
    Product product3 = customFaker.valueObject().product();

    assertThat(order.items()).isEmpty();

    order.addItem(
      product1,
      customFaker.valueObject().quantity(2, 4)
    );

    order.addItem(
      product2,
      customFaker.valueObject().quantity(1, 4)
    );

    order.addItem(
      product3,
      customFaker.valueObject().quantity(1, 4)
    );

    assertThat(order.items()).hasSize(3);
    assertThat(order.items()).allSatisfy(item -> assertThat(item.id()).isNotNull());
    assertThat(order.items())
      .extracting(OrderItem::productName)
      .extracting(ProductName::value)
      .containsExactlyInAnyOrder(
        product1.name().value(),
        product2.name().value(),
        product3.name().value()
      );
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
      .buildExisting();

    order.addItem(
      product1,
      orderItem1.quantity()
    );

    OrderItem orderItem2 = OrderItemDataBuilder.builder()
      .withQuantity(() -> new Quantity(1))
      .withTotalAmount(() -> new Money("20"))
      .buildExisting();

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
        .buildExistingList(customFaker.number().numberBetween(1, 5)))
      .build();

    order.place();

    assertThat(order.isPlaced()).isTrue();
    assertThat(order.placedAt()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenChangeStatusOrderFromPlacedToPlaced() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withStatus(() -> OrderStatus.PLACED)
      .withBillingInfo(() -> BillingDataBuilder.builder().build())
      .withShipping(() -> ShippingDataBuilder.builder().build())
      .withPaymentMethod(() -> customFaker.options().option(PaymentMethod.class))
      .withItems(() -> OrderItemDataBuilder.builder()
        .buildExistingList(customFaker.number().numberBetween(1, 5)))
      .build();

    assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
      .isThrownBy(order::place)
      .withMessage(String.format("Cannot change order %S from status PLACED to PLACED", order.id()));
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
  void shouldChangeBillingInfo() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order orderDraft = Order.draft(customerId);

    Order order = OrderDataBuilder.builder(orderDraft)
      .withShipping(() -> ShippingDataBuilder.builder().build())
      .build();

    assertThat(order.billing()).isNull();

    Billing billing = BillingDataBuilder.builder().build();
    order.changeBillingInfo(billing);

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
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId))
      .withStatus(() -> OrderStatus.PLACED)
      .withBillingInfo(() -> BillingDataBuilder.builder().build())
      .withShipping(() -> ShippingDataBuilder.builder().build())
      .withPaymentMethod(() -> customFaker.options().option(PaymentMethod.class))
      .withItems(() -> OrderItemDataBuilder.builder()
        .buildExistingList(customFaker.number().numberBetween(1, 5)))
      .build();

    assertThat(order.isPlaced()).isTrue();
    assertThat(order.isPaid()).isFalse();

    order.markAsPaid();
    assertThat(order.isPaid()).isTrue();
    assertThat(order.paidAt()).isNotNull();
  }

  @Test
  void shouldChangeItemQuantity() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = Order.draft(customerId);
    Product product = customFaker.valueObject().product(new Money("10"));

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

  @Test
  void shouldThrowExceptionWhenAddItemWithProductOutOfStock() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = Order.draft(customerId);
    Product product = customFaker.valueObject().product(false);

    ThrowableAssert.ThrowingCallable addItemTask =
      () -> order.addItem(product, new Quantity(2));

    assertThatExceptionOfType(ProductOutOfStockException.class)
      .isThrownBy(addItemTask)
      .withMessage(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, product.id()));
  }
}








































