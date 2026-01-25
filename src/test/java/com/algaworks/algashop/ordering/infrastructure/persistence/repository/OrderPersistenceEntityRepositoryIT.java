package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderPersistenceEntityRepositoryIT {

  @Autowired
  private OrderPersistenceEntityRepository orderPersistenceEntityRepository;

  @Test
  void shouldPersist() {
    long orderId = IdGenerator.generateTSID().toLong();

    OrderPersistenceEntity entity = OrderPersistenceEntity.builder()
      .id(orderId)
      .customerId(IdGenerator.generateUUID())
      .totalItems(2)
      .totalAmount(new BigDecimal(1000))
      .status("DRAFT")
      .paymentMethod("CREDIT_CART")
      .placedAt(OffsetDateTime.now())
      .build();

    orderPersistenceEntityRepository.saveAndFlush(entity);
    assertThat(orderPersistenceEntityRepository.existsById(orderId)).isTrue();
  }

  @Test
  void shouldCount() {
    long ordersCount = orderPersistenceEntityRepository.count();
    assertThat(ordersCount).isZero();
  }

}