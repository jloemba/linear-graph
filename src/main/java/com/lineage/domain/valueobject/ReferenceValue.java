package com.lineage.domain.valueobject;

import com.lineage.domain.valueobject.PropertyValue;

import java.util.Objects;
import java.util.UUID;

public final class ReferenceValue implements PropertyValue {

    private final UUID referencedNodeId;

    public ReferenceValue(UUID referencedNodeId) {
        Objects.requireNonNull(referencedNodeId, "Referenced node ID cannot be null");
        this.referencedNodeId = referencedNodeId;
    }

    public UUID getReferencedNodeId() {
        return referencedNodeId;
    }

    public boolean refersTo(UUID nodeId) {
        Objects.requireNonNull(nodeId, "Node ID cannot be null");
        return referencedNodeId.equals(nodeId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceValue)) return false;
        ReferenceValue that = (ReferenceValue) o;
        return referencedNodeId.equals(that.referencedNodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referencedNodeId);
    }

    @Override
    public String toString() {
        return "ReferenceValue{" +
                "referencedNodeId=" + referencedNodeId +
                '}';
    }
}