package com.lineage.domain.valueobject;

import java.util.Objects;

public final class NodeType {

    private final String value;

    private NodeType(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("NodeType cannot be null or blank");
        }
        this.value = value.trim().toUpperCase();
    }

    public static NodeType of(String value) {
        return new NodeType(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeType)) return false;
        NodeType nodeType = (NodeType) o;
        return value.equals(nodeType.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}