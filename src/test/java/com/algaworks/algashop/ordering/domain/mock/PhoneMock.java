package com.algaworks.algashop.ordering.domain.mock;

import com.algaworks.algashop.ordering.domain.valueobject.Phone;
import net.datafaker.Faker;

import java.util.Locale;

public class PhoneMock {
  static Faker faker = new Faker(Locale.US);

  public static Phone build() {
    return new Phone(faker.phoneNumber().cellPhone());
  }
}
