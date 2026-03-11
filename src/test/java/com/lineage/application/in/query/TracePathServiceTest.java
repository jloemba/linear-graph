package com.lineage.application.query;

import com.lineage.application.port.in.query.TracePathUseCase;
import com.lineage.application.port.in.query.TracePathUseCase.TracePathQuery;
import com.lineage.application.port.in.query.TracePathUseCase.PathResult;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.application.service.graph.TracePathService;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;
import com.lineage.domain.valueobject.DateValue;
import com.lineage.domain.valueobject.NodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TracePathServiceTest {

    @Mock
    private GraphRepository graphRepository;

    private TracePathUseCase service;

    private UUID graphId;
    private GraphAggregate graph;

    private Node gospel;
    private Node rhythmNBlues;
    private Node rockNRoll;
    private Node hardRock;
    private Node heavyMetal;

    @BeforeEach
    void setUp() {
        service  = new TracePathService(graphRepository);
        graphId  = UUID.randomUUID();
        graph    = new GraphAggregate(graphId,"Famille du Rock");

        gospel       = new Node(UUID.randomUUID(), "Gospel",          null, NodeType.of("GENRE"));
        rhythmNBlues = new Node(UUID.randomUUID(), "Rhythm'n'Blues",  null, NodeType.of("GENRE"));
        rockNRoll    = new Node(UUID.randomUUID(), "Rock'n'Roll",      null, NodeType.of("GENRE"));
        hardRock     = new Node(UUID.randomUUID(), "Hard Rock",        null, NodeType.of("GENRE"));
        heavyMetal   = new Node(UUID.randomUUID(), "Heavy Metal",      null, NodeType.of("GENRE"));

        graph.addNode(gospel);
        graph.addNode(rhythmNBlues);
        graph.addNode(rockNRoll);
        graph.addNode(hardRock);
        graph.addNode(heavyMetal);

        // Gospel → R&B → Rock'n'Roll → Hard Rock → Heavy Metal
        graph.addRelationship(new Relationship(gospel,       rhythmNBlues, "EVOLVED_INTO", new DateValue(LocalDate.of(1954, 1, 1)), null));
        graph.addRelationship(new Relationship(rhythmNBlues, rockNRoll,    "EVOLVED_INTO", new DateValue(LocalDate.of(1954, 1, 1)), null));
        graph.addRelationship(new Relationship(rockNRoll,    hardRock,     "EVOLVED_INTO", new DateValue(LocalDate.of(1968, 1, 1)), null));
        graph.addRelationship(new Relationship(hardRock,     heavyMetal,   "EVOLVED_INTO", new DateValue(LocalDate.of(1982, 1, 1)), null));
    }

    // -------------------------
    // Cas nominaux
    // -------------------------
    @Nested
    class NominalCases {

        @Test
        void shouldFindDirectPath() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            TracePathQuery query = new TracePathQuery(graphId, gospel.getId(), rhythmNBlues.getId());
            PathResult result = service.execute(query);

            assertThat(result).isNotNull();
            assertThat(result.nodes()).hasSize(2);
            assertThat(result.nodes())
                .extracting(n -> n.label())
                .containsExactly("Gospel", "Rhythm'n'Blues");
        }

        @Test
        void shouldFindLongPath() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            TracePathQuery query = new TracePathQuery(graphId, gospel.getId(), heavyMetal.getId());
            PathResult result = service.execute(query);

            assertThat(result.nodes()).hasSize(5);
            assertThat(result.nodes())
                .extracting(n -> n.label())
                .containsExactly("Gospel", "Rhythm'n'Blues", "Rock'n'Roll", "Hard Rock", "Heavy Metal");
        }

        @Test
        void shouldIncludeRelationshipsInPath() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            TracePathQuery query = new TracePathQuery(graphId, gospel.getId(), rockNRoll.getId());
            PathResult result = service.execute(query);

            assertThat(result.relationships()).hasSize(2);
            assertThat(result.relationships())
                .extracting(r -> r.type())
                .containsOnly("EVOLVED_INTO");
        }

        @Test
        void shouldReturnEmptyPathWhenNoPathExists() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            // heavyMetal → gospel n'existe pas (sens inverse)
            TracePathQuery query = new TracePathQuery(graphId, heavyMetal.getId(), gospel.getId());
            PathResult result = service.execute(query);

            assertThat(result.nodes()).isEmpty();
            assertThat(result.relationships()).isEmpty();
        }

        @Test
        void shouldReturnSingleNodeWhenFromAndToAreTheSame() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            TracePathQuery query = new TracePathQuery(graphId, gospel.getId(), gospel.getId());
            PathResult result = service.execute(query);

            assertThat(result.nodes()).hasSize(1);
            assertThat(result.relationships()).isEmpty();
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

            TracePathQuery query = new TracePathQuery(graphId, gospel.getId(), heavyMetal.getId());

            assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Graph not found");
        }

        @Test
        void shouldThrowIfFromNodeNotFound() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            TracePathQuery query = new TracePathQuery(graphId, UUID.randomUUID(), heavyMetal.getId());

            assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node not found");
        }

        @Test
        void shouldThrowIfToNodeNotFound() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

            TracePathQuery query = new TracePathQuery(graphId, gospel.getId(), UUID.randomUUID());

            assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node not found");
        }
    }

    // -------------------------
    // Construction du service
    // -------------------------
    @Nested
    class ServiceConstruction {

        @Test
        void shouldThrowIfRepositoryIsNull() {
            assertThatThrownBy(() -> new TracePathService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("GraphRepository cannot be null");
        }
    }
}