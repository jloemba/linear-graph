package com.joris.lineage.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Person {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;

    public Person(String firstName, String lastName, LocalDate birthDate) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty");
        if (birthDate == null)
            throw new IllegalArgumentException("Birth date cannot be null");

        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }

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