package com.lineage.application.service.graph;

import com.lineage.application.port.in.graph.AddNodeUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.valueobject.NodeType;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class AddNodeService implements AddNodeUseCase {

    private final GraphRepository graphRepository;

    public AddNodeService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public NodeId execute(AddNodeCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        GraphAggregate graph = graphRepository.findById(command.graphId())
            .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + command.graphId()));

        Node node = new Node(command.label(), null, NodeType.of(command.type()));

        graph.addNode(node);

        graphRepository.save(graph);

        return new NodeId(node.getId());
    }
} 
