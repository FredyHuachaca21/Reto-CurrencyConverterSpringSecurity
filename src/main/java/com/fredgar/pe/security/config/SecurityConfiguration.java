package com.fredgar.pe.security.config;

import com.fredgar.pe.security.user.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.fredgar.pe.security.user.Permission.*;
import static com.fredgar.pe.security.user.Role.ADMIN;
import static com.fredgar.pe.security.user.Role.MANAGER;
import static org.springframework.http.HttpMethod.GET;

/**
 * Configuración de seguridad para la aplicación.
 * Define las reglas de seguridad y los filtros para las solicitudes HTTP.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
        "/api/v1/auth/**",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/swagger-ui.html"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers ->
                headers.frameOptions(frameOptions ->
                    frameOptions.sameOrigin()
                )
            )
            .authorizeHttpRequests(req -> req
                // Permitir acceso a la consola H2, documentación de OpenApi y al punto de entrada de autenticación sin autenticación
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll() // Interfaz de Swagger
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll() // Para las definiciones de OpenAPI
                .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll() // Recursos de Swagger
                .requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll() // Recursos de Swagger desde WebJars

                // Rutas específicas para /api/exchange-rate/v1
                .requestMatchers(new AntPathRequestMatcher("/api/exchange-rate/v1/convert")).hasAnyRole(ADMIN.name())
                .requestMatchers(new AntPathRequestMatcher("/api/exchange-rate/v1/create")).hasAnyRole(ADMIN.name())
                .requestMatchers(new AntPathRequestMatcher("/api/exchange-rate/v1/update/*")).hasAnyRole(ADMIN.name())
                .requestMatchers(new AntPathRequestMatcher("/api/exchange-rate/v1/all")).hasAnyRole(ADMIN.name(), MANAGER.name())
                .requestMatchers(new AntPathRequestMatcher("/api/exchange-rate/v1/*")).authenticated()


                // Rutas específicas para /api/v1/demo-controller
                .requestMatchers(new AntPathRequestMatcher("/api/v1/demo-controller/admin")).hasAnyRole(ADMIN.name())
                .requestMatchers(new AntPathRequestMatcher("/api/v1/demo-controller/admin")).hasAnyAuthority(ADMIN_READ.name())
                .requestMatchers(new AntPathRequestMatcher("/api/v1/demo-controller/manager")).hasAnyRole(MANAGER.name())
                .requestMatchers(new AntPathRequestMatcher("/api/v1/demo-controller/manager")).hasAnyAuthority(MANAGER_READ.name())

                .requestMatchers(new AntPathRequestMatcher("/api/v1/demo-controller/public")).authenticated()

                // Requiere roles ADMIN o MANAGER para acceder a rutas de administración y gestión
                .requestMatchers(new AntPathRequestMatcher("/api/v1/management/**"))
                .hasAnyRole(ADMIN.name(), MANAGER.name())

                // Todas las demás solicitudes requieren autenticación
                .anyRequest()
                .authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout ->
                logout.logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
            );

        return http.build();
    }


}


