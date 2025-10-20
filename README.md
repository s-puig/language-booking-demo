# Language Booking Demo
Java Spring(boot) backend service for a language tutoring platform using a modular monolith architecture. 


This project is designed to showcase best practices in API design, documentation, test-driven development (TDD) with JUnit, CI/CD, authentication, role-based authorization, ~payment processing (Stripe)~ and scheduling.

# Tech stack

- **Language & Framework**: Java 21 - Spring Boot 3
- **Database**: H2, an in-memory database.
- **Authentication**: JWT using JJWT dependency.
- **Testing**: JUnit and Mockito
- **~Open API + Swagger~**
- ~**Payment processing**: Stripe API~
- Others:
  - **MapStruct** for mapping preprocessing of entities and their associated DTOs
  - **Lombok** for class method preprocessing like getter, setters, constructors, builders..


# Feature list

- [x] Users
- [x] Auth
  - Custom role-based authentification using JWT, filter, annotations and Aspects.\
_I'm aware of Spring Security, just wanted to play around with Java Annotation/Reflection API and Spring Aspect programming._
  - [x] Authentication
  - [x] Authorization
- [x] Schedule
  - Manage Tutor weekly schedule
  - [ ] Week-specific schedule
- [ ] Booking
  - Booking of lessons
  - [ ] (Opt) Email notification on booking success
- [ ] Payment
- [x] Lesson Offerings
  - Tutor lesson offerings types, prices and limits (e.g. trial lessons) 
- [ ] OpenAPI+Swagger integration
- [ ] CI/CD
  - [x] Test runner
  - [ ] Formatter
  - [ ] CD


