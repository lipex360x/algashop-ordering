package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderTest {

  static Faker faker = new Faker(Locale.US);


  @Test
  void shouldGenerate() {
    Order.draft(new CustomerId());
  }

  @Test
  void shouldAddItem() {
    Order order = Order.draft(new CustomerId());

    ProductName product1 = new ProductName(faker.commerce().productName());
    ProductName product2 = new ProductName(faker.commerce().productName());
    ProductName product3 = new ProductName(faker.commerce().productName());

    assertThat(order.items()).isEmpty();

    order.addItem(
      new ProductId(),
      product1,
      new Money(faker.commerce().price()),
      new Quantity(faker.number().positive())
    );

    order.addItem(
      new ProductId(),
      product2,
      new Money(faker.commerce().price()),
      new Quantity(faker.number().positive())
    );

    order.addItem(
      new ProductId(),
      product3,
      new Money(faker.commerce().price()),
      new Quantity(faker.number().positive())
    );

    assertThat(order.items())
      .hasSize(3)
      .allSatisfy(item -> assertThat(item.id()).isNotNull())
      .extracting(OrderItem::productName)
      .extracting(ProductName::value)
      .containsExactly(product1.value(), product2.value(), product3.value());
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
}