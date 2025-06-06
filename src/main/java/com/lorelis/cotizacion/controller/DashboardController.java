package com.lorelis.cotizacion.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DashboardController {

     @GetMapping("/")
    public String redirigirADashboard() {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
             // Usuario autenticado
             System.out.println("Usuario autenticado, vamos al dashboard");
             return "fragments/dashboard";
         } else {
             System.out.println("Usuario NO autenticado, redirigiendo a loginView");
             return "redirect:/loginView";
         }
    }
}
