package com.lorelis.cotizacion.dto.auth;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String name;
    private String email;
    private String rol;
    private String sede;
}
