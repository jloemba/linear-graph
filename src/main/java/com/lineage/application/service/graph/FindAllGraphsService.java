package com.lineage.application.service.graph;

import com.lineage.application.port.in.query.FindAllGraphsUseCase;
import com.lineage.application.port.out.GraphRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FindAllGraphsService implements FindAllGraphsUseCase {

    private final GraphRepository graphRepository;

    public FindAllGraphsService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public Result execute() {
        return new Result(
            graphRepository.findAll()
                .stream()
                .map(graph -> new GraphView(
                    graph.getId(),
                    graph.getName()
                ))
                .toList()
        );
    }
}