package com.lineage.domain.model;

import com.lineage.domain.model.Property;
import com.lineage.domain.valueobject.NodeType;
import java.util.*;

public final class Node {

    private final UUID id;
    private final String label;
    private final Map<String, Property> properties;
    private final NodeType type;


    public Node(UUID id, String label, Set<Property> properties,NodeType type) {

        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Node label cannot be empty");
        }

        this.id = id;
        this.label = label;
        this.properties = new HashMap<>();
        this.type = type;


        if (properties != null) {
            for (Property property : properties) {
                addProperty(property);
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Collection<Property> getProperties() {
        return Collections.unmodifiableCollection(properties.values());
    }

    public Optional<Property> getProperty(String name) {
        return Optional.ofNullable(properties.get(name));
    }

    public void addProperty(Property property) {
        Objects.requireNonNull(property, "Property cannot be null");

        if (properties.containsKey(property.getName())) {
            throw new IllegalArgumentException(
                "Property '" + property.getName() + "' already exists for this node"
            );
        }

        properties.put(property.getName(), property);
    }

    public void replaceProperty(Property property) {
        Objects.requireNonNull(property, "Property cannot be null");
        properties.put(property.getName(), property);
    }

    public void removeProperty(String name) {
        properties.remove(name);
    }

    public NodeType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}