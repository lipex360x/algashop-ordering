package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderDoesNotContainOrderItemException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(fluent = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Order implements AggregateRoot<OrderId> {

  @EqualsAndHashCode.Include
  @NonNull
  private OrderId id;

  @NonNull
  private CustomerId customerId;

  @NonNull
  private Money totalAmount;

  @NonNull
  private Quantity totalItems;

  private OffsetDateTime placedAt;
  private OffsetDateTime paidAt;
  private OffsetDateTime cancelledAt;
  private OffsetDateTime readyAt;

  private Billing billing;
  private Shipping shipping;

  @NonNull
  private OrderStatus status;

  private PaymentMethod paymentMethod;

  @Getter(AccessLevel.NONE)
  @NonNull
  private Set<OrderItem> items;

  private Long version;

  @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "buildExisting")
  public Order(
    OrderId id,
    CustomerId customerId,
    Money totalAmount,
    Quantity totalItems,
    OffsetDateTime placedAt,
    OffsetDateTime paidAt,
    OffsetDateTime cancelledAt,
    OffsetDateTime readyAt,
    Billing billing,
    Shipping shipping,
    OrderStatus status,
    PaymentMethod paymentMethod,
    Set<OrderItem> items,
    Long version
  ) {
    this.id(id);
    this.customerId(customerId);
    this.totalAmount(totalAmount);
    this.totalItems(totalItems);
    this.placedAt(placedAt);
    this.paidAt(paidAt);
    this.cancelledAt(cancelledAt);
    this.readyAt(readyAt);
    this.billing(billing);
    this.shipping(shipping);
    this.status(status);
    this.paymentMethod(paymentMethod);
    this.items(items);
    this.version(version);
  }

  public static Order draft(CustomerId customerId) {
    return new Order(
      new OrderId(),
      customerId,
      Money.ZERO,
      Quantity.ZERO,
      null,
      null,
      null,
      null,
      null,
      null,
      OrderStatus.DRAFT,
      null,
      new HashSet<>(),
      null
    );
  }

  public void addItem(
    @NonNull Product product,
    @NonNull Quantity quantity
  ) {
    this.verifyIfChangeable();
    product.checkOutOfStock();

    OrderItem orderItem = OrderItem.buildNew()
      .orderId(this.id())
      .product(product)
      .quantity(quantity)
      .build();

    this.items.add(orderItem);
    this.recalculateTotals();
  }

  public void changeItemQuantity(
    @NonNull OrderItemId orderItemId,
    @NonNull Quantity quantity
  ) {
    this.verifyIfChangeable();
    OrderItem orderItem = this.findOrderItemOrFail(orderItemId);
    orderItem.changeQuantity(quantity);
    this.recalculateTotals();
  }

  public void removeItem(@NonNull OrderItemId orderItemId) {
    this.verifyIfChangeable();
    OrderItem orderItem = this.findOrderItemOrFail(orderItemId);
    this.items.remove(orderItem);
    this.recalculateTotals();
  }

  public void place() {
    this.verifyIfCanChangeToPlaced();
    this.changeStatus(OrderStatus.PLACED);
    this.placedAt(OffsetDateTime.now());
  }

  public void markAsPaid() {
    this.changeStatus(OrderStatus.PAID);
    this.paidAt(OffsetDateTime.now());
  }

  public void markAsReady() {
    this.changeStatus(OrderStatus.READY);
    this.readyAt(OffsetDateTime.now());
  }

  public void cancel() {
    this.changeStatus(OrderStatus.CANCELLED);
    this.cancelledAt(OffsetDateTime.now());
  }

  public void changePaymentMethod(@NonNull PaymentMethod paymentMethod) {
    this.verifyIfChangeable();
    this.paymentMethod(paymentMethod);
  }

  public void changeBilling(@NonNull Billing billing) {
    this.verifyIfChangeable();
    this.billing(billing);
  }

  public void changeShipping(@NonNull Shipping shipping) {
    this.verifyIfChangeable();
    if (shipping.expectedDate().isBefore(LocalDate.now()))
      throw new OrderInvalidShippingDeliveryDateException(this.id());
    this.shipping(shipping);
  }

  public boolean isDraft() {
    return OrderStatus.DRAFT.equals(this.status());
  }

  public boolean isPlaced() {
    return OrderStatus.PLACED.equals(this.status()) || this.placedAt() != null;
  }

  public boolean isPaid() {
    return OrderStatus.PAID.equals(this.status()) || this.paidAt() != null;
  }

  public boolean isReady() {
    return OrderStatus.READY.equals(this.status()) || this.readyAt() != null;
  }

  public boolean isCanceled() {
    return OrderStatus.CANCELLED.equals(this.status()) || this.cancelledAt() != null;
  }

  public Set<OrderItem> items() {
    return Collections.unmodifiableSet(this.items);
  }

  private void recalculateTotals() {
    BigDecimal totalItemsAmount = this.items().stream()
      .map(i -> i.totalAmount().value())
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    Integer totalItemsQuantity = this.items().stream()
      .map(i -> i.quantity().value())
      .reduce(0, Integer::sum);

    BigDecimal moneyShippingCost = Objects.isNull(this.shipping()) ? BigDecimal.ZERO : this.shipping.cost().value();
    BigDecimal moneyTotalAmount = totalItemsAmount.add(moneyShippingCost);

    this.totalAmount(new Money(moneyTotalAmount));
    this.totalItems(new Quantity(totalItemsQuantity));
  }

  private void changeStatus(OrderStatus newStatus) {
    Objects.requireNonNull(newStatus);
    if (this.status.canNotChangeTo(newStatus))
      throw new OrderStatusCannotBeChangedException(this.id(), this.status(), newStatus);
    this.status(newStatus);
  }

  private void verifyIfCanChangeToPlaced() {
    if (Objects.isNull(this.shipping()))
      throw OrderCannotBePlacedException.noShippingInfo(this.id());
    if (this.shipping().expectedDate().isBefore(LocalDate.now()))
      throw OrderCannotBePlacedException.invalidShippingDate(this.id());
    if (Objects.isNull(this.paymentMethod()))
      throw OrderCannotBePlacedException.noPaymentMethod(this.id());
    if (Objects.isNull(this.billing()))
      throw OrderCannotBePlacedException.noBillingInfo(this.id());
    if (this.items().isEmpty() || Objects.isNull(this.items()))
      throw OrderCannotBePlacedException.noItems(this.id());
  }

  private void verifyIfChangeable() {
    if (!isDraft()) throw new OrderCannotBeEditedException(this.id(), this.status());
  }

  private OrderItem findOrderItemOrFail(@NonNull OrderItemId orderItemId) {
    return this.items().stream()
      .filter(i -> i.id().equals(orderItemId))
      .findFirst()
      .orElseThrow(() -> new OrderDoesNotContainOrderItemException(this.id(), orderItemId));
  }
}
