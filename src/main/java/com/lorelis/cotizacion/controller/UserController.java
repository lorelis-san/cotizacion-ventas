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

    // Vista de dashboard (después del login exitoso)
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "usuario/dashboard";
    }

    // Puedes agregar más vistas según el rol
    @GetMapping("/admin")
    public String showAdminPanel() {
        return "admin-dashboard";
    }
    @GetMapping("/home")
    public String showDashboardUser() {
        return "usuario/dashboard";
    }

    @GetMapping("/registerView")
    public String showRegisterForm() {
        return "usuario/register";
    }
}

