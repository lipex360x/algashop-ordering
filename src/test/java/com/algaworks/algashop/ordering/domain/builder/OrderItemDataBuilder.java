package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class OrderItemDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  @With
  private Supplier<OrderItemId> id = () -> customFaker.valueObject().orderItemId();

  @With
  private Supplier<OrderId> orderId = () -> customFaker.valueObject().orderId();

  @With
  private Supplier<Product> product = () -> customFaker.valueObject().product();

  @With
  private Supplier<Quantity> quantity = () -> customFaker.valueObject().quantity(1, 9);

  @With
  private Supplier<Money> totalAmount = () -> customFaker.valueObject().money(1, 100);

  public static OrderItemDataBuilder builder() {
    return new OrderItemDataBuilder();
  }

  public static OrderItemDataBuilder builder(OrderItem orderItem) {
    Product product = Product.builder()
      .id(orderItem.productId())
      .name(orderItem.productName())
      .price(orderItem.price())
      .inStock(true)
      .build();

    return new OrderItemDataBuilder(
      orderItem::id,
      orderItem::orderId,
      () -> product,
      orderItem::quantity,
      orderItem::totalAmount
    );
  }

  public OrderItem build() {
    return OrderItem.buildExisting()
      .id(id.get())
      .orderId(orderId.get())
      .product(product.get())
      .quantity(quantity.get())
      .totalAmount(totalAmount.get())
      .build();
  }

  public Set<OrderItem> buildList(final int amount){
    return Stream.generate(() -> OrderItemDataBuilder.builder().build())
      .limit(amount)
      .collect(Collectors.toSet());
  }
}
