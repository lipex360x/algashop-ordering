package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.builder.ShippingDataBuilder;
import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ShippingTest {

  private static final CustomFaker customFaker = new CustomFaker();

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
    Recipient recipient = customFaker.valueObject().recipient();
    Address address = customFaker.valueObject().address();
    Money cost = customFaker.valueObject().money(20, 100);
    LocalDate expectedDate = LocalDate.ofInstant(customFaker.timeAndDate().future(), UTC);

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
    Recipient recipient = customFaker.valueObject().recipient();
    Address address = customFaker.valueObject().address();
    Money cost = customFaker.valueObject().money(20, 100);
    LocalDate expectedDate = LocalDate.ofInstant(customFaker.timeAndDate().future(), UTC);

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