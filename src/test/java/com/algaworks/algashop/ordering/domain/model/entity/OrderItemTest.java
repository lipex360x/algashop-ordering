package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldBuildNew(){
    OrderId orderId = customFaker.valueObject().orderId();
    Quantity quantity = customFaker.valueObject().quantity();
    Money price = new Money("20");

    Product product = ProductDataBuilder.builder()
      .withPrice(() -> price)
      .build();

    OrderItem orderItem = OrderItem.buildNew()
      .orderId(orderId)
      .product(product)
      .quantity(quantity)
      .build();

    assertWith(orderItem,
      o -> assertThat(o.id()).isNotNull(),
      o -> assertThat(o.orderId()).isEqualTo(orderId),
      o -> assertThat(o.productId()).isEqualTo(product.id()),
      o -> assertThat(o.productName()).isEqualTo(product.name()),
      o -> assertThat(o.price()).isEqualTo(price),
      o -> assertThat(o.quantity()).isEqualTo(quantity),
      o -> assertThat(o.totalAmount()).isNotNull()
    );
  }

  @Test
  void shouldBuildExisting(){
    OrderItemId id = customFaker.valueObject().orderItemId();
    OrderId orderId = customFaker.valueObject().orderId();
    Quantity quantity = customFaker.valueObject().quantity();
    Money price = new Money("50");
    Money totalAmount = new Money("50");

    Product product = ProductDataBuilder.builder()
      .withPrice(() -> price)
      .build();

    OrderItem orderItem = OrderItem.buildExisting()
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