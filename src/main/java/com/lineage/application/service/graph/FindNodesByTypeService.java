package com.lineage.application.service.graph;

import com.lineage.application.port.in.query.FindNodesByTypeUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.valueobject.NodeType;

import java.util.List;
import java.util.Objects;

public class FindNodesByTypeService implements FindNodesByTypeUseCase {

    private final GraphRepository graphRepository;

    public FindNodesByTypeService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public Result execute(Query query) {
        Objects.requireNonNull(query, "Query cannot be null");

        if (query.nodeType() == null || query.nodeType().isBlank())
            throw new IllegalArgumentException("Node type cannot be blank");

        GraphAggregate graph = graphRepository.findById(query.graphId())
            .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + query.graphId()));

        List<NodeView> nodes = graph.findNodesByType(NodeType.of(query.nodeType()))
            .stream()
            .map(this::toNodeView)
            .toList();

        return new Result(nodes);
    }

    private NodeView toNodeView(Node node) {
        return new NodeView(
            node.getId(),
            node.getLabel(),
            node.getType().getValue()
        );
    }
}