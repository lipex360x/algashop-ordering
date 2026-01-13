package com.algaworks.algashop.ordering.domain.mock;

import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import net.datafaker.Faker;

import java.util.Locale;

public class FullNameMock {

  static Faker faker = new Faker(Locale.US);

  public static FullName build() {
    return new FullName(faker.name().firstName(), faker.name().lastName());
  }

}
