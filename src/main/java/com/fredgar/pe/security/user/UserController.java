package com.fredgar.pe.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Controlador REST para operaciones relacionadas con usuarios.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * Servicio que contiene la lógica de negocio para operaciones de usuarios.
     */
    private final UserService service;

    /**
     * Endpoint para cambiar la contraseña de un usuario.
     *
     * @param request Datos de la solicitud de cambio de contraseña.
     * @param connectedUser Usuario que realiza la solicitud.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @PatchMapping
    public ResponseEntity<?> changePassword(
        @RequestBody ChangePasswordRequest request,
        Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
