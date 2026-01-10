package com.algaworks.algashop.ordering.domain.exception;

public class ErrorMessages {

  private ErrorMessages() {}

  public static final String VALIDATION_ERROR_BIRTHDATE_IS_NULL = "BirthDate cannot be null";
  public static final String VALIDATION_ERROR_BIRTHDATE_IS_IN_THE_FUTURE = "BirthDate must be a past date";

  public static final String VALIDATION_ERROR_FULL_NAME_IS_NULL = "FullName cannot be null";
  public static final String VALIDATION_ERROR_FIRST_NAME_IS_NULL = "First name cannot be null";
  public static final String VALIDATION_ERROR_LAST_NAME_IS_NULL = "Last name cannot be null";
  public static final String VALIDATION_ERROR_FIRST_NAME_IS_BLANK = "First name cannot be blank";
  public static final String VALIDATION_ERROR_LAST_NAME_IS_BLANK = "Last name cannot be blank";

  public static final String VALIDATION_ERROR_VALUE_IS_NULL = "Value cannot be null";
  public static final String VALIDATION_ERROR_VALUE_IS_BLANK = "Value cannot be blank";
  public static final String VALIDATION_ERROR_VALUE_IS_NEGATIVE = "Value cannot be negative";
  public static final String VALIDATION_ERROR_VALUE_IS_ZERO = "Value cannot be zero";
  public static final String VALIDATION_ERROR_VALUE_IS_ZERO_OR_NEGATIVE = "Value cannot be zero or negative";

  public static final String VALIDATION_ERROR_EMAIL_IS_NULL = "Email cannot be null";
  public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is invalid";

  public static final String VALIDATION_ERROR_DOCUMENT_IS_NULL = "Document cannot be null";
  public static final String VALIDATION_ERROR_DOCUMENT_IS_BLANK = "Document cannot be blank";

  public static final String VALIDATION_ERROR_PHONE_IS_NULL = "Phone cannot be null";
  public static final String VALIDATION_ERROR_PHONE_IS_BLANK = "Phone cannot be blank";

  public static final String VALIDATION_ERROR_NUMBER_IS_NEGATIVE = "Value cannot be negative";
  public static final String VALIDATION_ERROR_NUMBER_IS_ZERO = "Value cannot be zero";

  public static final String VALIDATION_ERROR_LOYALTY_POINTS_IS_NULL = "Loyalty Points cannot be null";

  public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived. It cannot be changed";

}
