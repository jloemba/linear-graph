package com.lineage.infrastructure.persistance.mapper;

import com.lineage.domain.model.Property;
import com.lineage.domain.model.Relationship;
import com.lineage.domain.model.GraphAggregate;
import com.lineage.domain.model.Node;
import com.lineage.domain.valueobject.*;
import com.lineage.infrastructure.persistance.entity.GraphEntity;
import com.lineage.infrastructure.persistance.entity.NodeEntity;
import com.lineage.infrastructure.persistance.entity.NodePropertyEntity;
import com.lineage.infrastructure.persistance.entity.RelationshipEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class GraphMapper {

    public GraphEntity toEntity(GraphAggregate graph) {
        GraphEntity entity = new GraphEntity(
                graph.getId(),
                graph.getName());

        graph.getNodes().forEach(node -> entity.getNodes().add(toNodeEntity(node, entity)));

        graph.getRelationships().forEach(rel -> entity.getRelationships().add(toRelationshipEntity(rel, entity)));

        return entity;
    }

    public GraphAggregate toDomain(GraphEntity entity) {
        GraphAggregate graph = new GraphAggregate(
                entity.getName());

        entity.getNodes().forEach(nodeEntity -> graph.addNode(toDomainNode(nodeEntity)));

        entity.getRelationships().forEach(relEntity -> graph.addRelationship(toDomainRelationship(relEntity)));

        return graph;
    }

    public NodeEntity toNodeEntity(Node node, GraphEntity graph) {
        NodeEntity entity = new NodeEntity(
                node.getId(),
                node.getLabel(),
                node.getType().getValue(),
                graph);

        node.getProperties().forEach(property -> entity.getProperties().add(toPropertyEntity(property, entity)));

        return entity;
    }

    public Node toDomainNode(NodeEntity entity) {
        Node node = new Node(
                entity.getLabel(),
                null,
                NodeType.of(entity.getType()));

        entity.getProperties().forEach(propEntity -> node.addProperty(toDomainProperty(propEntity)));

        return node;
    }

    public RelationshipEntity toRelationshipEntity(Relationship rel, GraphEntity graph) {
        NodeEntity from = toNodeEntity(rel.getFrom(), graph);
        NodeEntity to = toNodeEntity(rel.getTo(), graph);

        return new RelationshipEntity(
                rel.getId(),
                graph,
                from,
                to,
                rel.getType(),
                rel.getStartDate().map(DateValue::getValue).orElse(null),
                rel.getEndDate().map(DateValue::getValue).orElse(null));
    }

    public Relationship toDomainRelationship(RelationshipEntity entity) {
        Node from = toDomainNode(entity.getFrom());
        Node to = toDomainNode(entity.getTo());

        DateValue startDate = entity.getStartDate() != null
                ? new DateValue(entity.getStartDate())
                : null;

        DateValue endDate = entity.getEndDate() != null
                ? new DateValue(entity.getEndDate())
                : null;

        return new Relationship(from, to, entity.getType(), startDate, endDate);
    }

    public NodePropertyEntity toPropertyEntity(Property property, NodeEntity node) {
        String valueType = null;
        String stringValue = null;
        LocalDate dateValue = null;
        UUID refNodeId = null;

        switch (property.getValue()) {
            case StringValue s -> {
                valueType = "STRING";
                stringValue = s.getValue();
            }
            case DateValue d -> {
                valueType = "DATE";
                dateValue = d.getValue();
            }
            case ReferenceValue r -> {
                valueType = "REFERENCE";
                refNodeId = r.getReferencedNodeId();
            }
        }

        return new NodePropertyEntity(
                UUID.randomUUID(),
                node,
                property.getName(),
                valueType,
                stringValue,
                dateValue,
                refNodeId);
    }

    public Property toDomainProperty(NodePropertyEntity entity) {
        PropertyValue value = switch (entity.getValueType()) {
            case "STRING" -> new StringValue(entity.getStringValue());
            case "DATE" -> new DateValue(entity.getDateValue());
            case "REFERENCE" -> new ReferenceValue(entity.getRefNodeId());
            default -> throw new IllegalArgumentException("Unknown value type : " + entity.getValueType());
        };

        return new Property(entity.getName(), value);
    }
}