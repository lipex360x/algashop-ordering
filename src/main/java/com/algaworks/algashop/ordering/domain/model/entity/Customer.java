package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.BirthDate;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.VALIDATION_ERROR_FULL_NAME_IS_NULL;

public class Customer {
  private CustomerId id;
  private FullName fullName;
  private BirthDate birthDate;
  private Email email;
  private Phone phone;
  private Document document;
  private Boolean promotionNotificationsAllowed;
  private Boolean archived;
  private OffsetDateTime registeredAt;
  private OffsetDateTime archivedAt;
  private LoyaltyPoints loyaltyPoints;
  private Address address;

  @Builder(builderClassName = "ExistingCustomerBuilder", builderMethodName = "buildExisting")
  private Customer(
    CustomerId id,
    FullName fullName,
    BirthDate birthDate,
    Email email,
    Phone phone,
    Document document,
    Boolean promotionNotificationsAllowed,
    Boolean archived,
    OffsetDateTime registeredAt,
    OffsetDateTime archivedAt,
    LoyaltyPoints loyaltyPoints,
    Address address
  )
  {
    this.setId(id);
    this.setFullName(fullName);
    this.setBirthDate(birthDate);
    this.setEmail(email);
    this.setPhone(phone);
    this.setDocument(document);
    this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
    this.setArchived(archived);
    this.setRegisteredAt(registeredAt);
    this.setArchivedAt(archivedAt);
    this.setLoyaltyPoints(loyaltyPoints);
    this.setAddress(address);
  }

  @Builder(builderClassName = "NewCustomerBuilder", builderMethodName = "buildNew")
  private static Customer createBrandNew(
    FullName fullName,
    BirthDate birthDate,
    Email email,
    Phone phone,
    Document document,
    Boolean promotionNotificationsAllowed,
    Address address
  ) {
    return new Customer(
      new CustomerId(),
      fullName,
      birthDate,
      email,
      phone,
      document,
      promotionNotificationsAllowed,
      false,
      OffsetDateTime.now(),
      null,
      LoyaltyPoints.ZERO,
      address
    );
  }

  public void addLoyaltyPoints(LoyaltyPoints loyaltyPointsAdded) {
    verifyIfChangeable();
    this.setLoyaltyPoints(this.loyaltyPoints().add(loyaltyPointsAdded));
  }

  public void archive() {
    verifyIfChangeable();
    this.setArchived(true);
    this.setArchivedAt(OffsetDateTime.now());
    this.setFullName(FullName.ANONYMOUS);
    this.setPhone(Phone.ANONYMOUS);
    this.setDocument(Document.ANONYMOUS);
    this.setEmail(Email.ANONYMOUS);
    this.setBirthDate(null);
    this.setPromotionNotificationsAllowed(false);
    this.setAddress(this.address().anonymized());
  }

  public void enablePromotionNotifications() {
    verifyIfChangeable();
    this.setPromotionNotificationsAllowed(true);
  }

  public void disablePromotionNotifications() {
    verifyIfChangeable();
    this.setPromotionNotificationsAllowed(false);
  }

  public void changeName(FullName fullName) {
    verifyIfChangeable();
    this.setFullName(fullName);
  }

  public void changeEmail(Email email) {
    verifyIfChangeable();
    this.setEmail(email);
  }

  public void changePhone(Phone phone) {
    verifyIfChangeable();
    this.setPhone(phone);
  }

  public void changeAddress(Address address) {
    verifyIfChangeable();
    this.setAddress(address);
  }

  public CustomerId id() {
    return id;
  }

  public FullName fullName() {
    return fullName;
  }

  public BirthDate birthDate() {
    return birthDate;
  }

  public Email email() {
    return email;
  }

  public Phone phone() {
    return phone;
  }

  public Document document() {
    return document;
  }

  public Boolean isPromotionNotificationsAllowed() {
    return promotionNotificationsAllowed;
  }

  public Boolean isArchived() {
    return archived;
  }

  public OffsetDateTime registeredAt() {
    return registeredAt;
  }

  public OffsetDateTime archivedAt() {
    return archivedAt;
  }

  public LoyaltyPoints loyaltyPoints() {
    return loyaltyPoints;
  }

  public Address address() {
    return address;
  }

  private void setId(CustomerId id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  private void setFullName(FullName fullName) {
    Objects.requireNonNull(fullName, ErrorMessages.VALIDATION_ERROR_FULL_NAME_IS_NULL);
    this.fullName = fullName;
  }

  private void setBirthDate(BirthDate birthDate) {
    this.birthDate = birthDate;
  }

  private void setEmail(Email email) {
    Objects.requireNonNull(email, ErrorMessages.VALIDATION_ERROR_EMAIL_IS_NULL);
    this.email = email;
  }

  private void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
    Objects.requireNonNull(loyaltyPoints);
    this.loyaltyPoints = loyaltyPoints;
  }

  private void setArchivedAt(OffsetDateTime archivedAt) {
    this.archivedAt = archivedAt;
  }

  private void setRegisteredAt(OffsetDateTime registeredAt) {
    Objects.requireNonNull(registeredAt);
    this.registeredAt = registeredAt;
  }

  private void setArchived(Boolean archived) {
    Objects.requireNonNull(archived);
    this.archived = archived;
  }

  private void setPromotionNotificationsAllowed(Boolean promotionNotificationsAllowed) {
    Objects.requireNonNull(promotionNotificationsAllowed);
    this.promotionNotificationsAllowed = promotionNotificationsAllowed;
  }

  private void setDocument(Document document) {
    this.document = document;
  }

  private void setPhone(Phone phone) {
    Objects.requireNonNull(phone, ErrorMessages.VALIDATION_ERROR_PHONE_IS_NULL);
    this.phone = phone;
  }

  private void setAddress(Address address) {
    Objects.requireNonNull(address);
    this.address = address;
  }

  private void verifyIfChangeable() {
    if (Boolean.TRUE.equals(this.isArchived())) throw new CustomerArchivedException();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Customer customer = (Customer) o;
    return Objects.equals(id, customer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
