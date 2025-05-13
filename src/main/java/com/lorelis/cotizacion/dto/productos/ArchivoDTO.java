package com.lorelis.cotizacion.dto.productos;

import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import jakarta.persistence.*;
@Entity
public class ArchivoDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id")
    private Cotizacion cotizacion;

    private String nombreArchivo;
    private String urlArchivo;

    // Getters y setters
}
