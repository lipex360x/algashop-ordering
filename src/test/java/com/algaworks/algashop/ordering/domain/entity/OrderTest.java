package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.mock.AddressMock;
import com.algaworks.algashop.ordering.domain.mock.DocumentMock;
import com.algaworks.algashop.ordering.domain.mock.FullNameMock;
import com.algaworks.algashop.ordering.domain.mock.PhoneMock;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.BillingInfo;
import com.algaworks.algashop.ordering.domain.valueobject.Document;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.ShippingInfo;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertWith;

class OrderTest {

  static Faker faker = new Faker(Locale.US);

  @Test
  void shouldGenerate() {
    Order order = Order.draft(new CustomerId());
    assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
  }

  @Test
  void shouldAddItem() {
    Order order = Order.draft(new CustomerId());

    ProductName product1 = new ProductName("Product 1");
    ProductName product2 = new ProductName("Product 2");
    ProductName product3 = new ProductName("Product 3");

    assertThat(order.items()).isEmpty();

    order.addItem(
      new ProductId(),
      product1,
      new Money(faker.commerce().price()),
      new Quantity(3)
    );

    order.addItem(
      new ProductId(),
      product2,
      new Money(faker.commerce().price()),
      new Quantity(2)
    );

    order.addItem(
      new ProductId(),
      product3,
      new Money(faker.commerce().price()),
      new Quantity(4)
    );

    assertThat(order.items())
      .hasSize(3)
      .allSatisfy(item -> assertThat(item.id()).isNotNull())
      .extracting(OrderItem::productName)
      .extracting(ProductName::value)
      .containsExactlyInAnyOrder(product1.value(), product2.value(), product3.value());
  }

  @Test
  void shouldGenerateExceptionWhenTryToChangeItemSet() {
    Order order = Order.draft(new CustomerId());

    order.addItem(
      new ProductId(),
      new ProductName(faker.commerce().productName()),
      new Money(faker.commerce().price()),
      new Quantity(faker.number().positive())
    );

    Set<OrderItem> items = order.items();

    assertThatExceptionOfType(UnsupportedOperationException.class)
      .isThrownBy(items::clear);
  }

  @Test
  void shouldCalculateTotals() {
    Order order = Order.draft(new CustomerId());

    order.addItem(
      new ProductId(),
      new ProductName(faker.commerce().productName()),
      new Money("10"),
      new Quantity(2)
    );

    order.addItem(
      new ProductId(),
      new ProductName(faker.commerce().productName()),
      new Money("20"),
      new Quantity(5)
    );

    assertThat(order.totalAmount()).isEqualTo(new Money("120"));
    assertThat(order.totalItems()).isEqualTo(new Quantity(7));
  }

  @Test
  void shouldChangeStatusOrderFromDraftToPlaced() {
    Order order = Order.draft(new CustomerId());
    order.place();
    assertThat(order.isPlaced()).isTrue();
  }

  @Test
  void shouldThrowExceptionWhenChangeStatusOrderFromPlacedToPlaced() {
    Order order = Order.draft(new CustomerId());
    order.place();
    assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
      .isThrownBy(order::place)
      .withMessage(String.format("Cannot change order %S from status PLACED to PLACED", order.id()));
  }

  @Test
  void shouldChangePaymentMethod() {
    Order order = Order.draft(new CustomerId());
    assertThat(order.paymentMethod()).isNull();
    order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
    assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
  }

  @Test
  void shouldChangeBillingInfo() {
    Order order = Order.draft(new CustomerId());

    Address address = AddressMock.build();
    Document document = DocumentMock.build();
    Phone phone = PhoneMock.build();
    FullName fullName = FullNameMock.build();

    BillingInfo billingInfo = BillingInfo.builder()
      .address(address)
      .document(document)
      .phone(phone)
      .fullName(fullName)
      .build();

    order.changeBillingInfo(billingInfo);

    BillingInfo expectedBillingInfo = BillingInfo.builder()
      .address(address)
      .document(document)
      .phone(phone)
      .fullName(fullName)
      .build();

    assertThat(order.billing()).isEqualTo(expectedBillingInfo);
  }

  @Test
  void shouldChangeShippingInfo() {
    Address address = AddressMock.build();
    Document document = DocumentMock.build();
    Phone phone = PhoneMock.build();
    FullName fullName = FullNameMock.build();

    ShippingInfo shippingInfo = ShippingInfo.builder()
      .address(address)
      .document(document)
      .phone(phone)
      .fullName(fullName)
      .build();

    Order order = Order.draft(new CustomerId());
    Money shippingCost = Money.ZERO;
    LocalDate expectedDeliveryDate = LocalDate.now().plusDays(1);
    order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);

    ShippingInfo expectedShippingInfo = ShippingInfo.builder()
      .address(address)
      .document(document)
      .phone(phone)
      .fullName(fullName)
      .build();

    assertWith(order,
      o -> assertThat(o.shipping()).isEqualTo(expectedShippingInfo),
      o -> assertThat(o.shippingCost()).isEqualTo(shippingCost),
      o -> assertThat(o.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate)
    );

  }

  @Test
  void shouldTrowExceptionWhenChangeShippingInfoWithPastDate() {
    Address address = AddressMock.build();
    Document document = DocumentMock.build();
    Phone phone = PhoneMock.build();
    FullName fullName = FullNameMock.build();

    ShippingInfo shippingInfo = ShippingInfo.builder()
      .address(address)
      .document(document)
      .phone(phone)
      .fullName(fullName)
      .build();

    Order order = Order.draft(new CustomerId());
    Money shippingCost = Money.ZERO;
    LocalDate expectedDeliveryDate = LocalDate.now().minusDays(1);
    assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
      .isThrownBy(() -> order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate))
      .withMessage(String.format("Order %s expected date cannot be in the past", order.id()));
  }
}








































