package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ShoppingCartDataBuilder;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class ShoppingCartTest {

  private static final CustomFaker customFaker = new CustomFaker();

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
    ShoppingCart shoppingCartDraft = ShoppingCart.startShopping(new CustomerId());

    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder(shoppingCartDraft).build();

    Product product = ProductDataBuilder.builder()
      .withPrice(() -> new Money("10"))
      .build();

    shoppingCart.addItem(product, new Quantity(2));

    assertWith(shoppingCart,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.customerId()).isNotNull(),
      s -> assertThat(s.totalAmount()).isEqualTo(new Money("20")),
      s -> assertThat(s.totalItems()).isEqualTo(new Quantity(2)),
      s -> assertThat(s.items()).hasSize(1)
    );
  }

  @Test
  void shouldRemoveShoppingCartItem() {
    ShoppingCart shoppingCartDraft = ShoppingCart.startShopping(new CustomerId());

    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder(shoppingCartDraft).build();

    Product product1 = ProductDataBuilder.builder().build();
    Product product2 = ProductDataBuilder.builder().build();
    Product product3 = ProductDataBuilder.builder().build();

    shoppingCart.addItem(product1, customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(product2, customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(product3, customFaker.valueObject().quantity(1, 9));

    assertThat(shoppingCart.items()).hasSize(3);

    ShoppingCartItem shoppingCartItem = shoppingCart.items().stream()
      .filter(i -> i.productId().equals(product2.id()))
      .findFirst()
      .orElseThrow();

    shoppingCart.removeItem(shoppingCartItem.id());

    assertThat(shoppingCart.items()).hasSize(2);
  }

  @Test
  void shouldCleanShoppingCartItemList() {
    ShoppingCart shoppingCartDraft = ShoppingCart.startShopping(new CustomerId());

    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder(shoppingCartDraft).build();

    shoppingCart.addItem(ProductDataBuilder.builder().build(), customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(ProductDataBuilder.builder().build(), customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(ProductDataBuilder.builder().build(), customFaker.valueObject().quantity(1, 9));

    assertThat(shoppingCart.items()).hasSize(3);

    shoppingCart.empty();

    assertWith(shoppingCart,
      s -> assertThat(s.id()).isNotNull(),
      s -> assertThat(s.customerId()).isNotNull(),
      s -> assertThat(s.totalAmount()).isEqualTo(Money.ZERO),
      s -> assertThat(s.totalItems()).isEqualTo(Quantity.ZERO),
      s -> assertThat(s.items()).isEmpty()
    );
  }

  @Test
  void shouldFindShoppingCartItemByProductId() {
    ShoppingCart shoppingCartDraft = ShoppingCart.startShopping(new CustomerId());

    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder(shoppingCartDraft).build();

    Product product1 = ProductDataBuilder.builder().build();
    Product product2 = ProductDataBuilder.builder().build();
    Product product3 = ProductDataBuilder.builder().build();

    shoppingCart.addItem(product1, customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(product2, customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(product3, customFaker.valueObject().quantity(1, 9));

    ShoppingCartItem shoppingCartItem = shoppingCart.findItemOrFail(product1.id());

    assertThat(shoppingCartItem).isNotNull();
    assertThat(shoppingCartItem.productId()).isEqualTo(product1.id());
  }

  @Test
  void shouldFindShoppingCartItemByShoppingCartItemId() {
    ShoppingCart shoppingCartDraft = ShoppingCart.startShopping(new CustomerId());

    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder(shoppingCartDraft).build();

    Product product1 = ProductDataBuilder.builder().build();
    Product product2 = ProductDataBuilder.builder().build();
    Product product3 = ProductDataBuilder.builder().build();

    shoppingCart.addItem(product1, customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(product2, customFaker.valueObject().quantity(1, 9));
    shoppingCart.addItem(product3, customFaker.valueObject().quantity(1, 9));

    ShoppingCartItem getShoppingCart = shoppingCart.items().stream()
      .filter(i -> i.productId().equals(product2.id()))
      .findFirst()
      .orElseThrow();

    ShoppingCartItem shoppingCartItem = shoppingCart.findItemOrFail(getShoppingCart.id());

    assertThat(shoppingCartItem).isNotNull();
    assertThat(shoppingCartItem.productId()).isEqualTo(product2.id());
  }

  @Test
  void shouldChangeShoppingCartItemQuantity() {
    ShoppingCart shoppingCartDraft = ShoppingCart.startShopping(new CustomerId());

    ShoppingCart shoppingCart = ShoppingCartDataBuilder.builder(shoppingCartDraft).build();

    Product product = ProductDataBuilder.builder()
      .withPrice(() -> new Money("10"))
      .build();

    shoppingCart.addItem(product, new Quantity(2));

    ShoppingCartItem shoppingCartItem = shoppingCart.findItemOrFail(product.id());

    assertThat(shoppingCartItem.quantity()).isEqualTo(new Quantity(2));

    assertWith(shoppingCartItem,
      s -> assertThat(s.quantity()).isEqualTo(new Quantity(2)),
      s -> assertThat(s.totalAmount()).isEqualTo(new Money("20"))
    );

    shoppingCart.changeItemQuantity(shoppingCartItem.id(), new Quantity(5));

    assertWith(shoppingCartItem,
      s -> assertThat(s.quantity()).isEqualTo(new Quantity(5)),
      s -> assertThat(s.totalAmount()).isEqualTo(new Money("50"))
    );
  }

}