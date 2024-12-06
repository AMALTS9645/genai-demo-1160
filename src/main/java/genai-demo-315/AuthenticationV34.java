```java
//code-start
package com.example.loginapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.Valid;

// Main application
@SpringBootApplication
public class LoginApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginApiApplication.class, args);
    }
}

// User credentials DTO
class UserCredentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

// Login Controller
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * Handles login requests
     * 
     * @param userCredentials The user's credentials
     * @return ResponseEntity indicating the login status
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserCredentials userCredentials) {
        if (userService.isValidUser(userCredentials.getUsername(), userCredentials.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    /**
     * Handles IllegalArgumentExceptions
     * 
     * @param ex The exception
     * @return ResponseEntity with an error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidArguments(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

// User Service
@Service
public class UserService {

    // Dummy method to validate the user credentials, replace with real implementation
    public boolean isValidUser(String username, String password) {
        // Security: Validate inputs to prevent injection attacks
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }

        // Dummy validation logic, replace with actual database or authentication service check
        return "user".equals(username) && "password".equals(password);
    }
}

// Security Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // Security: Disable CSRF for now, enable in production
            .authorizeRequests().antMatchers("/login").permitAll()
            .anyRequest().authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Security: Configure in-memory authentication, replace with actual user details service
        auth.inMemoryAuthentication()
            .withUser("user").password("{noop}password").roles("USER");
    }
}
//code-end
```