package com.algaworks.algashop.ordering.domain.mock;

import com.algaworks.algashop.ordering.domain.valueobject.Email;
import net.datafaker.Faker;

import java.util.Locale;

public class EmailMock {
  static Faker faker = new Faker(Locale.US);

  public static Email build() {
    return new Email(faker.internet().emailAddress());
  }
}
