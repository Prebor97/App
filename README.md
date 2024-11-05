# JWT and OAuth Security Application

This application demonstrates a secure backend setup using JSON Web Tokens (JWT) and OAuth for user authentication and authorization. Built with Spring Boot, the app focuses on secure user access, protecting endpoints, and handling user sessions efficiently.

---

## Getting Started

### Prerequisites

To run this application locally, ensure you have the following installed:

- **Java JDK 11** or higher
- **Maven** (for building and managing dependencies)
- **PostgreSQL** or any configured database
- **Git** (to clone the repository)

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/your-repo-name.git
   cd your-repo-name
## 2. Configure the Database

Create a PostgreSQL database (or your preferred database) and update the configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
## 3. Set Up Environment Variables for Security

For enhanced security, sensitive details like OAuth client secrets and JWT secrets should be set as environment variables.

```bash
export JWT_SECRET="your_jwt_secret_key"
export OAUTH_CLIENT_ID="your_oauth_client_id"
export OAUTH_CLIENT_SECRET="your_oauth_client_secret"
## 4. Build the Application

Run the following Maven command to build the application:

```bash
mvn clean install
## Run the Application

Once built, start the application using:

```bash
mvn spring-boot:run
The server should now be running at [http://localhost:8080](http://localhost:8080).
## Features

1. **User Registration and Login with JWT**
   - New users can register by providing their credentials, which will be stored securely.
   - Users can log in, generating a JWT token that must be provided in the Authorization header for secure access to protected endpoints.

2. **OAuth 2.0 Authentication**
   - Users can authenticate via third-party OAuth providers (e.g., Google, Facebook).
   - Upon successful OAuth login, a JWT token is generated, which is used for subsequent authorization.

3. **Role-Based Access Control**
   - User roles (e.g., USER, ADMIN) are defined to manage access levels.
   - Certain endpoints are restricted based on user roles, ensuring that only authorized users can access specific resources.

4. **Protected API Endpoints**
   - API endpoints are protected using JWT, requiring users to pass the `Authorization: Bearer <JWT Token>` header.
   - Unauthorized requests will receive a 401 Unauthorized response.

5. **Token Refresh Mechanism**
   - Users can refresh expired JWT tokens by hitting the `/refresh-token` endpoint.
   - The refresh token mechanism ensures minimal disruptions for users by allowing token regeneration without re-login.

6. **Logout and Token Revocation**
   - The logout endpoint invalidates the current JWT, revoking access for users.
   - This ensures security by invalidating tokens when a user logs out.
# JWT and OAuth Security Application

This application demonstrates a secure backend setup using JSON Web Tokens (JWT) and OAuth for user authentication and authorization. Built with Spring Boot, the app focuses on secure user access, protecting endpoints, and handling user sessions efficiently.

---

## Getting Started

### Prerequisites

To run this application locally, ensure you have the following installed:

- **Java JDK 11** or higher
- **Maven** (for building and managing dependencies)
- **PostgreSQL** or any configured database
- **Git** (to clone the repository)

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/your-repo-name.git
   cd your-repo-name
## Configure the Database

Create a PostgreSQL database (or your preferred database) and update the configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
## Set Up Environment Variables for Security

For enhanced security, sensitive details like OAuth client secrets and JWT secrets should be set as environment variables.

```bash
export JWT_SECRET="your_jwt_secret_key"
export OAUTH_CLIENT_ID="your_oauth_client_id"
export OAUTH_CLIENT_SECRET="your_oauth_client_secret"
## Build the Application

Run the following Maven command to build the application:

```bash
mvn clean install
## Run the Application

Once built, start the application using:

```bash
mvn spring-boot:run
## Server Information

The server should now be running at [http://localhost:8080](http://localhost:8080).

---

## Features

1. **User Registration and Login with JWT**
   - New users can register by providing their credentials, which will be stored securely.
   - Users can log in, generating a JWT token that must be provided in the Authorization header for secure access to protected endpoints.

2. **OAuth 2.0 Authentication**
   - Users can authenticate via third-party OAuth providers (e.g., Google, Facebook).
   - Upon successful OAuth login, a JWT token is generated, which is used for subsequent authorization.

3. **Role-Based Access Control**
   - User roles (e.g., USER, ADMIN) are defined to manage access levels.
   - Certain endpoints are restricted based on user roles, ensuring that only authorized users can access specific resources.

4. **Protected API Endpoints**
   - API endpoints are protected using JWT, requiring users to pass the `Authorization: Bearer <JWT Token>` header.
   - Unauthorized requests will receive a 401 Unauthorized response.

5. **Token Refresh Mechanism**
   - Users can refresh expired JWT tokens by hitting the `/refresh-token` endpoint.
   - The refresh token mechanism ensures minimal disruptions for users by allowing token regeneration without re-login.

6. **Logout and Token Revocation**
   - The logout endpoint invalidates the current JWT, revoking access for users.
   - This ensures security by invalidating tokens when a user logs out.

---

## Running Tests

To test the application, run:

```bash
mvn test
## API Documentation

### Swagger

This application includes Swagger for API documentation. Once the app is running, access the Swagger documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Project Structure

- `/src/main/java`: Contains the main application code organized by package.
  - `config`: Configuration files, including security and JWT settings.
  - `controller`: REST controllers for handling requests.
  - `service`: Business logic for handling core functionality.
  - `repository`: Interfaces for database interactions.
  - `model`: Entity classes representing database tables.
  - `security`: JWT and OAuth configuration and utilities.
  
- `/src/main/resources`: Configuration files, including `application.properties`.
## Usage Guide

### Authentication Workflow

- **User Registration**: Users register with their email and password, which is securely stored in the database.
- **Login**: After successful login, a JWT is generated and must be used in headers for secure access.
- **Accessing Protected Endpoints**: JWT must be included in the Authorization header for any request to a protected endpoint.
- **Token Refresh**: To refresh an expired token, use the `/refresh-token` endpoint.
- **Logout**: Hitting the logout endpoint invalidates the current JWT token, securing the session.

## Troubleshooting

- **Invalid Token Error**: Ensure the JWT is correctly set in the Authorization header and has not expired.
- **Database Connection Issues**: Verify database configurations in `application.properties` and that the database server is running.
- **OAuth Errors**: Ensure OAuth client credentials are correctly set in environment variables and match those in the OAuth provider's console.

## Contributing

1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## License

This project is licensed under the MIT License. See LICENSE for details. 
