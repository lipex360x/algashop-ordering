package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.builder.ShippingDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ShippingTest {

  private static final CustomFaker customFaker = new CustomFaker();

  private Recipient recipient;
  private Address address;
  private Money cost;
  private LocalDate expectedDate;

  @BeforeEach
  void setup() {
    recipient = customFaker.valueObject().recipient();
    address = customFaker.valueObject().address();
    cost = customFaker.valueObject().money(20, 100);
    expectedDate = LocalDate.ofInstant(customFaker.timeAndDate().future(), UTC);
  }

  @Test
  void shouldReturnShipping() {
    Shipping shipping = ShippingDataBuilder.builder().build();
    assertThat(shipping).isNotNull();
  }

  @Test
  void shouldThrowExceptionForNullValues() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Shipping(null, null, null, null));

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Shipping(
        customFaker.valueObject().recipient(),
        null,
        null,
        null
      ));

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Shipping(
        customFaker.valueObject().recipient(),
        customFaker.valueObject().address(),
        null,
        null
      ));

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Shipping(
        customFaker.valueObject().recipient(),
        customFaker.valueObject().address(),
        customFaker.valueObject().money(),
        null
      ));
  }

  @Test
  void shouldReturnCustomShipping() {
    Shipping shipping = ShippingDataBuilder.builder()
      .withRecipient(() -> recipient)
      .withAddress(() -> address)
      .withCost(() -> cost)
      .withExpectedDate(() -> expectedDate)
      .build();

    assertWith(shipping,
      s -> assertThat(s.recipient()).isEqualTo(recipient),
      s -> assertThat(s.address()).isEqualTo(address),
      s -> assertThat(s.cost()).isEqualTo(cost),
      s -> assertThat(s.expectedDate()).isEqualTo(expectedDate)
    );
  }

  @Test
  void shouldReturnCustomMockedShipping() {

    Shipping customShipping = Shipping.builder()
      .recipient(recipient)
      .address(address)
      .cost(cost)
      .expectedDate(expectedDate)
      .build();

    Shipping shipping = ShippingDataBuilder.builder(customShipping).build();

    assertWith(shipping,
      s -> assertThat(s.recipient()).isEqualTo(recipient),
      s -> assertThat(s.address()).isEqualTo(address),
      s -> assertThat(s.cost()).isEqualTo(cost),
      s -> assertThat(s.expectedDate()).isEqualTo(expectedDate)
    );
  }

}