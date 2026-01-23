package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShoppingCartItem {

  @EqualsAndHashCode.Include
  private ShoppingCartItemId id;

  private ShoppingCartId shoppingCartId;
  private ProductId productId;
  private ProductName productName;
  private Money price;
  private Quantity quantity;
  private Money totalAmount;
  private Boolean available;

  @Builder(builderClassName = "ExistingShoppingCartItemBuilder", builderMethodName = "buildExisting")
  private ShoppingCartItem(
    ShoppingCartItemId id,
    ShoppingCartId shoppingCartId,
    Product product,
    Quantity quantity,
    Money totalAmount,
    Boolean available
  ) {
    this.setId(id);
    this.setShoppingCartId(shoppingCartId);
    this.setProductId(product.id());
    this.setProductName(product.name());
    this.setPrice(product.price());
    this.setQuantity(quantity);
    this.setTotalAmount(totalAmount);
    this.setAvailable(available);
  }

  @Builder(builderClassName = "NewShoppingCartItemBuilder", builderMethodName = "buildNew")
  private static ShoppingCartItem createNew(
    @NonNull ShoppingCartId shoppingCartId,
    @NonNull Product product,
    @NonNull Quantity quantity
  ) {
    ShoppingCartItem shoppingCartItem = new ShoppingCartItem(
      new ShoppingCartItemId(),
      shoppingCartId,
      product,
      quantity,
      Money.ZERO,
      product.inStock()
    );

    shoppingCartItem.recalculateTotals();
    return shoppingCartItem;
  }

  public ShoppingCartItemId id() {
    return id;
  }

  public ShoppingCartId shoppingCartId() {
    return shoppingCartId;
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

  public Boolean available() {
    return available;
  }

  private void recalculateTotals() {
    this.setTotalAmount(this.price.multiply(this.quantity()));
  }


  private void setId(@NonNull ShoppingCartItemId id) {
    this.id = id;
  }

  private void setShoppingCartId(@NonNull ShoppingCartId shoppingCartId) {
    this.shoppingCartId = shoppingCartId;
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

  private void setAvailable(@NonNull Boolean available) {
    this.available = available;
  }
}
