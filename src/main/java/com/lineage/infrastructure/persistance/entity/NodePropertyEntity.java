package com.lineage.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "node_properties")
public class NodePropertyEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "TEXT")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private NodeEntity node;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value_type", nullable = false)
    private String valueType;  // STRING, DATE, REFERENCE

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "date_value")
    private LocalDate dateValue;

    @Column(name = "ref_node_id", columnDefinition = "TEXT")
    private UUID refNodeId;

    protected NodePropertyEntity() {}

    public NodePropertyEntity(UUID id, NodeEntity node, String name, String valueType, String stringValue, LocalDate dateValue, UUID refNodeId) {
        this.id          = id;
        this.node        = node;
        this.name        = name;
        this.valueType   = valueType;
        this.stringValue = stringValue;
        this.dateValue   = dateValue;
        this.refNodeId   = refNodeId;
    }

    public UUID getId()              { return id; }
    public NodeEntity getNode()      { return node; }
    public String getName()          { return name; }
    public String getValueType()     { return valueType; }
    public String getStringValue()   { return stringValue; }
    public LocalDate getDateValue()  { return dateValue; }
    public UUID getRefNodeId()       { return refNodeId; }
}