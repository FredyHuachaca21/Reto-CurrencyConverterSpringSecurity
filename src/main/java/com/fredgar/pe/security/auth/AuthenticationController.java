package com.fredgar.pe.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controlador REST para manejar operaciones de autenticación como registro, inicio de sesión y renovación de tokens.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  /**
   * Servicio de autenticación para realizar operaciones relacionadas con la autenticación.
   */
  private final AuthenticationService service;

  /**
   * Registra un nuevo usuario y genera tokens de acceso y actualización.
   *
   * @param request Datos del usuario a registrar.
   * @return Una respuesta de autenticación con los tokens generados.
   */
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  /**
   * Autentica a un usuario y genera nuevos tokens de acceso y actualización.
   *
   * @param request Datos del usuario para autenticar.
   * @return Una respuesta de autenticación con los tokens generados.
   */
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  /**
   * Renueva el token de acceso de un usuario a partir de su token de actualización.
   *
   * @param request Solicitud HTTP.
   * @param response Respuesta HTTP.
   * @throws IOException Si ocurre un error de entrada/salida.
   */
  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

}

