package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exception.ErrorMessages;
import lombok.Builder;

import java.util.Objects;

@Builder
public record ShippingInfo(FullName fullName, Document document, Phone phone, Address address) {

  public ShippingInfo {
    Objects.requireNonNull(fullName, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    Objects.requireNonNull(document, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    Objects.requireNonNull(phone, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
    Objects.requireNonNull(address, ErrorMessages.VALIDATION_ERROR_VALUE_IS_NULL);
  }

}
