package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DocumentTest {

  @Test
  void shouldThrowExceptionForNullValue() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> new Document(null));
  }

  @Test
  void shouldThrowExceptionForBlankValue() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> new Document(""));
  }

  @Test
  void shouldCreateDocument() {
    Document document = new Document("000-00-0000");
    assertThat(document.value()).hasToString("000-00-0000");
  }

  @Test
  void shouldReturnToStringCorrectly() {
    Document document = new Document("000-00-0000");
    assertThat(document.toString()).hasToString("000-00-0000");
  }

}