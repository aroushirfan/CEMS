package com.cems.cemsbackend.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthFilter authFilter) {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/events/**").hasAnyRole("USER", "FACULTY", "ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/events/**").hasAnyRole("FACULTY", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/events/**").hasAnyRole("FACULTY", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/events/*/approve").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/events/**").hasRole("ADMIN")
//                                .requestMatchers("/events/admin/**").hasAnyRole("FACULTY", "ADMIN")
                                .dispatcherTypeMatchers(
                                        DispatcherType.FORWARD,
                                        DispatcherType.ERROR
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
