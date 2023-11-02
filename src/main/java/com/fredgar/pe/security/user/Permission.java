package com.fredgar.pe.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeración que define los diferentes permisos en el sistema.
 * Cada permiso está asociado con una operación específica.
 */
@RequiredArgsConstructor
public enum Permission {

    /**
     * Permiso para leer información del administrador.
     */
    ADMIN_READ("admin:read"),

    /**
     * Permiso para actualizar información del administrador.
     */
    ADMIN_UPDATE("admin:update"),

    /**
     * Permiso para crear información del administrador.
     */
    ADMIN_CREATE("admin:create"),

    /**
     * Permiso para eliminar información del administrador.
     */
    ADMIN_DELETE("admin:delete"),

    /**
     * Permiso para leer información de gestión.
     */
    MANAGER_READ("management:read"),

    /**
     * Permiso para actualizar información de gestión.
     */
    MANAGER_UPDATE("management:update"),

    /**
     * Permiso para crear información de gestión.
     */
    MANAGER_CREATE("management:create"),

    /**
     * Permiso para eliminar información de gestión.
     */
    MANAGER_DELETE("management:delete"),

    /**
     * Permiso de privilegio de lectura.
     */
    READ_PRIVILEGE("read:privilege");


    /**
     * Representación en cadena del permiso.
     */
    @Getter
    private final String permission;
}
