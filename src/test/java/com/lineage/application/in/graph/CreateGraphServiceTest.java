package com.lineage.application.in.graph;

import com.lineage.application.port.in.graph.CreateGraphUseCase;
import com.lineage.application.port.in.graph.CreateGraphUseCase.CreateGraphCommand;
import com.lineage.application.port.in.graph.CreateGraphUseCase.GraphId;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.application.service.graph.CreateGraphService;
import com.lineage.domain.model.GraphAggregate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateGraphServiceTest {

    @Mock
    private GraphRepository graphRepository;

    private CreateGraphUseCase service;

    @BeforeEach
    void setUp() {
        service = new CreateGraphService(graphRepository);
    }

    @Test
    void shouldCreateGraphAndReturnId() {
        CreateGraphCommand command = new CreateGraphCommand("Famille du Rock", "GENRE_TREE");

        GraphId result = service.execute(command);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNotNull();
    }

    @Test
    void shouldSaveGraphToRepository() {
        CreateGraphCommand command = new CreateGraphCommand("Famille du Rock", "GENRE_TREE");

        service.execute(command);

        verify(graphRepository, times(1)).save(any(GraphAggregate.class));
    }

    @Test
    void shouldSaveGraphWithCorrectName() {
        CreateGraphCommand command = new CreateGraphCommand("Famille du Rock", "GENRE_TREE");

        service.execute(command);

        verify(graphRepository).save(argThat(graph -> graph.getName().equals("Famille du Rock")));
    }

    @Test
    void shouldReturnDifferentIdForEachGraph() {
        CreateGraphCommand command1 = new CreateGraphCommand("Famille du Rock", "GENRE_TREE");
        CreateGraphCommand command2 = new CreateGraphCommand("Ethnies Bantou", "ETHNIC_TREE");

        GraphId id1 = service.execute(command1);
        GraphId id2 = service.execute(command2);

        assertThat(id1.value()).isNotEqualTo(id2.value());
    }

    @Test
    void shouldThrowIfCommandIsNull() {
        assertThatThrownBy(() -> service.execute(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIfNameIsBlank() {
        CreateGraphCommand command = new CreateGraphCommand(" ", "GENRE_TREE");

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Graph name cannot be empty");
    }

    @Test
    void shouldThrowIfNameIsNull() {
        CreateGraphCommand command = new CreateGraphCommand(null, "GENRE_TREE");

        assertThatThrownBy(() -> service.execute(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldNotSaveIfCommandIsInvalid() {
        CreateGraphCommand command = new CreateGraphCommand(" ", "GENRE_TREE");

        assertThatThrownBy(() -> service.execute(command));

        verify(graphRepository, never()).save(any());
    }

    @Test
    void shouldThrowIfRepositoryIsNull() {
        assertThatThrownBy(() -> new CreateGraphService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("GraphRepository cannot be null");
    }

}