package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCartItemDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  @With
  private Supplier<ShoppingCartItemId> id = () -> customFaker.valueObject().shoppingCartItemId();

  @With
  private Supplier<ShoppingCartId> shoppingCartId = () -> customFaker.valueObject().shoppingCartId();

  @With
  private Supplier<Product> product = () -> customFaker.valueObject().product();

  @With
  private Supplier<Quantity> quantity = () -> customFaker.valueObject().quantity(1, 9);

  @With
  private Supplier<Money> price = () -> customFaker.valueObject().money(1, 100);

  @With
  private Supplier<Money> totalAmount = () -> customFaker.valueObject().money(1, 100);

  @With
  private Supplier<Boolean> available = () -> true;

  public static ShoppingCartItemDataBuilder builder() {
    return new ShoppingCartItemDataBuilder();
  }

  public static ShoppingCartItemDataBuilder builder(ShoppingCartItem shoppingCartItem) {
    Product product = Product.builder()
      .id(shoppingCartItem.productId())
      .name(shoppingCartItem.productName())
      .price(shoppingCartItem.price())
      .inStock(true)
      .build();

    return new ShoppingCartItemDataBuilder(
      shoppingCartItem::id,
      shoppingCartItem::shoppingCartId,
      () -> product,
      shoppingCartItem::quantity,
      shoppingCartItem::price,
      shoppingCartItem::totalAmount,
      () -> true
    );
  }

  public ShoppingCartItem build() {
    return ShoppingCartItem.buildExisting()
      .id(id.get())
      .shoppingCartId(shoppingCartId.get())
      .product(product.get())
      .quantity(quantity.get())
      .totalAmount(totalAmount.get())
      .available(available.get())
      .build();
  }

  public Set<ShoppingCartItem> buildList(int amount) {
    return Stream.generate(() -> ShoppingCartItemDataBuilder.builder().build())
      .limit(amount)
      .collect(Collectors.toSet());
  }
}
