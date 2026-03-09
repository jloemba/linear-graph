package com.lineage.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "nodes")
public class NodeEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "TEXT")
    private UUID id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graph_id", nullable = false)
    private GraphEntity graph;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NodePropertyEntity> properties = new ArrayList<>();

    protected NodeEntity() {}

    public NodeEntity(UUID id, String label, String type, GraphEntity graph) {
        this.id    = id;
        this.label = label;
        this.type  = type;
        this.graph = graph;
    }

    public UUID getId()                              { return id; }
    public String getLabel()                         { return label; }
    public String getType()                          { return type; }
    public GraphEntity getGraph()                    { return graph; }
    public List<NodePropertyEntity> getProperties()  { return properties; }
}