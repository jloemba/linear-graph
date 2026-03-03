package com.joris.lineage.domain.model;

import com.lineage.domain.model.Property;
import com.lineage.domain.valueobject.StringValue;
import com.lineage.domain.valueobject.StringValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PropertyTest {

    @Test
    void shouldCreateValidProperty() {
        Property property = new Property("firstName", new StringValue("Jean"));

        assertThat(property.getName()).isEqualTo("firstName");
    }

    @Test
    void shouldThrowIfNameBlank() {
        assertThatThrownBy(() -> new Property(" ", new StringValue("Jean")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIfValueNull() {
        assertThatThrownBy(() -> new Property("firstName", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void equalityShouldBeBasedOnNameAndValue() {
        Property p1 = new Property("firstName", new StringValue("Jean"));
        Property p2 = new Property("firstName", new StringValue("Jean"));

        assertThat(p1).isEqualTo(p2);
    }
}