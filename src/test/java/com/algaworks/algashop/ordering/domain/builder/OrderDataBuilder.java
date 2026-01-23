package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.entity.Order;
import com.algaworks.algashop.ordering.domain.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class OrderDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  @With
  private Supplier<OrderId> id = () -> customFaker.valueObject().orderId();

  @With
  private Supplier<CustomerId> customerId = () -> customFaker.valueObject().customerId();

  @With
  private Supplier<Money> totalAmount = () -> customFaker.valueObject().money(1, 100);

  @With
  private Supplier<Quantity> totalItems = () -> customFaker.valueObject().quantity(1, 9);

  @With
  private Supplier<OffsetDateTime> placedAt = OffsetDateTime::now;

  @With
  private Supplier<OffsetDateTime> paidAt = OffsetDateTime::now;

  @With
  private Supplier<OffsetDateTime> canceledAt = OffsetDateTime::now;

  @With
  private Supplier<OffsetDateTime> readyAt = OffsetDateTime::now;

  @With
  private Supplier<Billing> billingInfo = () -> BillingDataBuilder.builder().build();

  @With
  private Supplier<Shipping> shipping = () -> ShippingDataBuilder.builder().build();

  @With
  private Supplier<OrderStatus> status = () -> customFaker.options().option(OrderStatus.class);

  @With
  private Supplier<PaymentMethod> paymentMethod = () -> customFaker.options().option(PaymentMethod.class);

  @With
  private Supplier<Set<OrderItem>> items = () -> OrderItemDataBuilder.builder()
    .buildList(customFaker.number().numberBetween(1, 9));

  public static OrderDataBuilder builder() {
    return new OrderDataBuilder();
  }

  public static OrderDataBuilder builder(Order order) {
    Set<OrderItem> items = new HashSet<>(order.items());

    return new OrderDataBuilder(
      order::id,
      order::customerId,
      order::totalAmount,
      order::totalItems,
      order::placedAt,
      order::paidAt,
      order::cancelledAt,
      order::readyAt,
      order::billing,
      order::shipping,
      order::status,
      order::paymentMethod,
      () -> items
    );
  }

  public Order build(){
    return Order.buildExisting()
      .id(id.get())
      .customerId(customerId.get())
      .totalAmount(totalAmount.get())
      .totalItems(totalItems.get())
      .placedAt(placedAt.get())
      .paidAt(paidAt.get())
      .cancelledAt(canceledAt.get())
      .readyAt(readyAt.get())
      .billing(billingInfo.get())
      .shipping(shipping.get())
      .status(status.get())
      .paymentMethod(paymentMethod.get())
      .items(items.get())
      .build();
  }
}
