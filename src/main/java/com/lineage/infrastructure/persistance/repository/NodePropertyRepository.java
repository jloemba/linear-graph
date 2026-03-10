package com.lineage.infrastructure.persistance.repository;

import com.lineage.infrastructure.persistance.entity.NodePropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NodePropertyRepository extends JpaRepository<NodePropertyEntity, UUID> {
    List<NodePropertyEntity> findByNodeId(UUID nodeId);
}
