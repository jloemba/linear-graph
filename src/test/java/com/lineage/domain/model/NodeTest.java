package com.lineage.domain.model;

import com.lineage.domain.valueobject.NodeType;
import com.lineage.domain.valueobject.StringValue;
import com.lineage.domain.model.Property;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class NodeTest {

    private static final NodeType PERSON  = NodeType.of("PERSON");
    private static final NodeType KINGDOM = NodeType.of("KINGDOM");

    // -------------------------
    // Construction
    // -------------------------
    @Nested
    class Construction {

        @Test
        void shouldCreateNodeWithValidLabel() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);

            assertThat(node.getLabel()).isEqualTo("Alice");
            assertThat(node.getType()).isEqualTo(PERSON);
            assertThat(node.getProperties()).isEmpty();
            assertThat(node.getId()).isNotNull();
        }

        @Test
        void shouldCreateNodeWithInitialProperties() {
            Property prop = new Property("name", new StringValue("Kongo"));
            Node node = new Node(UUID.randomUUID(), "Royaume Kongo", Set.of(prop), KINGDOM);

            assertThat(node.getProperty("name")).isPresent();
        }

        @Test
        void shouldThrowIfLabelIsBlank() {
            assertThatThrownBy(() -> new Node(UUID.randomUUID(), " ", null, PERSON))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node label cannot be empty");
        }

        @Test
        void shouldThrowIfLabelIsNull() {
            assertThatThrownBy(() -> new Node(UUID.randomUUID(),    null, null, PERSON))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldGenerateUniqueIds() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Alice", null, PERSON);

            assertThat(a.getId()).isNotEqualTo(b.getId());
        }
    }

    // -------------------------
    // Gestion des propriétés
    // -------------------------
    @Nested
    class PropertyManagement {

        @Test
        void shouldAddProperty() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            node.addProperty(new Property("firstName", new StringValue("Alice")));

            assertThat(node.getProperty("firstName")).isPresent();
        }

        @Test
        void shouldRejectDuplicatePropertyName() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            node.addProperty(new Property("firstName", new StringValue("Alice")));

            assertThatThrownBy(() -> node.addProperty(new Property("firstName", new StringValue("Bob"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
        }

        @Test
        void shouldRejectNullProperty() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);

            assertThatThrownBy(() -> node.addProperty(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldReplaceExistingProperty() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            node.addProperty(new Property("firstName", new StringValue("Alice")));
            node.replaceProperty(new Property("firstName", new StringValue("Alicia")));

            assertThat(node.getProperty("firstName"))
                .get()
                .extracting(p -> ((StringValue) p.getValue()).getValue())
                .isEqualTo("Alicia");
        }

        @Test
        void shouldRemoveProperty() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            node.addProperty(new Property("firstName", new StringValue("Alice")));
            node.removeProperty("firstName");

            assertThat(node.getProperty("firstName")).isEmpty();
        }

        @Test
        void shouldReturnEmptyOptionalForUnknownProperty() {
            Node node = new Node(UUID.randomUUID(),     "Alice", null, PERSON);

            assertThat(node.getProperty("unknown")).isEmpty();
        }

        @Test
        void shouldReturnUnmodifiableProperties() {
            Node node = new Node(UUID.randomUUID(), "Alice", Set.of(
                new Property("firstName", new StringValue("Alice"))
            ), PERSON);

            assertThatThrownBy(() -> node.getProperties().clear())
                .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    // -------------------------
    // Egalité
    // -------------------------
    @Nested
    class Equality {

        @Test
        void shouldBeEqualToItself() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);

            assertThat(node).isEqualTo(node);
        }

        @Test
        void shouldNotBeEqualToNodeWithDifferentId() {
            // Même label et même type mais IDs différents
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Alice", null, PERSON);

            assertThat(a).isNotEqualTo(b);
        }

        @Test
        void shouldHaveSameHashCodeAsItself() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);

            assertThat(node.hashCode()).isEqualTo(node.hashCode());
        }
    }
}