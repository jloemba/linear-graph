package com.lineage.application.in.query;

import com.lineage.application.port.in.query.FindNeighboursUseCase;
import com.lineage.application.port.in.query.FindNeighboursUseCase.Query;
import com.lineage.application.port.in.query.FindNeighboursUseCase.Result;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.application.service.graph.FindNeighboursService;
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
class FindNeighboursServiceTest {

    @Mock
    private GraphRepository graphRepository;

    private FindNeighboursUseCase service;

    private UUID graphId;
    private GraphAggregate graph;

    private Node gospel;
    private Node rhythmNBlues;
    private Node rockNRoll;
    private Node muddy;

    @BeforeEach
    void setUp() {
        service = new FindNeighboursService(graphRepository);
        graphId = UUID.randomUUID();
        graph = new GraphAggregate(graphId,"Famille du Rock");

        gospel = new Node(UUID.randomUUID(), "Gospel", null, NodeType.of("GENRE"));
        rhythmNBlues = new Node(UUID.randomUUID(), "Rhythm'n'Blues", null, NodeType.of("GENRE"));
        rockNRoll = new Node(UUID.randomUUID(), "Rock'n'Roll", null, NodeType.of("GENRE"));
        muddy = new Node(UUID.randomUUID(), "Muddy Waters", null, NodeType.of("ARTIST"));

        graph.addNode(gospel);
        graph.addNode(rhythmNBlues);
        graph.addNode(rockNRoll);
        graph.addNode(muddy);

        // Gospel → R&B, Gospel → Rock'n'Roll, Muddy → Gospel
        graph.addRelationship(new Relationship(gospel, rhythmNBlues, "EVOLVED_INTO"));
        graph.addRelationship(new Relationship(gospel, rockNRoll, "EVOLVED_INTO"));
        graph.addRelationship(new Relationship(muddy, gospel, "BELONGS_TO"));
    }

    @Test
    void shouldFindAllNeighbours() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, gospel.getId());
        Result result = service.execute(query);

        assertThat(result.nodeId()).isEqualTo(gospel.getId());
        assertThat(result.neighbours()).hasSize(3);
    }

    @Test
    void shouldFindOutgoingNeighbours() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, gospel.getId());
        Result result = service.execute(query);

        assertThat(result.neighbours())
                .extracting(n -> n.label())
                .contains("Rhythm'n'Blues", "Rock'n'Roll");
    }

    @Test
    void shouldFindIncomingNeighbours() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, gospel.getId());
        Result result = service.execute(query);

        assertThat(result.neighbours())
                .extracting(n -> n.label())
                .contains("Muddy Waters");
    }

    @Test
    void shouldIncludeRelationshipType() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, gospel.getId());
        Result result = service.execute(query);

        assertThat(result.neighbours())
                .extracting(n -> n.viaRelationshipType())
                .containsExactlyInAnyOrder("EVOLVED_INTO", "EVOLVED_INTO", "BELONGS_TO");
    }

    @Test
    void shouldReturnCorrectNodeType() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, gospel.getId());
        Result result = service.execute(query);

        assertThat(result.neighbours())
                .extracting(n -> n.nodeType())
                .containsExactlyInAnyOrder("GENRE", "GENRE", "ARTIST");
    }

    @Test
    void shouldReturnEmptyWhenNoNeighbours() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        // muddy n'a aucun voisin entrant — il n'a qu'une relation sortante vers gospel
        // donc rhythmNBlues n'a que gospel comme voisin entrant et aucun sortant
        // utilisons un node isolé
        Node isolatedNode = new Node(UUID.randomUUID(), "Isolated", null, NodeType.of("GENRE"));
        graph.addNode(isolatedNode);

        Query query = new Query(graphId, isolatedNode.getId());
        Result result = service.execute(query);

        assertThat(result.neighbours()).isEmpty();
    }

    @Test
    void shouldThrowIfQueryIsNull() {
        assertThatThrownBy(() -> service.execute(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIfGraphNotFound() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.empty());

        Query query = new Query(graphId, gospel.getId());

        assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Graph not found");
    }

    @Test
    void shouldThrowIfNodeNotFound() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, UUID.randomUUID());

        assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node not found");
    }

    @Test
    void shouldThrowIfRepositoryIsNull() {
        assertThatThrownBy(() -> new FindNeighboursService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("GraphRepository cannot be null");
    }

}