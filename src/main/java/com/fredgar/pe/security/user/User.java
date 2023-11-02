package com.fredgar.pe.security.user;

import com.fredgar.pe.security.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Clase que representa un usuario en el sistema. Implementa la interfaz UserDetails
 * para integrarse con Spring Security.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  /**
      * Devuelve las autoridades (permisos) del usuario, basadas en su rol.
      *
      * @return Colección de GrantedAuthority que representa los permisos del usuario.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  /**
   * Devuelve la contraseña del usuario.
   *
   * @return Contraseña del usuario.
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Devuelve el nombre de usuario (email) para la autenticación.
   *
   * @return Email del usuario.
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * Indica si la cuenta del usuario ha expirado.
   *
   * @return Verdadero siempre, indicando que la cuenta no ha expirado.
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indica si la cuenta del usuario está bloqueada.
   *
   * @return Verdadero siempre, indicando que la cuenta no está bloqueada.
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * Indica si las credenciales del usuario han expirado.
   *
   * @return Verdadero siempre, indicando que las credenciales no han expirado.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indica si el usuario está habilitado.
   *
   * @return Verdadero siempre, indicando que el usuario está habilitado.
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}
