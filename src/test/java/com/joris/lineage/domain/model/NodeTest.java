package com.joris.lineage.domain.model;

import com.lineage.domain.model.Node;
import com.lineage.domain.model.Property;
import com.lineage.domain.valueobject.StringValue;
import com.lineage.domain.valueobject.PropertyValue;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class NodeTest {

    @Test
    void shouldCreateNodeWithValidLabel() {
        Node node = new Node("Person", null);

        assertThat(node.getLabel()).isEqualTo("Person");
        assertThat(node.getProperties()).isEmpty();
    }

    @Test
    void shouldThrowIfLabelIsBlank() {
        assertThatThrownBy(() -> new Node(" ", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAddProperty() {
        Node node = new Node("Person", null);

        Property property = new Property("firstName", new StringValue("Jean"));

        node.addProperty(property);

        assertThat(node.getProperty("firstName")).isPresent();
    }

    @Test
    void shouldNotAllowDuplicatePropertyName() {
        Node node = new Node("Person", null);

        Property property1 = new Property("firstName", new StringValue("Jean"));
        Property property2 = new Property("firstName", new StringValue("Paul"));

        node.addProperty(property1);

        assertThatThrownBy(() -> node.addProperty(property2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReplaceProperty() {
        Node node = new Node("Person", null);

        node.addProperty(new Property("firstName", new StringValue("Jean")));
        node.replaceProperty(new Property("firstName", new StringValue("Paul")));

        assertThat(node.getProperty("firstName"))
                .get()
                .extracting(p -> ((StringValue)p.getValue()).getValue())
                .isEqualTo("Paul");
    }

    @Test
    void propertiesCollectionShouldBeUnmodifiable() {
        Node node = new Node("Person", Set.of(
                new Property("firstName", new StringValue("Jean"))
        ));

        assertThatThrownBy(() -> node.getProperties().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }
}