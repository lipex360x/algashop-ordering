package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
  OrdersPersistenceProvider.class,
  OrderPersistenceEntityAssembler.class,
  OrderPersistenceEntityDisassembler.class
})
class OrdersIT {

  @Autowired
  private Orders orders;

  @Test
  void shouldPersistAndFind() {
    Order originalOrder = OrderDataBuilder.builder().build();
    OrderId orderId = originalOrder.id();
    orders.add(originalOrder);

    Optional<Order> possibleOrder = orders.ofId(orderId);

    assertThat(possibleOrder).isPresent();

    Order savedOrder = possibleOrder.get();

    assertThat(savedOrder).satisfies(
      s -> assertThat(s.id()).isEqualTo(orderId),
      s -> assertThat(s.customerId()).isEqualTo(originalOrder.customerId()),
      s -> assertThat(s.totalAmount()).isEqualTo(originalOrder.totalAmount()),
      s -> assertThat(s.totalItems()).isEqualTo(originalOrder.totalItems()),
      s -> assertThat(s.placedAt()).isEqualTo(originalOrder.placedAt()),
      s -> assertThat(s.paidAt()).isEqualTo(originalOrder.paidAt()),
      s -> assertThat(s.cancelledAt()).isEqualTo(originalOrder.cancelledAt()),
      s -> assertThat(s.readyAt()).isEqualTo(originalOrder.readyAt()),
      s -> assertThat(s.status()).isEqualTo(originalOrder.status()),
      s -> assertThat(s.paymentMethod()).isEqualTo(originalOrder.paymentMethod())
    );
  }

  @Test
  void shouldUpdateExistingOrder() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    orders.add(order);
    orders.ofId(order.id()).orElseThrow();

    order.markAsPaid();
    orders.add(order);
    orders.ofId(order.id()).orElseThrow();

    assertThat(order.isPaid()).isTrue();
  }

}