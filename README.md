# Auth Starter: Spring Boot JWT Authentication Template

## Description

Auth Starter is a ready-to-use Spring Boot template designed to bootstrap applications requiring secure user authentication using JSON Web Tokens (JWT). It provides a solid foundation for user registration and login, integrating essential security practices with Spring Security, Spring Data JPA, and JWT.

This project aims to save development time by offering pre-configured components for:
* User entity management (`User`, `Role`)
* Secure password storage (using BCrypt)
* JWT generation upon successful login
* JWT validation for protected endpoints via a custom filter
* Role-based authorization setup (though role assignment needs implementation)
* Basic REST API endpoints for user signup and login
* Input validation for requests
* Basic exception handling for common scenarios like duplicate emails

## Features (Current Implementation)

* **User Registration:** Endpoint (`/api/auth/signup`) to register new users with username, email, and password. Performs email uniqueness check.
* **User Login:** Endpoint (`/api/auth/login`) to authenticate users via email and password. Returns a JWT access token upon success.
* **JWT Authentication:** Stateless authentication using JWT. A `JwtAuthenticationFilter` intercepts requests, validates the token from the `Authorization: Bearer <token>` header, and sets the security context.
* **Password Encoding:** Uses `BCryptPasswordEncoder` for securely hashing user passwords before storing them.
* **Spring Security Integration:** Configures Spring Security for stateless sessions, defines public/protected routes, and integrates the custom JWT filter.
* **JPA Persistence:** Uses Spring Data JPA and Hibernate for database interaction with `User` and `Role` entities.
* **DTOs & Validation:** Uses Data Transfer Objects (DTOs) for request (`UserRegisterRequest`, `LoginRequest`) and response (`UserResponse`, `LoginResponse`) bodies, including input validation annotations.
* **Custom User Details:** Implements `UserDetailsService` (`CustomUserDetailsService`) and `UserDetails` (`CustomUserDetails`) to load user-specific data for Spring Security.

## Technologies Used

* Java 21
* Spring Boot 3.x (Ensure `pom.xml` reflects the correct stable version)
* Spring Security
* Spring Data JPA (Hibernate)
* Spring Web
* JWT (io.jsonwebtoken library)
* MySQL Connector (can be swapped for other databases)
* Lombok
* Maven

## Prerequisites

* JDK 21 or later
* Maven 3.x
* A running MySQL database instance (or configure for a different database)
* Set Environment Variables (see Configuration section)

## Configuration

The application requires the following environment variables to be set:

* `JWT_SECRET_KEY`: A Base64 encoded secret key for signing JWTs. Minimum length depends on the algorithm (HS256 needs a reasonably long key). You can generate one using online tools or code.
* `JWT_EXPIRATION_TIME`: Token expiration time in milliseconds (e.g., `3600000` for 1 hour).
* `DATABASE_URL`: The full JDBC URL for your database (e.g., `jdbc:mysql://localhost:3306/auth_db?useSSL=false&serverTimezone=UTC`).
* `DATABASE_USERNAME`: Username for database access.
* `DATABASE_PASSWORD`: Password for database access.

These are configured in `src/main/resources/application.properties` to read from the environment.

## Running the Application

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/suryadeepkoduri/auth-starter.git
    cd auth-starter
    ```
2.  **Set Environment Variables:** Export the required variables listed in the Configuration section in your terminal or IDE run configuration.
3.  **Database Setup:** Ensure your database schema exists (e.g., `auth_db`). The application uses `spring.jpa.hibernate.ddl-auto=update`, which will attempt to update the schema based on entities, but it's recommended to manage schema changes explicitly in production (e.g., using Flyway or Liquibase).
4.  **Build the application:**
    ```bash
    mvn clean install
    ```
5.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the packaged jar:
    ```bash
    java -jar target/auth-starter-0.0.1-SNAPSHOT.jar
    ```
The application will start on the default port (usually 8080).

## API Endpoints

* **POST `/api/auth/signup`**
    * **Description:** Registers a new user.
    * **Request Body:** `UserRegisterRequest` JSON (`{ "username": "...", "email": "...", "password": "..." }`)
    * **Responses:**
        * `201 Created`: Returns `UserResponse` JSON (`{ "userId": ..., "username": "...", "email": "..." }`) on success.
        * `400 Bad Request`: If email already exists or validation fails.
        * `500 Internal Server Error`: On unexpected errors.

* **POST `/api/auth/login`**
    * **Description:** Authenticates a user and returns a JWT.
    * **Request Body:** `LoginRequest` JSON (`{ "email": "...", "password": "..." }`)
    * **Responses:**
        * `200 OK`: Returns `LoginResponse` JSON (`{ "tokenType": "Bearer", "accessToken": "..." }`) on successful authentication.
        * `401 Unauthorized`: If credentials are invalid (handled by Spring Security/AuthenticationManager).
        * `500 Internal Server Error`: On unexpected errors.

* **Other Endpoints:** Any endpoint *not* starting with `/api/auth/**` requires a valid JWT in the `Authorization: Bearer <token>` header for access.

## Future Improvements & Considerations

This starter provides a basic setup. Consider implementing the following enhancements for production readiness:

1.  **Refresh Tokens:** Implement a refresh token mechanism to allow users to obtain new access tokens without repeatedly logging in. This involves generating, storing securely, validating, and rotating refresh tokens.
2.  **Enhanced Security:**
    * **Secret Management:** Use Vault, AWS Secrets Manager, GCP Secret Manager, or Azure Key Vault instead of environment variables for `JWT_SECRET_KEY` and `DATABASE_PASSWORD`.
    * **Password Policy:** Enforce stronger password complexity rules.
    * **Rate Limiting:** Protect login/signup endpoints against brute-force attacks.
3.  **Role Management:**
    * Implement logic to assign default roles (e.g., 'USER') upon registration.
    * Create endpoints or mechanisms to manage user roles (e.g., assigning 'ADMIN' roles).
    * Implement role-based access control (RBAC) more explicitly on protected endpoints using `@PreAuthorize` or similar annotations.
4.  **Robust Exception Handling:** Implement a global exception handler (`@ControllerAdvice`) to provide consistent, standardized error responses across the API.
5.  **Comprehensive Testing:** Add extensive unit tests (Services, Utils), integration tests (Controllers, Security Flow), and potentially contract tests.
6.  **Logging:** Implement structured logging throughout the application for monitoring and debugging.
7.  **CORS Configuration:** Make allowed CORS origins configurable via `application.properties` for different environments.
8.  **Database Migrations:** Use Flyway or Liquibase for reliable database schema management instead of `ddl-auto=update` in production.
9.  **User Management Features:** Add endpoints for profile updates, password reset/change functionality, email verification, etc.
10. **Dependency Updates:** Keep Spring Boot, security libraries (JJWT), and other dependencies up-to-date.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.
