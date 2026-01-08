package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BirthDateTest {

  @Test
  void shouldThrowExceptionForNullValue() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new BirthDate(null))
      .withMessage(ErrorMessages.VALIDATION_ERROR_BIRTHDATE_IS_NULL);
  }

  @Test
  void shouldThrowExceptionForFutureDate() {
    LocalDate futureDate = LocalDate.now().plusDays(1);
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new BirthDate(futureDate))
      .withMessage(ErrorMessages.VALIDATION_ERROR_BIRTHDATE_IS_IN_THE_FUTURE);
  }

  @Test
  void shouldCreateBirthDate() {
    LocalDate pastDate = LocalDate.now().minusYears(30);
    BirthDate birthDate = new BirthDate(pastDate);
    assertThat(birthDate).isNotNull();
    assertThat(birthDate.value()).isEqualTo(pastDate);
  }


  @Test
  void shouldCalculateAge() {
    LocalDate dateInPast = LocalDate.now().minusYears(20);
    BirthDate birthDate = new BirthDate(dateInPast);
    assertThat(birthDate.age()).isEqualTo(20);
  }

  @Test
  void shouldReturnToStringCorrectly() {
    LocalDate date = LocalDate.of(2000, 1, 1);
    BirthDate birthDate = new BirthDate(date);
    assertThat(birthDate.toString()).hasToString("2000-01-01");
  }
}