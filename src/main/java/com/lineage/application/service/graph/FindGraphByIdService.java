package com.lineage.application.service.graph;

import com.lineage.application.port.in.query.FindGraphByIdUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Property;
import com.lineage.domain.model.Relationship;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FindGraphByIdService implements FindGraphByIdUseCase {

    private final GraphRepository graphRepository;

    public FindGraphByIdService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public Result execute(Query query) {
        Objects.requireNonNull(query, "Query cannot be null");

        GraphAggregate graph = graphRepository.findById(query.graphId())
                .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + query.graphId()));

        return new Result(
                graph.getId(),
                graph.getName(),
                graph.getNodes().stream()
                        .map(this::toNodeView)
                        .toList(),
                graph.getRelationships().stream()
                        .map(this::toRelationshipView)
                        .toList());
    }

    private NodeView toNodeView(Node node) {
        return new NodeView(
                node.getId(),
                node.getLabel(),
                node.getType().getValue(),
                node.getProperties().stream()
                        .map(this::toPropertyView)
                        .toList());
    }

    private PropertyView toPropertyView(Property property) {
        return new PropertyView(
                property.getName(),
                property.getValue().toString()
        );
    }

    private RelationshipView toRelationshipView(Relationship rel) {
        return new RelationshipView(
                rel.getId(),
                rel.getFrom().getId(),
                rel.getTo().getId(),
                rel.getType(),
                rel.getStartDate().map(d -> d.getValue()).orElse(null),
                rel.getEndDate().map(d -> d.getValue()).orElse(null));
    }
}