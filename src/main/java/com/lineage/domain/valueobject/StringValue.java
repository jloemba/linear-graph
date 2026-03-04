package com.lineage.domain.valueobject;

import java.util.Objects;

public final class StringValue implements PropertyValue {

    private final String value;

    public StringValue(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Value cannot be empty");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StringValue))
            return false;
        StringValue that = (StringValue) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Property{value='" + value + '\'' + '}';
    }
}