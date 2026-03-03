package com.joris.lineage.domain.model;

import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RelationshipTest {

    @Test
    void shouldCreateValidRelationship() {
        Node from = new Node("A", null);
        Node to = new Node("B", null);

        Relationship relationship = new Relationship(from, to, "CONNECTED_TO");

        assertThat(relationship.getFrom()).isEqualTo(from);
        assertThat(relationship.getTo()).isEqualTo(to);
        assertThat(relationship.getType()).isEqualTo("CONNECTED_TO");
    }

    @Test
    void shouldThrowIfFromIsNull() {
        Node to = new Node("B", null);

        assertThatThrownBy(() -> new Relationship(null, to, "TYPE"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIfToIsNull() {
        Node from = new Node("A", null);

        assertThatThrownBy(() -> new Relationship(from, null, "TYPE"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIfTypeBlank() {
        Node from = new Node("A", null);
        Node to = new Node("B", null);

        assertThatThrownBy(() -> new Relationship(from, to, " "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}