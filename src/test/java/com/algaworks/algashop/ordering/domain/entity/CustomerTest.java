package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CustomerTest {

  @Test
  void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
    var id = new CustomerId();
    var name = new FullName("John", "Doe");
    var birthDate = LocalDate.of(1990, 1, 1);
    var invalidEmail = "invalid-email";
    var phone = "456-7894-1234";
    var document = "255-441-456";
    var active = false;
    var createdAt = OffsetDateTime.now();

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Customer(
        id,
        name,
        birthDate,
        invalidEmail,
        phone,
        document,
        active,
        createdAt
      ));
  }

  @Test
  void given_invalidEmail_whenTryUpdateCustomerEmail_shouldGenerateException() {
    var id = new CustomerId();
    var name = new FullName("John", "Doe");
    var birthDate = LocalDate.of(1990, 1, 1);
    var email = "mail@mail.com";
    var phone = "456-7894-1234";
    var document = "255-441-456";
    var active = false;
    var createdAt = OffsetDateTime.now();

    Customer customer = new Customer(
      id,
      name,
      birthDate,
      email,
      phone,
      document,
      active,
      createdAt
    );

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> customer.changeEmail("invalid-email"));
  }

  @Test
  void given_unarchivedCustomer_whenArchive_shouldAnonymize() {
    var id = new CustomerId();
    var name = new FullName("John", "Doe");
    var birthDate = LocalDate.of(1990, 1, 1);
    var email = "jhon@mail.com";
    var phone = "456-7894-1234";
    var document = "255-441-456";
    var active = false;
    var createdAt = OffsetDateTime.now();

    Customer customer = new Customer(
      id,
      name,
      birthDate,
      email,
      phone,
      document,
      active,
      createdAt
    );

    customer.archive();

    Assertions.assertWith((customer),
      c -> assertThat(c.isArchived()).isTrue(),
      c -> assertThat(c.archivedAt()).isNotNull(),
      c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
      c -> assertThat(c.email()).isNotEqualTo("jhon@mail.com"),
      c -> assertThat(c.phone()).isEqualTo("000-000-0000"),
      c -> assertThat(c.document()).isEqualTo("000-00-0000"),
      c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
      c -> assertThat(c.birthDate()).isNull()
    );
  }

  @Test
  void given_archivedCustomer_whenTryUpdate_shouldGenerateException() {
    var id = new CustomerId();
    var name = new FullName("John", "Doe");
    var birthDate = LocalDate.of(1990, 1, 1);
    var email = "jhon@mail.com";
    var phone = "456-7894-1234";
    var document = "255-441-456";
    var active = false;
    var createdAt = OffsetDateTime.now();

    Customer customer = new Customer(
      id,
      name,
      birthDate,
      email,
      phone,
      document,
      active,
      createdAt
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
      .isThrownBy(() -> customer.changeEmail("doe@mail.com"));

    assertThatExceptionOfType(CustomerArchivedException.class)
      .isThrownBy(() -> customer.changePhone("111-222-3333"));
  }

  @Test
  void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
    var id = new CustomerId();
    var name = new FullName("John", "Doe");
    var birthDate = LocalDate.of(1990, 1, 1);
    var email = "jhon@mail.com";
    var phone = "456-7894-1234";
    var document = "255-441-456";
    var active = false;
    var createdAt = OffsetDateTime.now();

    Customer customer = new Customer(
      id,
      name,
      birthDate,
      email,
      phone,
      document,
      active,
      createdAt
    );

    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
  }

  @Test
  void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException() {
    var id = new CustomerId();
    var name = new FullName("John", "Doe");
    var birthDate = LocalDate.of(1990, 1, 1);
    var email = "jhon@mail.com";
    var phone = "456-7894-1234";
    var document = "255-441-456";
    var active = false;
    var createdAt = OffsetDateTime.now();

    Customer customer = new Customer(
      id,
      name,
      birthDate,
      email,
      phone,
      document,
      active,
      createdAt
    );


    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));
  }
}
