package com.algaworks.algashop.ordering.domain.entity;

import java.util.Set;

public enum OrderStatus {

  DRAFT {
    @Override
    public Set<OrderStatus> allowedTransitions() {
      return Set.of(PLACED, CANCELLED);
    }
  },
  PLACED {
    @Override
    public Set<OrderStatus> allowedTransitions() {
      return Set.of(PAID, CANCELLED);
    }
  },
  PAID {
    @Override
    public Set<OrderStatus> allowedTransitions() {
      return Set.of(READY, CANCELLED);
    }
  },
  READY {
    @Override
    public Set<OrderStatus> allowedTransitions() {
      return Set.of(CANCELLED);
    }
  },
  CANCELLED {
    @Override
    public Set<OrderStatus> allowedTransitions() {
      return Set.of();
    }
  };

  public abstract Set<OrderStatus> allowedTransitions();

  public boolean canChangeTo(OrderStatus next) {
    return allowedTransitions().contains(next);
  }

  public boolean canNotChangeTo(OrderStatus next) {
    return !canChangeTo(next);
  }

}
