package com.lineage.application.service.node;

import com.lineage.application.port.in.graph.AddNodeUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.valueobject.NodeType;

import java.util.Objects;
import java.util.UUID;

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

        GraphAggregate graph = graphRepository.findById(command.nodeId())
                .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + command.nodeId()));

        Node node = new Node(UUID.randomUUID(), command.label(), command.properties(), NodeType.of(command.type()));
        graph.addNode(node);

        graphRepository.save(graph);

        return new NodeId(node.getId());
    }
}
