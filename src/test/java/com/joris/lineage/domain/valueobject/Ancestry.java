package com.joris.lineage.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AncestryTest {

    @Test
    void shouldCreateAndNormalize() {
        Ancestry a = new Ancestry("fr");
        assertThat(a.getValue()).isEqualTo("Fr");
    }

    @Test
    void shouldThrowOnNullOrEmpty() {
        assertThatThrownBy(() -> new Ancestry(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Ancestry("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Ancestry(" ")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBeEqualIgnoreCase() {
        Ancestry a1 = new Ancestry("fr");
        Ancestry a2 = new Ancestry("FR");
        assertThat(a1).isEqualTo(a2);
        assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
    }
}