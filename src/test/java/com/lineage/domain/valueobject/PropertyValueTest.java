package com.lineage.domain.valueobject;

import com.lineage.domain.valueobject.PropertyValue;
import com.lineage.domain.valueobject.DateValue;
import com.lineage.domain.valueobject.ReferenceValue;
import com.lineage.domain.valueobject.StringValue;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class PropertyValueTest {

    @Test
    void shouldSupportPolymorphism() {
        PropertyValue stringValue = new StringValue("Test");
        PropertyValue dateValue = new DateValue(LocalDate.now());
        PropertyValue referenceValue = new ReferenceValue(UUID.randomUUID());

        assertThat(stringValue).isInstanceOf(PropertyValue.class);
        assertThat(dateValue).isInstanceOf(PropertyValue.class);
        assertThat(referenceValue).isInstanceOf(PropertyValue.class);
    }
}