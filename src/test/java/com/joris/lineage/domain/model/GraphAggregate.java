package com.joris.lineage.domain.model;

import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;
import com.lineage.domain.model.GraphAggregate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GraphAggregateTest {

    @Test
    void shouldAddNodeToGraph() {
        GraphAggregate graph = new GraphAggregate("TestGraph");

        Node node = new Node("Person", null);
        graph.addNode(node);

        assertThat(graph.getNodes()).contains(node);
    }

    @Test
    void shouldNotAllowDuplicateNode() {
        GraphAggregate graph = new GraphAggregate("TestGraph");

        Node node = new Node("Person", null);

        graph.addNode(node);

        assertThatThrownBy(() -> graph.addNode(node))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAddRelationshipIfNodesExist() {
        GraphAggregate graph = new GraphAggregate("TestGraph");

        Node a = new Node("A", null);
        Node b = new Node("B", null);

        graph.addNode(a);
        graph.addNode(b);

        Relationship relationship = new Relationship(a, b, "CONNECTED");

        graph.addRelationship(relationship);

        assertThat(graph.getRelationships()).contains(relationship);
    }

    @Test
    void shouldThrowIfRelationshipNodesNotInGraph() {
        GraphAggregate graph = new GraphAggregate("TestGraph");

        Node a = new Node("A", null);
        Node b = new Node("B", null);

        graph.addNode(a);

        Relationship relationship = new Relationship(a, b, "CONNECTED");

        assertThatThrownBy(() -> graph.addRelationship(relationship))
                .isInstanceOf(IllegalArgumentException.class);
    }
}