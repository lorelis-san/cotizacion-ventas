package com.lorelis.cotizacion.dto.usuario;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RolDTO {
    private Long id;

    @NotNull
    private String nombre; // Ej. "ADMIN", "VENDEDOR"
}
