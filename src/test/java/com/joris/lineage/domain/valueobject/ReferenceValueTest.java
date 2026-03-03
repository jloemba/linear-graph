package com.joris.graphengine.domain.valueobject;

import com.lineage.domain.valueobject.ReferenceValue;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ReferenceValueTest {

    @Test
    void shouldCreateValidReference() {
        UUID id = UUID.randomUUID();
        ReferenceValue value = new ReferenceValue(id);

        assertThat(value.getReferencedNodeId()).isEqualTo(id);
    }

    @Test
    void shouldThrowIfIdNull() {
        assertThatThrownBy(() -> new ReferenceValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void equalityShouldBeBasedOnId() {
        UUID id = UUID.randomUUID();

        ReferenceValue v1 = new ReferenceValue(id);
        ReferenceValue v2 = new ReferenceValue(id);

        assertThat(v1).isEqualTo(v2);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfIdsDifferent() {
        ReferenceValue v1 = new ReferenceValue(UUID.randomUUID());
        ReferenceValue v2 = new ReferenceValue(UUID.randomUUID());

        assertThat(v1).isNotEqualTo(v2);
    }

    @Test
    void shouldVerifyReference() {
        UUID id = UUID.randomUUID();
        ReferenceValue value = new ReferenceValue(id);

        assertThat(value.refersTo(id)).isTrue();
        assertThat(value.refersTo(UUID.randomUUID())).isFalse();
    }
}