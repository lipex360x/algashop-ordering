package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class BillingInfoTest {
  static Faker faker = new Faker(Locale.US);

  private FullName fullName;
  private Document document;
  private Phone phone;
  private Address address;

  @BeforeEach
  void setup() {
    fullName = new FullName(faker.name().firstName(), faker.name().lastName());
    document = new Document(faker.idNumber().valid());
    phone = new Phone(faker.phoneNumber().cellPhone());
    address = Address.builder()
      .number(faker.address().streetAddressNumber())
      .street(faker.address().streetAddress())
      .neighborhood(faker.address().secondaryAddress())
      .city(faker.address().cityName())
      .state(faker.address().state())
      .zipCode(new ZipCode(faker.address().zipCode()))
      .build();
  }

  @Test
  void shouldThrowExceptionForNullValues() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new BillingInfo(null, null, null, null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new BillingInfo(fullName, null, null, null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new BillingInfo(fullName, document, null, null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);

    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new BillingInfo(fullName, document, phone, null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

  @Test
  void shouldReturnBillingInfo() {
    BillingInfo billingInfo = BillingInfo.builder()
      .fullName(fullName)
      .document(document)
      .phone(phone)
      .address(address)
      .build();

    assertWith(billingInfo,
      b -> assertThat(b.fullName()).isEqualTo(fullName),
      b -> assertThat(b.document()).isEqualTo(document),
      b -> assertThat(b.phone()).isEqualTo(phone),
      b -> assertThat(b.address()).isEqualTo(address)
    );
  }

}