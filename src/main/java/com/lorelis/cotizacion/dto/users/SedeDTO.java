package com.lorelis.cotizacion.dto.users;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class SedeDTO {
    private Long id;

    @NotNull
    private String nombre;

    private String direccion;
}
