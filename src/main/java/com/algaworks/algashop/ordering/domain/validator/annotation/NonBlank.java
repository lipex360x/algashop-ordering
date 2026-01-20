package com.algaworks.algashop.ordering.domain.validator.annotation;

import java.lang.annotation.*;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.VALIDATION_ERROR_VALUE_IS_BLANK;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface NonBlank {
  String message() default VALIDATION_ERROR_VALUE_IS_BLANK;
}
