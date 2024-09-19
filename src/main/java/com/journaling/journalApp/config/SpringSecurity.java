package com.journaling.journalApp.config;
import com.journaling.journalApp.serviceImpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SpringSecurity {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Configures the security filter chain
        return http
                // Authorize HTTP requests based on their path
                .authorizeHttpRequests(request -> request
                        // Allow all requests to paths starting with /public/** (no authentication required)
                        .requestMatchers("/public/**").permitAll()
//                     Allow only authenticated users to access /journal and /user
                        .requestMatchers("/journal/**").authenticated()

                        .requestMatchers("/user/**").authenticated()
                        // Only users with the ADMIN role can access paths starting with /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Any other request must be authenticated (users need to log in)
                        .anyRequest().authenticated()
                )
                // Enables HTTP Basic Authentication (username and password dialog in the browser or Postman)
                .httpBasic(Customizer.withDefaults())
                // Disable CSRF (Cross-Site Request Forgery) protection for stateless APIs (since you likely don't need it for an API)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Build the security configuration
                .build();
    }

//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//
//        return http.authorizeHttpRequests(request -> request
//                        .anyRequest().authenticated() )// Permit all requests
//                .httpBasic(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();
//    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
