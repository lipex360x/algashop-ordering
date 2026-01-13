package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.mock.AddressMock;
import com.algaworks.algashop.ordering.domain.mock.DocumentMock;
import com.algaworks.algashop.ordering.domain.mock.FullNameMock;
import com.algaworks.algashop.ordering.domain.mock.PhoneMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class BillingInfoTest {

  private FullName fullName;
  private Document document;
  private Phone phone;
  private Address address;

  @BeforeEach
  void setup() {
    fullName = FullNameMock.build();
    document = DocumentMock.build();
    phone = PhoneMock.build();
    address = AddressMock.build();
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