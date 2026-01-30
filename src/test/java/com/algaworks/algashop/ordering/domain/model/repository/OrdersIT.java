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
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    order = orders.ofId(order.id()).orElseThrow();
    order.markAsPaid();

    orders.add(order);
    order = orders.ofId(order.id()).orElseThrow();

    assertThat(order.isPaid()).isTrue();
  }

  @Test
  void shouldNotAllowStaleUpdate() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .withPaidAt(() -> null)
      .withCanceledAt(() -> null)
      .build();

    orders.add(order);
    Order orderT1 = orders.ofId(order.id()).orElseThrow();
    Order orderT2 = orders.ofId(order.id()).orElseThrow();

    orderT1.markAsPaid();
    orders.add(orderT1);

    orderT2.cancel();

    assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
      .isThrownBy(() -> orders.add(orderT2));

    Order savedOrder = orders.ofId(order.id()).orElseThrow();

    assertThat(savedOrder.cancelledAt()).isNull();
    assertThat(savedOrder.paidAt()).isNotNull();
  }
}