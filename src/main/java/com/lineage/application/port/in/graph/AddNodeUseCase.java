package com.lineage.application.port.in.graph;

import java.util.UUID;

public interface AddNodeUseCase {

    NodeId execute(AddNodeCommand command);

    record AddNodeCommand(
        UUID graphId,
        String label,
        String type
    ) {}

    record NodeId(UUID value) {}
}