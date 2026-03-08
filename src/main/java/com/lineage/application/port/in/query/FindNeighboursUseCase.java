package com.lineage.application.port.in.query;

import java.util.List;
import java.util.UUID;

public interface FindNeighboursUseCase {

    Result execute(Query query);

    record Query(
        UUID graphId,
        UUID nodeId
    ) {}

    record Result(
        UUID nodeId,
        List<NeighbourView> neighbours
    ) {}

    record NeighbourView(
        UUID nodeId,
        String label,
        String nodeType,
        String viaRelationshipType
    ) {}
}