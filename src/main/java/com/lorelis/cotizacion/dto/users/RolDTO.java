package com.lorelis.cotizacion.dto.users;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RolDTO {
    private Long id;

    @NotNull
    private String name; // Ej. "ADMIN", "VENDEDOR"
}
