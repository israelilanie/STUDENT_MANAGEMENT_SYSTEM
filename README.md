# Student Management System

A production-grade REST API built with Spring Boot 3 — designed to demonstrate real-world backend engineering patterns beyond tutorial-level CRUD.

Live API: [https://your-app.onrender.com/swagger-ui.html](https://your-app.onrender.com/swagger-ui.html)

---

## What This Project Demonstrates

- Multi-role JWT authentication (ADMIN / TEACHER / STUDENT)
- Clean layered architecture with strict boundary enforcement
- Domain-driven entity design with proper join entities (no @ManyToMany)
- Flyway database migrations — schema managed as code
- MapStruct DTO mapping — zero manual mapping
- Caffeine caching with smart eviction strategy
- Bucket4j rate limiting — brute force protection
- Async email notifications via Spring Mail
- File upload — local storage in dev, AWS S3 in production
- PDF transcript generation with iText
- Optimistic locking with @Version on all mutable entities
- Global exception handling — consistent error contract across all endpoints
- Unit tests (JUnit 5 + Mockito) and Integration tests (Testcontainers)
- Multi-profile configuration (dev / test / prod)
- Dockerized and deployed to Render with PostgreSQL

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Security | Spring Security + JWT (jjwt) |
| Database | PostgreSQL (prod) / H2 (dev) |
| Migrations | Flyway |
| ORM | Spring Data JPA + Hibernate |
| Mapping | MapStruct |
| Caching | Caffeine |
| Rate Limiting | Bucket4j |
| Email | Spring Mail + Mailtrap (dev) |
| File Storage | AWS S3 (prod) / Local (dev) |
| PDF | iText 5 |
| Testing | JUnit 5 + Mockito + Testcontainers |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |
| Deployment | Docker + Render |

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                   HTTP Requests                      │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│              Rate Limiting Filter                    │
│         (Bucket4j — per IP, per endpoint)            │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│            JWT Authentication Filter                 │
│      (validates token, sets SecurityContext)         │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│                  Controllers                         │
│     (entry point only — zero business logic)         │
│  AuthController / StudentController / TeacherController
│  AdminController / CourseController / EnrollmentController
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│                   Services                           │
│        (all business rules live here)                │
│   AuthService / EnrollmentService / GpaService       │
│   CourseService / ReportService / PdfService         │
└──────────┬──────────────────────┬───────────────────┘
           │                      │
┌──────────▼──────┐    ┌─────────▼──────────────────┐
│  Repositories   │    │    External Services         │
│  (JPA queries)  │    │  EmailService / S3Storage   │
└──────────┬──────┘    └────────────────────────────┘
           │
┌──────────▼──────────────────────────────────────────┐
│              PostgreSQL Database                     │
│         (schema managed by Flyway)                   │
└─────────────────────────────────────────────────────┘
```

---

## Domain Model

```
User (1:1) StudentProfile
User (1:1) TeacherProfile
TeacherProfile (1:N) Course
StudentProfile (1:N) Enrollment  ←── heart of the system
Course (1:N) Enrollment
Enrollment (1:1) Grade
```

Enrollment is a proper join entity — not @ManyToMany — because it carries
business data: enrolledAt, status, finalGrade, gradePoints, droppedAt.

---

## API Overview

### Auth (public)
```
POST /api/auth/register     Register as student
POST /api/auth/login        Login and receive JWT token
```

### Student endpoints (ROLE_STUDENT)
```
GET    /api/student/profile              My profile
PATCH  /api/student/profile              Update my profile
POST   /api/student/avatar               Upload profile photo
GET    /api/student/enrollments          My active enrollments
POST   /api/student/enrollments          Enroll in a course
DELETE /api/student/enrollments/{id}     Drop a course
GET    /api/student/enrollments/history  Full enrollment history
GET    /api/student/gpa                  My GPA
GET    /api/student/transcript           My academic transcript
GET    /api/student/transcript/pdf       Download transcript as PDF
```

### Teacher endpoints (ROLE_TEACHER)
```
GET   /api/teacher/profile                          My profile
PATCH /api/teacher/profile                          Update my profile
GET   /api/teacher/courses                          My courses
POST  /api/teacher/courses                          Create a course
GET   /api/teacher/courses/{id}/enrollments         Students in my course
POST  /api/teacher/grades                           Grade a student
```

### Admin endpoints (ROLE_ADMIN)
```
GET   /api/admin/users                              All users
POST  /api/admin/teachers                           Create teacher account
GET   /api/admin/students                           All students
GET   /api/admin/students/{id}                      Student by ID
GET   /api/admin/students/{id}/transcript           Student transcript
GET   /api/admin/students/{id}/transcript/pdf       Download PDF
GET   /api/admin/teachers                           All teachers
GET   /api/admin/reports/overview                   System overview
GET   /api/admin/reports/courses/{id}               Course report
GET   /api/admin/reports/gpa-distribution           GPA distribution
PATCH /api/admin/courses/{id}/assign-teacher/{tid}  Assign teacher
PATCH /api/admin/courses/{id}/archive               Archive course
```

### Courses (all authenticated users)
```
GET   /api/courses          Search and list active courses (paginated)
GET   /api/courses/{id}     Course details
```

---

## Running Locally

### Prerequisites
- Java 21
- Maven 3.9+
- Docker Desktop (for PostgreSQL or Testcontainers)

### Option 1 — Run with H2 (simplest, no Docker needed)

```bash
git clone https://github.com/yourusername/student-management-system.git
cd student-management-system
mvn spring-boot:run
```

App starts on `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`
H2 Console: `http://localhost:8080/h2-console`

H2 credentials:
- JDBC URL: `jdbc:h2:file:./devdb`
- Username: `sa`
- Password: `password`

### Option 2 — Run with Docker

```bash
docker build -t sms-app .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  sms-app
```

### Seeded accounts (dev profile only)

| Role | Email | Password |
|---|---|---|
| Admin | admin@sms.com | admin123 |
| Teacher | teacher@sms.com | teacher123 |
| Student | student@sms.com | student123 |

---

## Environment Variables (Production)

| Variable | Description |
|---|---|
| `SPRING_PROFILES_ACTIVE` | Set to `prod` |
| `DB_URL` | JDBC PostgreSQL URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for signing JWTs (min 32 chars) |
| `MAIL_HOST` | SMTP host |
| `MAIL_PORT` | SMTP port |
| `MAIL_USERNAME` | SMTP username |
| `MAIL_PASSWORD` | SMTP password |
| `MAIL_FROM` | From email address |
| `AWS_BUCKET_NAME` | S3 bucket name |
| `AWS_REGION` | AWS region |
| `AWS_ACCESS_KEY` | AWS access key |
| `AWS_SECRET_KEY` | AWS secret key |

---

## Running Tests

```bash
# Unit tests only (no Docker needed)
mvn test -Dtest="*Test"

# All tests including integration (Docker required)
mvn test

# Generate coverage report
mvn test jacoco:report
# Open target/site/jacoco/index.html
```

---

## Project Structure

```
src/
├── main/
│   ├── java/com/israel/studentmanagementsystem/
│   │   ├── config/          Spring configuration beans
│   │   ├── controller/      REST controllers (entry points only)
│   │   ├── dto/
│   │   │   ├── request/     Incoming request DTOs
│   │   │   └── response/    Outgoing response DTOs
│   │   ├── entity/          JPA entities
│   │   ├── enums/           Role, Status, Grade enums
│   │   ├── exception/       Custom exceptions + global handler
│   │   ├── mapper/          MapStruct interfaces
│   │   ├── repository/      Spring Data JPA repositories
│   │   ├── security/        JWT filter, UserDetailsService
│   │   └── service/         Business logic (all rules live here)
│   └── resources/
│       ├── db/migration/    Flyway SQL migrations (V1-V6)
│       ├── application.properties
│       ├── application-dev.properties
│       ├── application-test.properties
│       └── application-prod.properties
└── test/
    └── java/com/israel/studentmanagementsystem/
        ├── service/         Unit tests (Mockito)
        └── integration/     Integration tests (Testcontainers)
```

---

## Key Design Decisions

**Why Enrollment instead of @ManyToMany**
A plain @ManyToMany generates a join table with only two foreign keys.
Enrollment is a proper entity that carries enrolledAt, status, finalGrade,
gradePoints — data that would be impossible to store otherwise.

**Why Flyway instead of ddl-auto=update**
Flyway treats schema changes as versioned, reviewable, reversible migrations.
ddl-auto=update is unpredictable in production and has no history.
Every schema change in this project is a numbered SQL file.

**Why DTOs instead of exposing entities**
Entities contain passwordHash, @Version fields, and lazy collections.
Serializing them directly leaks internal data and causes
LazyInitializationException. DTOs give full control over the API surface.

**Why @Transactional on services not controllers**
Transactions held open during HTTP response serialization cause lazy
loading outside the session. Service methods own transaction boundaries —
they start and commit before the controller returns anything.

---

## What I'd add with more time

- Refresh token endpoint
- Password reset via email
- Redis for distributed caching (swap from Caffeine)
- WebSocket notifications for grade updates
- React frontend
- GitHub Actions CI/CD pipeline
- Prometheus + Grafana metrics dashboard

---

## Author

Israel ENDA ILANIE — self-taught Java backend developer (IBM and UDACITY Certified)                                                                                 

GitHub: https://github.com/israelilanie

LinkedIn: https://www.linkedin.com/in/israel-ilanie-a081a4187/ 