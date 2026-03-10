package com.lineage.infrastructure.persistance.repository;

import com.lineage.infrastructure.persistance.entity.GraphEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGraphRepository extends JpaRepository<GraphEntity, UUID> {
}