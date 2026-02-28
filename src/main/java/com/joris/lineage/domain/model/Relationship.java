package com.joris.lineage.domain.model;

import com.joris.lineage.domain.exception.InvalidRelationshipException;
import com.joris.lineage.domain.enums.RelationshipType;
import com.joris.lineage.domain.model.Person;


import java.util.Objects;

public final class Relationship {

    private final Person from;
    private final Person to;
    private final RelationshipType type;

    public Relationship(Person from, Person to, RelationshipType type) {
        Objects.requireNonNull(from, "from cannot be null");
        Objects.requireNonNull(to, "to cannot be null");
        Objects.requireNonNull(type, "type cannot be null");

        if (type == RelationshipType.PARENT_OF) {
            if (from.equals(to))
                throw new InvalidRelationshipException("Person cannot be their own parent");
            if (from.getBirthDate().isAfter(to.getBirthDate()))
                throw new InvalidRelationshipException("Parent cannot be younger than child");
        }

        this.from = from;
        this.to = to;
        this.type = type;
    }

    public Person getFrom() { return from; }
    public Person getTo() { return to; }
    public RelationshipType getType() { return type; }
}