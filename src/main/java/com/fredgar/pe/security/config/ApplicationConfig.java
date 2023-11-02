package com.fredgar.pe.security.config;

import com.fredgar.pe.security.auditing.ApplicationAuditAware;
import com.fredgar.pe.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración para la aplicación.
 * Define varios beans para la autenticación y manejo de usuarios.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  /**
   * Repositorio de usuarios para operaciones relacionadas con la base de datos.
   */
  private final UserRepository repository;

  /**
   * Bean para el servicio de detalles de usuario.
   *
   * @return UserDetailsService personalizado.
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> repository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  /**
   * Bean para el proveedor de autenticación.
   *
   * @return AuthenticationProvider configurado con UserDetailsService y PasswordEncoder.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

/**
   * Bean para el auditor de entidades.
   *
   * @return AuditorAware personalizado.
   */

  @Bean
  public AuditorAware<Integer> auditorAware() {
    return new ApplicationAuditAware();
  }

  /**
   * Bean para el gestor de autenticación.
   *
   * @param config Configuración de autenticación.
   * @return AuthenticationManager.
   * @throws Exception en caso de error durante la configuración.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Bean para el codificador de contraseñas.
   *
   * @return PasswordEncoder que utiliza BCrypt.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

