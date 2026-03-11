package com.lineage.domain.model;

import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;
import com.lineage.domain.valueobject.NodeType;

import java.util.*;

public final class GraphAggregate {

    private final UUID id;
    private final String name;
    //private final GraphType type; // FAMILY_TREE, GEOPOLITICAL, ETHNIC...
    private final Map<UUID, Node> nodes = new HashMap<>();
    private final Set<Relationship> relationships = new HashSet<>();
    // TODO : mettre les graph policy ici 

    public GraphAggregate(UUID id, String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Graph name cannot be empty");
        }

        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    public Set<Relationship> getRelationships() {
        return Collections.unmodifiableSet(relationships);
    }

    public void addNode(Node node) {
        Objects.requireNonNull(node, "Node cannot be null");

        if (nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException("Node already exists in graph");
        }

        nodes.put(node.getId(), node);
    }

    public Optional<Node> getNode(UUID nodeId) {
        return Optional.ofNullable(nodes.get(nodeId));
    }

    public void removeNode(UUID nodeId) {
        Objects.requireNonNull(nodeId, "Node id cannot be null");

        if (!nodes.containsKey(nodeId)) {
            throw new IllegalArgumentException("Node does not exist in graph");
        }

        // Remove relationships involving this node
        relationships.removeIf(r ->
                r.getFrom().getId().equals(nodeId) ||
                r.getTo().getId().equals(nodeId)
        );

        nodes.remove(nodeId);
    }

    public void addRelationship(Relationship relationship) {
        Objects.requireNonNull(relationship, "Relationship cannot be null"); //To simplifies the code and removes the need for multiple null checks

        UUID fromId = relationship.getFrom().getId();
        UUID toId = relationship.getTo().getId();

        if (!nodes.containsKey(fromId) || !nodes.containsKey(toId)) {
            throw new IllegalArgumentException(
                    "Both nodes must belong to the graph"
            );
        }

        if (relationships.contains(relationship)) {
            throw new IllegalArgumentException(
                    "Relationship already exists"
            );
        }

        relationships.add(relationship);
    }

    public void removeRelationship(Relationship relationship) {
        Objects.requireNonNull(relationship, "Relationship cannot be null");

        if (!relationships.contains(relationship)) {
            throw new IllegalArgumentException("Relationship does not exist");
        }

        relationships.remove(relationship);
    }

    public List<Node> findNodesByType(NodeType type) {
    return nodes.values().stream()
            .filter(node -> node.getType().equals(type))
            .toList();
}
}