package com.lorelis.cotizacion.dto.cotizacion;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailCotizacionDTO {
    private Long productoId;
    private String nombreProducto;
    private String codProducto;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
