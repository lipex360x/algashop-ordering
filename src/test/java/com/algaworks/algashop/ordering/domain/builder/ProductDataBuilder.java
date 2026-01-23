package com.algaworks.algashop.ordering.domain.builder;

import com.algaworks.algashop.ordering.domain.utility.CustomFaker;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDataBuilder {

  private static final CustomFaker customFaker = new CustomFaker();

  @With
  private Supplier<ProductId> id = () -> customFaker.valueObject().productId();

  @With
  private Supplier<ProductName> name = () -> customFaker.valueObject().productName();

  @With
  private Supplier<Money> price = () -> customFaker.valueObject().money(2, 20);

  @With
  private Supplier<Boolean> inStock = () -> true;

  public static ProductDataBuilder builder() {
    return new ProductDataBuilder();
  }

  public Product build() {
    return Product.builder()
      .id(id.get())
      .name(name.get())
      .price(price.get())
      .inStock(inStock.get())
      .build();
  }

  public static ProductDataBuilder builder(Product product) {
    return new ProductDataBuilder(
      product::id,
      product::name,
      product::price,
      product::inStock
    );
  }
}
