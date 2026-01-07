package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

  @Test
  void testingCustomer() {
    Customer customer = new Customer(
      IdGenerator.generateUUID(),
      "John Doe",
      LocalDate.of(1988,10,9),
      "john@mail.com",
      "312312313",
      "99904445SS",
      true,
      OffsetDateTime.now()
    );
    customer.addLoyaltyPoints(10);
    System.out.println(customer.id());
    System.out.println(IdGenerator.generateUUID());
  }

}
