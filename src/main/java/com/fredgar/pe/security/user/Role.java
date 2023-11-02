package com.fredgar.pe.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumeración que define los diferentes roles de usuario en el sistema.
 * Cada rol tiene un conjunto asociado de permisos.
 */
@RequiredArgsConstructor
public enum Role {
  /**
   * Rol de usuario común, sin permisos especiales.
   */
  USER(Collections.emptySet()),

  /**
   * Rol de administrador, con permisos para realizar todas las operaciones de administrador y de gerente.
   */
  ADMIN(
      Set.of(
          Permission.ADMIN_READ,
          Permission.ADMIN_UPDATE,
          Permission.ADMIN_DELETE,
          Permission.ADMIN_CREATE,
          Permission.MANAGER_READ,
          Permission.MANAGER_UPDATE,
          Permission.MANAGER_DELETE,
          Permission.MANAGER_CREATE
      )
  ),

  /**
   * Rol de gerente, con permisos para realizar operaciones de gestión.
   */
  MANAGER(
      Set.of(
          Permission.MANAGER_READ,
          Permission.MANAGER_UPDATE,
          Permission.MANAGER_DELETE,
          Permission.MANAGER_CREATE
      )
  );

  /**
   * Conjunto de permisos asociados a cada rol.
   */
  @Getter
  private final Set<Permission> permissions;

  /**
   * Obtiene las autoridades (permisos) del rol en formato compatible con Spring Security.
   *
   * @return Lista de SimpleGrantedAuthority, cada uno representando un permiso del rol.
   */
  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
        .stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
