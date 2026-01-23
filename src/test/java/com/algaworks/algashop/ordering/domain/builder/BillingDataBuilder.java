package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
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
    return new BillingDataBuilder(
      billing::recipient,
      billing::address,
      billing::email
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
