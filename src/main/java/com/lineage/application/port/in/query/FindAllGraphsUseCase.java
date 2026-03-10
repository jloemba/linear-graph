package com.lineage.application.port.in.query;

import java.util.List;
import java.util.UUID;

public interface FindAllGraphsUseCase {

    Result execute();

    record Result(List<GraphView> graphs) {}

    record GraphView(
        UUID id,
        String name
    ) {}
}