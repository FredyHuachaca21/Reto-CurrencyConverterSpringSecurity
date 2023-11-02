package com.fredgar.pe.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de respuesta para la autenticación de un usuario.
 * Incluye tokens generados tras una autenticación exitosa.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  /**
   * Token de acceso utilizado para autenticar solicitudes.
   * Este token generalmente tiene un periodo de validez corto.
   */
  @JsonProperty("access_token")
  private String accessToken;

  /**
   * Token de actualización (refresh token) utilizado para obtener un nuevo token de acceso.
   * Este token suele tener un periodo de validez más largo que el token de acceso.
   */
  @JsonProperty("refresh_token")
  private String refreshToken;
}

