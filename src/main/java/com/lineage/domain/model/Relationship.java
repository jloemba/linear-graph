package com.lineage.domain.model;

import com.lineage.domain.model.Node;

import java.util.Objects;
import java.util.UUID;

public final class Relationship {

    private final UUID id;
    private final Node from;
    private final Node to;
    private final String type;

    public Relationship(Node from, Node to, String type) {

        Objects.requireNonNull(from, "From node cannot be null");
        Objects.requireNonNull(to, "To node cannot be null");

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Relationship type cannot be empty");
        }

        if (from.equals(to)) {
            throw new IllegalArgumentException("A node cannot relate to itself");
        }

        this.id = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    /**
     * Two relationships are considered equal
     * if they connect the same nodes with the same type.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relationship)) return false;
        Relationship that = (Relationship) o;
        return from.equals(that.from)
                && to.equals(that.to)
                && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, type);
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "from=" + from.getId() +
                ", to=" + to.getId() +
                ", type='" + type + '\'' +
                '}';
    }
}