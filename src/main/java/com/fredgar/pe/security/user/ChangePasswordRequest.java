package com.fredgar.pe.security.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa una solicitud de cambio de contraseña.
 * Contiene la contraseña actual, la nueva contraseña y la confirmación de la nueva contraseña.
 */
@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
