package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCartDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  private Supplier<ShoppingCartId> id = () -> customFaker.valueObject().shoppingCartId();

  private Supplier<CustomerId> customerId = () -> customFaker.valueObject().customerId();

  private Supplier<Money> totalAmount = () -> customFaker.valueObject().money(1, 100);

  private Supplier<Quantity> totalItems = () -> customFaker.valueObject().quantity(1, 9);

  private Supplier<OffsetDateTime> createdAt = OffsetDateTime::now;

  private Supplier<Set<ShoppingCartItem>> items = () -> ShoppingCartItemDataBuilder.builder()
    .buildList(customFaker.number().numberBetween(1, 9));

  public static ShoppingCartDataBuilder builder() {
    return new ShoppingCartDataBuilder();
  }

  public static ShoppingCartDataBuilder builder(ShoppingCart shoppingCart) {
    return new ShoppingCartDataBuilder(
      shoppingCart::id,
      shoppingCart::customerId,
      shoppingCart::totalAmount,
      shoppingCart::totalItems,
      shoppingCart::createdAt,
      () -> new HashSet<>(shoppingCart.items())
    );
  }

  public ShoppingCart build() {
    return ShoppingCart.buildExisting()
      .id(id.get())
      .customerId(customerId.get())
      .totalItems(totalItems.get())
      .totalAmount(totalAmount.get())
      .createdAt(createdAt.get())
      .items(items.get())
      .build();
  }
}
