package com.lineage.application.in.graph;

import com.lineage.application.port.in.graph.ConnectNodesUseCase;
import com.lineage.application.port.in.graph.ConnectNodesUseCase.ConnectNodesCommand;
import com.lineage.application.port.in.graph.ConnectNodesUseCase.RelationshipId;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.application.service.node.ConnectNodesService;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectNodesServiceTest {

    @Mock
    private GraphRepository graphRepository;

    private ConnectNodesUseCase service;

    private UUID graphId;
    private GraphAggregate existingGraph;
    private Node gospel;
    private Node blues;

    @BeforeEach
    void setUp() {
        service = new ConnectNodesService(graphRepository);
        graphId = UUID.randomUUID();
        existingGraph = new GraphAggregate(graphId,"Famille du Rock");

        
        gospel = new Node(UUID.randomUUID(), "Gospel", null, NodeType.of("GENRE"));
        blues = new Node(UUID.randomUUID(), "Urban Blues", null, NodeType.of("GENRE"));

        existingGraph.addNode(gospel);
        existingGraph.addNode(blues);
    }

    @Test
    void shouldConnectNodesAndReturnRelationshipId() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, gospel.getId(), blues.getId(), "EVOLVED_INTO", null, null);

        RelationshipId result = service.execute(command);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNotNull();
    }

    @Test
    void shouldAddRelationshipToGraph() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, gospel.getId(), blues.getId(), "EVOLVED_INTO", null, null);

        service.execute(command);

        assertThat(existingGraph.getRelationships()).hasSize(1);
    }

    @Test
    void shouldSaveGraphAfterConnecting() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, gospel.getId(), blues.getId(), "EVOLVED_INTO", null, null);

        service.execute(command);

        verify(graphRepository, times(1)).save(existingGraph);
    }

    @Test
    void shouldConnectNodesWithTemporality() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId,
                gospel.getId(),
                blues.getId(),
                "EVOLVED_INTO",
                LocalDate.of(1954, 1, 1),
                null);

        service.execute(command);

        assertThat(existingGraph.getRelationships())
                .extracting(r -> r.getStartDate().isPresent())
                .containsOnly(true);
    }

    @Test
    void shouldConnectNodesWithStartAndEndDate() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId,
                gospel.getId(),
                blues.getId(),
                "INFLUENCED",
                LocalDate.of(1940, 1, 1),
                LocalDate.of(1960, 1, 1));

        service.execute(command);

        assertThat(existingGraph.getRelationships())
                .extracting(r -> r.isActive())
                .containsOnly(false);
    }

    @Test
    void shouldReturnDifferentIdForEachRelationship() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        Node rock = new Node(UUID.randomUUID(), "Rock'n'Roll", null, NodeType.of("GENRE"));
        existingGraph.addNode(rock);

        RelationshipId id1 = service.execute(new ConnectNodesCommand(
                graphId, gospel.getId(), blues.getId(), "EVOLVED_INTO", null, null));
        RelationshipId id2 = service.execute(new ConnectNodesCommand(
                graphId, gospel.getId(), rock.getId(), "EVOLVED_INTO", null, null));

        assertThat(id1.value()).isNotEqualTo(id2.value());
    }

    @Test
    void shouldThrowIfCommandIsNull() {
        assertThatThrownBy(() -> service.execute(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIfGraphNotFound() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.empty());

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, gospel.getId(), blues.getId(), "EVOLVED_INTO", null, null);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Graph not found");
    }

    @Test
    void shouldThrowIfFromNodeNotFound() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, UUID.randomUUID(), blues.getId(), "EVOLVED_INTO", null, null);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node not found");
    }

    @Test
    void shouldThrowIfToNodeNotFound() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, gospel.getId(), UUID.randomUUID(), "EVOLVED_INTO", null, null);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Node not found");
    }

    @Test
    void shouldThrowIfRelationshipTypeIsBlank() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, gospel.getId(), blues.getId(), " ", null, null);

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldNotSaveIfGraphNotFound() {
        when(graphRepository.findById(graphId)).thenReturn(Optional.empty());

        ConnectNodesCommand command = new ConnectNodesCommand(
                graphId, gospel.getId(), blues.getId(), "EVOLVED_INTO", null, null);

        assertThatThrownBy(() -> service.execute(command));

        verify(graphRepository, never()).save(any());
    }

    @Test
    void shouldThrowIfRepositoryIsNull() {
        assertThatThrownBy(() -> new ConnectNodesService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("GraphRepository cannot be null");
    }

}