package com.lineage.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "relationships")
public class RelationshipEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "TEXT")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graph_id", nullable = false)
    private GraphEntity graph;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", nullable = false)
    private NodeEntity from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", nullable = false)
    private NodeEntity to;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    protected RelationshipEntity() {}

    public RelationshipEntity(UUID id, GraphEntity graph, NodeEntity from, NodeEntity to, String type, LocalDate startDate, LocalDate endDate) {
        this.id        = id;
        this.graph     = graph;
        this.from      = from;
        this.to        = to;
        this.type      = type;
        this.startDate = startDate;
        this.endDate   = endDate;
    }

    public UUID getId()             { return id; }
    public GraphEntity getGraph()   { return graph; }
    public NodeEntity getFrom()     { return from; }
    public NodeEntity getTo()       { return to; }
    public String getType()         { return type; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate()   { return endDate; }
}