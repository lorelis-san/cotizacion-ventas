package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.cotizacion.CotizacionDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionResponseDTO;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import com.lorelis.cotizacion.service.client.ClientService;
import com.lorelis.cotizacion.service.cotizacion.CotizacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PruebaCotizacion {
    @Autowired
    private CotizacionService cotizacionService;


    @GetMapping("/nuevaCotizacion")
    public String vistaPrueba() {
        return "cotizacion/cotizacion";
    }

    @GetMapping("/listaCotizaciones")
    public String listarCotizaciones(Model model) {
        List<CotizacionResponseDTO> cotizaciones = cotizacionService.listarCotizaciones();
        model.addAttribute("cotizaciones", cotizaciones);
        return "cotizacion/listaCotizacion"; // Ruta a tu vista Thymeleaf
    }

    @GetMapping("/cotizaciones/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {

        CotizacionResponseDTO dto = cotizacionService.obtenerCotizacionResponsePorId(id);
        model.addAttribute("cotizacion", dto);

        return "cotizacion/editarCotizacion";
    }

    @PostMapping("/cotizaciones/actualizar")
    public String actualizarCotizacion(@ModelAttribute CotizacionResponseDTO cotizacionDTO) {
        cotizacionService.actualizarCotizacionDesdeDTO(cotizacionDTO);
        return "redirect:/listaCotizaciones";
    }


    @GetMapping("/cotizaciones/ver/{id}")
    public String mostrarCotizacion(@PathVariable Long id, Model model) {
        CotizacionResponseDTO dto = cotizacionService.obtenerCotizacionResponsePorId(id);
        model.addAttribute("cotizacion", dto);

        return "cotizacion/vistaCotizacion";
    }

    @DeleteMapping("/cotizaciones/{id}")
    public ResponseEntity<Void> eliminarCotizacion(@PathVariable Long id) {
        cotizacionService.eliminarCotizacion(id);
        return ResponseEntity.noContent().build();
    }


}
