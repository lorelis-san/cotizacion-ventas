package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.auth.LoginRequestDTO;
import com.lorelis.cotizacion.dto.auth.LoginResponseDTO;
import com.lorelis.cotizacion.service.usuario.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}
