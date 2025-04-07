# Car Rental Application

## Overview

The **Car Rental Application** is a RESTful web service built with **Spring Boot**, designed to manage car rentals. It allows users to register, book cars, process payments, and manage locations and cars, with role-based access control using **Spring Security**.

Technologies used:
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- SLF4J with Logback (Logging)

---

## Features

- **User Management**: Register users, manage user details (Admin/User roles).
- **Car Management**: CRUD operations for cars (Admin only for create/update/delete).
- **Booking Management**: Create, view, and delete bookings.
- **Payment Processing**: Payments and refunds for bookings.
- **Location Management**: Pickup and dropoff location handling.
- **Security**: Role-based authentication using HTTP Basic Auth.
- **Exception Handling**: Global handler with custom exceptions.
- **Logging**: SLF4J/Logback-based controller and service-level logging.

---

## Prerequisites

- Java 21
- Maven
- MySQL 8.0+
- IDE (e.g., IntelliJ IDEA or Eclipse) - optional

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd carrental
```

### 2. Configure MySQL
Create the database:
```sql
CREATE DATABASE carrental;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/carrental
spring.datasource.username=root
spring.datasource.password=Avin@mySQL
```

### 3. Install Dependencies
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The app will run on: `http://localhost:8080`

### 5. Seed Initial Data (Optional)
In `CarrentalApplication.java`:
```java
@Bean
CommandLineRunner init(UserRepository userRepo, PasswordEncoder encoder) {
    return args -> {
        userRepo.save(new User("admin@example.com", encoder.encode("admin123"), "ADMIN", "Admin User", "1234567890"));
        userRepo.save(new User("user@example.com", encoder.encode("user123"), "USER", "Regular User", "0987654321"));
    };
}
```
Then restart the application.

### 6. Test with Postman
Use HTTP Basic Auth:
- **Admin**: `admin@example.com` / `admin123`
- **User**: `user@example.com` / `user123`

---

## Project Structure

```
carrental/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── config/                     # Security configuration
│   │   │   ├── controller/                 # REST Controllers
│   │   │   ├── exception/                  # Custom exceptions and handlers
│   │   │   ├── model/                      # Entity models
│   │   │   ├── repository/                 # JPA repositories
│   │   │   ├── service/                    # Business logic services
│   │   │   └── CarrentalApplication.java   # Main class
│   │   └── resources/
│   │       └── application.properties
│   └── test/                               # Tests
├── pom.xml
└── README.md
```

---

## Dependencies

Defined in `pom.xml`:
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `mysql-connector-j`
- `lombok`
- `spring-boot-devtools`
- `spring-boot-starter-test`, `junit-jupiter`, `mockito`

---

## API Endpoints

### 🔐 Authentication
Uses HTTP Basic Auth with `ROLE_ADMIN` and `ROLE_USER`.

### 🚗 Car Endpoints

| Method | Endpoint     | Description     | Roles  | Sample Request |
|--------|--------------|------------------|--------|----------------|
| GET    | `/cars`      | List all cars    | Any    | -              |
| POST   | `/cars`      | Add a car        | ADMIN  | `{"model":"Model3", ...}` |
| GET    | `/cars/{id}` | Get car by ID    | Authenticated | - |
| PUT    | `/cars/{id}` | Update car       | ADMIN  | `{"model":"ModelX", ...}` |
| DELETE | `/cars/{id}` | Delete car       | ADMIN  | -              |

### 📅 Booking Endpoints

| Method | Endpoint        | Description       | Roles       | Sample Body |
|--------|------------------|-------------------|-------------|-------------|
| GET    | `/bookings`      | List all bookings | ADMIN       | -           |
| POST   | `/bookings`      | Create booking    | USER        | `{"car":{"id":1}, ...}` |
| GET    | `/bookings/{id}` | Get booking       | USER/ADMIN  | -           |
| DELETE | `/bookings/{id}` | Delete booking    | ADMIN       | -           |

### 💳 Payment Endpoints

| Method | Endpoint               | Description       | Roles | Params |
|--------|------------------------|-------------------|-------|--------|
| POST   | `/payments/pay/{id}`   | Process payment   | Any   | `?method=credit` |
| POST   | `/payments/refund/{id}`| Refund payment    | Any   | `?amount=200` |

### 👤 User Endpoints

| Method | Endpoint           | Description      | Roles  | Sample Body |
|--------|--------------------|------------------|--------|-------------|
| POST   | `/users/register`  | Register user    | Public | `{"email":"test@...","password":"..."}` |
| GET    | `/users`           | List users       | Any    | -           |
| GET    | `/users/{id}`      | Get user by ID   | Any    | -           |

### 📍 Location Endpoints

| Method | Endpoint       | Description         | Roles | Sample Body |
|--------|----------------|---------------------|-------|-------------|
| GET    | `/locations`   | List locations      | Any   | -           |
| POST   | `/locations`   | Add location        | Any   | `{"name":"Airport", ...}` |

---

## Exception Handling

- **BookingConflictException** (409): Booking conflicts.
- **PaymentFailedException** (400): Failed transactions.
- **GlobalExceptionHandler** handles common exceptions like:
  - `RuntimeException` (404)
  - `IllegalArgumentException` (400)
  - Others (500)

---

## Logging

- **Log file**: `logs/car-rental.log`
- **Levels**: `INFO` (root), `DEBUG` (controllers/services)
- **Format**: Timestamp, thread, level, logger, message

---

## Troubleshooting

- **401 Unauthorized**: Check credentials.
- **500 Internal Server Error**: Ensure MySQL is running and database is created.
- **No Logs**: Ensure `logs/` directory exists and is writable.

---

## Contributing

Feel free to fork this repository, submit issues, or open pull requests to contribute improvements!
