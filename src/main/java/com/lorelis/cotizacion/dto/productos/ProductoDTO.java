package com.lorelis.cotizacion.dto.productos;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long id;

    @NotNull
    private String nombre;

    private String descripcion;

    private String marca;

    private String modelo;

    private Integer anio;

    private BigDecimal precioCosto;

    private BigDecimal precioDealer;

    private BigDecimal precioVenta;

    private Integer stock;

    private String imagenUrl;

    @NotNull
    private Long categoriaProductoId; // Referencia a la Categoria
}
