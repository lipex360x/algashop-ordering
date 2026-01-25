package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityDataBuilder;
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
    OrderPersistenceEntity entity = OrderPersistenceEntityDataBuilder.existing().build();
    orderPersistenceEntityRepository.saveAndFlush(entity);
    assertThat(orderPersistenceEntityRepository.existsById(entity.getId())).isTrue();
  }

  @Test
  void shouldCount() {
    long ordersCount = orderPersistenceEntityRepository.count();
    assertThat(ordersCount).isZero();
  }

}