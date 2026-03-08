package com.lineage.application.port.in.query;

import java.util.List;
import java.util.UUID;

public interface FindNodesByTypeUseCase {

    Result execute(Query query);

    record Query(
        UUID graphId,
        String nodeType
    ) {}

    record Result(
        List<NodeView> nodes
    ) {}

    record NodeView(
        UUID nodeId,
        String label,
        String nodeType
    ) {}
}