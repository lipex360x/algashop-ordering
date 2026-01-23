package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ShoppingCartDataBuilder;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class ShoppingCartTest {

  @Test
  void shouldGenerateFromBuilder() {
    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder().build();

    assertWith(shoppingCart,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.customerId()).isNotNull(),
      s -> assertThat(s.totalAmount()).isNotEqualTo(Money.ZERO),
      s -> assertThat(s.totalItems()).isNotEqualTo(Quantity.ZERO),
      s -> assertThat(s.items()).isNotEmpty()
    );
  }

  @Test
  void shouldGenerateFromDraft() {
    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder(
      ShoppingCart.startShopping(new CustomerId())
    ).build();

    assertWith(shoppingCart,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.customerId()).isNotNull(),
      s -> assertThat(s.totalAmount()).isEqualTo(Money.ZERO),
      s -> assertThat(s.totalItems()).isEqualTo(Quantity.ZERO),
      s -> assertThat(s.items()).isEmpty()
    );
  }

  @Test
  void shouldClearItemList() {
    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder().build();

    assertWith(shoppingCart,
      s -> assertThat(s.totalAmount()).isNotEqualTo(Money.ZERO),
      s -> assertThat(s.totalItems()).isNotEqualTo(Quantity.ZERO),
      s -> assertThat(s.items()).isNotEmpty()
    );

    shoppingCart.empty();

    assertWith(shoppingCart,
      s -> assertThat(s.totalAmount()).isEqualTo(Money.ZERO),
      s -> assertThat(s.totalItems()).isEqualTo(Quantity.ZERO),
      s -> assertThat(s.items()).isEmpty()
    );
  }

  @Test
  void shouldAddShoppingCartItem() {
    ShoppingCart shoppingCart = ShoppingCart.startShopping(new CustomerId());

    ShoppingCart builder = ShoppingCartDataBuilder.builder(
      shoppingCart
    ).build();

    Product product = ProductDataBuilder.builder()
      .withPrice(() -> new Money("10"))
      .build();

    builder.addItem(product, new Quantity(2));

    assertWith(builder,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.customerId()).isNotNull(),
      s -> assertThat(s.totalAmount()).isEqualTo(new Money("20")),
      s -> assertThat(s.totalItems()).isEqualTo(new Quantity(2)),
      s -> assertThat(s.items()).hasSize(1)
    );
  }
}