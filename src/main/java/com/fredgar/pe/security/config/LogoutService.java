package com.fredgar.pe.security.config;

import com.fredgar.pe.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar el cierre de sesión (logout) en una aplicación que utiliza JWT.
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  /**
   * Repositorio para manejar tokens.
   */
  private final TokenRepository tokenRepository;

  /**
   * Procesa el cierre de sesión.
   *
   * @param request Solicitud HTTP.
   * @param response Respuesta HTTP.
   * @param authentication Información de autenticación actual.
   */
  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    // Extrae el token JWT del encabezado de autorización
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);

    // Busca el token en el repositorio
    var storedToken = tokenRepository.findByToken(jwt)
        .orElse(null);

    // Si el token existe, lo marca como expirado y revocado
    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      tokenRepository.save(storedToken);

      // Limpia el contexto de seguridad
      SecurityContextHolder.clearContext();
    }
  }
}

