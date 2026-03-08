package com.lineage.application.port.out;

import com.lineage.domain.model.GraphAggregate;

import java.util.Optional;
import java.util.UUID;

public interface GraphRepository {

    void save(GraphAggregate graph);

    Optional<GraphAggregate> findById(UUID id);

    void delete(UUID id);
}