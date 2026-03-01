package com.joris.lineage.domain.valueobject;

import java.util.Objects;

public final class BirthPlace {

    private final String city;
    private final String region;
    private final String country;
    
    public BirthPlace(String city, String region, String country) {

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }

        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }

        this.city = city.trim();
        this.region = (region == null || region.isBlank()) ? null : region.trim();
        this.country = country.trim();
    }

    public String getCity() {
        return this.city;
    }

    public String getRegion() {
        return this.region;
    }

    public String getCountry() {
        return this.country;
    }

    public String format() {
        if (region == null) {
            return this.city + ", " + this.country;
        }
        return this.city + ", " + this.region + ", " + this.country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BirthPlace)) return false;
        BirthPlace that = (BirthPlace) o;
        return this.city.equals(that.city)
                && Objects.equals(region, that.region)
                && this.country.equals(that.country);
    }

    @Override
    public int hashCode() { 
        return Objects.hash(this.city, this.region, this.country);
    }

    @Override
    public String toString() {
        return format();
    }
}