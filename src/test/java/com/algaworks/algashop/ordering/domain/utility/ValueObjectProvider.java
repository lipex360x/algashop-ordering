package com.algaworks.algashop.ordering.domain.utility;

import com.algaworks.algashop.ordering.domain.model.valueobject.Address;
import com.algaworks.algashop.ordering.domain.model.valueobject.BirthDate;
import com.algaworks.algashop.ordering.domain.model.valueobject.Document;
import com.algaworks.algashop.ordering.domain.model.valueobject.Email;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Recipient;
import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
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

  public ShoppingCartId shoppingCartId() {
    return new ShoppingCartId();
  }

  public ShoppingCartItemId shoppingCartItemId() {
    return new ShoppingCartItemId();
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
    return loyaltyPoints(1, 100);
  }

  public LoyaltyPoints loyaltyPoints(int min, int max) {
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

  public Product product() {
    return Product.builder()
      .id(this.productId())
      .name(this.productName())
      .price(money())
      .inStock(true)
      .build();
  }

  public Product product(ProductName name) {
    return Product.builder()
      .id(this.productId())
      .name(name)
      .price(money())
      .inStock(true)
      .build();
  }

  public Product product(Money price) {
    return Product.builder()
      .id(this.productId())
      .name(this.productName())
      .price(price)
      .inStock(true)
      .build();
  }

  public Product product(Boolean inStock) {
    return Product.builder()
      .id(this.productId())
      .name(this.productName())
      .price(money())
      .inStock(inStock)
      .build();
  }

  public Recipient recipient() {
    return Recipient.builder()
      .fullName(this.fullName())
      .document(this.document())
      .phone(this.phone())
      .build();
  }

}
