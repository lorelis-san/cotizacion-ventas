package com.lorelis.cotizacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    // Vista de login
    @GetMapping("/loginView")
    public String showLoginForm() {
        return "usuario/login";
    }

    @GetMapping("/registerView")
    public String showRegisterForm() {
        return "usuario/register";
    }
}

