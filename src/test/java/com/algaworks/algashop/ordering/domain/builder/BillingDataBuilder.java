package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Email;
import com.algaworks.algashop.ordering.domain.valueobject.Recipient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class BillingDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  @With
  private Supplier<Recipient> recipient = () -> customFaker.valueObject().recipient();

  @With
  private Supplier<Address> address = () -> customFaker.valueObject().address();

  @With
  private Supplier<Email> email = () -> customFaker.valueObject().email();

  public static BillingDataBuilder builder() {
    return new BillingDataBuilder();
  }

  public static BillingDataBuilder builder(Billing billing) {
    Recipient recipient = billing.recipient();
    Address address = billing.address();
    Email email = billing.email();

    return new BillingDataBuilder(
      () -> recipient,
      () -> address,
      () -> email
    );
  }

  public Billing build() {
    return Billing.builder()
      .recipient(recipient.get())
      .address(address.get())
      .email(email.get())
      .build();
  }
}
