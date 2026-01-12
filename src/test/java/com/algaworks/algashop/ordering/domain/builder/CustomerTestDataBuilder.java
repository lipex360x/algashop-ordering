package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.BirthDate;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.Document;
import com.algaworks.algashop.ordering.domain.valueobject.Email;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;

public class CustomerTestDataBuilder {
  static Faker faker = new Faker(Locale.US);

  private CustomerTestDataBuilder() {}

  public static Customer.BrandNewCustomerBuilder brandNewCustomer() {
    return Customer.brandNew()
      .fullName(new FullName(faker.name().firstName(), faker.name().lastName()))
      .birthDate(new BirthDate(LocalDate.of(1990, 1, 1)))
      .email(new Email(faker.internet().emailAddress()))
      .phone(new Phone(faker.phoneNumber().cellPhone()))
      .document(new Document(faker.idNumber().valid()))
      .promotionNotificationsAllowed(false)
      .address(Address.builder()
        .number(faker.address().streetAddressNumber())
        .street(faker.address().streetAddress())
        .neighborhood(faker.address().secondaryAddress())
        .city(faker.address().cityName())
        .state(faker.address().state())
        .zipCode(new ZipCode(faker.address().zipCode()))
        .build());
  }

  public static Customer.ExistingCustomerBuilder existingCustomer() {
    return Customer.existing()
      .id(new CustomerId())
      .fullName(new FullName(faker.name().firstName(), faker.name().lastName()))
      .birthDate(new BirthDate(LocalDate.of(1990, 1, 1)))
      .email(new Email(faker.internet().emailAddress()))
      .phone(new Phone(faker.phoneNumber().cellPhone()))
      .document(new Document(faker.idNumber().valid()))
      .promotionNotificationsAllowed(true)
      .archived(false)
      .registeredAt(OffsetDateTime.now())
      .archivedAt(null)
      .loyaltyPoints(LoyaltyPoints.ZERO)
      .address(Address.builder()
        .number(faker.address().streetAddressNumber())
        .street(faker.address().streetAddress())
        .neighborhood(faker.address().secondaryAddress())
        .city(faker.address().cityName())
        .state(faker.address().state())
        .zipCode(new ZipCode(faker.address().zipCode()))
        .build());
  }
}
