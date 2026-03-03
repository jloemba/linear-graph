package com.lineage.domain.valueobject;

import com.lineage.domain.valueobject.PropertyValue;
import java.time.LocalDate;
import java.util.Objects;

public final class DateValue implements PropertyValue {

    private final LocalDate value;

    public DateValue(LocalDate value) {
        Objects.requireNonNull(value, "Date value cannot be null");
        this.value = value;
    }

    public LocalDate getValue() {
        return value;
    }

    public boolean isBefore(DateValue other) {
        Objects.requireNonNull(other, "Other DateValue cannot be null");
        return this.value.isBefore(other.value);
    }

    public boolean isAfter(DateValue other) {
        Objects.requireNonNull(other, "Other DateValue cannot be null");
        return this.value.isAfter(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateValue)) return false;
        DateValue that = (DateValue) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}