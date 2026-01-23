package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ShoppingCartItemDataBuilder;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartItemIncompatibleProductException;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartItemInvalidQuantityException;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
      s -> assertThat(s.isAvailable()).isNotNull()
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
      s -> assertThat(s.isAvailable()).isNotNull()
    );
  }

  @Test
  void shouldRefreshProduct() {
    Product product = ProductDataBuilder.builder()
      .withName(() -> new ProductName("Product"))
      .withPrice(() -> new Money("10"))
      .build();

    ShoppingCartItem shoppingCartItem = ShoppingCartItemDataBuilder.builder()
      .withProduct(() -> product)
      .build();

    assertWith(shoppingCartItem,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.shoppingCartId()).isNotNull(),
      s -> assertThat(s.productName()).isEqualTo(new ProductName("Product")),
      s -> assertThat(s.price()).isEqualTo(new Money("10"))
    );

    Product productUpdated = ProductDataBuilder.builder(product)
      .withName(() -> new ProductName("Product updated"))
      .withPrice(() -> new Money("50"))
      .build();

    shoppingCartItem.refresh(productUpdated);

    assertWith(shoppingCartItem,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.shoppingCartId()).isNotNull(),
      s -> assertThat(s.productName()).isEqualTo(new ProductName("Product updated")),
      s -> assertThat(s.price()).isEqualTo(new Money("50"))
    );
  }

  @Test
  void shouldThrowExceptionWhenUpdateQuantityWithZero() {
    Product product = ProductDataBuilder.builder()
      .withName(() -> new ProductName("Product"))
      .withPrice(() -> new Money("10"))
      .build();

    ShoppingCartItem shoppingCartItem = ShoppingCartItemDataBuilder.builder()
      .withProduct(() -> product)
      .build();

    ThrowableAssert.ThrowingCallable changeShoppingCartItemProductTask =
      () -> shoppingCartItem.changeQuantity(Quantity.ZERO);

    assertThatExceptionOfType(ShoppingCartItemInvalidQuantityException.class)
      .isThrownBy(changeShoppingCartItemProductTask);
  }

  @Test
  void shouldThrowExceptionWhenUpdateWithInvalidProduct() {
    Product product = ProductDataBuilder.builder()
      .withName(() -> new ProductName("Product"))
      .withPrice(() -> new Money("10"))
      .build();

    ShoppingCartItem shoppingCartItem = ShoppingCartItemDataBuilder.builder()
      .withProduct(() -> product)
      .build();

    assertWith(shoppingCartItem,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.shoppingCartId()).isNotNull(),
      s -> assertThat(s.productName()).isEqualTo(new ProductName("Product")),
      s -> assertThat(s.price()).isEqualTo(new Money("10"))
    );

    Product updatedProduct = ProductDataBuilder.builder()
      .withName(() -> new ProductName("New Product 1"))
      .withPrice(() -> new Money("50"))
      .build();

    ThrowableAssert.ThrowingCallable changeShoppingCartItemProductTask =
      () -> shoppingCartItem.refresh(updatedProduct);

    assertThatExceptionOfType(ShoppingCartItemIncompatibleProductException.class)
      .isThrownBy(changeShoppingCartItemProductTask);
  }

}