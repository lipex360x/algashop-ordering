package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.entity.Order;
import com.algaworks.algashop.ordering.domain.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.BillingInfo;
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
  private Supplier<Money> totalAmount = () -> customFaker.valueObject().money();

  @With
  private Supplier<Quantity> totalItems = () -> customFaker.valueObject().quantity();

  @With
  private Supplier<OffsetDateTime> placedAt = OffsetDateTime::now;

  @With
  private Supplier<OffsetDateTime> paidAt = OffsetDateTime::now;

  @With
  private Supplier<OffsetDateTime> canceledAt = OffsetDateTime::now;

  @With
  private Supplier<OffsetDateTime> readyAt = OffsetDateTime::now;

  @With
  private Supplier<BillingInfo> billingInfo = () -> BillingInfoDataBuilder.builder().buildNew();

  @With
  private Supplier<Shipping> shipping = () -> ShippingDataBuilder.builder().build();

  @With
  private Supplier<OrderStatus> status = () -> customFaker.options().option(OrderStatus.class);

  @With
  private Supplier<PaymentMethod> paymentMethod = () -> customFaker.options().option(PaymentMethod.class);

  @With
  private Supplier<Set<OrderItem>> items = () -> OrderItemDataBuilder.builder()
    .buildExistingList(customFaker.number().numberBetween(1, 9));

  public static OrderDataBuilder builder() {
    return new OrderDataBuilder();
  }

  public static OrderDataBuilder builder(final Order order) {
    final OrderId id = order.id();
    final CustomerId customerId = order.customerId();
    final Money totalAmount = order.totalAmount();
    final Quantity totalItems = order.totalItems();
    final OffsetDateTime placedAt = order.placedAt();
    final OffsetDateTime paidAt = order.paidAt();
    final OffsetDateTime canceledAt = order.cancelledAt();
    final OffsetDateTime readyAt = order.readyAt();
    final BillingInfo billing = order.billing();
    final Shipping shipping = order.shipping();
    final OrderStatus orderStatus = order.status();
    final PaymentMethod paymentMethod = order.paymentMethod();
    final Set<OrderItem> items = new HashSet<>(order.items());

    return new OrderDataBuilder(
      () -> id,
      () -> customerId,
      () -> totalAmount,
      () -> totalItems,
      () -> placedAt,
      () -> paidAt,
      () -> canceledAt,
      () -> readyAt,
      () -> billing,
      () -> shipping,
      () -> orderStatus,
      () -> paymentMethod,
      () -> items
    );
  }

  public Order build(){
    return Order.existing()
      .id(id.get())
      .customerId(customerId.get())
      .totalAmount(totalAmount.get())
      .totalItems(totalItems.get())
      .placedAt(placedAt.get())
      .paidAt(paidAt.get())
      .cancelledAt(canceledAt.get())
      .readyAt(readyAt.get())
      .billingInfo(billingInfo.get())
      .shipping(shipping.get())
      .status(status.get())
      .paymentMethod(paymentMethod.get())
      .items(items.get())
      .build();
  }
}
