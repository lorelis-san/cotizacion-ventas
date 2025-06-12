package com.lorelis.cotizacion.dto.users;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    private String password;

    private Long rolId;   // Referencia al Rol

    private Long sedeId;  // Referencia a la Sede

   // private boolean enabled;
}
