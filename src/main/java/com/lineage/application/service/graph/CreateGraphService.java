package com.lineage.application.service.graph;

import com.lineage.application.port.in.graph.CreateGraphUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;

import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CreateGraphService implements CreateGraphUseCase {

    private final GraphRepository graphRepository;

    public CreateGraphService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public GraphId execute(CreateGraphCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        UUID graphId = UUID.randomUUID();
        GraphAggregate graph = new GraphAggregate(graphId, command.name());

        graphRepository.save(graph);

        return new GraphId(graph.getId());
    }
}