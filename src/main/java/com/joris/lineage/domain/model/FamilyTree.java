package com.joris.lineage.domain.model;

import java.util.*;

public final class FamilyTree {

    private final String name;
    private final Map<UUID, Person> members = new HashMap<>();
    private final List<Relationship> relationships = new ArrayList<>();

    public FamilyTree(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("FamilyTree name cannot be empty");
        this.name = name;
    }

    public void addPerson(Person person) {
        if (members.containsKey(person.getId()))
            throw new IllegalArgumentException("Person already exists in tree");
        members.put(person.getId(), person);
    }

    public void addRelationship(Relationship relationship) {
        if (!members.containsKey(relationship.getFrom().getId()) ||
            !members.containsKey(relationship.getTo().getId()))
            throw new IllegalArgumentException("Both persons must belong to the tree");

        if (relationships.contains(relationship))
            throw new IllegalArgumentException("Relationship already exists");

        relationships.add(relationship);
    }

    public Collection<Person> getMembers() {
        return Collections.unmodifiableCollection(members.values());
    }

    public List<Relationship> getRelationships() {
        return Collections.unmodifiableList(relationships);
    }

    public String getName() {
        return name;
    }
}