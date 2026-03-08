package com.lineage.application.service.graph;

import com.lineage.application.port.in.query.FindNeighboursUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FindNeighboursService implements FindNeighboursUseCase {

    private final GraphRepository graphRepository;

    public FindNeighboursService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public Result execute(Query query) {
        Objects.requireNonNull(query, "Query cannot be null");

        GraphAggregate graph = graphRepository.findById(query.graphId())
            .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + query.graphId()));

        Node node = graph.getNode(query.nodeId())
            .orElseThrow(() -> new IllegalArgumentException("Node not found : " + query.nodeId()));

        List<NeighbourView> neighbours = findNeighbours(graph, node);

        return new Result(node.getId(), neighbours);
    }

    private List<NeighbourView> findNeighbours(GraphAggregate graph, Node node) {
        List<NeighbourView> neighbours = new ArrayList<>();

        for (Relationship rel : graph.getRelationships()) {

            // Voisins sortants — node → voisin
            if (rel.getFrom().getId().equals(node.getId())) {
                neighbours.add(toNeighbourView(rel.getTo(), rel.getType()));
            }

            // Voisins entrants — voisin → node
            else if (rel.getTo().getId().equals(node.getId())) {
                neighbours.add(toNeighbourView(rel.getFrom(), rel.getType()));
            }
        }

        return neighbours;
    }

    private NeighbourView toNeighbourView(Node node, String relationshipType) {
        return new NeighbourView(
            node.getId(),
            node.getLabel(),
            node.getType().getValue(),
            relationshipType
        );
    }
}