package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class BillingTest {

  private static final CustomFaker customFaker = new CustomFaker();

  private Recipient recipient;
  private Address address;
  private Email email;

  @BeforeEach
  void setup() {
    address = customFaker.valueObject().address();
    recipient = customFaker.valueObject().recipient();
    email = customFaker.valueObject().email();
  }

  @Test
  void shouldThrowExceptionForNullValues() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Billing(null, null, null));

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Billing(
        customFaker.valueObject().recipient(),
         null,
        null
      ));

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Billing(
        customFaker.valueObject().recipient(),
        customFaker.valueObject().address(),
        null
      ));
  }

  @Test
  void shouldReturnBillingInfo() {
    Billing billing = Billing.builder()
      .recipient(recipient)
      .address(address)
      .email(email)
      .build();

    assertWith(billing,
      b -> assertThat(b.recipient().fullName()).isEqualTo(recipient.fullName()),
      b -> assertThat(b.recipient().document()).isEqualTo(recipient.document()),
      b -> assertThat(b.recipient().phone()).isEqualTo(recipient.phone()),
      b -> assertThat(b.address()).isEqualTo(address)
    );
  }

}