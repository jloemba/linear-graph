package com.joris.lineage.domain.model;

import com.joris.lineage.domain.valueobject.Ancestry;
import com.joris.lineage.domain.valueobject.BirthPlace;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;
import static org.assertj.core.api.Assertions.*;

class PersonTest {

    @Test
    void shouldCreateValidPerson() {
        BirthPlace bp = new BirthPlace("Uppsala", null, "Sweden");
        Set<Ancestry> ancestries = Set.of(new Ancestry("IRN"), new Ancestry("SWE"));

        Person p = new Person("Snoh", "Alegra",
                LocalDate.of(1987, 9, 13),
                bp,
                null,
                ancestries);

        assertThat(p.getFirstName()).isEqualTo("Snoh");
        assertThat(p.getLastName()).isEqualTo("Alegra");
        assertThat(p.getBirthPlace()).isEqualTo(bp);
        assertThat(p.getOrigins()).containsExactlyElementsOf(ancestries);
        assertThat(p.isDeceased()).isFalse();
    }

    @Test
    void shouldThrowIfDeathBeforeBirth() {
        BirthPlace bp = new BirthPlace("Uppsala", null, "Sweden");
        assertThatThrownBy(() -> new Person(
                "Snoh", "Alegra",
                LocalDate.of(1985, 2, 5),
                bp,
                LocalDate.of(1980, 1, 1),
                Set.of(new Ancestry("PT"))
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldTreatNullAncestryAsEmptySet() {
        BirthPlace bp = new BirthPlace("Uppsala", null, "Sweden");
        Person p = new Person("Snoh", "Alegra",
                LocalDate.of(1987, 9, 13),
                bp,
                null,
                null);
        assertThat(p.getOrigins()).isEmpty();
    }

    @Test
    void shouldThrowIfFirstOrLastNameNullOrBlank() {
        BirthPlace bp = new BirthPlace("Uppsala", null, "Sweden");
        assertThatThrownBy(() -> new Person(null, "Alegra", LocalDate.of(1987,9,13), bp, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Person("Snoh", "", LocalDate.of(1987,9,13), bp, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReturnImmutableAncestrySet() {
        BirthPlace bp = new BirthPlace("Uppsala", null, "Sweden");
        Set<Ancestry> ancestries = Set.of(new Ancestry("SWE"));
        Person p = new Person("Snoh", "Alegra",
                LocalDate.of(1987, 9, 13),
                bp,
                null,
                ancestries);

        assertThatThrownBy(() -> p.getOrigins().add(new Ancestry("ES")))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}