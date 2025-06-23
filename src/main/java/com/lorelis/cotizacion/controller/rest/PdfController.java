package com.lorelis.cotizacion.controller.rest;

import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import com.lorelis.cotizacion.service.cotizacion.CotizacionService;
import com.lorelis.cotizacion.service.cotizacion.PdfGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final CotizacionService cotizacionService;
    private final PdfGeneratorService pdfGeneratorService;

    public PdfController(CotizacionService cotizacionService, PdfGeneratorService pdfGeneratorService) {
        this.cotizacionService = cotizacionService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @GetMapping("/cotizacion/{id}")
    public ResponseEntity<byte[]> generarPDF(@PathVariable Long id) {
        Cotizacion cotizacion = cotizacionService.obtenerPorId(id);

        ByteArrayInputStream bis = pdfGeneratorService.generarCotizacionPDF(cotizacion);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + cotizacion.getNumeroCotizacion()+"-"+ cotizacion.getVehiculo().getPlaca()+"-"+cotizacion.getVehiculo().getMarca() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    private String getUsuarioActual() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
