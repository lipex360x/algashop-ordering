package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.builder.ShoppingCartItemDataBuilder;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class ShoppingCartItemTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldGenerateFromEntity() {
    ShoppingCartItem shoppingCartItem = ShoppingCartItem.buildNew()
      .shoppingCartId(customFaker.valueObject().shoppingCartId())
      .product(customFaker.valueObject().product())
      .quantity(customFaker.valueObject().quantity(1, 9))
      .build();

    assertWith(shoppingCartItem,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.shoppingCartId()).isNotNull(),
      s -> assertThat(s.productName()).isNotNull(),
      s -> assertThat(s.price()).isNotEqualTo(Money.ZERO),
      s -> assertThat(s.quantity()).isNotEqualTo(Quantity.ZERO),
      s -> assertThat(s.totalAmount()).isNotEqualTo(Money.ZERO),
      s -> assertThat(s.available()).isNotNull()
    );
  }
  @Test
  void shouldGenerateFromBuilder() {
    ShoppingCartItem shoppingCartItem = ShoppingCartItemDataBuilder.builder().build();

    assertWith(shoppingCartItem,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.shoppingCartId()).isNotNull(),
      s -> assertThat(s.productName()).isNotNull(),
      s -> assertThat(s.price()).isNotEqualTo(Money.ZERO),
      s -> assertThat(s.quantity()).isNotEqualTo(Quantity.ZERO),
      s -> assertThat(s.totalAmount()).isNotEqualTo(Money.ZERO),
      s -> assertThat(s.available()).isNotNull()
    );
  }

}