package com.joris.lineage.domain.model;

import com.joris.lineage.domain.valueobject.Ancestry;
import com.joris.lineage.domain.valueobject.BirthPlace;
import com.joris.lineage.domain.enums.RelationshipType;
import com.joris.lineage.domain.exception.InvalidRelationshipException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RelationshipTest {

    private Person createPerson(String firstName, String lastName, LocalDate birthDate, String countryCode) {
        BirthPlace bp = new BirthPlace("City", "Region", countryCode);
        Set<Ancestry> ancestries = Set.of(new Ancestry(countryCode));
        return new Person(firstName, lastName, birthDate, bp, null, ancestries);
    }

    @Test
    void shouldCreateParentRelationship() {
        Person parent = createPerson("John", "Doe", LocalDate.of(1970, 1, 1), "FR");
        Person child = createPerson("Mike", "Doe", LocalDate.of(2000, 1, 1), "FR");

        Relationship r = new Relationship(parent, child, RelationshipType.PARENT_OF);

        assertThat(r.getFrom()).isEqualTo(parent);
        assertThat(r.getTo()).isEqualTo(child);
        assertThat(r.getType()).isEqualTo(RelationshipType.PARENT_OF);
    }

    @Test
    void shouldCreateMarriedRelationship() {
        Person spouse1 = createPerson("Alice", "Smith", LocalDate.of(1980, 1, 1), "US");
        Person spouse2 = createPerson("Bob", "Smith", LocalDate.of(1980, 1, 1), "US");

        Relationship r = new Relationship(spouse1, spouse2, RelationshipType.MARRIED_TO);

        assertThat(r.getFrom()).isEqualTo(spouse1);
        assertThat(r.getTo()).isEqualTo(spouse2);
        assertThat(r.getType()).isEqualTo(RelationshipType.MARRIED_TO);
    }

    @Test
    void shouldThrowIfParentYoungerThanChild() {
        Person youngParent = createPerson("John", "Doe", LocalDate.of(2000, 1, 1), "FR");
        Person olderChild = createPerson("Mike", "Doe", LocalDate.of(1990, 1, 1), "FR");

        assertThatThrownBy(() ->
                new Relationship(youngParent, olderChild, RelationshipType.PARENT_OF))
                .isInstanceOf(InvalidRelationshipException.class)
                .hasMessageContaining("Parent cannot be younger than child");
    }

    @Test
    void shouldThrowIfPersonIsOwnParent() {
        Person john = createPerson("John", "Doe", LocalDate.of(1970, 1, 1), "FR");

        assertThatThrownBy(() ->
                new Relationship(john, john, RelationshipType.PARENT_OF))
                .isInstanceOf(InvalidRelationshipException.class)
                .hasMessageContaining("Person cannot be their own parent");
    }

    @Test
    void shouldThrowIfNullArguments() {
        Person john = createPerson("John", "Doe", LocalDate.of(1970, 1, 1), "FR");
        Person mike = createPerson("Mike", "Doe", LocalDate.of(2000, 1, 1), "FR");

        assertThatThrownBy(() -> new Relationship(null, mike, RelationshipType.PARENT_OF))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("from cannot be null");

        assertThatThrownBy(() -> new Relationship(john, null, RelationshipType.PARENT_OF))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("to cannot be null");

        assertThatThrownBy(() -> new Relationship(john, mike, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("type cannot be null");
    }
}