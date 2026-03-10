package com.lineage.infrastructure.persistance.repository;

import com.lineage.infrastructure.persistance.entity.RelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RelationshipRepository extends JpaRepository<RelationshipEntity, UUID> {
    List<RelationshipEntity> findByGraphId(UUID graphId);
}