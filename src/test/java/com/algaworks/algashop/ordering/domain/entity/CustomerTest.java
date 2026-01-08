package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.BirthDate;
import com.algaworks.algashop.ordering.domain.valueobject.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.Document;
import com.algaworks.algashop.ordering.domain.valueobject.Email;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CustomerTest {
  Faker faker = new Faker(Locale.US);

  @Test
  void shouldArchiveAExistingCustomer() {
    Customer customer = Customer.existing()
      .id(new CustomerId())
      .fullName(new FullName(faker.name().firstName(), faker.name().lastName()))
      .birthDate(new BirthDate(LocalDate.of(1990, 1, 1)))
      .email(new Email(faker.internet().emailAddress()))
      .phone(new Phone(faker.phoneNumber().cellPhone()))
      .document(new Document(faker.idNumber().valid()))
      .promotionNotificationsAllowed(false)
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
        .build())
      .build();

    customer.archive();

    Assertions.assertWith((customer),
      c -> assertThat(c.isArchived()).isTrue(),
      c -> assertThat(c.archivedAt()).isNotNull(),
      c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
      c -> assertThat(c.email().value()).doesNotHaveToString("jhon@mail.com"),
      c -> assertThat(c.phone()).hasToString("000-000-0000"),
      c -> assertThat(c.document()).hasToString("000-00-0000"),
      c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
      c -> assertThat(c.birthDate()).isNull(),
      c -> assertThat(c.address().number()).hasToString("Anon"),
      c -> assertThat(c.address().complement()).isNull()
    );
  }

  @Test
  void shouldThrowException_whenUpdatingArchivedCustomer() {
    Customer customer = Customer.brandNew()
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
        .build())
      .build();

    customer.archive();

    assertThatExceptionOfType(CustomerArchivedException.class)
      .isThrownBy(customer::archive);

    assertThatExceptionOfType(CustomerArchivedException.class)
      .isThrownBy(customer::enablePromotionNotifications);

    assertThatExceptionOfType(CustomerArchivedException.class)
      .isThrownBy(customer::disablePromotionNotifications);

    assertThatExceptionOfType(CustomerArchivedException.class)
      .isThrownBy(() -> customer.changeName(new FullName("John", "Arbas")));

    assertThatExceptionOfType(CustomerArchivedException.class)
      .isThrownBy(() -> customer.changeEmail(new Email("doe@mail.com")));

    assertThatExceptionOfType(CustomerArchivedException.class)
      .isThrownBy(() -> customer.changePhone(new Phone("111-222-3333")));
  }

  @Test
  void shouldSumPoints_whenAddingLoyaltyPoints() {
    Customer customer = Customer.brandNew()
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
        .build())
      .build();

    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
  }

}
