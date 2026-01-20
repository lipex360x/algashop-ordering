package com.algaworks.algashop.ordering.domain.validator.annotation;

import com.algaworks.algashop.ordering.domain.validator.FieldValidation;

import java.lang.reflect.RecordComponent;
import java.time.LocalDate;

public final class AnnotationFieldValidator {

  private AnnotationFieldValidator() {}

  public static void validate(Object target) {
    if (target == null) throw new NullPointerException("target is null");

    Class<?> type = target.getClass();

    if (!type.isRecord()) {
      throw new IllegalArgumentException("Validation only supported for records: " + type.getName());
    }

    for (RecordComponent rc : type.getRecordComponents()) {
      Object value = read(target, rc);

      NonBlank nonBlank = rc.getAnnotation(NonBlank.class);
      if (nonBlank != null) {
        FieldValidation.requireNonBlank((String) value, nonBlank.message());
      }

      DateInPast dateInPast = rc.getAnnotation(DateInPast.class);
      if (dateInPast != null) {
        assert value instanceof LocalDate;
        FieldValidation.requiresDateInPast((LocalDate) value, dateInPast.message());
      }
    }
  }

  private static Object read(Object target, RecordComponent rc) {
    try {
      return rc.getAccessor().invoke(target);
    } catch (Exception e) {
      throw new IllegalStateException("Could not read record component: " + rc.getName(), e);
    }
  }
}
