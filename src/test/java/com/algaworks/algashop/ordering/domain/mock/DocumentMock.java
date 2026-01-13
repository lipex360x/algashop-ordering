package com.algaworks.algashop.ordering.domain.mock;

import com.algaworks.algashop.ordering.domain.valueobject.Document;
import net.datafaker.Faker;

import java.util.Locale;

public class DocumentMock {
  static Faker faker = new Faker(Locale.US);

  public static Document build() {
    return new Document(faker.idNumber().valid());
  }
}
