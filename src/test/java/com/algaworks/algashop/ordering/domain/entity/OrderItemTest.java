package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class OrderItemTest {

  static Faker faker = new Faker(Locale.US);

  @Test
  void shouldGenerate() {
    OrderItem.brandNew()
      .productId(new ProductId())
      .orderId(new OrderId())
      .productName(new ProductName(faker.commerce().productName()))
      .quantity(new Quantity(1))
      .price(new Money(Money.ZERO.value()))
      .build();
  }
}