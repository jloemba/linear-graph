package com.lineage.application.port.in.query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FindGraphByIdUseCase {

    Result execute(Query query);

    record Query(UUID graphId) {}

    record Result(
        UUID id,
        String name,
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
        String type,
        LocalDate startDate,
        LocalDate endDate
    ) {}
}