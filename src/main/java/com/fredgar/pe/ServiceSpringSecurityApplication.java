package com.fredgar.pe;

import com.fredgar.pe.security.auth.AuthenticationService;
import com.fredgar.pe.security.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static com.fredgar.pe.security.user.Role.*;

/**
 * Clase principal de la aplicación Spring Boot que configura y ejecuta la aplicación.
 * Utiliza varias anotaciones para configurar el comportamiento de Spring y Spring Boot.
 *
 * @SpringBootApplication - Anotación de conveniencia que agrega @Configuration, @EnableAutoConfiguration, y @ComponentScan.
 *                          Esto permite la configuración automática basada en el classpath y la definición de beans.
 * @EnableJpaAuditing - Habilita la auditoría JPA en la aplicación, permitiendo el llenado automático de campos de auditoría.
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ServiceSpringSecurityApplication {

  /**
   * Punto de entrada principal para la aplicación Spring Boot.
   * Inicia la aplicación y crea un contexto de aplicación Spring.
   *
   * @param args Argumentos de la línea de comandos pasados al iniciar la aplicación.
   */
  public static void main(String[] args) {
    SpringApplication.run(ServiceSpringSecurityApplication.class, args);
  }

  /**
   * Bean CommandLineRunner que se ejecuta después de que el contexto de la aplicación ha sido inicializado.
   * Utilizado para ejecutar código específico al inicio de la aplicación, como la configuración inicial o la creación de usuarios.
   *
   * @param service Servicio de autenticación utilizado para registrar usuarios.
   * @return Un CommandLineRunner que ejecuta la lógica definida en su método run.
   */
  @Bean
  public CommandLineRunner commandLineRunner(AuthenticationService service) {
    return args -> {
      // Creación y registro del usuario administrador.
      var admin = RegisterRequest.builder()
          .firstname("Admin")
          .lastname("Admin")
          .email("admin@mail.com")
          .password("password")
          .role(ADMIN)
          .build();
      System.out.println("Admin token: " + service.register(admin).getAccessToken());

      // Creación y registro del usuario manager.
      var manager = RegisterRequest.builder()
          .firstname("Manager")
          .lastname("Manager")
          .email("manager@mail.com")
          .password("password")
          .role(MANAGER)
          .build();
      System.out.println("Manager token: " + service.register(manager).getAccessToken());

      // Creación y registro del usuario user.
      var user = RegisterRequest.builder()
          .firstname("User")
          .lastname("User")
          .email("user@gmail.com")
          .password("password")
          .role(USER)
          .build();
      System.out.println("User token: " + service.register(user).getAccessToken());

      // Creación y registro del usuario específico.
      var specific = RegisterRequest.builder()
          .firstname("Specific")
          .lastname("Specific")
          .email("specific@gmail.com")
          .password("password")
          .role(ADMIN)
          .build();
      System.out.println("Specific token: " + service.register(specific).getAccessToken());
    };
  }
}
