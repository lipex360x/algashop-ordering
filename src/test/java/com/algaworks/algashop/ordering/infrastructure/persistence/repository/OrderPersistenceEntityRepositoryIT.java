package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
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

  @Test
  void shouldSetAuditingValues() {
    OrderPersistenceEntity entity = OrderPersistenceEntityDataBuilder.existing().build();
    entity = orderPersistenceEntityRepository.saveAndFlush(entity);

    assertThat(entity.getCreatedByUserId()).isNotNull();
    assertThat(entity.getLastModifiedAt()).isNotNull();
    assertThat(entity.getLastModifiedByUserId()).isNotNull();
  }

}