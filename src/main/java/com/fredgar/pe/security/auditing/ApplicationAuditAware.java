package com.fredgar.pe.security.auditing;

import com.fredgar.pe.security.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Implementación de {@link AuditorAware} que proporciona el ID del usuario actualmente autenticado.
 * Esta clase se utiliza para integrar información de auditoría (como el usuario que creó o modificó una entidad)
 * en operaciones de persistencia de datos con Spring Data JPA.
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {

    /**
     * Obtiene el ID del auditor actual, es decir, el usuario autenticado.
     *
     * @return Un {@link Optional} que contiene el ID del usuario autenticado si está disponible,
     *         o {@link Optional#empty()} si no hay autenticación o el usuario es anónimo.
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication =
            SecurityContextHolder
                .getContext()
                .getAuthentication();

        // Verifica si la autenticación está activa y no es anónima
        if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Obtiene el usuario autenticado y devuelve su ID
        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}