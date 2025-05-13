package com.lorelis.cotizacion.dto.quotation;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class DetalleCotizacionDTO {
    private Long id;

    @NotNull
    private Long cotizacionId; // Referencia a la cotización (solo ID)

    @NotNull
    private Long productoId; // Referencia al producto (solo ID)

    @NotNull
    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal precioTotal;
}
