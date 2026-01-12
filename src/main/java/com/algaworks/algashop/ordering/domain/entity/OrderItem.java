package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {

  @EqualsAndHashCode.Include
  private OrderItemId id;
  private OrderId orderId;

  private ProductId productId;
  private ProductName productName;

  private Money price;
  private Quantity quantity;

  private Money totalAmount;

  public OrderItem(
    OrderItemId id,
    OrderId orderId,
    ProductId productId,
    ProductName productName,
    Money price,
    Quantity quantity,
    Money totalAmount
  ) {
    this.setId(id);
    this.setOrderId(orderId);
    this.setProductId(productId);
    this.setProductName(productName);
    this.setPrice(price);
    this.setQuantity(quantity);
    this.setTotalAmount(totalAmount);
  }

  public OrderItemId id() {
    return id;
  }

  public OrderId orderId() {
    return orderId;
  }

  public ProductId productId() {
    return productId;
  }

  public ProductName productName() {
    return productName;
  }

  public Money price() {
    return price;
  }

  public Quantity quantity() {
    return quantity;
  }

  public Money totalAmount() {
    return totalAmount;
  }

  private void setId(@NonNull OrderItemId id) {
    this.id = id;
  }

  private void setOrderId(@NonNull OrderId orderId) {
    this.orderId = orderId;
  }

  private void setProductId(@NonNull ProductId productId) {
    this.productId = productId;
  }

  private void setProductName(@NonNull ProductName productName) {
    this.productName = productName;
  }

  private void setPrice(@NonNull Money price) {
    this.price = price;
  }

  private void setQuantity(@NonNull Quantity quantity) {
    this.quantity = quantity;
  }

  private void setTotalAmount(@NonNull Money totalAmount) {
    this.totalAmount = totalAmount;
  }
}
