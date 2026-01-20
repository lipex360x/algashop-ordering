package com.algaworks.algashop.ordering.domain.factory;

import com.algaworks.algashop.ordering.domain.builder.BillingDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ProductDataBuilder;
import com.algaworks.algashop.ordering.domain.builder.ShippingDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.Order;
import com.algaworks.algashop.ordering.domain.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class OrderFactoryTest {

  private static final CustomFaker customFaker = new CustomFaker();

  @Test
  void shouldGenerate() {
    CustomerId customerId = customFaker.valueObject().customerId();
    Shipping shipping = ShippingDataBuilder.builder().build();
    Billing billing = BillingDataBuilder.builder().build();
    PaymentMethod paymentMethod = customFaker.options().option(PaymentMethod.class);
    Product product = ProductDataBuilder.builder().build();
    Quantity quantity = customFaker.valueObject().quantity();

    Order order = OrderFactory.filled(customerId, shipping, billing, paymentMethod, product, quantity);

    assertWith(order,
      o -> assertThat(o.isDraft()).isTrue(),
      o -> assertThat(o.customerId()).isEqualTo(customerId),
      o -> assertThat(o.shipping()).isEqualTo(shipping),
      o -> assertThat(o.billing()).isEqualTo(billing),
      o -> assertThat(o.paymentMethod()).isEqualTo(paymentMethod),
      o -> assertThat(o.items()).hasSize(1)
    );

    order.place();
    assertThat(order.isPlaced()).isTrue();
  }

}