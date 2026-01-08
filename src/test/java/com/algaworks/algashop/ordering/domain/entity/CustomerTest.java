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
    var id = new CustomerId();
    var fullName = new FullName(faker.name().firstName(), faker.name().lastName());
    var birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
    var email = new Email(faker.internet().emailAddress());
    var phone = new Phone(faker.phoneNumber().cellPhone());
    var document = new Document(faker.idNumber().valid());
    var promotionNotificationsAllowed = false;
    var archived = false;
    var registeredAt = OffsetDateTime.now();
    var archivedAt = OffsetDateTime.now();
    var loyaltyPoints = LoyaltyPoints.ZERO;
    var address = Address.builder()
      .number(faker.address().streetAddressNumber())
      .street(faker.address().streetAddress())
      .neighborhood(faker.address().secondaryAddress())
      .city(faker.address().cityName())
      .state(faker.address().state())
      .zipCode(new ZipCode(faker.address().zipCode()))
      .build();

    Customer customer = Customer.existing(
      id,
      fullName,
      birthDate,
      email,
      phone,
      document,
      promotionNotificationsAllowed,
      archived,
      registeredAt,
      archivedAt,
      loyaltyPoints,
      address
    );

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
    var fullName = new FullName(faker.name().firstName(), faker.name().lastName());
    var birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
    var email = new Email(faker.internet().emailAddress());
    var phone = new Phone(faker.phoneNumber().cellPhone());
    var document = new Document(faker.idNumber().valid());
    var promotionNotificationsAllowed = false;
    var address = Address.builder()
      .number(faker.address().streetAddressNumber())
      .street(faker.address().streetAddress())
      .neighborhood(faker.address().secondaryAddress())
      .city(faker.address().cityName())
      .state(faker.address().state())
      .zipCode(new ZipCode(faker.address().zipCode()))
      .build();

    Customer customer = Customer.brandNew(
      fullName,
      birthDate,
      email,
      phone,
      document,
      promotionNotificationsAllowed,
      address
    );

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
    var fullName = new FullName(faker.name().firstName(), faker.name().lastName());
    var birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
    var email = new Email(faker.internet().emailAddress());
    var phone = new Phone(faker.phoneNumber().cellPhone());
    var document = new Document(faker.idNumber().valid());
    var promotionNotificationsAllowed = false;
    var address = Address.builder()
      .number(faker.address().streetAddressNumber())
      .street(faker.address().streetAddress())
      .neighborhood(faker.address().secondaryAddress())
      .city(faker.address().cityName())
      .state(faker.address().state())
      .zipCode(new ZipCode(faker.address().zipCode()))
      .build();

    Customer customer = Customer.brandNew(
      fullName,
      birthDate,
      email,
      phone,
      document,
      promotionNotificationsAllowed,
      address
    );

    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
  }

}
