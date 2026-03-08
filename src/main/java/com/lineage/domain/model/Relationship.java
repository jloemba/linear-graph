package com.lineage.domain.model;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.lineage.domain.valueobject.DateValue;

public final class Relationship {

    private final UUID id;
    private final Node from;
    private final Node to;
    private final String type;
    private final DateValue startDate;
    private final DateValue endDate;

    public Relationship(Node from, Node to, String type) {
        this(from, to, type, null, null);
    }

    public Relationship(Node from, Node to, String type, DateValue startDate, DateValue endDate) {

        Objects.requireNonNull(from, "From node cannot be null");
        Objects.requireNonNull(to,   "To node cannot be null");

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Relationship type cannot be empty");
        }

        if (from.equals(to)) {
            throw new IllegalArgumentException("A node cannot relate to itself");
        }

        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        this.id        = UUID.randomUUID();
        this.from      = from;
        this.to        = to;
        this.type      = type;
        this.startDate = startDate;
        this.endDate   = endDate;
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

    public Optional<DateValue> getStartDate() {
        return Optional.ofNullable(startDate);
    }

    public Optional<DateValue> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public boolean isActive() {
        return endDate == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relationship)) return false;
        Relationship that = (Relationship) o;
        return from.equals(that.from)
            && to.equals(that.to)
            && type.equals(that.type)
            && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, type, startDate);
    }

    @Override
    public String toString() {
        return "Relationship{" +
            "from=" + from.getId() +
            ", to=" + to.getId() +
            ", type='" + type + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            '}';
    }
}