package com.lorelis.cotizacion.dto.quotation;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CotizacionDTO {
    private Long id;

    @NotNull
    private String nroCotizacion;

    @NotNull
    private Long clienteId; // Referencia al cliente (solo ID)

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private BigDecimal total;

    @NotNull
    private String estado; // El estado puede ser un String o Enum dependiendo de cómo lo quieras manejar
}
