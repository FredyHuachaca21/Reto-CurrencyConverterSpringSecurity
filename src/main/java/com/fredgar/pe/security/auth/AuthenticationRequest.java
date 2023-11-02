package com.fredgar.pe.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de solicitud para la autenticación de un usuario.
 * Contiene los detalles requeridos para el proceso de autenticación.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  private String email;
  String password;
}
