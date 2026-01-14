package com.algaworks.algashop.ordering.domain.utility;

import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.BirthDate;
import com.algaworks.algashop.ordering.domain.valueobject.Document;
import com.algaworks.algashop.ordering.domain.valueobject.Email;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

public class ValueObjectProvider extends AbstractProvider<BaseProviders> {

  protected ValueObjectProvider(BaseProviders faker) {
    super(faker);
  }

  public ProductId productId() {
    return new ProductId();
  }

  public CustomerId customerId() {
    return new CustomerId();
  }

  public OrderId orderId() {
    return new OrderId();
  }

  public OrderItemId orderItemId() {
    return new OrderItemId();
  }

  public Phone phone() {
    return new Phone(faker.phoneNumber().cellPhone());
  }

  public FullName fullName() {
    return new FullName(faker.name().firstName(), faker.name().lastName());
  }

  public Email email() {
    return new Email(faker.internet().emailAddress());
  }

  public Document document() {
    return new Document(faker.idNumber().valid());
  }

  public BirthDate birthDate(){
    return new BirthDate(faker.timeAndDate().birthday());
  }

  public BirthDate birthDate(int minAge, int maxAge) {
    return new BirthDate(faker.timeAndDate().birthday(minAge, maxAge));
  }

  public LoyaltyPoints loyaltyPoints() {
    return loyaltyPoints(1,100);
  }

  public LoyaltyPoints loyaltyPoints(final int min, final int max) {
    return new LoyaltyPoints(faker.number().numberBetween(min, max));
  }

  public Address address() {
    return Address.builder()
      .number(faker.address().streetAddressNumber())
      .street(faker.address().streetAddress())
      .neighborhood(faker.address().secondaryAddress())
      .city(faker.address().cityName())
      .state(faker.address().state())
      .zipCode(new ZipCode(faker.address().zipCode()))
      .build();
  }

  public Money money(){
    return money(1, Integer.MAX_VALUE);
  }

  public Money money(final int min, final int max) {
    return new Money(Double.toString(faker.number().randomDouble(2 ,min, max)));
  }

  public Quantity quantity(){
    return quantity(0, Integer.MAX_VALUE);
  }

  public Quantity quantity(final int min, final int max) {
    return new Quantity(faker.number().numberBetween(min, max));
  }

  public ZipCode zipCode(){
    return new ZipCode(faker.address().zipCode());
  }

  public ProductName productName() {
    return new ProductName(faker.book().title());
  }
}
