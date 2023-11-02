package com.fredgar.pe.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para la gestión de tokens JWT (JSON Web Token).
 */
@Service
public class JwtService {

  /**
   * Clave secreta para firmar los tokens JWT, obtenida del archivo de configuración.
   */
  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  /**
   * Tiempo de expiración del token JWT.
   */
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  /**
   * Tiempo de expiración del token de actualización (refresh token).
   */
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  /**
   * Extrae el nombre de usuario del token JWT.
   *
   * @param token Token JWT.
   * @return Nombre de usuario.
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extrae una reclamación (claim) específica del token JWT.
   *
   * @param token Token JWT.
   * @param claimsResolver Función para resolver la reclamación.
   * @param <T> Tipo de la reclamación.
   * @return Reclamación solicitada.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Genera un token JWT para un usuario.
   *
   * @param userDetails Detalles del usuario.
   * @return Token JWT.
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  /**
   * Genera un token JWT con reclamaciones adicionales.
   *
   * @param extraClaims Reclamaciones adicionales para incluir en el token.
   * @param userDetails Detalles del usuario.
   * @return Token JWT.
   */
  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  /**
   * Genera un token de actualización (refresh token) para un usuario.
   *
   * @param userDetails Detalles del usuario.
   * @return Refresh token.
   */
  public String generateRefreshToken(
      UserDetails userDetails
  ) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  /**
   * Construye un token JWT.
   *
   * @param extraClaims Reclamaciones adicionales.
   * @param userDetails Detalles del usuario.
   * @param expiration Tiempo de expiración del token.
   * @return Token JWT.
   */
  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      long expiration
  ) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Verifica si un token JWT es válido.
   *
   * @param token Token JWT.
   * @param userDetails Detalles del usuario.
   * @return Verdadero si el token es válido.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  /**
   * Verifica si un token JWT ha expirado.
   *
   * @param token Token JWT.
   * @return Verdadero si el token ha expirado.
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extrae la fecha de expiración de un token JWT.
   *
   * @param token Token JWT.
   * @return Fecha de expiración.
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extrae todas las reclamaciones de un token JWT.
   *
   * @param token Token JWT.
   * @return Reclamaciones del token.
   */
  private Claims extractAllClaims(String token) {
    return Jwts
        .parser()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Obtiene la clave de firma para los tokens JWT.
   *
   * @return Clave de firma.
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
