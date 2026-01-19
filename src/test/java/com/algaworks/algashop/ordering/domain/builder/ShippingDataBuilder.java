package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.Document;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ShippingDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  @With
  private Supplier<Recipient> recipient = () -> customFaker.valueObject().recipient();

  @With
  private Supplier<Address> address = () -> customFaker.valueObject().address();

  @With
  private Supplier<Money> cost = () -> customFaker.valueObject().money();

  @With
  private Supplier<LocalDate> expectedDate = () -> LocalDate.now().plusWeeks(1);

  public static ShippingDataBuilder builder() {
    return new ShippingDataBuilder();
  }

  public static ShippingDataBuilder builder(final Shipping shipping) {
    final Recipient recipient = shipping.recipient();
    final Address address = shipping.address();
    final Money cost = shipping.cost();
    final LocalDate expectedDate = shipping.expectedDate();

    return new ShippingDataBuilder(
      () -> recipient,
      () -> address,
      () -> cost,
      () -> expectedDate
    );
  }

  public Shipping build() {
    return Shipping.builder()
      .recipient(recipient.get())
      .address(address.get())
      .cost(cost.get())
      .expectedDate(expectedDate.get())
      .build();
  }
}
