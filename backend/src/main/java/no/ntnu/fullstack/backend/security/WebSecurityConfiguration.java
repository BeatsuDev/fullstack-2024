package no.ntnu.fullstack.backend.security;

import java.lang.reflect.Method;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration {
  @Bean
  public UserDetailsService UserDetailsService(final PasswordEncoder encoder) {
    final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    manager.createUser(
        User.withUsername("user@example.org").password(encoder.encode("userpassword"))
            .roles("USER").build());
    manager.createUser(
        User.withUsername("admin@example.org").password(encoder.encode("adminpassword"))
            .roles("USER", "ADMIN").build());

    return manager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http)
      throws Exception {
    return http
        .csrf((csrfConfigurer) -> {
          csrfConfigurer.disable();
        })
        .exceptionHandling(Customizer.withDefaults())
        .sessionManagement(session -> {
          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        })
        .authorizeHttpRequests(authorize -> {
          authorize
              .requestMatchers(HttpMethod.POST, "/user/session").permitAll()
              .anyRequest().authenticated();
        })
        .httpBasic(Customizer.withDefaults())
        .build();
  }
}
