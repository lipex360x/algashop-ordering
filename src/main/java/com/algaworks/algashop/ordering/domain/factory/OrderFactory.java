package com.algaworks.algashop.ordering.domain.factory;

import com.algaworks.algashop.ordering.domain.entity.Order;
import com.algaworks.algashop.ordering.domain.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderFactory {

  public static Order filled(
    @NonNull CustomerId customerId,
    @NonNull Shipping shipping,
    @NonNull Billing billing,
    @NonNull PaymentMethod paymentMethod,
    @NonNull Product product,
    @NonNull Quantity productQuantity
  ) {
    Order order = Order.draft(customerId);
    order.changeBilling(billing);
    order.changeShipping(shipping);
    order.changePaymentMethod(paymentMethod);
    order.addItem(product, productQuantity);
    return order;
  }

}
