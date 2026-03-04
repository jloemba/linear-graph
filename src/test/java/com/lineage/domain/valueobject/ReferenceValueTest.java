package com.lineage.domain.valueobject;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ReferenceValueTest {

    private static final UUID NODE_ID = UUID.randomUUID();

    // -------------------------
    // Construction
    // -------------------------
    @Nested
    class Construction {

        @Test
        void shouldCreateValidReference() {
            ReferenceValue value = new ReferenceValue(NODE_ID);

            assertThat(value.getReferencedNodeId()).isEqualTo(NODE_ID);
        }

        @Test
        void shouldThrowIfIdIsNull() {
            assertThatThrownBy(() -> new ReferenceValue(null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    // -------------------------
    // Egalité
    // -------------------------
    @Nested
    class Equality {

        @Test
        void shouldBeEqualForSameId() {
            ReferenceValue v1 = new ReferenceValue(NODE_ID);
            ReferenceValue v2 = new ReferenceValue(NODE_ID);

            assertThat(v1).isEqualTo(v2);
        }

        @Test
        void shouldNotBeEqualForDifferentIds() {
            ReferenceValue v1 = new ReferenceValue(UUID.randomUUID());
            ReferenceValue v2 = new ReferenceValue(UUID.randomUUID());

            assertThat(v1).isNotEqualTo(v2);
        }

        @Test
        void shouldBeEqualToItself() {
            ReferenceValue v = new ReferenceValue(NODE_ID);

            assertThat(v).isEqualTo(v);
        }

        @Test
        void shouldNotBeEqualToNull() {
            ReferenceValue v = new ReferenceValue(NODE_ID);

            assertThat(v).isNotEqualTo(null);
        }

        @Test
        void shouldNotBeEqualToDifferentType() {
            ReferenceValue v = new ReferenceValue(NODE_ID);

            assertThat(v).isNotEqualTo(NODE_ID);
        }

        @Test
        void shouldHaveConsistentHashCode() {
            ReferenceValue v1 = new ReferenceValue(NODE_ID);
            ReferenceValue v2 = new ReferenceValue(NODE_ID);

            assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
        }
    }

    // -------------------------
    // refersTo
    // -------------------------
    @Nested
    class RefersTo {

        @Test
        void shouldReturnTrueForMatchingId() {
            ReferenceValue value = new ReferenceValue(NODE_ID);

            assertThat(value.refersTo(NODE_ID)).isTrue();
        }

        @Test
        void shouldReturnFalseForDifferentId() {
            ReferenceValue value = new ReferenceValue(NODE_ID);

            assertThat(value.refersTo(UUID.randomUUID())).isFalse();
        }

        @Test
        void shouldThrowIfRefersToCalledWithNull() {
            ReferenceValue value = new ReferenceValue(NODE_ID);

            assertThatThrownBy(() -> value.refersTo(null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    // -------------------------
    // Cas d'usage métier
    // -------------------------
    @Nested
    class BusinessUseCases {

        @Test
        void shouldReferenceAPersonNode() {
            UUID personId = UUID.randomUUID();
            ReferenceValue origin = new ReferenceValue(personId);

            assertThat(origin.refersTo(personId)).isTrue();
        }

        @Test
        void shouldReferenceATerritoryNode() {
            // Modélise l'origine géographique d'une personne ou d'un peuple
            UUID territoryId = UUID.randomUUID();
            ReferenceValue origin = new ReferenceValue(territoryId);

            assertThat(origin.refersTo(territoryId)).isTrue();
        }
    }
}