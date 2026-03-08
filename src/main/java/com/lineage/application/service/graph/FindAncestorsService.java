package com.lineage.application.service.graph;

import com.lineage.application.port.in.query.FindAncestorsUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;

import java.util.*;

public class FindAncestorsService implements FindAncestorsUseCase {

    private final GraphRepository graphRepository;

    public FindAncestorsService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public Result execute(Query query) {
        Objects.requireNonNull(query, "Query cannot be null");

        if (query.maxDepth() <= 0)
            throw new IllegalArgumentException("Max depth must be greater than 0");

        GraphAggregate graph = graphRepository.findById(query.graphId())
            .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + query.graphId()));

        Node startNode = graph.getNode(query.nodeId())
            .orElseThrow(() -> new IllegalArgumentException("Node not found : " + query.nodeId()));

        List<AncestorView> ancestors = findAncestors(
            graph,
            startNode,
            query.relationshipType(),
            query.maxDepth()
        );

        return new Result(startNode.getId(), ancestors);
    }

    // -------------------------
    // BFS remontant
    // -------------------------
    private List<AncestorView> findAncestors(
        GraphAggregate graph,
        Node startNode,
        String relationshipType,
        int maxDepth
    ) {
        List<AncestorView> ancestors = new ArrayList<>();
        Queue<NodeWithDepth> queue   = new LinkedList<>();
        Set<UUID> visited            = new HashSet<>();

        queue.add(new NodeWithDepth(startNode, 0));
        visited.add(startNode.getId());

        while (!queue.isEmpty()) {
            NodeWithDepth current = queue.poll();

            if (current.depth() >= maxDepth) continue;

            for (Node parent : getParents(graph, current.node(), relationshipType)) {
                if (visited.contains(parent.getId())) continue;

                int depth = current.depth() + 1;

                ancestors.add(new AncestorView(
                    parent.getId(),
                    parent.getLabel(),
                    parent.getType().getValue(),
                    depth
                ));

                visited.add(parent.getId());
                queue.add(new NodeWithDepth(parent, depth));
            }
        }

        return ancestors;
    }

    // -------------------------
    // Parents directs d'un node
    // -------------------------
    private List<Node> getParents(GraphAggregate graph, Node node, String relationshipType) {
        return graph.getRelationships().stream()
            .filter(r -> r.getTo().getId().equals(node.getId()))
            .filter(r -> r.getType().equals(relationshipType))
            .map(Relationship::getFrom)
            .toList();
    }

    // -------------------------
    // Record interne pour le BFS
    // -------------------------
    private record NodeWithDepth(Node node, int depth) {}
}