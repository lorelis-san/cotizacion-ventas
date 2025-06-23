package com.lorelis.cotizacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    // Vista de login
    @GetMapping("/iniciarSesion")
    public String showLoginForm() {
        return "usuario/login";
    }

    @GetMapping("/registrarse")
    public String showRegisterForm() {
        return "usuario/register";
    }
}

