package com.lorelis.cotizacion.dto.cotizacion;

import com.lorelis.cotizacion.dto.products.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCotizacionDTO {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private String imagenUrl;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
