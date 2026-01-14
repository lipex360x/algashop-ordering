package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.builder.CustomerDataBuilder;
import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.Email;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.valueobject.Phone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CustomerTest {

  @Test
  void shouldArchiveAExistingCustomer() {
    Customer customer = CustomerDataBuilder.builder().buildExisting();
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
    Customer customer = CustomerDataBuilder.builder().buildNew();
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
    Customer customer = CustomerDataBuilder.builder().buildNew();
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    customer.addLoyaltyPoints(new LoyaltyPoints(10));
    assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
  }

}
