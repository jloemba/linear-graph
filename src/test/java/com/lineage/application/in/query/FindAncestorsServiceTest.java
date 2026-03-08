package com.lineage.application.in.query;

import com.lineage.application.port.in.query.FindAncestorsUseCase;
import com.lineage.application.port.in.query.FindAncestorsUseCase.Query;
import com.lineage.application.port.in.query.FindAncestorsUseCase.Result;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.application.service.graph.FindAncestorsService;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;
import com.lineage.domain.valueobject.NodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAncestorsServiceTest {

    @Mock
    private GraphRepository graphRepository;

    private FindAncestorsUseCase service;

    private UUID graphId;
    private GraphAggregate graph;

    private Node grandParent;
    private Node parent;
    private Node child;
    private Node grandChild;

    @BeforeEach
    void setUp() {
        service = new FindAncestorsService(graphRepository);
        graphId = UUID.randomUUID();
        graph = new GraphAggregate("Arbre Généalogique");

        grandParent = new Node("Grand-père", null, NodeType.of("PERSON"));
        parent = new Node("Père", null, NodeType.of("PERSON"));
        child = new Node("Enfant", null, NodeType.of("PERSON"));
        grandChild = new Node("Petit-fils", null, NodeType.of("PERSON"));

        graph.addNode(grandParent);
        graph.addNode(parent);
        graph.addNode(child);
        graph.addNode(grandChild);

        // Grand-père → Père → Enfant → Petit-fils
        graph.addRelationship(new Relationship(grandParent, parent, "PARENT_OF"));
        graph.addRelationship(new Relationship(parent, child, "PARENT_OF"));
        graph.addRelationship(new Relationship(child, grandChild, "PARENT_OF"));
    }

    // -------------------------
    // Cas nominaux
    // -------------------------
    @Nested
    class NominalCases {

        @Test
        void shouldFindDirectParent() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            Query query = new Query(graphId, child.getId(), "PARENT_OF", 1);
            Result result = service.execute(query);

            assertThat(result.nodeId()).isEqualTo(child.getId());
            assertThat(result.ancestors()).hasSize(1);
            assertThat(result.ancestors())
                    .extracting(a -> a.label())
                    .containsExactly("Père");
        }

        @Test
        void shouldFindAllAncestors() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            Query query = new Query(graphId, grandChild.getId(), "PARENT_OF", 10);
            Result result = service.execute(query);

            assertThat(result.ancestors()).hasSize(3);
            assertThat(result.ancestors())
                    .extracting(a -> a.label())
                    .containsExactlyInAnyOrder("Enfant", "Père", "Grand-père");
        }

        @Test
        void shouldRespectMaxDepth() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            Query query = new Query(graphId, grandChild.getId(), "PARENT_OF", 1);
            Result result = service.execute(query);

            assertThat(result.ancestors()).hasSize(1);
            assertThat(result.ancestors())
                    .extracting(a -> a.label())
                    .containsExactly("Enfant");
        }

        @Test
        void shouldReturnCorrectDepthForEachAncestor() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            Query query = new Query(graphId, grandChild.getId(), "PARENT_OF", 10);
            Result result = service.execute(query);

            assertThat(result.ancestors())
                    .extracting(a -> a.depth())
                    .containsExactlyInAnyOrder(1, 2, 3);
        }

        @Test
        void shouldReturnEmptyWhenNoAncestors() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            // grandParent n'a pas d'ancêtres
            Query query = new Query(graphId, grandParent.getId(), "PARENT_OF", 10);
            Result result = service.execute(query);

            assertThat(result.nodeId()).isEqualTo(grandParent.getId());
            assertThat(result.ancestors()).isEmpty();
        }

        @Test
        void shouldFilterByRelationshipType() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            // Ajouter une relation d'un autre type
            graph.addRelationship(new Relationship(parent, child, "MARRIED_TO"));

            Query query = new Query(graphId, child.getId(), "PARENT_OF", 10);
            Result result = service.execute(query);

            // Ne doit retourner que les ancêtres via PARENT_OF
            assertThat(result.ancestors()).hasSize(2);
            assertThat(result.ancestors())
                    .extracting(a -> a.label())
                    .containsExactlyInAnyOrder("Père", "Grand-père");
        }

        @Test
        void shouldReturnCorrectNodeType() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            Query query = new Query(graphId, child.getId(), "PARENT_OF", 1);
            Result result = service.execute(query);

            assertThat(result.ancestors())
                    .extracting(a -> a.nodeType())
                    .containsOnly("PERSON");
        }
    }

    // -------------------------
    // Cas d'erreur
    // -------------------------
    @Nested
    class ErrorCases {

        @Test
        void shouldThrowIfQueryIsNull() {
            assertThatThrownBy(() -> service.execute(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldThrowIfGraphNotFound() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.empty());

            Query query = new Query(graphId, child.getId(), "PARENT_OF", 10);

            assertThatThrownBy(() -> service.execute(query))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Graph not found");
        }

        @Test
        void shouldThrowIfNodeNotFound() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            Query query = new Query(graphId, UUID.randomUUID(), "PARENT_OF", 10);

            assertThatThrownBy(() -> service.execute(query))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Node not found");
        }

        @Test
        void shouldThrowIfMaxDepthIsZero() {
            Query query = new Query(graphId, child.getId(), "PARENT_OF", 0);

            assertThatThrownBy(() -> service.execute(query))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Max depth must be greater than 0");
        }

        @Test
        void shouldThrowIfMaxDepthIsNegative() {
            Query query = new Query(graphId, child.getId(), "PARENT_OF", -1);

            assertThatThrownBy(() -> service.execute(query))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Max depth must be greater than 0");
        }
    }

    // -------------------------
    // Construction du service
    // -------------------------
    @Nested
    class ServiceConstruction {

        @Test
        void shouldThrowIfRepositoryIsNull() {
            assertThatThrownBy(() -> new FindAncestorsService(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("GraphRepository cannot be null");
        }
    }
}