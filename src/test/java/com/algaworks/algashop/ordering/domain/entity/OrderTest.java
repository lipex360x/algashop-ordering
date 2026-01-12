package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

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
}