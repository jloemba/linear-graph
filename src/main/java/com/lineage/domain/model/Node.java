package com.lineage.domain.model;

import com.lineage.domain.model.Property;
import java.util.*;

public final class Node {

    private final UUID id;
    private final String label;
    private final Map<String, Property> properties;

    public Node(String label, Set<Property> properties) {

        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Node label cannot be empty");
        }

        this.id = UUID.randomUUID();
        this.label = label;
        this.properties = new HashMap<>();

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