package com.lineage.application.port.in.query;

import java.util.List;
import java.util.UUID;

public interface FindAncestorsUseCase {

    Result execute(Query query);

    record Query(
        UUID graphId,
        UUID nodeId,
        String relationshipType,
        int maxDepth
    ) {}

    record Result(
        UUID nodeId,
        List<AncestorView> ancestors
    ) {}

    record AncestorView(
        UUID nodeId,
        String label,
        String nodeType,
        int depth
    ) {}
}