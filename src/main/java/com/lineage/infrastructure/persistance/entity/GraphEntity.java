package com.lineage.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "graphs")
public class GraphEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "TEXT")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "graph", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NodeEntity> nodes = new ArrayList<>();

    @OneToMany(mappedBy = "graph", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelationshipEntity> relationships = new ArrayList<>();

    protected GraphEntity() {}

    public GraphEntity(UUID id, String name) {
        this.id        = id;
        this.name      = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId()                                { return id; }
    public String getName()                            { return name; }
    public LocalDateTime getCreatedAt()                { return createdAt; }
    public LocalDateTime getUpdatedAt()                { return updatedAt; }
    public List<NodeEntity> getNodes()                 { return nodes; }
    public List<RelationshipEntity> getRelationships() { return relationships; }

    public void setUpdatedAt(LocalDateTime updatedAt)  { this.updatedAt = updatedAt; }
}