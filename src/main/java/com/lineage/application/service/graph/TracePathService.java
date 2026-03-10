package com.lineage.application.service.graph;

import com.lineage.application.port.in.query.TracePathUseCase;
import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class TracePathService implements TracePathUseCase {

    private final GraphRepository graphRepository;

    public TracePathService(GraphRepository graphRepository) {
        this.graphRepository = Objects.requireNonNull(graphRepository, "GraphRepository cannot be null");
    }

    @Override
    public PathResult execute(TracePathQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        GraphAggregate graph = graphRepository.findById(query.graphId())
            .orElseThrow(() -> new IllegalArgumentException("Graph not found : " + query.graphId()));

        Node from = graph.getNode(query.fromNodeId())
            .orElseThrow(() -> new IllegalArgumentException("Node not found : " + query.fromNodeId()));

        Node to = graph.getNode(query.toNodeId())
            .orElseThrow(() -> new IllegalArgumentException("Node not found : " + query.toNodeId()));

        // Cas trivial — même node
        if (from.equals(to)) {
            return new PathResult(
                List.of(toNodeView(from)),
                List.of()
            );
        }

        // BFS
        List<Node> nodePath             = bfs(graph, from, to);
        List<RelationshipView> relViews = buildRelationshipViews(graph, nodePath);
        List<NodeView> nodeViews        = nodePath.stream()
            .map(this::toNodeView)
            .toList();

        return new PathResult(nodeViews, relViews);
    }

    // -------------------------
    // BFS — Breadth First Search
    // -------------------------
    private List<Node> bfs(GraphAggregate graph, Node from, Node to) {

        // File d'attente BFS — chaque entrée est un chemin partiel
        Queue<List<Node>> queue   = new LinkedList<>();
        Set<UUID> visited         = new HashSet<>();

        queue.add(List.of(from));
        visited.add(from.getId());

        while (!queue.isEmpty()) {
            List<Node> currentPath = queue.poll();
            Node current           = currentPath.get(currentPath.size() - 1);

            for (Node neighbour : getNeighbours(graph, current)) {
                if (visited.contains(neighbour.getId())) continue;

                List<Node> newPath = new ArrayList<>(currentPath);
                newPath.add(neighbour);

                if (neighbour.equals(to)) {
                    return newPath;
                }

                visited.add(neighbour.getId());
                queue.add(newPath);
            }
        }

        // Aucun chemin trouvé
        return List.of();
    }

    // -------------------------
    // Voisins directs d'un node
    // -------------------------
    private List<Node> getNeighbours(GraphAggregate graph, Node node) {
        return graph.getRelationships().stream()
            .filter(r -> r.getFrom().getId().equals(node.getId()))
            .map(Relationship::getTo)
            .toList();
    }

    // -------------------------
    // Construction des relations du chemin
    // -------------------------
    private List<RelationshipView> buildRelationshipViews(GraphAggregate graph, List<Node> path) {
        if (path.size() < 2) return List.of();

        List<RelationshipView> views = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next    = path.get(i + 1);

            graph.getRelationships().stream()
                .filter(r ->
                    r.getFrom().getId().equals(current.getId()) &&
                    r.getTo().getId().equals(next.getId())
                )
                .findFirst()
                .map(this::toRelationshipView)
                .ifPresent(views::add);
        }

        return views;
    }

    // -------------------------
    // Mappers vers les vues
    // -------------------------
    private NodeView toNodeView(Node node) {
        return new NodeView(node.getId(), node.getLabel(), node.getType().getValue());
    }

    private RelationshipView toRelationshipView(Relationship rel) {
        return new RelationshipView(
            rel.getId(),
            rel.getFrom().getId(),
            rel.getTo().getId(),
            rel.getType()
        );
    }
}