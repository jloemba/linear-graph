package com.joris.graphengine.domain.valueobject;

import com.lineage.domain.valueobject.StringValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StringValueTest {

    @Test
    void shouldCreateValidStringValue() {
        StringValue value = new StringValue("Jean");

        assertThat(value.getValue()).isEqualTo("Jean");
    }

    @Test
    void shouldThrowIfValueNull() {
        assertThatThrownBy(() -> new StringValue(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIfValueBlank() {
        assertThatThrownBy(() -> new StringValue(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equalityShouldBeBasedOnValue() {
        StringValue v1 = new StringValue("Jean");
        StringValue v2 = new StringValue("Jean");

        assertThat(v1).isEqualTo(v2);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfValuesDifferent() {
        StringValue v1 = new StringValue("Jean");
        StringValue v2 = new StringValue("Paul");

        assertThat(v1).isNotEqualTo(v2);
    }
}