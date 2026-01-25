package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity.OrderPersistenceEntityBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPersistenceEntityDataBuilder {

  public static OrderPersistenceEntityBuilder existing() {
    return OrderPersistenceEntity.builder()
      .id(IdGenerator.generateTSID().toLong())
      .customerId(IdGenerator.generateUUID())
      .totalItems(2)
      .totalAmount(new BigDecimal(1000))
      .status("DRAFT")
      .paymentMethod("CREDIT_CART")
      .placedAt(OffsetDateTime.now());

  }
}
