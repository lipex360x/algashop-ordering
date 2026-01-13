package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import com.algaworks.algashop.ordering.domain.mock.AddressMock;
import com.algaworks.algashop.ordering.domain.mock.DocumentMock;
import com.algaworks.algashop.ordering.domain.mock.EmailMock;
import com.algaworks.algashop.ordering.domain.mock.FullNameMock;
import com.algaworks.algashop.ordering.domain.mock.PhoneMock;
import com.algaworks.algashop.ordering.domain.valueobject.BirthDate;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestDataBuilder {

  private CustomerTestDataBuilder() {}

  public static Customer.BrandNewCustomerBuilder brandNewCustomer() {
    return Customer.brandNew()
      .fullName(FullNameMock.build())
      .birthDate(new BirthDate(LocalDate.of(1990, 1, 1)))
      .email(EmailMock.build())
      .phone(PhoneMock.build())
      .document(DocumentMock.build())
      .promotionNotificationsAllowed(false)
      .address(AddressMock.build());
  }

  public static Customer.ExistingCustomerBuilder existingCustomer() {
    return Customer.existing()
      .id(new CustomerId())
      .fullName(FullNameMock.build())
      .birthDate(new BirthDate(LocalDate.of(1990, 1, 1)))
      .email(EmailMock.build())
      .phone(PhoneMock.build())
      .document(DocumentMock.build())
      .promotionNotificationsAllowed(true)
      .archived(false)
      .registeredAt(OffsetDateTime.now())
      .archivedAt(null)
      .loyaltyPoints(LoyaltyPoints.ZERO)
      .address(AddressMock.build());
  }
}
