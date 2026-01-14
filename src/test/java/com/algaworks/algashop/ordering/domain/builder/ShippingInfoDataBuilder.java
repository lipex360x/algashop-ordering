package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.valueobject.Address;
import com.algaworks.algashop.ordering.domain.valueobject.Document;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.Phone;
import com.algaworks.algashop.ordering.domain.valueobject.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ShippingInfoDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  @With
  private Supplier<FullName> fullName = () -> customFaker.valueObject().fullName();

  @With
  private Supplier<Document> document = () -> customFaker.valueObject().document();

  @With
  private Supplier<Phone> phone = () -> customFaker.valueObject().phone();

  @With
  private Supplier<Address> address = () -> customFaker.valueObject().address();

  public static ShippingInfoDataBuilder builder() {
    return new ShippingInfoDataBuilder();
  }

  public ShippingInfo buildNew() {
    return ShippingInfo.builder()
      .fullName(fullName.get())
      .document(document.get())
      .address(address.get())
      .phone(phone.get())
      .build();
  }
}
