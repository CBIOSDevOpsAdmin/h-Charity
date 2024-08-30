package com.himanism.hcharityapi.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.himanism.hcharityapi.security.jwt.AuthEntryPointJwt;
import com.himanism.hcharityapi.security.jwt.AuthTokenFilter;
import com.himanism.hcharityapi.security.services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/entity/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/appeal/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/files/**")).permitAll()
                .anyRequest().authenticated())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // @Bean
  // CorsConfigurationSource corsConfigurationSource() {
  // CorsConfiguration corsConfiguration = new CorsConfiguration();
  // corsConfiguration.setAllowCredentials(true);
  // corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
  // corsConfiguration.setAllowedHeaders(Arrays.asList("Origin",
  // "Access-Control-Allow-Origin", "Content-Type",
  // "Accept", "Authorization", "X-Requested-With",
  // "Access-Control-Request-Method",
  // "Access-Control-Request-Headers","Access-Control-Allow-Headers"));
  // corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type",
  // "Accept",
  // "Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
  // "Access-Control-Allow-Credentials"));
  // corsConfiguration.setAllowedMethods(Arrays.asList("Access-Control-Allow-Methods",
  // "GET", "POST", "PUT", "DELETE", "OPTIONS"));
  // UrlBasedCorsConfigurationSource source = new
  // UrlBasedCorsConfigurationSource();
  // source.registerCorsConfiguration("/**", corsConfiguration);

  // return source;
  // }
}

// Reference:https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication/blob/master/README.md