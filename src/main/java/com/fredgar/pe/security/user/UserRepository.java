package com.fredgar.pe.security.user;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz del repositorio para la entidad User.
 * Extiende JpaRepository, proporcionando operaciones CRUD para User.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

  /**
   * Busca un usuario por su dirección de email.
   *
   * @param email El email del usuario a buscar.
   * @return Un Optional<User> que contiene el usuario si es encontrado, o un Optional vacío si no es encontrado.
   */
  Optional<User> findByEmail(String email);

}
