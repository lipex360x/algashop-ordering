package com.algaworks.algashop.ordering.domain.mock;

import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import net.datafaker.Faker;

import java.util.Locale;

public class AddressMock {

  static Faker faker = new Faker(Locale.US);

  public static Address build() {
    return Address.builder()
      .number(faker.address().streetAddressNumber())
      .street(faker.address().streetAddress())
      .neighborhood(faker.address().secondaryAddress())
      .city(faker.address().cityName())
      .state(faker.address().state())
      .zipCode(new ZipCode(faker.address().zipCode()))
      .build();
  }
}
