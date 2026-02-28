# Lineage

**Lineage** is a **Spring Boot** project designed to model **family trees** and their relationships.  
It is built following **TDD (Test-Driven Development)** principles and serves as a **portfolio project demonstrating architecture, domain modeling, and code quality**.

## Features

- Create **persons** with first name, last name, and birthdate
- Define **relationships** between persons:
  - Parent → Child
  - Married
  - Adopted
- Manage a **FamilyTree** containing members and their relationships
- **Business rule validations**:
  - A parent cannot be younger than their child
  - A person cannot be their own parent
  - Relationships must be between members of the tree
- Unit tests covering all domain logic, ensuring **TDD compliance**

---

## Technology Stack

- **Java 21**  
- **Spring Boot 3.5.11**  
- **Maven** for dependency management  
- **Spring Boot Starter Test** (JUnit 5, AssertJ, Mockito) for unit tests  
- **Lombok** to reduce boilerplate code  
- **GitHub Actions** for **CI/CD** to automatically run builds and tests

---
