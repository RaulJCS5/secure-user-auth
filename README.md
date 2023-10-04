# SecureUserAuth - Spring Boot JWT Authentication with Cookie Storage in React Client

## Project Description

SecureUserAuth is a full-stack web application that implements user authentication and authorization using Spring Boot on the server side and React on the client side. The application allows users to register, log in, and log out. Authentication is based on JSON Web Tokens (JWT), and authorization is managed using the Bearer token scheme. Additionally, it includes the storage of JWT tokens as cookies on the client side and manages user data with a PostgreSQL database, using JDBI for efficient database interactions.

## Features

🔐 Secure Authentication: Users can register and log in securely with their credentials, with passwords, and stored in the server database PostgreSQL.

🔑 JWT-Based Authentication: Utilizes JSON Web Tokens (JWT) for user authentication, providing a secure and scalable authentication mechanism.

🚀 Efficient Authorization: Implements Bearer token authorization, ensuring that only authenticated users with valid tokens can access protected resources.

🍪 Cookie Storage: The JWT token received during login is securely stored in a client-side cookie, simplifying subsequent authentication requests.

🔒 Logout Functionality: Users can securely log out, invalidating their JWT token ensuring data privacy, and removing cookies.

📦 Technology Stack: Utilizes Spring Boot on the server side and React on the client side, combined with industry-standard tools and libraries for robust development.

🔍 Very Simple User Interface: Offers the most simplified React-based UI

💼 Database Management with PostgreSQL and JDBI: User data is managed efficiently using a PostgreSQL database. JDBI, a lightweight and efficient database library for Java, is employed for streamlined database interactions.

📋 Documentation: Provides clear and comprehensive documentation for setup, usage, and maintenance.

🚀 Scalability: Built to support future enhancements, including role-based authorization, email verification, cookie headers, and additional features.

## Skills

- Server Side (Spring Boot):
  - Spring Security for user authentication and authorization.
  - JWT (JSON Web Tokens) for secure token-based authentication.
  - PostgreSQL as the database.
  - Spring Boot Web for RESTful API endpoints.
  - JDBI a SQL convenience library for Java.

- Client Side (React):

  - React for building the user interface.
  - React Router for handling client-side routing.
  - fetch API for making HTTP requests to the server.
  - typescript-cookie TypeScript API for handling cookies.

## Development Phases

- Backend Development:

  - Create a Spring Boot project with Spring Security.
  - Implement user registration, login, and JWT token generation.
  - Set up database models and integrate JDBI.
  - Create RESTful API endpoints for user-related actions.

- Frontend Development:

  - Create a React application for the client side.
  - Implement user registration and login forms.
  - Set up protected routes that require a valid JWT token for access.
  - Implement the logout feature.

## Improves

- [&cross;] Implement role-based authorization for different user roles (e.g., admin, regular user).
- [&cross;] Adding email verification during registration for added security.
- [&cross;] Enhance the UI with features like error handling, loading, etc.

- Use an Authentication Middleware:
  - [&check;] Implement an authentication middleware in your Spring Boot application that checks incoming requests for valid access tokens.
  - [&check;] Verify the token's signature, expiration, and integrity on the server side before processing the request.

- Access Token and Refresh Token:
  - [&cross;] Implement access tokens and refresh tokens. The access token should have a shorter lifespan (e.g., minutes), and the refresh token should have a longer lifespan (e.g., days).
  - [&cross;] When the access token expires, the client can use the refresh token to obtain a new access token without requiring the user to log in again.

- Secure Cookie Handling:
  - [&cross;] When sending tokens to the client, use HTTP-only cookies for enhanced security.
  - [&cross;] Cookies with the HttpOnly flag cannot be accessed by JavaScript, reducing the risk of XSS attacks.

- Logging and Monitoring:
  - [&check;] Implement comprehensive logging and monitoring to detect and respond to suspicious activities or unauthorized token usage.

- Token Validation on Each Request:
  - [&check;] Validate tokens on each incoming request using your authentication middleware. Reject requests with invalid tokens.

- Token Expiration Management:
  - [&cross;] Consider implementing a token expiration policy, which includes automatic removal of expired tokens from storage.

- User Authentication and Authorization:
  - [&check;] Implement user authentication and authorization logic based on the user's token. Ensure that only authorized users can access protected resources.
