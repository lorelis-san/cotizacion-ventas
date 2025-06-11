package com.lorelis.cotizacion.dto.cotizacion;


import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CotizacionResponseDTO {
    private Long id;
    private String numeroCotizacion;
    private String estado;
    private LocalDate fecha;
    private String observaciones;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private ClientDTO cliente;
    private VehicleDTO vehiculo;
    private List<DetalleCotizacionDTO> detalles;
}
