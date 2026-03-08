package com.lineage.application.port.in.query;

import java.util.List;
import java.util.UUID;

public interface TracePathUseCase {

    PathResult execute(TracePathQuery query);

    record TracePathQuery(
        UUID graphId,
        UUID fromNodeId,
        UUID toNodeId
    ) {}

    record PathResult(
        List<NodeView> nodes,
        List<RelationshipView> relationships
    ) {}

    record NodeView(
        UUID id,
        String label,
        String type
    ) {}

    record RelationshipView(
        UUID id,
        UUID fromId,
        UUID toId,
        String type
    ) {}
}