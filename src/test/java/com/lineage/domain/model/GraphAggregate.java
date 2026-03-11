package com.lineage.domain.model;

import com.lineage.domain.valueobject.NodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

class GraphAggregateTest {

    // NodeTypes réutilisables
    private static final NodeType PERSON = NodeType.of("PERSON");
    private static final NodeType KINGDOM = NodeType.of("KINGDOM");

    private GraphAggregate graph;
    private UUID graphId;    
    @BeforeEach
    void setUp() {
        graphId = UUID.randomUUID();
        graph = new GraphAggregate(graphId,"TestGraph");
    }

    // -------------------------
    // Construction du graphe
    // -------------------------
    @Nested
    class Construction {

        @Test
        void shouldRejectBlankName() {
            assertThatThrownBy(() -> new GraphAggregate(graphId,"  "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Graph name cannot be empty");
        }

        @Test
        void shouldRejectNullName() {
            assertThatThrownBy(() -> new GraphAggregate(graphId,null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldInitializeWithEmptyCollections() {
            assertThat(graph.getNodes()).isEmpty();
            assertThat(graph.getRelationships()).isEmpty();
        }
    }

    // -------------------------
    // Gestion des nodes
    // -------------------------
    @Nested
    class NodeManagement {

        @Test
        void shouldAddNodeToGraph() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            graph.addNode(node);

            assertThat(graph.getNodes()).contains(node);
        }

        @Test
        void shouldRejectDuplicateNode() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            graph.addNode(node);

            assertThatThrownBy(() -> graph.addNode(node))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Node already exists in graph");
        }

        @Test
        void shouldRejectNullNode() {
            assertThatThrownBy(() -> graph.addNode(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldRemoveNodeFromGraph() {
            Node node = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            graph.addNode(node);

            graph.removeNode(node.getId());

            assertThat(graph.getNodes()).doesNotContain(node);
        }

        @Test
        void shouldThrowWhenRemovingNonExistentNode() {
            Node node = new Node(UUID.randomUUID(), "Ghost", null, PERSON);

            assertThatThrownBy(() -> graph.removeNode(node.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Node does not exist in graph");
        }

        @Test
        void shouldCascadeDeleteRelationshipsWhenNodeIsRemoved() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Bob", null, PERSON);
            graph.addNode(a);
            graph.addNode(b);

            Relationship rel = new Relationship(a, b, "PARENT_OF");
            graph.addRelationship(rel);

            graph.removeNode(a.getId());

            assertThat(graph.getRelationships()).doesNotContain(rel);
        }

        @Test
        void shouldFindNodesByType() {
            Node person = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node kingdom = new Node(UUID.randomUUID(), "Royaume Kongo", null, KINGDOM);
            graph.addNode(person);
            graph.addNode(kingdom);

            assertThat(graph.findNodesByType(PERSON))
                    .containsExactly(person)
                    .doesNotContain(kingdom);
        }

        @Test
        void shouldReturnEmptyListWhenNoNodeMatchesType() {
            Node person = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            graph.addNode(person);

            assertThat(graph.findNodesByType(KINGDOM)).isEmpty();
        }
    }

    // -------------------------
    // Gestion des relations
    // -------------------------
    @Nested
    class RelationshipManagement {

        @Test
        void shouldAddRelationshipWhenBothNodesExist() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Bob", null, PERSON);
            graph.addNode(a);
            graph.addNode(b);

            Relationship rel = new Relationship(a, b, "PARENT_OF");
            graph.addRelationship(rel);

            assertThat(graph.getRelationships()).contains(rel);
        }

        @Test
        void shouldThrowWhenFromNodeNotInGraph() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Bob", null, PERSON);
            graph.addNode(a); // b n'est pas ajouté

            Relationship rel = new Relationship(a, b, "PARENT_OF");

            assertThatThrownBy(() -> graph.addRelationship(rel))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Both nodes must belong to the graph");
        }

        @Test
        void shouldThrowWhenBothNodesNotInGraph() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Bob", null, PERSON);

            Relationship rel = new Relationship(a, b, "PARENT_OF");

            assertThatThrownBy(() -> graph.addRelationship(rel))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldRejectDuplicateRelationship() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Bob", null, PERSON);
            graph.addNode(a);
            graph.addNode(b);

            Relationship rel = new Relationship(a, b, "PARENT_OF");
            graph.addRelationship(rel);

            assertThatThrownBy(() -> graph.addRelationship(rel))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Relationship already exists");
        }

        @Test
        void shouldRemoveRelationship() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Bob", null, PERSON);
            graph.addNode(a);
            graph.addNode(b);

            Relationship rel = new Relationship(a, b, "PARENT_OF");
            graph.addRelationship(rel);
            graph.removeRelationship(rel);

            assertThat(graph.getRelationships()).doesNotContain(rel);
        }

        @Test
        void shouldThrowWhenRemovingNonExistentRelationship() {
            Node a = new Node(UUID.randomUUID(), "Alice", null, PERSON);
            Node b = new Node(UUID.randomUUID(), "Bob", null, PERSON);
            graph.addNode(a);
            graph.addNode(b);

            Relationship rel = new Relationship(a, b, "PARENT_OF");

            assertThatThrownBy(() -> graph.removeRelationship(rel))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Relationship does not exist");
        }

        @Test
        void shouldRejectNullRelationship() {
            assertThatThrownBy(() -> graph.addRelationship(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}