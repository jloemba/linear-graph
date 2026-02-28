package com.joris.lineage.domain.enums;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RelationshipTypeTest {

    @Test
    void shouldContainAllExpectedTypes() {
        List<RelationshipType> types = Arrays.asList(RelationshipType.values());

        assertThat(types)
                .containsExactlyInAnyOrder(
                        RelationshipType.PARENT_OF,
                        RelationshipType.MARRIED_TO,
                        RelationshipType.ADOPTED
                );
    }

    @Test
    void shouldHaveNonNullNames() {
        for (RelationshipType type : RelationshipType.values()) {
            assertThat(type.name()).isNotNull();
            assertThat(type.name()).isNotBlank();
        }
    }
}