package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.exception.OrderCannotBePlacedException;
import com.algaworks.algashop.ordering.domain.exception.OrderDoesNotContainOrderItemException;
import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

  @EqualsAndHashCode.Include
  private OrderId id;
  private CustomerId customerId;

  private Money totalAmount;
  private Quantity totalItems;

  private OffsetDateTime placedAt;
  private OffsetDateTime paidAt;
  private OffsetDateTime cancelledAt;
  private OffsetDateTime readyAt;

  private Billing billing;
  private Shipping shipping;

  private OrderStatus status;
  private PaymentMethod paymentMethod;

  private Set<OrderItem> items;

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
    Set<OrderItem> items
  ) {
    this.setId(id);
    this.setCustomerId(customerId);
    this.setTotalAmount(totalAmount);
    this.setTotalItems(totalItems);
    this.setPlacedAt(placedAt);
    this.setPaidAt(paidAt);
    this.setCancelledAt(cancelledAt);
    this.setReadyAt(readyAt);
    this.setBilling(billing);
    this.setShipping(shipping);
    this.setStatus(status);
    this.setPaymentMethod(paymentMethod);
    this.setItems(items);
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
      new HashSet<>()
    );
  }

  public void addItem(
    @NonNull Product product,
    @NonNull Quantity quantity
  ) {
    verifyIfChangeable();
    product.checkOutOfStock();

    OrderItem orderItem = OrderItem.buildNew()
      .orderId(this.id())
      .product(product)
      .quantity(quantity)
      .build();

    if (this.items == null) this.items = new HashSet<>();
    this.items.add(orderItem);
    this.recalculateTotals();
  }

  public void place() {
    this.verifyIfCanChangeToPlaced();
    this.setPlacedAt(OffsetDateTime.now());
    this.changeStatus(OrderStatus.PLACED);
  }

  public void markAsPaid() {
    this.setPaidAt(OffsetDateTime.now());
    this.changeStatus(OrderStatus.PAID);
  }

  public void changePaymentMethod(@NonNull PaymentMethod paymentMethod) {
    verifyIfChangeable();
    this.setPaymentMethod(paymentMethod);
  }

  public void changeBilling(@NonNull Billing billing) {
    verifyIfChangeable();
    this.setBilling(billing);
  }

  public void changeShipping(@NonNull Shipping shipping) {
    verifyIfChangeable();
    if (shipping.expectedDate().isBefore(LocalDate.now()))
      throw new OrderInvalidShippingDeliveryDateException(this.id());
    this.setShipping(shipping);
  }

  public void changeItemQuantity(
    @NonNull OrderItemId orderItemId,
    @NonNull Quantity quantity
  ) {
    verifyIfChangeable();
    OrderItem orderItem = this.findOrderItem(orderItemId);
    orderItem.changeQuantity(quantity);
    recalculateTotals();
  }

  public boolean isDraft() {
    return OrderStatus.DRAFT.equals(this.status());
  }

  public boolean isPlaced() {
    return OrderStatus.PLACED.equals(this.status());
  }

  public boolean isPaid() {
    return OrderStatus.PAID.equals(this.status());
  }

  public OrderId id() {
    return id;
  }

  public CustomerId customerId() {
    return customerId;
  }

  public Money totalAmount() {
    return totalAmount;
  }

  public Quantity totalItems() {
    return totalItems;
  }

  public OffsetDateTime placedAt() {
    return placedAt;
  }

  public OffsetDateTime paidAt() {
    return paidAt;
  }

  public OffsetDateTime cancelledAt() {
    return cancelledAt;
  }

  public OffsetDateTime readyAt() {
    return readyAt;
  }

  public Billing billing() {
    return billing;
  }

  public Shipping shipping() {
    return shipping;
  }

  public OrderStatus status() {
    return status;
  }

  public PaymentMethod paymentMethod() {
    return paymentMethod;
  }

  public Set<OrderItem> items() {
    return Collections.unmodifiableSet(this.items);
  }

  private void recalculateTotals() {
    BigDecimal totalItemsAmount = this.items().stream().map(i -> i.totalAmount().value())
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    Integer totalItemsQuantity = this.items().stream().map(i -> i.quantity().value())
      .reduce(0, Integer::sum);

    BigDecimal moneyShippingCost = this.shipping() == null ? BigDecimal.ZERO : this.shipping.cost().value();

    BigDecimal moneyTotalAmount = totalItemsAmount.add(moneyShippingCost);

    this.setTotalAmount(new Money(moneyTotalAmount));
    this.setTotalItems(new Quantity(totalItemsQuantity));
  }

  private void changeStatus(OrderStatus newStatus) {
    Objects.requireNonNull(newStatus);
    if (this.status.canNotChangeTo(newStatus))
      throw new OrderStatusCannotBeChangedException(this.id(), this.status(), newStatus);
    this.setStatus(newStatus);
  }

  private void verifyIfCanChangeToPlaced() {
    if (this.shipping() == null) throw OrderCannotBePlacedException.noShippingInfo(this.id());
    if (this.shipping().expectedDate().isBefore(LocalDate.now()))
      throw OrderCannotBePlacedException.invalidShippingDate(this.id());
    if (this.paymentMethod() == null) throw OrderCannotBePlacedException.noPaymentMethod(this.id());
    if (this.billing() == null) throw OrderCannotBePlacedException.noBillingInfo(this.id());
    if (this.items().isEmpty() || this.items() == null)
      throw OrderCannotBePlacedException.noItems(this.id());
  }

  private void verifyIfChangeable() {
    if (!isDraft()) throw new OrderCannotBeEditedException(this.id(), this.status());
  }

  private OrderItem findOrderItem(@NonNull OrderItemId orderItemId) {
    return this.items().stream()
      .filter(i -> i.id() == orderItemId)
      .findFirst()
      .orElseThrow(() -> new OrderDoesNotContainOrderItemException(this.id(), orderItemId));
  }

  private void setId(@NonNull OrderId id) {
    this.id = id;
  }

  private void setCustomerId(@NonNull CustomerId customerId) {
    this.customerId = customerId;
  }

  private void setTotalAmount(@NonNull Money totalAmount) {
    this.totalAmount = totalAmount;
  }

  private void setTotalItems(@NonNull Quantity totalItems) {
    this.totalItems = totalItems;
  }

  private void setPlacedAt(OffsetDateTime placedAt) {
    this.placedAt = placedAt;
  }

  private void setPaidAt(OffsetDateTime paidAt) {
    this.paidAt = paidAt;
  }

  private void setCancelledAt(OffsetDateTime cancelledAt) {
    this.cancelledAt = cancelledAt;
  }

  private void setReadyAt(OffsetDateTime readyAt) {
    this.readyAt = readyAt;
  }

  private void setBilling(Billing billing) {
    this.billing = billing;
  }

  private void setShipping(Shipping shipping) {
    this.shipping = shipping;
  }

  private void setStatus(@NonNull OrderStatus status) {
    this.status = status;
  }

  private void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  private void setItems(@NonNull Set<OrderItem> items) {
    this.items = items;
  }
}
