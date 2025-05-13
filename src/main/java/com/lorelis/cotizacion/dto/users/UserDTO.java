package com.lorelis.cotizacion.dto.users;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
public class UserDTO {
    private Long id;

    @NotNull
    private String name;

    @Email @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Long rolId;   // Referencia al Rol

    @NotNull
    private Long sedeId;  // Referencia a la Sede
}
