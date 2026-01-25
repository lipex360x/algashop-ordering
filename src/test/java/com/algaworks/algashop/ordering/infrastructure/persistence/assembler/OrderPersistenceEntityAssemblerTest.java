package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceEntityAssemblerTest {

  private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

  @Test
  void shouldConvertToDomain() {
    Order order = OrderDataBuilder.builder().build();
    OrderPersistenceEntity persistenceEntity = assembler.fromDomain(order);

    assertThat(persistenceEntity).satisfies(
      p -> assertThat(p.getId()).isEqualTo(order.id().value().toLong()),
      p -> assertThat(p.getCustomerId()).isEqualTo(order.customerId().value()),
      p -> assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value()),
      p -> assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value()),
      p -> assertThat(p.getStatus()).isEqualTo(order.status().name()),
      p -> assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
      p -> assertThat(p.getPlacedAt()).isEqualTo(order.placedAt()),
      p -> assertThat(p.getPaidAt()).isEqualTo(order.paidAt()),
      p -> assertThat(p.getCancelledAt()).isEqualTo(order.cancelledAt()),
      p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt())
    );
  }
}