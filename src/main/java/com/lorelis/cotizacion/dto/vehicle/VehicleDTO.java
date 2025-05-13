package com.lorelis.cotizacion.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleDTO {
    @NotNull
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer year;

}
