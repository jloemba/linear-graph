package com.joris.lineage.domain.model;

import com.joris.lineage.domain.enums.RelationshipType;
import com.joris.lineage.domain.exception.InvalidRelationshipException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FamilyTreeTest {

    @Test
    void shouldAddPersonToTree() {
        FamilyTree tree = new FamilyTree("Doe Family");
        Person john = new Person("John", "Doe", LocalDate.of(1970, 1, 1));

        tree.addPerson(john);

        assertThat(tree.getMembers()).contains(john);
    }

    @Test
    void shouldAddRelationship() {
        FamilyTree tree = new FamilyTree("Doe Family");
        Person parent = new Person("John", "Doe", LocalDate.of(1970, 1, 1));
        Person child = new Person("Mike", "Doe", LocalDate.of(2000, 1, 1));
        tree.addPerson(parent);
        tree.addPerson(child);

        Relationship r = new Relationship(parent, child, RelationshipType.PARENT_OF);
        tree.addRelationship(r);

        assertThat(tree.getRelationships()).contains(r);
    }

    @Test
    void shouldNotAllowDuplicatePerson() {
        FamilyTree tree = new FamilyTree("Doe Family");
        Person john = new Person("John", "Doe", LocalDate.of(1970, 1, 1));
        tree.addPerson(john);

        assertThatThrownBy(() -> tree.addPerson(john))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldNotAllowRelationshipWithNonMember() {
        FamilyTree tree = new FamilyTree("Doe Family");
        Person john = new Person("John", "Doe", LocalDate.of(1970, 1, 1));
        Person mike = new Person("Mike", "Doe", LocalDate.of(2000, 1, 1));

        tree.addPerson(john);

        Relationship r = new Relationship(john, mike, RelationshipType.PARENT_OF);

        assertThatThrownBy(() -> tree.addRelationship(r))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must belong to the tree");
    }

    @Test
    void shouldReturnUnmodifiableCollections() {
        FamilyTree tree = new FamilyTree("Doe Family");
        Person john = new Person("John", "Doe", LocalDate.of(1970, 1, 1));
        tree.addPerson(john);

        assertThatThrownBy(() -> tree.getMembers().add(john))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}