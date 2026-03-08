package com.lineage.application.port.in.graph;

import java.time.LocalDate;
import java.util.UUID;

public interface ConnectNodesUseCase {

    RelationshipId execute(ConnectNodesCommand command);

    record ConnectNodesCommand(
        UUID graphId,
        UUID fromNodeId,
        UUID toNodeId,
        String type,
        LocalDate startDate,  // nullable
        LocalDate endDate     // nullable
    ) {}

    record RelationshipId(UUID value) {}
}