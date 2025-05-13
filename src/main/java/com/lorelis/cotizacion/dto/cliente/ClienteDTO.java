package com.lorelis.cotizacion.dto.cliente;

import lombok.Data;

@Data
public class ClienteDTO {

    private Long id;

    private String nombres;

    private String apellidos;

    private String nroDocumento;  // Un solo campo para documento (DNI o RUC)

    private String razonSocial;  // Solo se llena si el nroDocumento es RUC

    private String celular;

    private String correo;
}
