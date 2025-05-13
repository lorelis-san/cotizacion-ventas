package com.lorelis.cotizacion.dto.productos;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CategoriaDTO {
    private Long id;

    @NotNull
    private String nombre;

    private String descripcion;
}
