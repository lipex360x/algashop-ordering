package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldCreateBrandNew(){
    final var orderId = customFaker.valueObject().orderId();
    final var quantity = customFaker.valueObject().quantity();

    Product product = customFaker.valueObject().product(new Money("10"));

    final var orderItem = OrderItem.brandNew()
      .orderId(orderId)
      .product(product)
      .quantity(quantity)
      .build();

    assertWith(orderItem,
      o -> assertThat(o.id()).isNotNull(),
      o -> assertThat(o.orderId()).isEqualTo(orderId),
      o -> assertThat(o.productId()).isEqualTo(product.id()),
      o -> assertThat(o.productName()).isEqualTo(product.name()),
      o -> assertThat(o.price()).isEqualTo(product.price()),
      o -> assertThat(o.quantity()).isEqualTo(quantity),
      o -> assertThat(o.totalAmount()).isNotNull()
    );
  }

  @Test
  void shouldCreateExisting(){
    final var id = customFaker.valueObject().orderItemId();
    final var orderId = customFaker.valueObject().orderId();
    final var quantity = customFaker.valueObject().quantity();
    final var totalAmount = customFaker.valueObject().money();

    Product product = customFaker.valueObject().product(new Money("10"));

    final var orderItem = OrderItem.existing()
      .id(id)
      .orderId(orderId)
      .product(product)
      .quantity(quantity)
      .totalAmount(totalAmount)
      .build();

    assertWith(orderItem,
      o -> assertThat(o.id()).isEqualTo(id),
      o -> assertThat(o.orderId()).isEqualTo(orderId),
      o -> assertThat(o.productId()).isEqualTo(product.id()),
      o -> assertThat(o.productName()).isEqualTo(product.name()),
      o -> assertThat(o.price()).isEqualTo(product.price()),
      o -> assertThat(o.quantity()).isEqualTo(quantity),
      o -> assertThat(o.totalAmount()).isEqualTo(totalAmount)
    );
  }
}