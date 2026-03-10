package com.lineage.application.service.graph;

import com.lineage.application.port.in.graph.ConnectNodesUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;
import com.lineage.domain.valueobject.DateValue;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class ConnectNodesService implements ConnectNodesUseCase {

    private final GraphRepository graphRepository;

    public ConnectNodesService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public RelationshipId execute(ConnectNodesCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        GraphAggregate graph = graphRepository.findById(command.graphId())
            .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + command.graphId()));

        Node from = graph.getNode(command.fromNodeId())
            .orElseThrow(() -> new IllegalArgumentException("Node not found : " + command.fromNodeId()));

        Node to = graph.getNode(command.toNodeId())
            .orElseThrow(() -> new IllegalArgumentException("Node not found : " + command.toNodeId()));

        DateValue startDate = command.startDate() != null
            ? new DateValue(command.startDate())
            : null;

        DateValue endDate = command.endDate() != null
            ? new DateValue(command.endDate())
            : null;

        Relationship relationship = new Relationship(from, to, command.type(), startDate, endDate);

        graph.addRelationship(relationship);

        graphRepository.save(graph);

        return new RelationshipId(relationship.getId());
    }
}