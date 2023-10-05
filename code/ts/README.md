# React app

## Installation

To start a new Create React App project with TypeScript, you can run:
```
npx create-react-app my-app --template typescript
```

## [React Router DOM](https://www.npmjs.com/package/react-router-dom)

The react-router-dom package contains bindings for using [React Router](https://github.com/remix-run/react-router) in web applications. Please see the [Getting Started guide](https://reactrouter.com/en/main/start/tutorial) for more information on how to get started with React Router.
```
npm i react-router-dom
```

## [TypeScript Cookie](https://www.npmjs.com/package/typescript-cookie)

A simple, lightweight TypeScript API for handling cookies.

```
npm i typescript-cookie
```

## Cross-Origin Resource Sharing (CORS) policy

### Problem

- React Request from port 3000 to Spring Boot 8080 blocked by client
  - I have my spring server running at localhost:8080 and my web application at localhost:3000 and in my browser at localhost:300/register while trying to fetch to my spring server it occurs this error in the register.

```
Access to fetch at 'http://localhost:8080/users' from origin 'http://localhost:3000' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource. If an opaque response serves your needs, set the request's mode to 'no-cors' to fetch the resource with CORS disabled. Register.tsx:37 POST http://localhost:8080/users net::ERR_FAILED
```

### Solution

Configure CORS settings for a Spring Boot application, allowing requests from "http://localhost:3000" to access resources using specified HTTP methods, and ensuring that credentials (such as cookies) are included in the requests. This is useful when building web applications that need to handle cross-origin requests, such as Single Page Applications (SPAs) interacting with a backend API.

@Configuration: This annotation indicates that the class is a configuration class and defines one or more @Bean methods. Configuration classes are used to configure Spring Beans and other application settings.

@EnableWebSecurity: This annotation is used to enable Spring Security in a Spring Boot application. It indicates that the class is configuring security-related settings for the application.

@EnableWebMvc: This annotation is used to enable Spring MVC (Model-View-Controller) for handling web requests. It indicates that the class is configuring settings related to the MVC framework.

WebMvcConfigurer: This is an interface that provides callback methods for customizing the configuration of Spring MVC. In this case, the class WebSecurityConfig is implementing this interface to customize CORS configuration.

override fun addCorsMappings(registry: CorsRegistry): This method is an overridden method from the WebMvcConfigurer interface. It is used to configure CORS settings for the application.

Inside the addCorsMappings method:

registry.addMapping("/**"): This line specifies that the CORS configuration applies to all URL paths (i.e., any endpoint) within the application.

.allowedOrigins("http://localhost:3000"): This line specifies that requests from the origin "http://localhost:3000" are allowed to access the resources served by this application. In CORS, the "origin" refers to the domain or URL of the client making the request.

.allowedMethods("GET", "POST", "PUT", "DELETE"): This line specifies which HTTP methods (GET, POST, PUT, DELETE) are allowed for cross-origin requests. Only these specified methods are allowed when accessing the resources from the allowed origin.

.allowCredentials(true): This line indicates that the browser should include any cookies associated with the domain of the request when making cross-origin requests. This is important when dealing with authentication and session management.

```kotlin
@Configuration
@EnableWebSecurity
@EnableWebMvc
class WebSecurityConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Specifies which URL paths an annotated controller class is mapped to.
            .allowedOrigins("http://localhost:3000") // Specifies which origins are allowed to access the resources.
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Specifies which methods are allowed to access the resources.
            .allowCredentials(true) // Specifies whether the browser should include any cookies associated with the domain of the request being annotated.
    }
}
```

[Stackoverflow](https://stackoverflow.com/questions/71802709/react-js-request-from-port-3000-to-spring-boot-8080-blocked-by-client)

[Cors spring boot](https://rajendraprasadpadma.medium.com/what-the-cors-ft-spring-boot-spring-security-562f24d705c9)

## Contact

- In this Contact form I use:

  - The ``useState`` hook to manage form data (name, email, and message) in the component's state.

  - The ``handleChange`` function is used to update the form data as the user types into the input fields.

  - The ``handleSubmit`` function is called when the user submits the form. It composes an email subject and body based on the form data and constructs a mailto link with the email client's default behavior to open the user's email client with the pre-filled data.

  - Replace _'your_email@example.com'_ with your actual email address to ensure emails are sent to the correct destination.

  - When the user clicks the _Send Email_ button, their default email client will open with the subject and body pre-filled, allowing them to send an email to the specified email address.
