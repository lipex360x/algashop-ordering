package com.algaworks.algashop.ordering.domain.model.validator.annotation;

import java.lang.annotation.*;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.VALIDATION_ERROR_BIRTHDATE_IS_IN_THE_FUTURE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface DateInPast {
  String message() default VALIDATION_ERROR_BIRTHDATE_IS_IN_THE_FUTURE;
}
