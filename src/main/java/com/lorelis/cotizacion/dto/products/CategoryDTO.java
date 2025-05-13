package com.lorelis.cotizacion.dto.products;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CategoryDTO {
    private Long id;

    @NotNull
    private String nombre;

    private String descripcion;
}
