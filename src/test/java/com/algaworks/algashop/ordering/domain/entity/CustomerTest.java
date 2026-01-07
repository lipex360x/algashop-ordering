package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

  @Test
  void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
    var id = IdGenerator.generateUUID();
    var name = "John Doe";
    var birthDate = LocalDate.of(1990, 1, 1);
    var invalidEmail = "invalid-email";
    var phone = "456-7894-1234";
    var document = "255-441-456";
    var active = false;
    var createdAt = OffsetDateTime.now();

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
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
    var id = IdGenerator.generateUUID();
    var name = "John Doe";
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

    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> customer.changeEmail("invalid-email"));
  }
}
