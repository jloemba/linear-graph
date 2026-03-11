package com.lineage.application.in.graph;

import com.lineage.application.port.in.graph.AddNodeUseCase;
import com.lineage.application.port.in.graph.AddNodeUseCase.AddNodeCommand;
import com.lineage.application.port.in.graph.AddNodeUseCase.NodeId;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.application.service.node.AddNodeService;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Property;
import com.lineage.domain.valueobject.NodeType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddNodeServiceTest {

    @Mock
    private GraphRepository graphRepository;

    private AddNodeUseCase service;
    private Set<Property> properties;

    private UUID graphId;
    private GraphAggregate existingGraph;

    @BeforeEach
    void setUp() {
        properties = Set.of();
        service = new AddNodeService(graphRepository);
        graphId = UUID.randomUUID();
        existingGraph = new GraphAggregate(graphId, "Famille du Rock");
    }

    // -------------------------
    // Cas nominaux
    // -------------------------
    @Nested
    class NominalCases {

        @Test
        void shouldAddNodeAndReturnNodeId() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

            AddNodeCommand command = new AddNodeCommand(graphId, "Gospel", properties, "GENRE");
            NodeId result = service.execute(command);

            assertThat(result).isNotNull();
            assertThat(result.value()).isNotNull();
        }

        @Test
        void shouldAddNodeToGraph() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

            AddNodeCommand command = new AddNodeCommand(graphId, "Gospel", properties, "GENRE");
            service.execute(command);

            assertThat(existingGraph.getNodes()).hasSize(1);
            assertThat(existingGraph.getNodes())
                    .extracting(n -> n.getLabel())
                    .contains("Gospel");
        }

        @Test
        void shouldSaveGraphAfterAddingNode() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

            AddNodeCommand command = new AddNodeCommand(graphId, "Gospel", properties, "GENRE");
            service.execute(command);

            verify(graphRepository, times(1)).save(existingGraph);
        }

        @Test
        void shouldAddNodeWithCorrectType() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));
            AddNodeCommand command = new AddNodeCommand(graphId, "Gospel", properties, "GENRE");
            service.execute(command);

            assertThat(existingGraph.getNodes())
                    .extracting(n -> n.getType())
                    .contains(NodeType.of("GENRE"));
        }

        @Test
        void shouldReturnDifferentIdForEachNode() {
            when(graphRepository.findById(graphId))
                    .thenReturn(Optional.of(new GraphAggregate(graphId, "Famille du Rock")))
                    .thenReturn(Optional.of(new GraphAggregate(graphId, "Famille du Rock")));

            NodeId id1 = service.execute(new AddNodeCommand(graphId, "Gospel", properties, "GENRE"));
            NodeId id2 = service.execute(new AddNodeCommand(graphId, "Urban Blues", properties, "GENRE"));

            assertThat(id1.value()).isNotEqualTo(id2.value());
        }
    }

    // -------------------------
    // Cas d'erreur
    // -------------------------
    @Nested
    class ErrorCases {

        @Test
        void shouldThrowIfCommandIsNull() {
            assertThatThrownBy(() -> service.execute(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldThrowIfGraphNotFound() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.empty());

            AddNodeCommand command = new AddNodeCommand(graphId, "Gospel", properties, "GENRE");

            assertThatThrownBy(() -> service.execute(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Graph not found");
        }

        @Test
        void shouldThrowIfLabelIsBlank() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.of(existingGraph));

            AddNodeCommand command = new AddNodeCommand(graphId, " ", properties, "GENRE");

            assertThatThrownBy(() -> service.execute(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void shouldNotSaveIfGraphNotFound() {
            when(graphRepository.findById(graphId)).thenReturn(Optional.empty());

            AddNodeCommand command = new AddNodeCommand(graphId, "Gospel", properties, "GENRE");

            assertThatThrownBy(() -> service.execute(command));

            verify(graphRepository, never()).save(any());
        }
    }

    // -------------------------
    // Construction du service
    // -------------------------
    @Nested
    class ServiceConstruction {

        @Test
        void shouldThrowIfRepositoryIsNull() {
            assertThatThrownBy(() -> new AddNodeService(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("GraphRepository cannot be null");
        }
    }
}