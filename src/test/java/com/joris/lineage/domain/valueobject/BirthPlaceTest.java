package com.joris.lineage.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class BirthPlaceTest {

    @Test
    void shouldCreateBirthPlaceWithRegion() {
        BirthPlace bp = new BirthPlace("Angers", "Maine-et-Loire", "France");
        assertThat(bp.getCity()).isEqualTo("Angers");
        assertThat(bp.getRegion()).isEqualTo("Maine-et-Loire");
        assertThat(bp.getCountry()).isEqualTo("France");
        assertThat(bp.format()).isEqualTo("Angers, Maine-et-Loire, France");
    }

    @Test
    void shouldCreateBirthPlaceWithoutRegion() {
        BirthPlace bp = new BirthPlace("Lagos", null, "Nigeria");
        assertThat(bp.getCity()).isEqualTo("Lagos");
        assertThat(bp.getRegion()).isNull();
        assertThat(bp.getCountry()).isEqualTo("Nigeria");
        assertThat(bp.format()).isEqualTo("Lagos, Nigeria");
    }

    @Test
    void shouldThrowIfCityOrCountryEmpty() {
        assertThatThrownBy(() -> new BirthPlace(null, "Region", "France"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new BirthPlace("City", "Region", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldConsiderEqualByValue() {
        BirthPlace bp1 = new BirthPlace("Paris", "Île-de-France", "France");
        BirthPlace bp2 = new BirthPlace("Paris", "Île-de-France", "France");
        assertThat(bp1).isEqualTo(bp2);
        assertThat(bp1.hashCode()).isEqualTo(bp2.hashCode());
    }
}