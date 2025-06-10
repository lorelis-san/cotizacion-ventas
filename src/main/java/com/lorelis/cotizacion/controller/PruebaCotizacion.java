package com.lorelis.cotizacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PruebaCotizacion {


    @GetMapping("/pruebaCotizacion")
    public String vistaPrueba() {
        return "cotizacion/cotizacion";
    }
}
