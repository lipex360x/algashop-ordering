package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.builder.OrderDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
  OrdersPersistenceProvider.class,
  OrderPersistenceEntityAssembler.class,
  OrderPersistenceEntityDisassembler.class,
  SpringDataAuditingConfig.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrdersPersistenceProviderIT {

  private final OrdersPersistenceProvider persistenceProvider;
  private final OrderPersistenceEntityRepository entityRepository;

  @Autowired
  public OrdersPersistenceProviderIT(
    OrdersPersistenceProvider persistenceProvider,
    OrderPersistenceEntityRepository entityRepository
  ) {
    this.persistenceProvider = persistenceProvider;
    this.entityRepository = entityRepository;
  }

  @Test
  void shouldUpdateAndKeepPersistenceEntityState() {
    Order order = OrderDataBuilder.builder()
      .withStatus(() -> OrderStatus.PLACED)
      .build();

    long orderId = order.id().value().toLong();
    persistenceProvider.add(order);

    var persistenceEntity = entityRepository.findById(orderId).orElseThrow();

    assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());
    assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

    order = persistenceProvider.ofId(order.id()).orElseThrow();
    order.markAsPaid();
    persistenceProvider.add(order);

    persistenceEntity = entityRepository.findById(orderId).orElseThrow();

    assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());
    assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
    assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
    assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
  }
}