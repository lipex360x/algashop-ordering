package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
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

  public static ShippingDataBuilder builder(Shipping shipping) {
    return new ShippingDataBuilder(
      shipping::recipient,
      shipping::address,
      shipping::cost,
      shipping::expectedDate
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
