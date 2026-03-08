package com.lineage.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.lineage.domain.valueobject.NodeType;

class RelationShipTest {

    private static final NodeType PERSON  = NodeType.of("PERSON");
    private static final NodeType KINGDOM = NodeType.of("KINGDOM");

    private Node alice;
    private Node bob;

    @BeforeEach
    void setUp() {
        alice = new Node("Alice", null, PERSON);
        bob   = new Node("Bob",   null, PERSON);
    }

    // -------------------------
    // Construction
    // -------------------------
    @Nested
    class Construction {

        @Test
        void shouldCreateValidRelationship() {
            Relationship rel = new Relationship(alice, bob, "PARENT_OF");

            assertThat(rel.getFrom()).isEqualTo(alice);
            assertThat(rel.getTo()).isEqualTo(bob);
            assertThat(rel.getType()).isEqualTo("PARENT_OF");
            assertThat(rel.getId()).isNotNull();
        }

        @Test
        void shouldThrowIfFromIsNull() {
            assertThatThrownBy(() -> new Relationship(null, bob, "PARENT_OF"))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldThrowIfToIsNull() {
            assertThatThrownBy(() -> new Relationship(alice, null, "PARENT_OF"))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldThrowIfTypeIsBlank() {
            assertThatThrownBy(() -> new Relationship(alice, bob, " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Relationship type cannot be empty");
        }

        @Test
        void shouldThrowIfTypeIsNull() {
            assertThatThrownBy(() -> new Relationship(alice, bob, null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldThrowIfFromAndToAreTheSameNode() {
            assertThatThrownBy(() -> new Relationship(alice, alice, "PARENT_OF"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("A node cannot relate to itself");
        }

        @Test
        void shouldGenerateUniqueIds() {
            Relationship rel1 = new Relationship(alice, bob, "PARENT_OF");
            Relationship rel2 = new Relationship(alice, bob, "PARENT_OF");

            assertThat(rel1.getId()).isNotEqualTo(rel2.getId());
        }

        @Test
        void shouldAllowDifferentTypeBetweenSameNodes() {
            Relationship parent  = new Relationship(alice, bob, "PARENT_OF");
            Relationship married = new Relationship(alice, bob, "MARRIED_TO");

            assertThat(parent).isNotEqualTo(married);
        }

        @Test
        void shouldAllowRelationshipBetweenDifferentNodeTypes() {
            Node kingdom = new Node("Royaume Kongo", null, KINGDOM);
            Relationship rel = new Relationship(alice, kingdom, "ORIGINATED_FROM");

            assertThat(rel.getFrom()).isEqualTo(alice);
            assertThat(rel.getTo()).isEqualTo(kingdom);
        }
    }

    // -------------------------
    // Egalité
    // -------------------------
    @Nested
    class Equality {

        @Test
        void shouldBeEqualForSameFromToAndType() {
            Relationship rel1 = new Relationship(alice, bob, "PARENT_OF");
            Relationship rel2 = new Relationship(alice, bob, "PARENT_OF");

            assertThat(rel1).isEqualTo(rel2);
        }

        @Test
        void shouldNotBeEqualWhenTypeIsDifferent() {
            Relationship rel1 = new Relationship(alice, bob, "PARENT_OF");
            Relationship rel2 = new Relationship(alice, bob, "MARRIED_TO");

            assertThat(rel1).isNotEqualTo(rel2);
        }

        @Test
        void shouldNotBeEqualWhenFromIsDifferent() {
            Node charlie = new Node("Charlie", null, PERSON);

            Relationship rel1 = new Relationship(alice,   bob, "PARENT_OF");
            Relationship rel2 = new Relationship(charlie, bob, "PARENT_OF");

            assertThat(rel1).isNotEqualTo(rel2);
        }

        @Test
        void shouldNotBeEqualWhenToIsDifferent() {
            Node charlie = new Node("Charlie", null, PERSON);

            Relationship rel1 = new Relationship(alice, bob,     "PARENT_OF");
            Relationship rel2 = new Relationship(alice, charlie, "PARENT_OF");

            assertThat(rel1).isNotEqualTo(rel2);
        }

        @Test
        void shouldBeEqualToItself() {
            Relationship rel = new Relationship(alice, bob, "PARENT_OF");

            assertThat(rel).isEqualTo(rel);
        }

        @Test
        void shouldHaveConsistentHashCode() {
            Relationship rel1 = new Relationship(alice, bob, "PARENT_OF");
            Relationship rel2 = new Relationship(alice, bob, "PARENT_OF");

            assertThat(rel1.hashCode()).isEqualTo(rel2.hashCode());
        }
    }

    // -------------------------
    // ToString
    // -------------------------
    @Nested
    class ToStringRepresentation {

        @Test
        void shouldContainFromAndToIds() {
            Relationship rel = new Relationship(alice, bob, "PARENT_OF");
            String str = rel.toString();

            assertThat(str)
                .contains(alice.getId().toString())
                .contains(bob.getId().toString())
                .contains("PARENT_OF");
        }
    }
}
