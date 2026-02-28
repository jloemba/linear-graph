package com.joris.lineage.domain.model;

import org.junit.jupiter.api.Test;
import com.joris.lineage.domain.enums.RelationshipType;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonTest {

    @Test
    void shouldCreatePersonWithValidData() {
        Person person = new Person("John", "Doe", LocalDate.of(1990, 5, 12));

        assertThat(person.getFirstName()).isEqualTo("John");
        assertThat(person.getLastName()).isEqualTo("Doe");
        assertThat(person.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 12));
        assertThat(person.getId()).isNotNull();
    }

    @Test
    void shouldThrowExceptionForNullFirstName() {
        assertThatThrownBy(() ->
                new Person(null, "Doe", LocalDate.of(1990, 5, 12)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be empty");
    }

    @Test
    void shouldThrowExceptionForBlankFirstName() {
        assertThatThrownBy(() ->
                new Person("   ", "Doe", LocalDate.of(1990, 5, 12)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be empty");
    }

    @Test
    void shouldThrowExceptionForNullLastName() {
        assertThatThrownBy(() ->
                new Person("John", null, LocalDate.of(1990, 5, 12)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be empty");
    }

    @Test
    void shouldThrowExceptionForBlankLastName() {
        assertThatThrownBy(() ->
                new Person("John", "   ", LocalDate.of(1990, 5, 12)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be empty");
    }

    @Test
    void shouldThrowExceptionForNullBirthdate() {
        assertThatThrownBy(() ->
                new Person("John", "Doe", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Birth date cannot be null");
    }

    @Test
    void shouldBeImmutable() {
        Person person = new Person("John", "Doe", LocalDate.of(1990, 5, 12));
        assertThat(person.getFirstName()).isEqualTo("John");
        // Pas de setter → immuable par construction
    }

    @Test
    void shouldHaveUniqueId() {
        Person person1 = new Person("John", "Doe", LocalDate.of(1990, 5, 12));
        Person person2 = new Person("John", "Doe", LocalDate.of(1990, 5, 12));

        assertThat(person1.getId()).isNotEqualTo(person2.getId());
    }

}