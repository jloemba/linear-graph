package com.lineage.domain.model;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

import com.lineage.domain.valueobject.DateValue;
import com.lineage.domain.valueobject.StringValue;

class PropertyTest {

    @Test
    void shouldCreateValidPropertyWithStringValue() {
        Property property = new Property("firstName", new StringValue("Jean"));

        assertThat(property.getName()).isEqualTo("firstName");
        assertThat(property.getValue()).isInstanceOf(StringValue.class);
    }

    @Test
    void shouldCreateValidPropertyWithDateValue() {
        Property property = new Property("birthDate", new DateValue(LocalDate.of(1990, 1, 1)));

        assertThat(property.getName()).isEqualTo("birthDate");
        assertThat(property.getValue()).isInstanceOf(DateValue.class);
    }

    @Test
    void shouldThrowIfNameIsBlank() {
        assertThatThrownBy(() -> new Property(" ", new StringValue("Jean")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Property name cannot be empty");
    }

    @Test
    void shouldThrowIfNameIsNull() {
        assertThatThrownBy(() -> new Property(null, new StringValue("Jean")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIfValueIsNull() {
        assertThatThrownBy(() -> new Property("firstName", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBeEqualWhenNameAndValueAreTheSame() {
        Property p1 = new Property("firstName", new StringValue("Jean"));
        Property p2 = new Property("firstName", new StringValue("Jean"));

        assertThat(p1).isEqualTo(p2);
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        Property p1 = new Property("firstName", new StringValue("Jean"));
        Property p2 = new Property("firstName", new StringValue("Paul"));

        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    void shouldNotBeEqualWhenNamesAreDifferent() {
        Property p1 = new Property("firstName", new StringValue("Jean"));
        Property p2 = new Property("lastName", new StringValue("Jean"));

        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    void shouldBeEqualToItself() {
        Property p = new Property("firstName", new StringValue("Jean"));

        assertThat(p).isEqualTo(p);
    }

    @Test
    void shouldHaveConsistentHashCode() {
        Property p1 = new Property("firstName", new StringValue("Jean"));
        Property p2 = new Property("firstName", new StringValue("Jean"));

        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void shouldNotBeEqualToNull() {
        Property p = new Property("firstName", new StringValue("Jean"));

        assertThat(p).isNotEqualTo(null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        Property p = new Property("firstName", new StringValue("Jean"));

        assertThat(p).isNotEqualTo("firstName");
    }

}