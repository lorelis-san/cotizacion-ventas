package com.lorelis.cotizacion.dto.cotizacion;

import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.model.cotizacion.DetalleCotizacion;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionDTO {
    private String numeroCotizacion;
    private Long clienteId;
    private Long vehiculoId;
    private List<DetalleCotizacionDTO> detalles;
    private String observaciones;
}
