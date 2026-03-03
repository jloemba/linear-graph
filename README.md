# GraphEngine Project

## Overview

**GraphEngine** is a generic graph modeling engine built with **Spring Boot**.  
It allows modeling and visualizing structured data as a **graph** composed of nodes and relationships.  

Initially designed for genealogical trees, it has been refactored to support **any domain** where entities and their connections can be represented as a graph.

---

## Core Concepts

### Node

- Represents an **entity** in the graph.  
- Can have multiple **properties** (string, date, references, etc.).  
- Immutable once created.

### Relationship

- Connects two nodes with a **type** (e.g., `parent_of`, `married_to`, or any custom type).  
- Immutable and ensures nodes exist in the graph.  
- Prevents self-references.  

### Property & PropertyValue

- `Property` stores a **key** and a `PropertyValue`.  
- `PropertyValue` is a **sealed type** that can be:
  - `StringValue`
  - `DateValue`
  - `ReferenceValue` (links to another node)  

---

## Aggregate

`GraphAggregate` is the **root of the graph**:

- Manages all nodes and relationships.
- Ensures **consistency and invariants**:
  - Relationships can only exist between nodes in the graph.
  - Removing a node deletes all its relationships.
- Encapsulates all mutations (add/remove nodes or relationships).

---

## Example Usage

```java
GraphAggregate graph = new GraphAggregate("Family Graph");

Node john = new Node("John");
Node mike = new Node("Mike");

graph.addNode(john);
graph.addNode(mike);

Relationship parentChild = new Relationship(john, mike, "parent_of");
graph.addRelationship(parentChild);

Property birthPlace = new Property("birthPlace", new StringValue("Lisbon, Portugal"));
john.addProperty(birthPlace);