package com.joris.graphengine.domain.valueobject;

import com.lineage.domain.valueobject.DateValue;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class DateValueTest {

    @Test
    void shouldCreateValidDateValue() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        DateValue value = new DateValue(date);

        assertThat(value.getValue()).isEqualTo(date);
    }

    @Test
    void shouldThrowIfDateNull() {
        assertThatThrownBy(() -> new DateValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void equalityShouldBeBasedOnDate() {
        DateValue v1 = new DateValue(LocalDate.of(2020, 1, 1));
        DateValue v2 = new DateValue(LocalDate.of(2020, 1, 1));

        assertThat(v1).isEqualTo(v2);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }

    @Test
    void shouldCompareDatesCorrectly() {
        DateValue earlier = new DateValue(LocalDate.of(2020, 1, 1));
        DateValue later = new DateValue(LocalDate.of(2021, 1, 1));

        assertThat(earlier.isBefore(later)).isTrue();
        assertThat(later.isAfter(earlier)).isTrue();
    }

    @Test
    void comparisonShouldThrowIfOtherNull() {
        DateValue value = new DateValue(LocalDate.now());

        assertThatThrownBy(() -> value.isBefore(null))
                .isInstanceOf(NullPointerException.class);
    }
}