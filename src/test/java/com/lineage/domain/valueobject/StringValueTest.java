package com.lineage.domain.valueobject;

import com.lineage.domain.valueobject.StringValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class StringValueTest {

    String mockValue = "Royaume Kongo";

    @Test
    void shouldCreateValidStringValue() {
        StringValue value = new StringValue("Jean");

        assertThat(value.getValue()).isEqualTo("Jean");
    }

    @Test
    void shouldThrowIfValueIsNull() {
        assertThatThrownBy(() -> new StringValue(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIfValueIsBlank() {
        assertThatThrownBy(() -> new StringValue(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIfValueIsEmpty() {
        assertThatThrownBy(() -> new StringValue(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBeEqualForSameValue() {
        StringValue v1 = new StringValue("Jean");
        StringValue v2 = new StringValue("Jean");

        assertThat(v1).isEqualTo(v2);
    }

    @Test
    void shouldNotBeEqualForDifferentValues() {
        StringValue v1 = new StringValue("Jean");
        StringValue v2 = new StringValue("Paul");

        assertThat(v1).isNotEqualTo(v2);
    }

    @Test
    void shouldBeEqualToItself() {
        StringValue v = new StringValue("Jean");

        assertThat(v).isEqualTo(v);
    }

    @Test
    void shouldNotBeEqualToNull() {
        StringValue v = new StringValue("Jean");

        assertThat(v).isNotEqualTo(null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        StringValue v = new StringValue("Jean");

        assertThat(v).isNotEqualTo("Jean");
    }

    @Test
    void shouldHaveConsistentHashCode() {
        StringValue v1 = new StringValue("Jean");
        StringValue v2 = new StringValue("Jean");

        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
    }

    @Test
    void shouldBeCaseSensitive() {
        StringValue v1 = new StringValue("jean");
        StringValue v2 = new StringValue("Jean");

        // Documente le comportement attendu — à ajuster si tu veux normaliser
        assertThat(v1).isNotEqualTo(v2);
    }

    @Test
    void shouldContainValue() {
        StringValue v = new StringValue(mockValue);

        assertThat(v.toString()).contains(mockValue);
    }

    @Test
    void shouldModelPersonFirstName() {
        StringValue firstName = new StringValue("Kimpa");

        assertThat(firstName.getValue()).isEqualTo("Kimpa");
    }

    @Test
    void shouldModelKingdomName() {
        StringValue kingdomName = new StringValue(mockValue);

        assertThat(kingdomName.getValue()).isEqualTo(mockValue);
    }

    @Test
    void shouldModelEthnicGroupName() {
        StringValue ethnicGroup = new StringValue("Bakongo");

        assertThat(ethnicGroup.getValue()).isEqualTo("Bakongo");
    }

}