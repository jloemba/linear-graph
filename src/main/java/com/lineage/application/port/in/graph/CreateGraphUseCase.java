package com.lineage.application.port.in.graph;

import java.util.UUID;

public interface CreateGraphUseCase {

    GraphId execute(CreateGraphCommand command);

    record CreateGraphCommand(String name,String relationshipType ) {}

    record GraphId(UUID value) {}
}