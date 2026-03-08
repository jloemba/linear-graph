package com.lineage.application.in.query;

import com.lineage.application.port.in.query.FindNodesByTypeUseCase;
import com.lineage.application.port.in.query.FindNodesByTypeUseCase.Query;
import com.lineage.application.port.in.query.FindNodesByTypeUseCase.Result;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.application.service.graph.FindNodesByTypeService;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
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
class FindNodesByTypeServiceTest {

    @Mock
    private GraphRepository graphRepository;

    private FindNodesByTypeUseCase service;

    private UUID graphId;
    private GraphAggregate graph;

    private Node gospel;
    private Node blues;
    private Node muddy;
    private Node elvis;

    @BeforeEach
    void setUp() {
        service = new FindNodesByTypeService(graphRepository);
        graphId = UUID.randomUUID();
        graph = new GraphAggregate("Famille du Rock");

        gospel = new Node("Gospel", null, NodeType.of("GENRE"));
        blues = new Node("Urban Blues", null, NodeType.of("GENRE"));
        muddy = new Node("Muddy Waters", null, NodeType.of("ARTIST"));
        elvis = new Node("Elvis Presley", null, NodeType.of("ARTIST"));

        graph.addNode(gospel);
        graph.addNode(blues);
        graph.addNode(muddy);
        graph.addNode(elvis);
    }

    @Test
    void shouldFindAllNodesByType() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, "GENRE");
        Result result = service.execute(query);

        assertThat(result.nodes()).hasSize(2);
        assertThat(result.nodes())
                .extracting(n -> n.label())
                .containsExactlyInAnyOrder("Gospel", "Urban Blues");
    }

    @Test
    void shouldReturnCorrectNodeType() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, "ARTIST");
        Result result = service.execute(query);

        assertThat(result.nodes()).hasSize(2);
        assertThat(result.nodes())
                .extracting(n -> n.nodeType())
                .containsOnly("ARTIST");
    }

    @Test
    void shouldReturnCorrectNodeId() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, "GENRE");
        Result result = service.execute(query);

        assertThat(result.nodes())
                .extracting(n -> n.nodeId())
                .containsExactlyInAnyOrder(gospel.getId(), blues.getId());
    }

    @Test
    void shouldReturnEmptyWhenNoNodeMatchesType() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, "KINGDOM");
        Result result = service.execute(query);

        assertThat(result.nodes()).isEmpty();
    }

    @Test
    void shouldNotReturnNodesOfOtherTypes() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(graph));

        Query query = new Query(graphId, "GENRE");
        Result result = service.execute(query);

        assertThat(result.nodes())
                .extracting(n -> n.label())
                .doesNotContain("Muddy Waters", "Elvis Presley");
    }

    @Test
    void shouldThrowIfQueryIsNull() {
        assertThatThrownBy(() -> service.execute(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIfGraphNotFound() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.empty());

        Query query = new Query(graphId, "GENRE");

        assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Graph not found");
    }

    @Test
    void shouldThrowIfNodeTypeIsBlank() {
        Query query = new Query(graphId, " ");

        assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node type cannot be blank");
    }

    @Test
    void shouldThrowIfNodeTypeIsNull() {
        Query query = new Query(graphId, null);

        assertThatThrownBy(() -> service.execute(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node type cannot be blank");
    }

    @Test
    void shouldThrowIfRepositoryIsNull() {
        assertThatThrownBy(() -> new FindNodesByTypeService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("GraphRepository cannot be null");
    }

}