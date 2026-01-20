package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderAddItemTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldAddItem() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Order order = OrderDataBuilder.builder(Order.draft(customerId)).build();

    Product product1 = customFaker.valueObject().product();
    Product product2 = customFaker.valueObject().product();
    Product product3 = customFaker.valueObject().product();

    assertThat(order.items()).isEmpty();

    order.addItem(
      product1,
      customFaker.valueObject().quantity(2, 4)
    );

    order.addItem(
      product2,
      customFaker.valueObject().quantity(1, 4)
    );

    order.addItem(
      product3,
      customFaker.valueObject().quantity(1, 4)
    );

    assertThat(order.items()).hasSize(3);
    assertThat(order.items()).allSatisfy(item -> assertThat(item.id()).isNotNull());
    assertThat(order.items())
      .extracting(OrderItem::productName)
      .extracting(ProductName::value)
      .containsExactlyInAnyOrder(
        product1.name().value(),
        product2.name().value(),
        product3.name().value()
      );
  }

  @Test
  void shouldThrowExceptionWhenAddItemWithProductOutOfStock() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.DRAFT)
      .build();

    Product product = ProductDataBuilder.builder()
      .withInStock(() -> false)
      .build();

    ThrowableAssert.ThrowingCallable addItemTask =
      () -> order.addItem(product, new Quantity(2));

    assertThatExceptionOfType(ProductOutOfStockException.class)
      .isThrownBy(addItemTask)
      .withMessage(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, product.id()));
  }
}
