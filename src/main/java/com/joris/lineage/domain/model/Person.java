package com.joris.lineage.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import java.util.Set;

import com.joris.lineage.domain.valueobject.BirthPlace;
import com.joris.lineage.domain.valueobject.Ancestry;

public final class Person {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final LocalDate deathDate; // nullable
    private final BirthPlace birthPlace;
    private final Set<Ancestry> origins;

    public Person(
            String firstName,
            String lastName,
            LocalDate birthDate,
            BirthPlace birthPlace,
            LocalDate deathDate,
            Set<Ancestry> origins
    ) {

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty");

        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty");

        if (birthDate == null)
            throw new IllegalArgumentException("Birth date cannot be null");

        if (birthPlace == null)
            throw new IllegalArgumentException("Birth place cannot be null");

        if (deathDate != null && deathDate.isBefore(birthDate))
            throw new IllegalArgumentException("Death date cannot be before birth date");

        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.deathDate = deathDate;
        this.origins = (origins == null) ? Set.of() : Set.copyOf(origins);
    }

    public UUID getId() { return this.id; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public LocalDate getBirthDate() { return this.birthDate; }
    public LocalDate getDeathDate() { return this.deathDate; }
    public BirthPlace getBirthPlace() { return this.birthPlace; }
    public Set<Ancestry> getOrigins() { return this.origins; }

    public boolean isDeceased() {
        return deathDate != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}