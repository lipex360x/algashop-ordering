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

  public static final String ERROR_CUSTOMER_ARCHIVED
    = "Customer is archived. It cannot be changed";

  public static final String ERROR_ORDER_STATUS_CANNOT_BE_CHANGED
    = "Cannot change order %s from status %s to %s";

  public static final String ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST
    = "Order %s expected date cannot be in the past";

  public static final String ERROR_ORDER_CANNOT_BE_PLACED
    = "Order %s cannot be in closed. Reason: %s";

  public static final String VALIDATION_ORDER_ITEMS_LIST_IS_EMPTY
    = "Order %s - Items cannot be empty";

  public static final String VALIDATION_ORDER_NO_SHIPPING_INFO
    = "Order %s - Shipping info cannot be empty";

  public static final String VALIDATION_ORDER_NO_BILLING_INFO
    = "Order %s - Billing info cannot be empty";

  public static final String VALIDATION_ORDER_INVALID_SHIPPING_COST
    = "Order %s - Invalid shipping cost";

  public static final String VALIDATION_ORDER_INVALID_EXPECTED_DATE
    = "Order %s - Invalid expected date";

  public static final String VALIDATION_ORDER_NO_PAYMENT_METHOD
    = "Order %s - Payment method cannot be empty";


}
