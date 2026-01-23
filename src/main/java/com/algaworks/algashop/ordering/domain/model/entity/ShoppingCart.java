package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartDoesNotContainShoppingCartItemException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShoppingCart {

  @EqualsAndHashCode.Include
  private ShoppingCartId id;

  private CustomerId customerId;

  private Money totalAmount;

  private Quantity totalItems;

  private OffsetDateTime createdAt;

  private Set<ShoppingCartItem> items;

  @Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "buildExisting")
  private ShoppingCart(
    ShoppingCartId id,
    CustomerId customerId,
    Money totalAmount,
    Quantity totalItems,
    OffsetDateTime createdAt,
    Set<ShoppingCartItem> items

  ) {
    this.setId(id);
    this.setCustomerId(customerId);
    this.setTotalAmount(totalAmount);
    this.setTotalItems(totalItems);
    this.setCreatedAt(createdAt);
    this.setItems(items);
  }

  public static ShoppingCart startShopping(CustomerId customerId) {
    return new ShoppingCart(
      new ShoppingCartId(),
      customerId,
      Money.ZERO,
      Quantity.ZERO,
      OffsetDateTime.now(),
      new HashSet<>()
    );
  }

  public void addItem(
    @NonNull Product product,
    @NonNull Quantity quantity
  ) {
    product.checkOutOfStock();

    if (Objects.isNull(this.items)) this.items = new HashSet<>();

    ShoppingCartItem shoppingCartItem = ShoppingCartItem.buildNew()
      .shoppingCartId(this.id())
      .product(product)
      .quantity(quantity)
      .build();

    this.upsertShoppingCartItem(product, quantity, shoppingCartItem);

    this.recalculateTotals();
  }

  private void upsertShoppingCartItem(Product product, Quantity quantity, ShoppingCartItem shoppingCartItem) {
    this.findCartItemByProduct(product).ifPresentOrElse(
      i -> this.updateShoppingItem(i, product, quantity),
      () -> this.insertShoppingItem(shoppingCartItem)
    );
  }

  private void updateShoppingItem(
    @NonNull ShoppingCartItem shoppingCartItem,
    @NonNull Product product,
    @NonNull Quantity quantity
  ) {
    shoppingCartItem.refresh(product);
    shoppingCartItem.changeQuantity(shoppingCartItem.quantity().add(quantity));
  }

  private Optional<ShoppingCartItem> findCartItemByProduct(@NonNull Product product) {
    return this.items.stream()
      .filter(i -> i.productId().equals(product.id()))
      .findFirst();
  }

  private void insertShoppingItem(ShoppingCartItem shoppingCartItem) {
    this.items.add(shoppingCartItem);
  }

  public void removeItem(@NonNull ShoppingCartItemId itemId) {
    ShoppingCartItem shoppingCartItem = findItemOrFail(itemId);
    this.items.remove(shoppingCartItem);
  }

  public void empty() {
    this.items.clear();
    this.recalculateTotals();
  }

  public void changeItemQuantity(
    @NonNull ShoppingCartItemId shoppingCartItemId,
    @NonNull Quantity quantity
  ) {
    ShoppingCartItem shoppingCartItem = this.findItemOrFail(shoppingCartItemId);
    shoppingCartItem.changeQuantity(quantity);
    this.recalculateTotals();
  }

  public void refreshItem(Product product) {
    ShoppingCartItem shoppingCartItem = this.findItemOrFail(product.id());
    shoppingCartItem.refresh(product);
    this.recalculateTotals();
  }

  public ShoppingCartId id() {
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

  public OffsetDateTime createdAt() {
    return createdAt;
  }

  public Set<ShoppingCartItem> items() {
    return Collections.unmodifiableSet(items);
  }

  public ShoppingCartItem findItemOrFail(ShoppingCartItemId itemId) {
    return this.items().stream()
      .filter(i -> i.id().equals(itemId))
      .findFirst()
      .orElseThrow(() -> ShoppingCartDoesNotContainShoppingCartItemException.noItemByItemId(this.id(), itemId));
  }

  public ShoppingCartItem findItemOrFail(ProductId productId) {
    return this.items().stream()
      .filter(i -> i.productId().equals(productId))
      .findFirst()
      .orElseThrow(() -> ShoppingCartDoesNotContainShoppingCartItemException.noItemByProductId(this.id(), productId));
  }

  public Boolean containsUnavailableItems() {
    return this.items().stream().anyMatch(i -> !i.isAvailable());
  }

  private void recalculateTotals() {
    Integer totalItemsReduced = this.items().stream()
      .map(i -> i.quantity().value())
      .reduce(0, Integer::sum);

    this.setTotalItems(new Quantity(totalItemsReduced));

    BigDecimal totalItemsAmountReduced = this.items().stream()
      .map(i -> i.totalAmount().value())
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    this.setTotalAmount(new Money(totalItemsAmountReduced));
  }

  private void setId(@NonNull ShoppingCartId id) {
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

  private void setCreatedAt(@NonNull OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  private void setItems(@NonNull Set<ShoppingCartItem> items) {
    this.items = items;
  }
}
