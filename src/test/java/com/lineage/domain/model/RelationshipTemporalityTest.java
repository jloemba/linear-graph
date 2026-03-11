package com.lineage.domain.model;

import com.lineage.domain.model.Relationship;
import com.lineage.domain.model.Node;
import com.lineage.domain.valueobject.DateValue;
import com.lineage.domain.valueobject.NodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class RelationshipTemporalityTest {

    private static final NodeType GENRE = NodeType.of("GENRE");

    private Node gospel;
    private Node blues;
    private Node rock;

    @BeforeEach
    void setUp() {
        gospel = new Node(UUID.randomUUID(), "Gospel", null, GENRE);
        blues = new Node(UUID.randomUUID(), "Urban Blues", null, GENRE);
        rock = new Node(UUID.randomUUID(), "Rock'n'Roll", null, GENRE);
    }

    @Test
    void shouldCreateRelationshipWithStartDate() {
        DateValue start = new DateValue(LocalDate.of(1954, 1, 1));

        Relationship rel = new Relationship(gospel, rock, "EVOLVED_INTO", start, null);

        assertThat(rel.getStartDate()).contains(start);
        assertThat(rel.getEndDate()).isEmpty();
    }

    @Test
    void shouldCreateRelationshipWithStartAndEndDate() {
        DateValue start = new DateValue(LocalDate.of(1950, 1, 1));
        DateValue end = new DateValue(LocalDate.of(1960, 1, 1));

        Relationship rel = new Relationship(gospel, blues, "INFLUENCED", start, end);

        assertThat(rel.getStartDate()).contains(start);
        assertThat(rel.getEndDate()).contains(end);
    }

    @Test
    void shouldCreateRelationshipWithoutDates() {
        Relationship rel = new Relationship(gospel, rock, "EVOLVED_INTO", null, null);

        assertThat(rel.getStartDate()).isEmpty();
        assertThat(rel.getEndDate()).isEmpty();
    }

    @Test
    void shouldThrowIfEndDateIsBeforeStartDate() {
        DateValue start = new DateValue(LocalDate.of(1960, 1, 1));
        DateValue end = new DateValue(LocalDate.of(1950, 1, 1));

        assertThatThrownBy(() -> new Relationship(gospel, rock, "EVOLVED_INTO", start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("End date cannot be before start date");
    }

    @Test
    void shouldThrowIfEndDateEqualsStartDate() {
        DateValue date = new DateValue(LocalDate.of(1954, 1, 1));

        assertThatThrownBy(() -> new Relationship(gospel, rock, "EVOLVED_INTO", date, date))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("End date cannot be before start date");
    }

    @Test
    void shouldBeActiveWhenNoEndDate() {
        DateValue start = new DateValue(LocalDate.of(1954, 1, 1));
        Relationship rel = new Relationship(gospel, rock, "EVOLVED_INTO", start, null);

        assertThat(rel.isActive()).isTrue();
    }

    @Test
    void shouldNotBeActiveWhenEndDateIsSet() {
        DateValue start = new DateValue(LocalDate.of(1950, 1, 1));
        DateValue end = new DateValue(LocalDate.of(1960, 1, 1));

        Relationship rel = new Relationship(gospel, blues, "INFLUENCED", start, end);

        assertThat(rel.isActive()).isFalse();
    }

    @Test
    void shouldBeActiveWhenNoDatesAtAll() {
        Relationship rel = new Relationship(gospel, rock, "EVOLVED_INTO", null, null);

        assertThat(rel.isActive()).isTrue();
    }

    @Test
    void shouldNotBeEqualWhenStartDatesDiffer() {
        // Gospel → Rock en 1950 vs Gospel → Rock en 1960
        // Ce sont deux événements historiques différents
        DateValue date1 = new DateValue(LocalDate.of(1950, 1, 1));
        DateValue date2 = new DateValue(LocalDate.of(1960, 1, 1));

        Relationship rel1 = new Relationship(gospel, rock, "EVOLVED_INTO", date1, null);
        Relationship rel2 = new Relationship(gospel, rock, "EVOLVED_INTO", date2, null);

        assertThat(rel1).isNotEqualTo(rel2);
    }

    @Test
    void shouldBeEqualWhenAllFieldsMatch() {
        DateValue start = new DateValue(LocalDate.of(1954, 1, 1));

        Relationship rel1 = new Relationship(gospel, rock, "EVOLVED_INTO", start, null);
        Relationship rel2 = new Relationship(gospel, rock, "EVOLVED_INTO", start, null);

        assertThat(rel1).isEqualTo(rel2);
    }

    @Test
    void shouldModelGospelEvolvingIntoRockNRoll() {
        DateValue emergence = new DateValue(LocalDate.of(1954, 1, 1));
        Relationship rel = new Relationship(gospel, rock, "EVOLVED_INTO", emergence, null);

        assertThat(rel.getFrom().getLabel()).isEqualTo("Gospel");
        assertThat(rel.getTo().getLabel()).isEqualTo("Rock'n'Roll");
        assertThat(rel.getStartDate()).contains(emergence);
        assertThat(rel.isActive()).isTrue();
    }

    @Test
    void shouldModelInfluenceWithTimeRange() {
        DateValue start = new DateValue(LocalDate.of(1940, 1, 1));
        DateValue end = new DateValue(LocalDate.of(1960, 1, 1));

        Relationship rel = new Relationship(gospel, blues, "INFLUENCED", start, end);

        assertThat(rel.isActive()).isFalse();
        assertThat(rel.getStartDate()).contains(start);
        assertThat(rel.getEndDate()).contains(end);
    }

}