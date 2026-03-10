package com.lineage.infrastructure.persistance.adapter;

import com.lineage.application.port.out.GraphRepository;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.infrastructure.persistance.entity.GraphEntity;
import com.lineage.infrastructure.persistance.mapper.GraphMapper;
import com.lineage.infrastructure.persistance.repository.JpaGraphRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GraphRepositoryAdapter implements GraphRepository {

    private final JpaGraphRepository jpaRepository;
    private final GraphMapper mapper;

    public GraphRepositoryAdapter(JpaGraphRepository jpaRepository, GraphMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper        = mapper;
    }

    @Override
    public void save(GraphAggregate graph) {
        GraphEntity entity = mapper.toEntity(graph);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<GraphAggregate> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
public List<GraphAggregate> findAll() {
    return jpaRepository.findAll()
        .stream()
        .map(mapper::toDomain)
        .toList();
}
}