package com.lineage.infrastructure.persistance.repository;

import com.lineage.infrastructure.persistance.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NodeRepository extends JpaRepository<NodeEntity, UUID> {
    List<NodeEntity> findByGraphId(UUID graphId);
    List<NodeEntity> findByGraphIdAndType(UUID graphId, String type);
}