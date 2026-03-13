package com.lineage.domain.model;

import com.lineage.domain.valueobject.PropertyValue;

import java.util.Objects;

public final class Property {

    private final String name;
    private final PropertyValue value;

    public Property(String name, PropertyValue value) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Property name cannot be empty");
        }

        Objects.requireNonNull(value, "Property value cannot be null");

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public PropertyValue getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Property))
            return false;
        Property property = (Property) o;
        return name.equals(property.name) &&
                value.equals(property.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }


    @Override
    public String toString() {
        return this.value.toString();
    }
}