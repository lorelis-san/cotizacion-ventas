package com.lorelis.cotizacion.dto.users;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
public class UsuarioDTO {
    private Long id;

    @NotNull
    private String nombre;

    @Email @NotNull
    private String correo;

    @NotNull
    private String contrasena;

    @NotNull
    private Long rolId;   // Referencia al Rol

    @NotNull
    private Long sedeId;  // Referencia a la Sede
}
