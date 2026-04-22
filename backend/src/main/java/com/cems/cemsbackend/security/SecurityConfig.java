package com.cems.cemsbackend.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration for the application.
 * Defines access control, session management, and filter chains.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the security filter chain, endpoint permissions, and authentication filters.
   *
   * @param httpSecurity the security builder
   * @param authFilter   the custom JWT authentication filter
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthFilter authFilter) {
    return httpSecurity
        // CSRF disabled because this is a stateless JWT-based API
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/me").authenticated()
            .requestMatchers(HttpMethod.PUT, "/users/me").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/users/me").authenticated()
            .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
            .requestMatchers("/events/approved").permitAll()
            .requestMatchers(HttpMethod.POST, "/events/**").hasAnyRole("FACULTY", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/events/**").hasAnyRole("FACULTY", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/events/*/approve").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/events/**").hasRole("ADMIN")
            .requestMatchers("/events/admin/**").hasAnyRole("FACULTY", "ADMIN")
            .dispatcherTypeMatchers(
                DispatcherType.FORWARD,
                DispatcherType.ERROR
            )
            .permitAll()
            .anyRequest()
            .authenticated()
        )
        .exceptionHandling(configurer ->
            configurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}
