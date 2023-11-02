package com.fredgar.pe.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredgar.pe.security.config.JwtService;
import com.fredgar.pe.security.user.UserRepository;
import com.fredgar.pe.security.token.Token;
import com.fredgar.pe.security.token.TokenRepository;
import com.fredgar.pe.security.token.TokenType;
import com.fredgar.pe.security.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Servicio para manejar operaciones de autenticación como registro, inicio de sesión y renovación de tokens.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  /**
   * Repositorio para manejar operaciones relacionadas con los usuarios.
   */
  private final UserRepository repository;

  /**
   * Repositorio para manejar tokens de usuario.
   */
  private final TokenRepository tokenRepository;

  /**
   * Codificador de contraseñas para la seguridad de las contraseñas de usuario.
   */
  private final PasswordEncoder passwordEncoder;

  /**
   * Servicio para manejar operaciones relacionadas con JWT.
   */
  private final JwtService jwtService;

  /**
   * Gestor de autenticación para manejar procesos de autenticación.
   */
  private final AuthenticationManager authenticationManager;

  /**
   * Registra un nuevo usuario y genera tokens de acceso y actualización.
   *
   * @param request Datos del usuario a registrar.
   * @return Una respuesta de autenticación con los tokens generados.
   */
  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  /**
   * Autentica a un usuario y genera nuevos tokens de acceso y actualización.
   *
   * @param request Datos del usuario para autenticar.
   * @return Una respuesta de autenticación con los tokens generados.
   */
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  /**
   * Guarda el token JWT en el repositorio de tokens.
   *
   * @param user Usuario asociado con el token.
   * @param jwtToken Token JWT a guardar.
   */
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  /**
   * Revoca todos los tokens válidos de un usuario.
   *
   * @param user Usuario cuyos tokens se revocarán.
   */
  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  /**
   * Renueva el token de acceso de un usuario a partir de su token de actualización.
   *
   * @param request Solicitud HTTP.
   * @param response Respuesta HTTP.
   * @throws IOException Si ocurre un error de entrada/salida.
   */
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
