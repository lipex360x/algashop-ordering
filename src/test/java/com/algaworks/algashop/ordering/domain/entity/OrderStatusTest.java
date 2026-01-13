package com.algaworks.algashop.ordering.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderStatusTest {

  @Test
  void canChangeTo() {
    assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
    assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELLED)).isTrue();
    assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.DRAFT)).isFalse();
  }

  @Test
  void canNotChangeTo() {
    assertThat(OrderStatus.PLACED.canNotChangeTo(OrderStatus.DRAFT)).isTrue();
    assertThat(OrderStatus.DRAFT.canNotChangeTo(OrderStatus.PLACED)).isFalse();
  }

}