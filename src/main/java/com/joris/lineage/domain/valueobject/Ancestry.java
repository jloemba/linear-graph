package com.joris.lineage.domain.valueobject;

import java.util.Locale;

public final class Ancestry {

    private final String value;

    public Ancestry(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ancestry cannot be empty");
        }

        String normalized = normalize(value);

        if (normalized.length() < 2) {
            throw new IllegalArgumentException("Ancestry must contain at least 2 characters");
        }

        this.value = normalized;
    }

    private String normalize(String input) {
        String trimmed = input.trim().toLowerCase(Locale.ROOT);
        return trimmed.substring(0, 1).toUpperCase(Locale.ROOT) + trimmed.substring(1);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ancestry)) return false;
        Ancestry ancestry = (Ancestry) o;
        return value.equalsIgnoreCase(ancestry.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}