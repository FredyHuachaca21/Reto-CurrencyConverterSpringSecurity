package com.fredgar.pe.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 * Servicio para operaciones relacionadas con usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * Codificador de contraseñas para realizar operaciones de codificación y verificación.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Repositorio de usuarios para interactuar con la base de datos.
     */
    private final UserRepository repository;

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param request Objeto que contiene la contraseña actual, la nueva contraseña y la confirmación.
     * @param connectedUser Usuario que realiza la solicitud.
     * @throws IllegalStateException Si la contraseña actual es incorrecta o las nuevas contraseñas no coinciden.
     */
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        // Obtiene el usuario actual a partir del objeto Principal
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Verifica si la contraseña actual es correcta
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        // Verifica si las dos nuevas contraseñas son iguales
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }

        // Actualiza la contraseña
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Guarda la nueva contraseña en la base de datos
        repository.save(user);
    }
}